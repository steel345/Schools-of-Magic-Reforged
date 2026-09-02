package com.paleimitations.schoolsofmagic.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.EntityAnvilForge;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HotAnvilRenderer {
   private static final int LOOK = 12;

   private static final int SOAK = 80;
   private static final int FADE = 60;
   private static final int WARM = SOAK + FADE;

   private static final Map<BlockPos, Integer> heats = new HashMap<>();

   public static ResourceLocation modelFor(BlockState anvil) {
      if (anvil.is(Blocks.CHIPPED_ANVIL)) return HotAnvilModels.CHIPPED;
      if (anvil.is(Blocks.DAMAGED_ANVIL)) return HotAnvilModels.DAMAGED;
      return HotAnvilModels.PLAIN;
   }

   @SubscribeEvent
   public static void onClientTick(TickEvent.ClientTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      Minecraft mc = Minecraft.getInstance();
      if (mc.level == null || mc.player == null || mc.isPaused()) return;
      if (mc.player.tickCount % 4 != 0) return;

      BlockPos middle = mc.player.blockPosition();
      heats.keySet().removeIf(at -> at.distSqr(middle) > (LOOK + 4) * (LOOK + 4));

      for (BlockPos at : BlockPos.betweenClosed(middle.offset(-LOOK, -LOOK, -LOOK),
            middle.offset(LOOK, LOOK, LOOK))) {
         if (!mc.level.getBlockState(at).is(BlockTags.ANVIL)) continue;

         BlockPos key = at.immutable();
         int heat = heats.getOrDefault(key, 0);
         boolean fire = EntityAnvilForge.burning(mc.level, key.below());

         heat = fire ? Math.min(WARM, heat + 4) : Math.max(0, heat - 4);
         if (heat <= 0) heats.remove(key); else heats.put(key, heat);
      }
   }

   @SubscribeEvent
   public static void onRender(RenderLevelStageEvent event) {
      if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;
      if (heats.isEmpty()) return;

      Minecraft mc = Minecraft.getInstance();
      Level level = mc.level;
      if (level == null) return;

      Vec3 eye = event.getCamera().getPosition();
      PoseStack pose = event.getPoseStack();
      BlockRenderDispatcher blocks = mc.getBlockRenderer();
      MultiBufferSource.BufferSource buf = mc.renderBuffers().bufferSource();

      for (Map.Entry<BlockPos, Integer> entry : heats.entrySet()) {
         BlockPos at = entry.getKey();
         BlockState anvil = level.getBlockState(at);
         if (!anvil.is(BlockTags.ANVIL)) continue;

         float heat = (entry.getValue() - SOAK) / (float) FADE;
         if (heat <= 0.0F) continue;
         heat = Math.min(1.0F, heat);

         BakedModel model = mc.getModelManager().getModel(modelFor(anvil));

         pose.pushPose();
         pose.translate(at.getX() - eye.x, at.getY() - eye.y, at.getZ() - eye.z);

         pose.translate(0.5D, 0.5D, 0.5D);
         if (anvil.hasProperty(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING)) {
            pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees(
               -anvil.getValue(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING).toYRot()));
         }
         pose.translate(-0.5D, -0.5D, -0.5D);

         boolean full = heat >= 0.999F;
         VertexConsumer out = buf.getBuffer(full ? HotAnvilLayer.SOLID : HotAnvilLayer.FADING);
         if (!full) out = new Faded(out, heat);

         blocks.getModelRenderer().renderModel(pose.last(), out, anvil, model,
            1.0F, 1.0F, 1.0F, 15728880, OverlayTexture.NO_OVERLAY);

         pose.popPose();
      }
      buf.endBatch(HotAnvilLayer.SOLID);
      buf.endBatch(HotAnvilLayer.FADING);
   }

   private record Faded(VertexConsumer out, float alpha) implements VertexConsumer {
      @Override
      public VertexConsumer vertex(double x, double y, double z) {
         return this.out.vertex(x, y, z);
      }

      @Override
      public VertexConsumer color(int r, int g, int b, int a) {
         return this.out.color(r, g, b, (int) (a * this.alpha));
      }

      @Override
      public VertexConsumer uv(float u, float v) {
         return this.out.uv(u, v);
      }

      @Override
      public VertexConsumer overlayCoords(int u, int v) {
         return this.out.overlayCoords(u, v);
      }

      @Override
      public VertexConsumer uv2(int u, int v) {
         return this.out.uv2(u, v);
      }

      @Override
      public VertexConsumer normal(float x, float y, float z) {
         return this.out.normal(x, y, z);
      }

      @Override
      public void endVertex() {
         this.out.endVertex();
      }

      @Override
      public void defaultColor(int r, int g, int b, int a) {
         this.out.defaultColor(r, g, b, a);
      }

      @Override
      public void unsetDefaultColor() {
         this.out.unsetDefaultColor();
      }
   }

   @Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT,
      bus = Mod.EventBusSubscriber.Bus.MOD)
   public static class HotAnvilModels {
      public static final ResourceLocation PLAIN = new ResourceLocation("som", "block/anvil_hot");
      public static final ResourceLocation CHIPPED = new ResourceLocation("som", "block/chipped_anvil_hot");
      public static final ResourceLocation DAMAGED = new ResourceLocation("som", "block/damaged_anvil_hot");
      public static final ResourceLocation HAMMER = new ResourceLocation("som", "item/hammer_overlay");

      @SubscribeEvent
      public static void onRegisterModels(ModelEvent.RegisterAdditional event) {
         event.register(PLAIN);
         event.register(CHIPPED);
         event.register(DAMAGED);
         event.register(HAMMER);
      }
   }
}
