package com.paleimitations.schoolsofmagic.client.tileentity.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.paleimitations.schoolsofmagic.common.blocks.BlockSandstoneTablet;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySandstoneTablet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;

public class TileEntityRendererSandstoneTablet implements BlockEntityRenderer<TileEntitySandstoneTablet> {
   public static final ResourceLocation AIR = new ResourceLocation("som", "textures/blocks/air.png");
   public static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/blocks/sandstone_runes.png");

   public TileEntityRendererSandstoneTablet(BlockEntityRendererProvider.Context ctx) {
   }

   @Override
   public void render(TileEntitySandstoneTablet te, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
      if (te.getLevel() == null) return;
      BlockState state = te.getLevel().getBlockState(te.getBlockPos());

      if (!state.hasProperty(BlockSandstoneTablet.FACING)) return;
      Direction facing = state.getValue(BlockSandstoneTablet.FACING);
      VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
      Matrix4f m = poseStack.last().pose();
      if (facing == Direction.SOUTH) {
         quad(consumer, m, packedLight, packedOverlay, 0.0F, 0.0F, 0.13F, 0.0F, 1.0F,  1.0F, 0.0F, 0.13F, 1.0F, 1.0F,  1.0F, 1.0F, 0.13F, 1.0F, 0.0F,  0.0F, 1.0F, 0.13F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0F);
      } else if (facing == Direction.NORTH) {
         quad(consumer, m, packedLight, packedOverlay, 1.0F, 1.0F, 0.87F, 0.0F, 0.0F,  1.0F, 0.0F, 0.87F, 0.0F, 1.0F,  0.0F, 0.0F, 0.87F, 1.0F, 1.0F,  0.0F, 1.0F, 0.87F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F);
      } else if (facing == Direction.WEST) {
         quad(consumer, m, packedLight, packedOverlay, 0.87F, 0.0F, 0.0F, 0.0F, 1.0F,  0.87F, 0.0F, 1.0F, 1.0F, 1.0F,  0.87F, 1.0F, 1.0F, 1.0F, 0.0F,  0.87F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F);
      } else if (facing == Direction.EAST) {
         quad(consumer, m, packedLight, packedOverlay, 0.13F, 1.0F, 1.0F, 0.0F, 0.0F,  0.13F, 0.0F, 1.0F, 0.0F, 1.0F,  0.13F, 0.0F, 0.0F, 1.0F, 1.0F,  0.13F, 1.0F, 0.0F, 1.0F, 0.0F, -1.0F, 0.0F, 0.0F);
      }
   }

   private static void quad(VertexConsumer c, Matrix4f m, int light, int overlay,
                            float x1, float y1, float z1, float u1, float v1,
                            float x2, float y2, float z2, float u2, float v2,
                            float x3, float y3, float z3, float u3, float v3,
                            float x4, float y4, float z4, float u4, float v4,
                            float nx, float ny, float nz) {
      v(c, m, light, overlay, x1, y1, z1, u1, v1, nx, ny, nz);
      v(c, m, light, overlay, x2, y2, z2, u2, v2, nx, ny, nz);
      v(c, m, light, overlay, x3, y3, z3, u3, v3, nx, ny, nz);
      v(c, m, light, overlay, x4, y4, z4, u4, v4, nx, ny, nz);
   }

   private static void v(VertexConsumer c, Matrix4f m, int light, int overlay, float x, float y, float z, float u, float v, float nx, float ny, float nz) {
      c.vertex(m, x, y, z).color(1.0F, 1.0F, 1.0F, 1.0F).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(nx, ny, nz).endVertex();
   }
}
