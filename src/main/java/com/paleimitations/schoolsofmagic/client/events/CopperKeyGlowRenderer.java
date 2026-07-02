package com.paleimitations.schoolsofmagic.client.events;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CopperKeyGlowRenderer {

   private static final Map<BlockPos, Long> GLOW = new HashMap<>();

   public static void addGlow(BlockPos pos, long until) {
      GLOW.put(pos.immutable(), until);
   }

   @SubscribeEvent
   public static void onRender(RenderLevelStageEvent event) {
      if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
         return;
      }
      Minecraft mc = Minecraft.getInstance();
      if (mc.level == null || GLOW.isEmpty()) {
         return;
      }
      long now = mc.level.getGameTime();
      Iterator<Map.Entry<BlockPos, Long>> it = GLOW.entrySet().iterator();
      while (it.hasNext()) {
         if (it.next().getValue() < now) {
            it.remove();
         }
      }
      if (GLOW.isEmpty()) {
         return;
      }
      Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();
      PoseStack pose = event.getPoseStack();
      MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
      VertexConsumer vc = buffer.getBuffer(RenderType.lines());
      pose.pushPose();
      pose.translate(-cam.x, -cam.y, -cam.z);
      for (BlockPos pos : GLOW.keySet()) {
         AABB box = new AABB(pos).inflate(0.01D);
         LevelRenderer.renderLineBox(pose, vc, box, 1.0F, 0.82F, 0.3F, 1.0F);
      }
      pose.popPose();
      buffer.endBatch(RenderType.lines());
   }
}
