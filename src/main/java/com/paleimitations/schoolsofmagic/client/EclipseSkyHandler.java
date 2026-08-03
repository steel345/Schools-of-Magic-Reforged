package com.paleimitations.schoolsofmagic.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

// Draws the shadow crossing the sun and drains the light out of the sky as it goes.
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EclipseSkyHandler {

   private static final ResourceLocation ECLIPSE = new ResourceLocation("som", "textures/environment/eclipse.png");
   private static final int FRAMES = 5;
   // How dark the world goes at totality: near enough to night.
   private static final float MAX_DARKEN = 1.0F;

   @SubscribeEvent
   public static void onClientTick(TickEvent.ClientTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      if (Minecraft.getInstance().isPaused()) return;
      ClientEclipse.tick();
   }

   // The sky itself. Drawn after vanilla's, so it lies over the sun.
   @SubscribeEvent
   public static void onRenderSky(RenderLevelStageEvent event) {
      if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) return;
      float darkness = ClientEclipse.getDarkness();
      if (darkness <= 0.001F) return;
      Minecraft mc = Minecraft.getInstance();
      if (mc.level == null) return;

      PoseStack pose = event.getPoseStack();
      float shown = ClientEclipse.getShownStage();

      RenderSystem.enableBlend();
      RenderSystem.depthMask(false);
      RenderSystem.disableCull();

      RenderSystem.defaultBlendFunc();
      RenderSystem.setShader(GameRenderer::getPositionColorShader);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

      // The real sun goes first, under a round patch of plain sky. Round, so there
      // are no corners to catch the eye, and in the sky's own untouched colour so it
      // vanishes into the background. The shell below then dims patch and sky alike,
      // which keeps them matched however dark it gets.
      pose.pushPose();
      pose.mulPose(Axis.YP.rotationDegrees(-90.0F));
      pose.mulPose(Axis.XP.rotationDegrees(mc.level.getTimeOfDay(event.getPartialTick()) * 360.0F));
      sunHider(pose.last().pose(), skyColour(mc, event.getPartialTick()));
      pose.popPose();

      // Everything vanilla drew — blue sky, stars, and the patch above — is buried
      // under a shell of night, so nothing of the old sky shows through.
      pose.pushPose();
      nightShell(pose.last().pose(), darkness);
      pose.popPose();

      // Vanilla's celestial frame, unaltered, so the disc lands on the sun exactly
      // rather than beside it. Any offset here reads as a second sun.
      pose.pushPose();
      pose.mulPose(Axis.YP.rotationDegrees(-90.0F));
      pose.mulPose(Axis.XP.rotationDegrees(mc.level.getTimeOfDay(event.getPartialTick()) * 360.0F));
      Matrix4f matrix = pose.last().pose();

      // Added to the sky exactly as vanilla adds the sun, so the black ground of the
      // art contributes nothing and only the lit part of the disc shows.
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      RenderSystem.setShaderTexture(0, ECLIPSE);
      RenderSystem.blendFunc(com.mojang.blaze3d.platform.GlStateManager.SourceFactor.SRC_ALPHA,
         com.mojang.blaze3d.platform.GlStateManager.DestFactor.ONE);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      quad(matrix, 30.0F, 98.0F, frameFor(shown), true);
      pose.popPose();

      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.defaultBlendFunc();
      RenderSystem.enableCull();
      RenderSystem.depthMask(true);
      RenderSystem.disableBlend();
   }

   private static float[] skyColour(Minecraft mc, float partial) {
      net.minecraft.world.phys.Vec3 c =
         mc.level.getSkyColor(mc.gameRenderer.getMainCamera().getPosition(), partial);
      return new float[]{(float) c.x, (float) c.y, (float) c.z};
   }

   // A disc of sky big enough to swallow the sun's quad whole.
   private static void sunHider(Matrix4f matrix, float[] colour) {
      float radius = 46.0F;
      float dist = 99.5F;
      int segments = 32;
      BufferBuilder buf = Tesselator.getInstance().getBuilder();
      buf.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
      buf.vertex(matrix, 0.0F, dist, 0.0F).color(colour[0], colour[1], colour[2], 1.0F).endVertex();
      for (int i = 0; i <= segments; ++i) {
         float angle = (float) (i * Math.PI * 2.0D / segments);
         buf.vertex(matrix, Mth.cos(angle) * radius, dist, Mth.sin(angle) * radius)
            .color(colour[0], colour[1], colour[2], 1.0F).endVertex();
      }
      com.mojang.blaze3d.vertex.BufferUploader.drawWithShader(buf.end());
   }

   // A box of night around the camera, just inside where the sky was drawn.
   private static void nightShell(Matrix4f matrix, float darkness) {
      float d = 99.0F;
      float a = Mth.clamp(darkness * MAX_DARKEN, 0.0F, 1.0F);
      BufferBuilder buf = Tesselator.getInstance().getBuilder();
      buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
      face(buf, matrix, a, d, 0);
      face(buf, matrix, a, d, 1);
      face(buf, matrix, a, d, 2);
      face(buf, matrix, a, d, 3);
      face(buf, matrix, a, d, 4);
      com.mojang.blaze3d.vertex.BufferUploader.drawWithShader(buf.end());
   }

   // Five of the six sides: the ground below never needs covering.
   private static void face(BufferBuilder buf, Matrix4f m, float a, float d, int side) {
      float[][] c = switch (side) {
         case 0 -> new float[][]{{-d, d, -d}, {d, d, -d}, {d, d, d}, {-d, d, d}};
         case 1 -> new float[][]{{-d, -d, -d}, {-d, d, -d}, {d, d, -d}, {d, -d, -d}};
         case 2 -> new float[][]{{d, -d, d}, {d, d, d}, {-d, d, d}, {-d, -d, d}};
         case 3 -> new float[][]{{-d, -d, d}, {-d, d, d}, {-d, d, -d}, {-d, -d, -d}};
         default -> new float[][]{{d, -d, -d}, {d, d, -d}, {d, d, d}, {d, -d, d}};
      };
      for (float[] v : c) {
         buf.vertex(m, v[0], v[1], v[2]).color(0.0F, 0.0F, 0.0F, a).endVertex();
      }
   }

   // Stage 1 is the newborn eclipse at the right of the strip, and the shadow walks
   // leftwards from there to totality at the far end.
   private static int frameFor(float shownStage) {
      int stage = Mth.clamp(Math.round(shownStage), 1, FRAMES);
      return FRAMES - stage;
   }

   private static void quad(Matrix4f matrix, float half, float dist, int frame, boolean lit) {
      float u0 = frame / (float) FRAMES;
      float u1 = (frame + 1) / (float) FRAMES;
      BufferBuilder buf = Tesselator.getInstance().getBuilder();
      buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
      buf.vertex(matrix, -half, dist, -half).uv(u0, 0.0F).endVertex();
      buf.vertex(matrix, half, dist, -half).uv(u1, 0.0F).endVertex();
      buf.vertex(matrix, half, dist, half).uv(u1, 1.0F).endVertex();
      buf.vertex(matrix, -half, dist, half).uv(u0, 1.0F).endVertex();
      com.mojang.blaze3d.vertex.BufferUploader.drawWithShader(buf.end());
   }

   // The sky dimming alone leaves the ground lit as if it were noon, so the whole
   // view is drawn down with it.
   @SubscribeEvent
   public static void onOverlay(net.minecraftforge.client.event.RenderGuiOverlayEvent.Pre event) {
      if (!event.getOverlay().id().equals(
            net.minecraftforge.client.gui.overlay.VanillaGuiOverlay.HOTBAR.id())) {
         return;
      }
      float darkness = ClientEclipse.getDarkness();
      if (darkness <= 0.001F) return;
      Minecraft mc = Minecraft.getInstance();
      if (mc.player == null || mc.options.hideGui) return;
      int alpha = (int) (darkness * 0.45F * 255.0F) << 24;
      event.getGuiGraphics().fill(0, 0, mc.getWindow().getGuiScaledWidth(),
         mc.getWindow().getGuiScaledHeight(), alpha);
   }

   // Pulls the fog down with the sky, so the land dims rather than the sky alone.
   @SubscribeEvent
   public static void onFogColour(ViewportEvent.ComputeFogColor event) {
      float darkness = ClientEclipse.getDarkness();
      if (darkness <= 0.001F) return;
      float fade = 1.0F - darkness * MAX_DARKEN;
      event.setRed(event.getRed() * fade);
      event.setGreen(event.getGreen() * fade);
      event.setBlue(event.getBlue() * fade);
   }
}
