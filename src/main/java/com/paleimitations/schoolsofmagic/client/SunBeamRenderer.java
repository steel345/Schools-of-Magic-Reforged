package com.paleimitations.schoolsofmagic.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

// The ritual finish ray: four upright quads forming a shaft, drawn additively,
// solid at the foot and fading to nothing at the crown. It lands at full height and
// draws in on itself from that moment on, so it is never still.
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class SunBeamRenderer {

   private static final int LIFETIME = 20;
   // The ray is five blocks tall where it comes from; this one is a sun blast.
   private static final float HEIGHT = 5.0F * 9.0F;
   private static final float WIDTH = 1.6F;
   public static final float[] SUN = {1.0F, 0.55F, 0.12F};
   public static final float[] GRAVE = {0.11F, 0.045F, 0.012F};

   private static final List<Beam> BEAMS = new ArrayList<>();

   private static class Beam {
      final Vec3 pos;
      final float r, g, b;
      // Two shapes of collapse: one narrows away to a thread, one sinks straight down.
      final boolean thins;
      int age;

      Beam(Vec3 pos, float r, float g, float b, boolean thins) {
         this.pos = pos;
         this.r = r;
         this.g = g;
         this.b = b;
         this.thins = thins;
      }
   }

   public static void add(double x, double y, double z, float r, float g, float b, boolean thins) {
      BEAMS.add(new Beam(new Vec3(x, y, z), r, g, b, thins));
   }

   @SubscribeEvent
   public static void onClientTick(TickEvent.ClientTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      Iterator<Beam> it = BEAMS.iterator();
      while (it.hasNext()) {
         if (++it.next().age > LIFETIME) it.remove();
      }
   }

   // Sine ease, in and out.
   private static float inOutSine(float t) {
      return -(Mth.cos((float) Math.PI * t) - 1.0F) / 2.0F;
   }

   @SubscribeEvent
   public static void onRender(RenderLevelStageEvent event) {
      if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;
      Minecraft mc = Minecraft.getInstance();
      if (BEAMS.isEmpty() || mc.level == null) return;

      Vec3 cam = event.getCamera().getPosition();
      PoseStack pose = event.getPoseStack();
      float partial = event.getPartialTick();

      RenderSystem.enableBlend();
      // Additive, so the shaft glows rather than tints what is behind it.
      RenderSystem.blendFunc(com.mojang.blaze3d.platform.GlStateManager.SourceFactor.SRC_ALPHA,
         com.mojang.blaze3d.platform.GlStateManager.DestFactor.ONE);
      RenderSystem.depthMask(false);
      RenderSystem.disableCull();
      RenderSystem.setShader(GameRenderer::getPositionColorShader);

      Tesselator tess = Tesselator.getInstance();
      BufferBuilder buf = tess.getBuilder();
      buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

      for (Beam beam : BEAMS) {
         float smoothAge = beam.age + partial;
         if (smoothAge >= LIFETIME) continue;

         float flashProgress = inOutSine(1.0F - smoothAge / LIFETIME);
         float half, height, baseAlpha;
         if (beam.thins) {
            // Narrowed away to a thread: the walls close to nothing as the light drains.
            half = 0.65F * 0.5F * WIDTH * flashProgress;
            height = HEIGHT * (0.35F + 0.65F * flashProgress);
            baseAlpha = flashProgress;
         } else {
            // Sinks back into the ground at full breadth.
            half = Mth.lerp(flashProgress, 0.9F * 0.5F, 0.65F * 0.5F) * WIDTH;
            height = HEIGHT * flashProgress;
            baseAlpha = Mth.lerp(flashProgress, 0.5F, 1.0F);
         }

         pose.pushPose();
         pose.translate(beam.pos.x - cam.x, beam.pos.y - cam.y, beam.pos.z - cam.z);
         Matrix4f mat = pose.last().pose();

         // The four walls of the shaft, opaque at the foot, clear at the crown.
         wall(buf, mat, -half, -half, -half, half, height, baseAlpha, beam);
         wall(buf, mat, -half, -half, half, -half, height, baseAlpha, beam);
         wall(buf, mat, half, -half, half, half, height, baseAlpha, beam);
         wall(buf, mat, -half, half, half, half, height, baseAlpha, beam);

         pose.popPose();
      }

      tess.end();

      RenderSystem.enableCull();
      RenderSystem.depthMask(true);
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableBlend();
   }

   private static void wall(BufferBuilder buf, Matrix4f mat,
                            float x0, float z0, float x1, float z1, float height, float baseAlpha, Beam beam) {
      buf.vertex(mat, x0, 0.0F, z0).color(beam.r, beam.g, beam.b, baseAlpha).endVertex();
      buf.vertex(mat, x1, 0.0F, z1).color(beam.r, beam.g, beam.b, baseAlpha).endVertex();
      buf.vertex(mat, x1, height, z1).color(beam.r, beam.g, beam.b, 0.0F).endVertex();
      buf.vertex(mat, x0, height, z0).color(beam.r, beam.g, beam.b, 0.0F).endVertex();
   }
}
