package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.paleimitations.schoolsofmagic.client.entity.model.ModelPhoenix;
import com.paleimitations.schoolsofmagic.common.entity.EntityPhoenix;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RenderPhoenix extends MobRenderer<EntityPhoenix, ModelPhoenix<EntityPhoenix>> {
   public static final ResourceLocation TEXTURES = new ResourceLocation("som", "textures/entity/phoenix.png");
   public static boolean RENDERING_GUI = false;

   public RenderPhoenix(EntityRendererProvider.Context context) {
      super(context, new ModelPhoenix<>(context.bakeLayer(ModelPhoenix.LAYER_LOCATION)), 0.5F);
      this.addLayer(new com.paleimitations.schoolsofmagic.client.entity.layers.LayerPhoenixCold(this));
   }

   @Override
   public ResourceLocation getTextureLocation(EntityPhoenix entity) {
      return TEXTURES;
   }

   @Override
   protected void scale(EntityPhoenix entity, PoseStack poseStack, float partialTick) {
      if (entity.isBaby()) poseStack.scale(0.5F, 0.5F, 0.5F);
   }

   @Override
   protected net.minecraft.client.renderer.RenderType getRenderType(EntityPhoenix entity, boolean bodyVisible, boolean translucent, boolean glowing) {
      float fade = entity.getFade();
      if (fade > 0.001F && fade < 0.999F) {
         return net.minecraft.client.renderer.RenderType.entityTranslucent(this.getTextureLocation(entity));
      }
      return super.getRenderType(entity, bodyVisible, translucent, glowing);
   }

   @Override
   public void render(EntityPhoenix entity, float entityYaw, float partialTick, PoseStack poseStack,
                      MultiBufferSource buffer, int packedLight) {
      float fade = entity.getFade();
      if (fade >= 0.999F) return;
      if (fade > 0.001F) {
         super.render(entity, entityYaw, partialTick, poseStack, new FadeBufferSource(buffer, 1.0F - fade), packedLight);
         return;
      }
      super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
      if (RENDERING_GUI) return;
      float bodyYaw = Mth.rotLerp(partialTick, entity.yBodyRotO, entity.yBodyRot);
      float h = entity.getBbHeight();

      ItemStack carried = entity.getCarried();
      if (!carried.isEmpty() && !entity.isVehicle() && !entity.isFlying()) {
         double beakY = entity.isInSittingPose() ? h * 0.5D : h * 0.82D;
         poseStack.pushPose();
         poseStack.translate(0.0D, beakY, 0.0D);
         poseStack.mulPose(Axis.YP.rotationDegrees(-bodyYaw));
         poseStack.translate(0.0D, 0.0D, 1.15D);
         poseStack.mulPose(Axis.XP.rotationDegrees(75.0F));
         poseStack.scale(0.5F, 0.5F, 0.5F);
         draw(carried, ItemDisplayContext.GROUND, poseStack, buffer, packedLight, entity);
         poseStack.popPose();
      }

      if (entity.isChested() && !entity.isInvisible()) {
         double sideOff = entity.isFlying() ? 0.3D : 0.45D;
         for (int side = -1; side <= 1; side += 2) {
            poseStack.pushPose();
            poseStack.translate(0.0D, h * 0.42D, 0.0D);
            poseStack.mulPose(Axis.YP.rotationDegrees(-bodyYaw));
            poseStack.translate(side * sideOff, 0.0D, 0.1D);
            poseStack.mulPose(Axis.YP.rotationDegrees(side > 0 ? 270.0F : 90.0F));
            poseStack.scale(0.55F, 0.55F, 0.55F);
            draw(new ItemStack(net.minecraft.world.item.Items.CHEST), ItemDisplayContext.FIXED, poseStack, buffer, packedLight, entity);
            poseStack.popPose();
         }
      }
   }

   private static void draw(ItemStack stack, ItemDisplayContext ctx, PoseStack poseStack, MultiBufferSource buffer, int light, EntityPhoenix entity) {
      Minecraft.getInstance().getItemRenderer().renderStatic(stack, ctx, light, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), entity.getId());
   }
}
