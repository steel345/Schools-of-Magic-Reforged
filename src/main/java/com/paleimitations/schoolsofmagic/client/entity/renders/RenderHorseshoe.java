package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityHorseshoe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RenderHorseshoe extends EntityRenderer<EntityHorseshoe> {
   private static final ResourceLocation TEXTURE =
      new ResourceLocation("som", "textures/items/iron_horseshoe.png");

   public RenderHorseshoe(EntityRendererProvider.Context context) {
      super(context);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityHorseshoe entity) {
      return TEXTURE;
   }

   @Override
   public void render(EntityHorseshoe shoe, float yaw, float partialTicks,
                      PoseStack pose, MultiBufferSource buf, int light) {
      ItemStack held = shoe.shoe();
      if (held.isEmpty()) return;

      pose.pushPose();
      pose.mulPose(Axis.YP.rotationDegrees(Mth.rotLerp(partialTicks, shoe.spin() - 45.0F, shoe.spin())));
      pose.mulPose(Axis.XP.rotationDegrees(90.0F));
      pose.scale(0.75F, 0.75F, 0.75F);
      Minecraft.getInstance().getItemRenderer().renderStatic(held, ItemDisplayContext.FIXED,
         light, OverlayTexture.NO_OVERLAY, pose, buf, shoe.level(), shoe.getId());
      pose.popPose();

      super.render(shoe, yaw, partialTicks, pose, buf, light);
   }
}
