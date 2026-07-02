package com.paleimitations.schoolsofmagic.client.tileentity.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityDynamicWeb;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;

public class TileEntityRendererDynamicWeb implements BlockEntityRenderer<TileEntityDynamicWeb> {

   public TileEntityRendererDynamicWeb(BlockEntityRendererProvider.Context ctx) {
   }

   public boolean canConnectTo(LevelReader level, BlockPos pos, Direction facing) {
      BlockState s = level.getBlockState(pos);
      return s.getBlock() == BlockRegistry.dynamic_web.get()
         || (facing != null && s.isFaceSturdy(level, pos, facing));
   }

   @Override
   public void render(TileEntityDynamicWeb te, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
      Level level = te.getLevel();
      if (level == null) return;
      BlockPos pos = te.getBlockPos();

      String xPath = "textures/blocks/webs/"
         + connEdge(level, pos.offset(-1, -1, 0), Direction.UP, Direction.EAST)
         + connEdge(level, pos.offset(0, -1, 0), Direction.UP, null)
         + connEdge(level, pos.offset(1, -1, 0), Direction.UP, Direction.WEST)
         + connEdge(level, pos.offset(1, 0, 0), Direction.WEST, null)
         + connEdge(level, pos.offset(1, 1, 0), Direction.DOWN, Direction.WEST)
         + connEdge(level, pos.offset(0, 1, 0), Direction.DOWN, null)
         + connEdge(level, pos.offset(-1, 1, 0), Direction.DOWN, Direction.EAST)
         + connTail(level, pos.offset(-1, 0, 0), Direction.EAST) + ".png";
      drawWebQuad(poseStack, buffer, packedLight, packedOverlay, new ResourceLocation("som", xPath), 'X');

      String zPath = "textures/blocks/webs/"
         + connEdge(level, pos.offset(0, -1, -1), Direction.UP, Direction.SOUTH)
         + connEdge(level, pos.offset(0, -1, 0), Direction.UP, null)
         + connEdge(level, pos.offset(0, -1, 1), Direction.UP, Direction.NORTH)
         + connEdge(level, pos.offset(0, 0, 1), Direction.NORTH, null)
         + connEdge(level, pos.offset(0, 1, 1), Direction.DOWN, Direction.NORTH)
         + connEdge(level, pos.offset(0, 1, 0), Direction.DOWN, null)
         + connEdge(level, pos.offset(0, 1, -1), Direction.DOWN, Direction.SOUTH)
         + connTail(level, pos.offset(0, 0, -1), Direction.SOUTH) + ".png";
      drawWebQuad(poseStack, buffer, packedLight, packedOverlay, new ResourceLocation("som", zPath), 'Z');

      String yPath = "textures/blocks/webs/"
         + connEdge(level, pos.offset(-1, 0, -1), Direction.SOUTH, Direction.EAST)
         + connEdge(level, pos.offset(0, 0, -1), Direction.SOUTH, null)
         + connEdge(level, pos.offset(1, 0, -1), Direction.SOUTH, Direction.WEST)
         + connEdge(level, pos.offset(1, 0, 0), Direction.WEST, null)
         + connEdge(level, pos.offset(1, 0, 1), Direction.WEST, Direction.NORTH)
         + connEdge(level, pos.offset(0, 0, 1), Direction.NORTH, null)
         + connEdge(level, pos.offset(-1, 0, 1), Direction.NORTH, Direction.EAST)
         + connTail(level, pos.offset(-1, 0, 0), Direction.EAST) + ".png";
      drawWebQuad(poseStack, buffer, packedLight, packedOverlay, new ResourceLocation("som", yPath), 'Y');
   }

   private String connEdge(LevelReader level, BlockPos pos, Direction d1, Direction d2) {
      boolean ok = this.canConnectTo(level, pos, d1) || (d2 != null && this.canConnectTo(level, pos, d2));
      return ok ? "t_" : "f_";
   }

   private String connTail(LevelReader level, BlockPos pos, Direction d) {
      return this.canConnectTo(level, pos, d) ? "t" : "f";
   }

   private void drawWebQuad(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, ResourceLocation tex, char axis) {
      VertexConsumer c = buffer.getBuffer(RenderType.entityCutoutNoCull(tex));
      Matrix4f m = poseStack.last().pose();

      if (axis == 'X') {
         quad(c, m, packedLight, packedOverlay, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F,  1.0F, 0.0F, 0.5F, 0.0F, 1.0F,  1.0F, 1.0F, 0.5F, 0.0F, 0.0F,  0.0F, 1.0F, 0.5F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F);
      } else if (axis == 'Z') {
         quad(c, m, packedLight, packedOverlay, 0.5F, 0.0F, 0.0F, 1.0F, 1.0F,  0.5F, 0.0F, 1.0F, 0.0F, 1.0F,  0.5F, 1.0F, 1.0F, 0.0F, 0.0F,  0.5F, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F);
      } else {
         quad(c, m, packedLight, packedOverlay, 0.0F, 0.5F, 0.0F, 1.0F, 1.0F,  1.0F, 0.5F, 0.0F, 0.0F, 1.0F,  1.0F, 0.5F, 1.0F, 0.0F, 0.0F,  0.0F, 0.5F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F);
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
