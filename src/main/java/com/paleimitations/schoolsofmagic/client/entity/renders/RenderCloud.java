package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;

import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityCloud;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderCloud extends EntityRenderer<EntityCloud> {
   public static final ResourceLocation TEXTURES = new ResourceLocation("som", "textures/entity/cocoon.png");

   public RenderCloud(EntityRendererProvider.Context context) {
      super(context);
   }

   @Override
   public void render(EntityCloud entity, float entityYaw, float partialTicks,
                      PoseStack pose, MultiBufferSource buf, int packedLight) {

   }

   @Override
   public ResourceLocation getTextureLocation(EntityCloud entity) {
      return TEXTURES;
   }
}
