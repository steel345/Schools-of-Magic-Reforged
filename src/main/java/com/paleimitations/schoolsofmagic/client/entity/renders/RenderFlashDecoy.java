package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.paleimitations.schoolsofmagic.common.entity.EntityFlashDecoy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class RenderFlashDecoy extends LivingEntityRenderer<EntityFlashDecoy, PlayerModel<EntityFlashDecoy>> {
   private static ResourceLocation fallbackFor(java.util.UUID id) {
      return id == null
         ? net.minecraft.client.resources.DefaultPlayerSkin.getDefaultSkin()
         : net.minecraft.client.resources.DefaultPlayerSkin.getDefaultSkin(id);
   }

   public RenderFlashDecoy(EntityRendererProvider.Context context) {
      super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
      this.addLayer(new HumanoidArmorLayer<>(this,
         new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
         new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
         context.getModelManager()));
      this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));

      this.addLayer(new com.paleimitations.schoolsofmagic.client.entity.layers.LayerWand<>(this));
   }

   @Override
   public ResourceLocation getTextureLocation(EntityFlashDecoy decoy) {
      if (decoy.getOwnerId() != null && Minecraft.getInstance().level != null) {
         Player owner = Minecraft.getInstance().level.getPlayerByUUID(decoy.getOwnerId());
         if (owner instanceof AbstractClientPlayer client) {
            return client.getSkinTextureLocation();
         }
      }
      return fallbackFor(decoy.getOwnerId());
   }

   private static net.minecraft.client.model.HumanoidModel.ArmPose armPose(
         EntityFlashDecoy decoy, net.minecraft.world.InteractionHand hand) {
      net.minecraft.world.item.ItemStack held = decoy.getItemInHand(hand);
      if (held.isEmpty()) return net.minecraft.client.model.HumanoidModel.ArmPose.EMPTY;
      if (held.getItem() instanceof net.minecraft.world.item.CrossbowItem) {
         return net.minecraft.client.model.HumanoidModel.ArmPose.CROSSBOW_HOLD;
      }
      return net.minecraft.client.model.HumanoidModel.ArmPose.ITEM;
   }

   @Override
   protected void setupRotations(EntityFlashDecoy decoy, PoseStack pose, float age, float bodyYaw, float partial) {
      super.setupRotations(decoy, pose, age, bodyYaw, partial);
   }

   @Override
   protected void scale(EntityFlashDecoy decoy, PoseStack pose, float partial) {
      pose.scale(0.9375F, 0.9375F, 0.9375F);
   }

   @Override
   public void render(EntityFlashDecoy decoy, float yaw, float partial, PoseStack pose,
                      MultiBufferSource buffer, int light) {
      this.model.crouching = decoy.isCrouchingPose();
      this.model.young = false;

      this.model.rightArmPose = armPose(decoy, net.minecraft.world.InteractionHand.MAIN_HAND);
      this.model.leftArmPose = armPose(decoy, net.minecraft.world.InteractionHand.OFF_HAND);
      super.render(decoy, yaw, partial, pose, buffer, light);
   }
}
