package com.paleimitations.schoolsofmagic.client.astral;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.registries.DimensionRegistry;
import com.paleimitations.schoolsofmagic.common.world.dimensions.AstralCorridorGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

// the walls are barriers and barriers draw as nothing, so the edge of the corridor is a curtain of
// light instead. it hangs off the floor, waves along its length and fades out before the top
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AstralAurora {
   private static final int ALONG = 30;
   private static final int SLICES = 4;
   private static final float TALL = 2.1F;
   private static final float SPILL = 2.7F;
   private static final float STEP = 1.0F;

   // bottom to top. the last one is only there to fade the rest away
   private static final float[][] BAND = {
      {0.42F, 0.10F, 0.86F, 0.70F},
      {0.66F, 0.16F, 0.98F, 0.46F},
      {0.52F, 0.24F, 1.00F, 0.20F},
      {0.40F, 0.36F, 1.00F, 0.00F}
   };

   // the floor gets the same colour lying flat, running the other way and at its own pace, so
   // the two do not look like one thing folded over
   private static final float[] SPILL_NEAR = {0.58F, 0.18F, 0.98F, 0.62F};
   private static final float[] SPILL_FAR = {0.30F, 0.32F, 1.00F, 0.00F};

   @SubscribeEvent
   public static void onRender(RenderLevelStageEvent event) {
      if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;
      Minecraft mc = Minecraft.getInstance();
      if (mc.level == null || !mc.level.dimension().equals(DimensionRegistry.ASTRAL_PLANE_RIFT)) return;

      Vec3 eye = event.getCamera().getPosition();
      float time = mc.level.getGameTime() + event.getPartialTick();

      PoseStack pose = event.getPoseStack();
      pose.pushPose();

      RenderSystem.enableBlend();
      RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
      RenderSystem.depthMask(false);
      RenderSystem.disableCull();
      RenderSystem.setShader(GameRenderer::getPositionColorShader);

      Tesselator tesselator = Tesselator.getInstance();
      BufferBuilder builder = tesselator.getBuilder();
      builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

      Matrix4f matrix = pose.last().pose();
      // everything is worked out against the eye. the lanes sit far enough out along z that a
      // float cannot count in whole blocks out there
      double baseZ = Math.floor(eye.z / STEP) * STEP;
      float floor = (float) (AstralCorridorGenerator.FLOOR + 1 - eye.y);

      float west = (float) (AstralCorridorGenerator.WEST_WALL + 1 - eye.x);
      float east = (float) (AstralCorridorGenerator.EAST_WALL - eye.x);

      curtain(builder, matrix, west, baseZ, eye, floor, time);
      curtain(builder, matrix, east, baseZ, eye, floor, time);
      spill(builder, matrix, west, SPILL, baseZ, eye, floor, time);
      spill(builder, matrix, east, -SPILL, baseZ, eye, floor, time);

      tesselator.end();

      RenderSystem.defaultBlendFunc();
      RenderSystem.depthMask(true);
      RenderSystem.enableCull();
      RenderSystem.disableBlend();
      pose.popPose();
   }

   private static void curtain(BufferBuilder out, Matrix4f matrix, float x, double baseZ,
                               Vec3 eye, float floor, float time) {
      for (int i = -ALONG; i < ALONG; i++) {
         float z0 = (float) (baseZ + i * STEP - eye.z);
         float z1 = z0 + STEP;

         float near = 1.0F - Math.min(1.0F, Math.abs(i) / (float) ALONG);
         near *= near;

         for (int slice = 0; slice < SLICES - 1; slice++) {
            float y0 = floor + TALL * slice / (SLICES - 1);
            float y1 = floor + TALL * (slice + 1) / (SLICES - 1);
            float[] low = BAND[slice];
            float[] high = BAND[slice + 1];

            quad(out, matrix, x, z0, z1, y0, y1, low, high,
               ripple(baseZ + i * STEP, time) * near,
               ripple(baseZ + i * STEP + STEP, time) * near);
         }
      }
   }

   // the light lying on the floor. it reaches in from the wall and dies off, and it runs the
   // other way down the corridor so it does not march in step with the curtain above it
   private static void spill(BufferBuilder out, Matrix4f matrix, float x, float reach, double baseZ,
                             Vec3 eye, float floor, float time) {
      float y = floor + 0.06F;
      for (int i = -ALONG; i < ALONG; i++) {
         float z0 = (float) (baseZ + i * STEP - eye.z);
         float z1 = z0 + STEP;

         float near = 1.0F - Math.min(1.0F, Math.abs(i) / (float) ALONG);
         near *= near;
         float a0 = wash(baseZ + i * STEP, time) * near;
         float a1 = wash(baseZ + i * STEP + STEP, time) * near;

         out.vertex(matrix, x, y, z0).color(SPILL_NEAR[0], SPILL_NEAR[1], SPILL_NEAR[2], SPILL_NEAR[3] * a0).endVertex();
         out.vertex(matrix, x, y, z1).color(SPILL_NEAR[0], SPILL_NEAR[1], SPILL_NEAR[2], SPILL_NEAR[3] * a1).endVertex();
         out.vertex(matrix, x + reach, y, z1).color(SPILL_FAR[0], SPILL_FAR[1], SPILL_FAR[2], 0.0F).endVertex();
         out.vertex(matrix, x + reach, y, z0).color(SPILL_FAR[0], SPILL_FAR[1], SPILL_FAR[2], 0.0F).endVertex();
      }
   }

   // two waves of different length crossing each other, which is what stops it reading as a
   // repeating pattern. they travel fast enough now that you can watch them go past
   private static float ripple(double z, float time) {
      float slow = Mth.sin((float) (z * 0.21D) + time * 0.055F);
      float fast = Mth.sin((float) (z * 0.53D) - time * 0.115F);
      return 0.30F + 0.50F * (0.6F + 0.4F * slow) * (0.6F + 0.4F * fast);
   }

   private static float wash(double z, float time) {
      float slow = Mth.sin((float) (z * 0.13D) - time * 0.038F);
      float fast = Mth.sin((float) (z * 0.31D) + time * 0.071F);
      return 0.25F + 0.55F * (0.6F + 0.4F * slow) * (0.6F + 0.4F * fast);
   }

   private static void quad(BufferBuilder out, Matrix4f matrix, float x, float z0, float z1,
                            float y0, float y1, float[] low, float[] high, float a0, float a1) {
      out.vertex(matrix, x, y0, z0).color(low[0], low[1], low[2], low[3] * a0).endVertex();
      out.vertex(matrix, x, y0, z1).color(low[0], low[1], low[2], low[3] * a1).endVertex();
      out.vertex(matrix, x, y1, z1).color(high[0], high[1], high[2], high[3] * a1).endVertex();
      out.vertex(matrix, x, y1, z0).color(high[0], high[1], high[2], high[3] * a0).endVertex();
   }
}
