package com.paleimitations.schoolsofmagic.client.items;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.CapabilityWandData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class WandItemRenderer extends BlockEntityWithoutLevelRenderer {

   public WandItemRenderer() {
      super(Minecraft.getInstance().getBlockEntityRenderDispatcher(),
            Minecraft.getInstance().getEntityModels());
   }

   @Override
   public void renderByItem(ItemStack stack, ItemDisplayContext ctx, PoseStack pose,
                            MultiBufferSource buffer, int light, int overlay) {

      if (ctx == ItemDisplayContext.FIRST_PERSON_LEFT_HAND
            || ctx == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
            || ctx == ItemDisplayContext.THIRD_PERSON_LEFT_HAND
            || ctx == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) {
         return;
      }

      IWandData data = stack.getCapability(CapabilityWandData.WAND_DATA_CAPABILITY).orElse(null);

      if ((data == null || data.getCoreType() == null)
            && stack.getTag() != null && stack.getTag().contains("wand_data")) {
         com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandData wd =
            new com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandData();
         wd.deserializeNBT(stack.getTag().getCompound("wand_data"));
         data = wd;
      }
      boolean isApprentice = stack.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemApprenticeWand;

      if (ctx == ItemDisplayContext.GROUND || ctx == ItemDisplayContext.FIXED) {
         pose.pushPose();
         pose.translate(0.5D, 0.30D, 0.5D);

         if (isApprentice) {
            int rank = net.minecraft.util.Mth.clamp(stack.getDamageValue(), 0, 3);
            new com.paleimitations.schoolsofmagic.client.items.models.ModelApprenticeWand()
               .render(pose, buffer, light, 0.0625F, rank);
         } else {
            com.paleimitations.schoolsofmagic.client.items.models.ModelWand.getWandTexture(data);
            new com.paleimitations.schoolsofmagic.client.items.models.ModelWand()
               .render(pose, buffer, light, 0.0625F);
         }
         pose.popPose();
         return;
      }

      ResourceLocation tex;
      if (isApprentice) {
         int rank = net.minecraft.util.Mth.clamp(stack.getDamageValue(), 0, 3);
         tex = new ResourceLocation("som", "textures/items/basic_wand_" + rank + ".png");
      } else {

         tex = WandIconCache.getComposited(data);
      }

      pose.pushPose();

      pose.translate(0.5D, 0.5D, 0.5D);

      drawLayeredQuad(pose, buffer, tex, LightTexture.FULL_BRIGHT, overlay);
      pose.popPose();
   }

   private static void drawLayeredQuad(PoseStack pose, MultiBufferSource buffer, ResourceLocation tex,
                                       int light, int overlay) {

      VertexConsumer vc = buffer.getBuffer(RenderType.entityCutoutNoCull(tex));
      var mat = pose.last().pose();
      var nrm = pose.last().normal();

      quad(vc, mat, nrm, -0.5F, -0.5F, 0.0F, 0F, 1F,  0.5F, -0.5F, 0.0F, 1F, 1F,
                       0.5F,  0.5F, 0.0F, 1F, 0F, -0.5F,  0.5F, 0.0F, 0F, 0F,
                       0, 1, 0, light, overlay);
   }

   private static void quad(VertexConsumer vc, org.joml.Matrix4f mat, org.joml.Matrix3f nrm,
                            float x1, float y1, float z1, float u1, float v1,
                            float x2, float y2, float z2, float u2, float v2,
                            float x3, float y3, float z3, float u3, float v3,
                            float x4, float y4, float z4, float u4, float v4,
                            float nx, float ny, float nz, int light, int overlay) {
      vert(vc, mat, nrm, x1, y1, z1, u1, v1, nx, ny, nz, light, overlay);
      vert(vc, mat, nrm, x2, y2, z2, u2, v2, nx, ny, nz, light, overlay);
      vert(vc, mat, nrm, x3, y3, z3, u3, v3, nx, ny, nz, light, overlay);
      vert(vc, mat, nrm, x4, y4, z4, u4, v4, nx, ny, nz, light, overlay);
   }

   private static void vert(VertexConsumer vc, org.joml.Matrix4f mat, org.joml.Matrix3f nrm,
                            float x, float y, float z, float u, float v,
                            float nx, float ny, float nz, int light, int overlay) {
      vc.vertex(mat, x, y, z)
        .color(255, 255, 255, 255)
        .uv(u, v)
        .overlayCoords(overlay)
        .uv2(light)
        .normal(nrm, nx, ny, nz)
        .endVertex();
   }
}
