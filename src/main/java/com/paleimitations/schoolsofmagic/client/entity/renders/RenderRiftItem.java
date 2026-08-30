package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.common.entity.EntityRiftItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

// laid flat on the floor and left alone. no bobbing, no turning, no stack of copies behind it
public class RenderRiftItem extends EntityRenderer<EntityRiftItem> {
   public RenderRiftItem(EntityRendererProvider.Context context) {
      super(context);
      this.shadowRadius = 0.12F;
      this.shadowStrength = 0.5F;
   }

   @Override
   public ResourceLocation getTextureLocation(EntityRiftItem entity) {
      return net.minecraft.client.renderer.texture.TextureAtlas.LOCATION_BLOCKS;
   }

   @Override
   public void render(EntityRiftItem entity, float yaw, float partialTicks,
                      PoseStack pose, MultiBufferSource buf, int light) {
      ItemStack stack = entity.shown();
      if (stack.isEmpty()) return;

      pose.pushPose();
      pose.translate(0.0D, 0.06D, 0.0D);
      pose.mulPose(Axis.XP.rotationDegrees(90.0F));
      pose.mulPose(Axis.ZP.rotationDegrees(entity.lie()));
      pose.scale(0.9F, 0.9F, 0.9F);

      Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED,
         light, OverlayTexture.NO_OVERLAY, pose, buf, entity.level(), entity.getId());

      pose.popPose();
      super.render(entity, yaw, partialTicks, pose, buf, light);
   }
}
