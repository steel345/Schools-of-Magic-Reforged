package com.paleimitations.schoolsofmagic.client.tileentity.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityCatalystBasin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class TileEntityRendererCatalystBasin implements BlockEntityRenderer<TileEntityCatalystBasin> {

   public TileEntityRendererCatalystBasin(BlockEntityRendererProvider.Context ctx) {
   }

   @Override
   public void render(TileEntityCatalystBasin te, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
      IItemHandler itemHandler = te.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
      if (itemHandler == null) {
         return;
      }
      ItemStack itemStack0 = itemHandler.getStackInSlot(0);
      ItemStack itemStack1 = itemHandler.getStackInSlot(1);
      ItemStack itemStack2 = itemHandler.getStackInSlot(2);
      ItemStack itemStack3 = itemHandler.getStackInSlot(3);
      Minecraft mc = Minecraft.getInstance();

      renderSlot(poseStack, buffer, mc, packedLight, packedOverlay, itemStack0, 0.375F, 0.625F, 0.625F, 90.0F);
      renderSlot(poseStack, buffer, mc, packedLight, packedOverlay, itemStack1, 0.625F, 0.625F, 0.625F, 90.0F);
      renderSlot(poseStack, buffer, mc, packedLight, packedOverlay, itemStack2, 0.625F, 0.625F, 0.375F, -90.0F);
      renderSlot(poseStack, buffer, mc, packedLight, packedOverlay, itemStack3, 0.375F, 0.625F, 0.375F, -90.0F);

      if (te.isActive()) {
         renderActiveOverlay(poseStack, buffer);
      }
   }

   private static final ResourceLocation ACTIVE_OVERLAY =
      new ResourceLocation("som", "textures/blocks/basin_active_overlay.png");

   /** Glowing (fullbright, additive) overlay laid over the top face while the basin is brewing. */
   private static void renderActiveOverlay(PoseStack poseStack, MultiBufferSource buffer) {
      VertexConsumer vc = buffer.getBuffer(RenderType.eyes(ACTIVE_OVERLAY));
      Matrix4f mat = poseStack.last().pose();
      Matrix3f nrm = poseStack.last().normal();
      float y = 1.001F;
      // top-facing
      v(vc, mat, nrm, 0, y, 1, 0, 1, 1.0F);
      v(vc, mat, nrm, 0, y, 0, 0, 0, 1.0F);
      v(vc, mat, nrm, 1, y, 0, 1, 0, 1.0F);
      v(vc, mat, nrm, 1, y, 1, 1, 1, 1.0F);
      // reverse winding so it shows even if culled from above
      v(vc, mat, nrm, 1, y, 1, 1, 1, -1.0F);
      v(vc, mat, nrm, 1, y, 0, 1, 0, -1.0F);
      v(vc, mat, nrm, 0, y, 0, 0, 0, -1.0F);
      v(vc, mat, nrm, 0, y, 1, 0, 1, -1.0F);
   }

   private static void v(VertexConsumer vc, Matrix4f mat, Matrix3f nrm, float x, float y, float z, float u, float w, float ny) {
      vc.vertex(mat, x, y, z)
         .color(255, 255, 255, 255)
         .uv(u, w)
         .overlayCoords(OverlayTexture.NO_OVERLAY)
         .uv2(0xF000F0)
         .normal(nrm, 0.0F, ny, 0.0F)
         .endVertex();
   }

   private static void renderSlot(PoseStack poseStack, MultiBufferSource buffer, Minecraft mc, int packedLight, int packedOverlay, ItemStack stack, float dx, float dy, float dz, float pitchDeg) {
      poseStack.pushPose();
      poseStack.translate(dx, dy, dz);
      poseStack.mulPose(Axis.XP.rotationDegrees(pitchDeg));
      poseStack.scale(0.5F, 0.5F, 0.5F);
      mc.getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND,
         packedLight, packedOverlay, poseStack, buffer, mc.level, 0);
      poseStack.popPose();
   }
}
