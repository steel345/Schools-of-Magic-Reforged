package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;

import com.paleimitations.schoolsofmagic.common.entity.EntityHuman;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderHuman extends MobRenderer<EntityHuman, PlayerModel<EntityHuman>> {

   public RenderHuman(EntityRendererProvider.Context context) {
      super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
      this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
      this.addLayer(new HumanoidArmorLayer<>(this,
            new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
            new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
            context.getModelManager()));
      this.addLayer(new ArrowLayer<>(context, this));
   }

   private static final ResourceLocation FALLBACK = new ResourceLocation("som", "textures/entity/human/skin0.png");

   @Override
   public ResourceLocation getTextureLocation(EntityHuman entity) {
      return LayeredEntityTexture.get(entity.getHumanTexture(), entity.getVariantTexturePaths(), "human", FALLBACK);
   }

   private void setModelVisibilities(EntityHuman entity) {
      PlayerModel<EntityHuman> model = this.getModel();
      ItemStack mainHand = entity.getMainHandItem();
      ItemStack offHand = entity.getOffhandItem();
      model.setAllVisible(true);
      model.crouching = entity.isShiftKeyDown();
      HumanoidModel.ArmPose mainPose = HumanoidModel.ArmPose.EMPTY;
      HumanoidModel.ArmPose offPose = HumanoidModel.ArmPose.EMPTY;
      if (!mainHand.isEmpty()) {
         mainPose = HumanoidModel.ArmPose.ITEM;
         if (entity.getUseItemRemainingTicks() > 0) {
            UseAnim anim = mainHand.getUseAnimation();
            if (anim == UseAnim.BLOCK) mainPose = HumanoidModel.ArmPose.BLOCK;
            else if (anim == UseAnim.BOW) mainPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
         }
      }
      if (!offHand.isEmpty()) {
         offPose = HumanoidModel.ArmPose.ITEM;
         if (entity.getUseItemRemainingTicks() > 0) {
            UseAnim anim = offHand.getUseAnimation();
            if (anim == UseAnim.BLOCK) offPose = HumanoidModel.ArmPose.BLOCK;
            else if (anim == UseAnim.BOW) offPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
         }
      }
      if (entity.getMainArm() == HumanoidArm.RIGHT) {
         model.rightArmPose = mainPose;
         model.leftArmPose = offPose;
      } else {
         model.rightArmPose = offPose;
         model.leftArmPose = mainPose;
      }
   }

   @Override
   protected void setupRotations(EntityHuman entity, PoseStack pose, float ageInTicks, float rotationYaw, float partialTicks) {
      if (entity.isAlive() && entity.isSleeping()) {
         pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees(this.getFlipDegrees(entity)));
         pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees(270.0F));
      } else if (entity.isFallFlying()) {
         super.setupRotations(entity, pose, ageInTicks, rotationYaw, partialTicks);
         float f = (float) entity.getFallFlyingTicks() + partialTicks;
         float f1 = Mth.clamp(f * f / 100.0F, 0.0F, 1.0F);
         pose.mulPose(com.mojang.math.Axis.XP.rotationDegrees(f1 * (-90.0F - entity.getXRot())));
         Vec3 vec3 = entity.getViewVector(partialTicks);
         Vec3 motion = entity.getDeltaMovement();
         double d0 = motion.horizontalDistanceSqr();
         double d1 = vec3.horizontalDistanceSqr();
         if (d0 > 0.0 && d1 > 0.0) {
            double d2 = (motion.x * vec3.x + motion.z * vec3.z) / Math.sqrt(d0 * d1);
            double d3 = motion.x * vec3.z - motion.z * vec3.x;
            pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees((float) (Math.signum(d3) * Math.acos(d2)) * 180.0F / (float) Math.PI));
         }
      } else {
         super.setupRotations(entity, pose, ageInTicks, rotationYaw, partialTicks);
      }
   }
}
