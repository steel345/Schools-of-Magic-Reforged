package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;

import com.paleimitations.schoolsofmagic.client.entity.model.ModelCocoon;
import com.paleimitations.schoolsofmagic.common.entity.EntityWebbedCocoon;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderCocoon extends MobRenderer<EntityWebbedCocoon, ModelCocoon<EntityWebbedCocoon>> {
   public static final ResourceLocation TEXTURES = new ResourceLocation("som", "textures/entity/cocoon.png");

   public RenderCocoon(EntityRendererProvider.Context context) {
      super(context, new ModelCocoon<>(context.bakeLayer(ModelCocoon.LAYER_LOCATION)), 0.5F);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityWebbedCocoon entity) {
      return TEXTURES;
   }

   @Override
   protected void scale(EntityWebbedCocoon entity, PoseStack pose, float partialTickTime) {
      pose.scale(entity.getBbWidth(), entity.getBbHeight(), entity.getBbWidth());
   }
}
