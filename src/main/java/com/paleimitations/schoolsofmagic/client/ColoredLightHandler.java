package com.paleimitations.schoolsofmagic.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import org.joml.Vector3f;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ColoredLightHandler {
   private static final int MAX_LIGHTS = 64;
   private static final int CHUNK_RANGE = 3;
   private static final int RESCAN_TICKS = 10;

   private static ShaderInstance shader;
   private static long lastScan = -1L;

   @Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
   public static class Loader {
      @SubscribeEvent
      public static void onRegisterShaders(RegisterShadersEvent event) {
         try {
            event.registerShader(
               new ShaderInstance(event.getResourceProvider(),
                  new ResourceLocation(SchoolsOfMagic.MODID, "som_colored_light"),
                  DefaultVertexFormat.POSITION),
               loaded -> shader = loaded);
         } catch (Exception e) {
            shader = null;
            com.mojang.logging.LogUtils.getLogger().error("Coloured light shader failed to load; the effect is disabled.", e);
         }
      }
   }

   @SubscribeEvent
   public static void onRenderStage(RenderLevelStageEvent event) {
      if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;
      if (shader == null) return;
      if (!com.paleimitations.schoolsofmagic.common.config.SOMClientConfig.coloredLighting()) return;

      Minecraft mc = Minecraft.getInstance();
      if (mc.level == null || mc.player == null) return;

      rescan(mc);
      java.util.List<ColoredLightSources.Source> lights = ColoredLightSources.live();
      if (lights.isEmpty()) return;

      net.minecraft.world.phys.Vec3 camera = mc.gameRenderer.getMainCamera().getPosition();

      Matrix4f inverse = new Matrix4f(event.getProjectionMatrix())
         .mul(event.getPoseStack().last().pose())
         .invert();

      int depthTexture = mc.getMainRenderTarget().getDepthTextureId();
      if (depthTexture <= 0) return;

      RenderSystem.enableBlend();
      RenderSystem.blendFunc(com.mojang.blaze3d.platform.GlStateManager.SourceFactor.ONE,
         com.mojang.blaze3d.platform.GlStateManager.DestFactor.ONE);
      RenderSystem.depthMask(false);
      RenderSystem.disableDepthTest();
      RenderSystem.disableCull();
      RenderSystem.setShader(() -> shader);
      RenderSystem.setShaderTexture(0, depthTexture);

      if (shader.getUniform("InverseVP") != null) {
         shader.getUniform("InverseVP").set(inverse);
      }

      int drawn = 0;
      for (ColoredLightSources.Source light : lights) {
         if (drawn++ >= MAX_LIGHTS) break;
         Vector3f rel = new Vector3f(
            (float) (light.x() - camera.x),
            (float) (light.y() - camera.y),
            (float) (light.z() - camera.z));
         if (shader.getUniform("LightPos") != null) {
            shader.getUniform("LightPos").set(rel.x, rel.y, rel.z, light.radius());
         }
         if (shader.getUniform("LightColor") != null) {
            shader.getUniform("LightColor").set(light.r(), light.g(), light.b(), light.strength());
         }
         fullscreenQuad();
      }

      RenderSystem.enableDepthTest();
      RenderSystem.enableCull();
      RenderSystem.depthMask(true);
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableBlend();
   }

   private static void fullscreenQuad() {
      BufferBuilder buf = Tesselator.getInstance().getBuilder();
      buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
      buf.vertex(0.0D, 0.0D, 0.0D).endVertex();
      buf.vertex(1.0D, 0.0D, 0.0D).endVertex();
      buf.vertex(1.0D, 1.0D, 0.0D).endVertex();
      buf.vertex(0.0D, 1.0D, 0.0D).endVertex();
      BufferUploader.drawWithShader(buf.end());
   }

   private static void rescan(Minecraft mc) {
      long now = mc.level.getGameTime();
      if (lastScan >= 0L && now - lastScan < RESCAN_TICKS) return;
      lastScan = now;
      ColoredLightSources.clearCandidates();
      BlockPos at = mc.player.blockPosition();
      int cx = at.getX() >> 4;
      int cz = at.getZ() >> 4;
      for (int ox = -CHUNK_RANGE; ox <= CHUNK_RANGE; ++ox) {
         for (int oz = -CHUNK_RANGE; oz <= CHUNK_RANGE; ++oz) {
            LevelChunk chunk = mc.level.getChunkSource().getChunk(cx + ox, cz + oz, false);
            if (chunk == null) continue;
            for (java.util.Map.Entry<BlockPos, BlockEntity> e : chunk.getBlockEntities().entrySet()) {
               ColoredLightSources.addCandidate(e.getValue());
               if (ColoredLightSources.candidateCount() >= MAX_LIGHTS) return;
            }
         }
      }
   }
}
