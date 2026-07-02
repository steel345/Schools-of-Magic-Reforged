package com.paleimitations.schoolsofmagic.client.tileentity.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.client.tileentity.models.ModelSpellObelisk;
import com.paleimitations.schoolsofmagic.common.blocks.BlockSpellForge;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySpellObelisk;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityRendererSpellObelisk implements BlockEntityRenderer<TileEntitySpellObelisk> {
   private static final ResourceLocation TEXTURE =
      new ResourceLocation("som", "textures/entity/spellobelisk.png");
   private final ModelSpellObelisk model;

   public TileEntityRendererSpellObelisk(BlockEntityRendererProvider.Context ctx) {
      this.model = new ModelSpellObelisk(ctx.bakeLayer(ModelSpellObelisk.LAYER_LOCATION));
   }

   @Override
   public void render(TileEntitySpellObelisk te, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

      int j = 0;
      if (te.hasLevel()) {
         BlockState state = te.getLevel().getBlockState(te.getBlockPos());
         if (state.getBlock() instanceof BlockSpellForge) {
            Direction facing = state.getValue(BlockSpellForge.FACING);
            if (facing == Direction.NORTH) j = 180;
            else if (facing == Direction.SOUTH) j = 0;
            else if (facing == Direction.EAST) j = 90;
            else if (facing == Direction.WEST) j = -90;
         }
      }
      poseStack.pushPose();
      poseStack.translate(0.5D, 1.5D, 0.5D);
      poseStack.mulPose(Axis.YP.rotationDegrees((float)j));
      poseStack.scale(1.0F, -1.0F, -1.0F);
      VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
      int tick = (int)(te.getLevel() != null ? te.getLevel().getDayTime() % 20L : 0L);
      this.model.renderAll(poseStack, consumer, packedLight, packedOverlay, tick, te);
      poseStack.popPose();
   }
}
