package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMysteriousPlane;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RenderMysteriousPlane extends EntityRenderer<EntityMysteriousPlane> {

   private static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/atlas/blocks.png");

   public RenderMysteriousPlane(EntityRendererProvider.Context context) {
      super(context);
   }

   @Override
   public void render(EntityMysteriousPlane entity, float entityYaw, float partialTick, PoseStack poseStack,
                      MultiBufferSource buffer, int packedLight) {
      float fade = entity.getFade();
      if (fade >= 0.99F) return;
      float scale = 0.9F * (1.0F - fade);
      poseStack.pushPose();
      poseStack.scale(scale, scale, scale);
      float spin = (entity.tickCount + partialTick) * 18.0F;
      poseStack.mulPose(Axis.YP.rotationDegrees(-entityYaw + 90.0F));
      poseStack.mulPose(Axis.ZP.rotationDegrees(spin));
      poseStack.mulPose(Axis.XP.rotationDegrees(spin * 0.6F));
      ItemStack stack = new ItemStack(ItemRegistry.mysterious_application.get());
      Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, packedLight,
         OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), entity.getId());
      poseStack.popPose();
      super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityMysteriousPlane entity) {
      return TEXTURE;
   }
}
