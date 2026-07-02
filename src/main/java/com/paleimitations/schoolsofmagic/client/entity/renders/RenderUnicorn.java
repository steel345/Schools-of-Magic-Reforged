package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.paleimitations.schoolsofmagic.client.entity.model.ModelUnicorn;
import com.paleimitations.schoolsofmagic.common.entity.EntityUnicorn;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderUnicorn extends MobRenderer<EntityUnicorn, ModelUnicorn<EntityUnicorn>> {
   public static final ResourceLocation TEXTURES = new ResourceLocation("som", "textures/entity/unicorn.png");

   public RenderUnicorn(EntityRendererProvider.Context context) {
      super(context, new ModelUnicorn<>(context.bakeLayer(ModelUnicorn.LAYER_LOCATION)), 0.5F);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityUnicorn entity) {
      return TEXTURES;
   }
}
