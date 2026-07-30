package com.paleimitations.schoolsofmagic.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.network.PacketKnowledgeAnimate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Renders the flying books entirely on the client, interpolated every frame, so
// the motion is perfectly smooth (no dependence on entity position sync).
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class KnowledgeAnimationClient {

   private static final List<Anim> ACTIVE = new ArrayList<>();

   // The book has to snap shut before it floats out.
   private static final int KNOWLEDGE_DELAY = 8;

   private static class Anim {
      final Vec3 foundStart, foundEnd;
      final Vec3 knowledgeStart, knowledgeEnd;
      final ItemStack found, knowledge;
      final int duration;
      final long start;

      Anim(Vec3 fs, Vec3 fe, Vec3 ks, Vec3 ke, ItemStack found, ItemStack knowledge, int duration, long start) {
         this.foundStart = fs; this.foundEnd = fe;
         this.knowledgeStart = ks; this.knowledgeEnd = ke;
         this.found = found; this.knowledge = knowledge;
         this.duration = duration; this.start = start;
      }
   }

   public static void add(PacketKnowledgeAnimate msg) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.level == null) return;
      Vec3 shelfC = Vec3.atCenterOf(msg.shelf);
      Vec3 lecternC = Vec3.atCenterOf(msg.lectern).add(0.0, 0.55, 0.0);
      Vec3 readingC = Vec3.atCenterOf(msg.reading).add(0.0, 0.55, 0.0);
      Vec3 floatC = readingC.add(-0.25, 1.5, -0.25);
      // Forward: found shelf->lectern, book reading->float. Reverse swaps both.
      Vec3 fs = msg.reverse ? lecternC : shelfC;
      Vec3 fe = msg.reverse ? shelfC : lecternC;
      Vec3 ks = msg.reverse ? floatC : readingC;
      Vec3 ke = msg.reverse ? readingC : floatC;
      ACTIVE.add(new Anim(fs, fe, ks, ke, msg.found, msg.knowledge, msg.duration, mc.level.getGameTime()));
      if (!msg.reverse) {
         net.minecraft.world.level.block.entity.BlockEntity be = mc.level.getBlockEntity(msg.reading);
         if (be instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium podium) {
            podium.forceCloseTicks = msg.duration;
         } else if (be instanceof net.minecraft.world.level.block.entity.LecternBlockEntity) {
            LecternKnowledgeCache.startClose(msg.reading, mc.level.getGameTime());
         }
      }
   }

   @SubscribeEvent
   public static void onRenderLevel(RenderLevelStageEvent event) {
      if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return;
      if (ACTIVE.isEmpty()) return;
      Minecraft mc = Minecraft.getInstance();
      if (mc.level == null) return;

      float partial = event.getPartialTick();
      long now = mc.level.getGameTime();
      Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();
      PoseStack pose = event.getPoseStack();
      MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();

      Iterator<Anim> it = ACTIVE.iterator();
      while (it.hasNext()) {
         Anim a = it.next();
         float elapsed = (now - a.start) + partial;
         float t = elapsed / a.duration;
         if (t >= 1.0F) { it.remove(); continue; }
         if (t < 0.0F) t = 0.0F;
         float s = t * t * (3.0F - 2.0F * t);
         float spin = elapsed * 6.0F;
         // The found book flies right away.
         renderItem(mc, pose, buffers, cam, a.found,
            a.foundStart.add(a.foundEnd.subtract(a.foundStart).scale(s)), spin);
         // The Book of Knowledge waits for the close, then floats up.
         float kt = (elapsed - KNOWLEDGE_DELAY) / (a.duration - KNOWLEDGE_DELAY);
         if (kt > 0.0F) {
            if (kt > 1.0F) kt = 1.0F;
            float ks = kt * kt * (3.0F - 2.0F * kt);
            renderItem(mc, pose, buffers, cam, a.knowledge,
               a.knowledgeStart.add(a.knowledgeEnd.subtract(a.knowledgeStart).scale(ks)), spin);
         }
      }
      buffers.endBatch();
   }

   private static void renderItem(Minecraft mc, PoseStack pose, MultiBufferSource buffers, Vec3 cam,
                                  ItemStack stack, Vec3 pos, float spin) {
      if (stack == null || stack.isEmpty()) return;
      pose.pushPose();
      pose.translate(pos.x - cam.x, pos.y - cam.y, pos.z - cam.z);
      pose.scale(0.6F, 0.6F, 0.6F);
      pose.mulPose(Axis.YP.rotationDegrees(spin));
      int light = LevelRenderer.getLightColor(mc.level, BlockPos.containing(pos));
      mc.getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY,
         pose, buffers, mc.level, 0);
      pose.popPose();
   }
}
