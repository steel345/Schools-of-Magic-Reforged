package com.paleimitations.schoolsofmagic.client.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.common.handlers.HorseshoeCharmHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class LayerWornHorseshoe<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
   public LayerWornHorseshoe(RenderLayerParent<T, M> parent) {
      super(parent);
   }

   @Override
   public void render(PoseStack pose, MultiBufferSource buf, int light,
                      T entity, float limbSwing, float limbSwingAmount,
                      float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      if (!(entity instanceof Player player) || player.isInvisible()) return;

      if (!com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots.shows(player, com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.IGarmentData.SHOW_CHARM)) return;
      ItemStack shoe = HorseshoeCharmHandler.worn(player);
      if (shoe.isEmpty()) return;

      pose.pushPose();
      this.getParentModel().rightLeg.translateAndRotate(pose);
      pose.translate(-0.14F, 0.53F, 0.0F);
      pose.mulPose(Axis.YP.rotationDegrees(90.0F));
      pose.mulPose(Axis.ZP.rotationDegrees(180.0F));
      pose.scale(0.5F, 0.5F, 0.5F);
      Minecraft.getInstance().getItemRenderer().renderStatic(shoe, ItemDisplayContext.FIXED,
         light, OverlayTexture.NO_OVERLAY, pose, buf, player.level(), player.getId());
      pose.popPose();
   }
}
