package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityBlockProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class RenderBlockProjectile<T extends EntityBlockProjectile> extends EntityRenderer<T> {

   public RenderBlockProjectile(EntityRendererProvider.Context context) {
      super(context);
   }

   @Override
   public ResourceLocation getTextureLocation(T entity) {
      return InventoryMenu.BLOCK_ATLAS;
   }

   @Override
   public void render(T entity, float entityYaw, float partialTicks, PoseStack pose, MultiBufferSource buf, int packedLight) {
      pose.pushPose();
      float scale = entity.getRenderScale();
      float spin = ((float) entity.tickCount + partialTicks) * 12.0F;
      pose.scale(scale, scale, scale);

      pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees(spin));
      pose.mulPose(com.mojang.math.Axis.XP.rotationDegrees(spin * 0.6F));
      pose.translate(-0.5, -0.5, -0.5);
      net.minecraft.client.Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
         entity.getRenderBlock(), pose, buf, packedLight, OverlayTexture.NO_OVERLAY);
      pose.popPose();
      super.render(entity, entityYaw, partialTicks, pose, buf, packedLight);
   }
}
