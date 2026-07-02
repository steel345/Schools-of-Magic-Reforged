package com.paleimitations.schoolsofmagic.client.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;

import com.paleimitations.schoolsofmagic.client.entity.model.ModelSqueakard;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderSqueakard;
import com.paleimitations.schoolsofmagic.common.entity.EntitySqueakard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerSqueakardHeldItem extends RenderLayer<EntitySqueakard, ModelSqueakard<EntitySqueakard>> {

   public LayerSqueakardHeldItem(RenderSqueakard renderer) {
      super(renderer);
   }

   @Override
   public void render(PoseStack pose, MultiBufferSource buf, int packedLight,
                      EntitySqueakard entity, float limbSwing, float limbSwingAmount,
                      float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      boolean flag = entity.getMainArm() == HumanoidArm.RIGHT;
      ItemStack stackLeftFromHand = flag ? entity.getOffhandItem() : entity.getMainHandItem();
      ItemStack stackRightFromHand = flag ? entity.getMainHandItem() : entity.getOffhandItem();
      if (!stackLeftFromHand.isEmpty() || !stackRightFromHand.isEmpty()) {
         pose.pushPose();
         if (this.getParentModel().young) {
            pose.translate(0.0F, 0.75F, 0.0F);
            pose.scale(0.5F, 0.5F, 0.5F);
         }
         this.renderHeldItem(pose, buf, packedLight, entity, stackRightFromHand, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT);
         this.renderHeldItem(pose, buf, packedLight, entity, stackLeftFromHand, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, HumanoidArm.LEFT);
         pose.popPose();
      }
   }

   private void renderHeldItem(PoseStack pose, MultiBufferSource buf, int packedLight,
                               EntitySqueakard entity, ItemStack stack, ItemDisplayContext ctx, HumanoidArm handSide) {
      if (stack.isEmpty()) return;
      pose.pushPose();
      if (entity.isShiftKeyDown()) pose.translate(0.0F, 0.2F, 0.0F);
      this.translateToHand(pose, handSide);
      pose.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-90.0F));
      pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees(180.0F));
      boolean flag = handSide == HumanoidArm.LEFT;
      pose.translate((float) (flag ? -1 : 1) / 16.0F, 0.125F, -0.625F);
      Minecraft.getInstance().getItemRenderer().renderStatic(
            entity, stack, ctx, flag, pose, buf, entity.level(), packedLight,
            net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY, entity.getId());
      pose.popPose();
   }

   protected void translateToHand(PoseStack pose, HumanoidArm side) {
      this.getParentModel().postRenderArm(pose, side);
   }
}
