package com.paleimitations.schoolsofmagic.client.entity.layers;

import java.util.Map;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;

import com.paleimitations.schoolsofmagic.client.items.models.ModelWand;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.CapabilityWandData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class LayerWand<T extends LivingEntity, M extends HumanoidModel<T>>
      extends RenderLayer<T, M> {

   private static final Map<String, ResourceLocation> LAYERED_LOCATION_CACHE = Maps.newHashMap();

   public LayerWand(LivingEntityRenderer<T, M> renderer) {
      super(renderer);
   }

   @Override
   public void render(PoseStack pose, MultiBufferSource buf, int packedLight,
                      T entity, float limbSwing, float limbSwingAmount,
                      float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

      boolean flag = entity.getMainArm() == HumanoidArm.RIGHT;
      ItemStack stackLeftHandFromPlayer = flag ? entity.getOffhandItem() : entity.getMainHandItem();
      ItemStack stackRightHandFromPlayer = flag ? entity.getMainHandItem() : entity.getOffhandItem();
      boolean hasWandL = stackLeftHandFromPlayer.getCapability(CapabilityWandData.WAND_DATA_CAPABILITY).isPresent();
      boolean hasWandR = stackRightHandFromPlayer.getCapability(CapabilityWandData.WAND_DATA_CAPABILITY).isPresent();
      if (hasWandL || hasWandR) {
         pose.pushPose();
         if (this.getParentModel().young) {
            pose.translate(0.0F, 0.75F, 0.0F);
            pose.scale(0.5F, 0.5F, 0.5F);
         }
         this.renderHeldItem(pose, buf, packedLight, entity, stackRightHandFromPlayer, HumanoidArm.RIGHT);
         this.renderHeldItem(pose, buf, packedLight, entity, stackLeftHandFromPlayer, HumanoidArm.LEFT);
         pose.popPose();
      }
   }

   private void renderHeldItem(PoseStack pose, MultiBufferSource buf, int packedLight,
                               T entity, ItemStack stack, HumanoidArm handSide) {

      boolean isApprentice = stack.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemApprenticeWand;
      IWandData data = stack.getCapability(CapabilityWandData.WAND_DATA_CAPABILITY).orElse(null);
      if (data == null) return;
      if (!isApprentice && data.getCoreType() == null) return;
      pose.pushPose();
      if (entity.isShiftKeyDown()) pose.translate(0.0F, 1.2F, 0.0F);
      this.translateToHand(pose, handSide);
      pose.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-90.0F));
      pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees(180.0F));
      boolean flag = handSide == HumanoidArm.LEFT;
      pose.translate((float) (flag ? -1 : 1) / 16.0F, 0.125F, -0.625F);
      if (isApprentice) {

         int rank = net.minecraft.util.Mth.clamp(stack.getDamageValue(), 0, 3);
         new com.paleimitations.schoolsofmagic.client.items.models.ModelApprenticeWand()
            .render(pose, buf, packedLight, 0.0625F, rank);
      } else {

         ModelWand.getWandTexture(data);
         new ModelWand().render(pose, buf, packedLight, 0.0625F);
      }
      pose.popPose();
   }

   public static ResourceLocation getWandTexture(IWandData data) {
      return ModelWand.getWandTexture(data);
   }

   protected void translateToHand(PoseStack pose, HumanoidArm side) {
      this.getParentModel().translateToHand(side, pose);
   }
}
