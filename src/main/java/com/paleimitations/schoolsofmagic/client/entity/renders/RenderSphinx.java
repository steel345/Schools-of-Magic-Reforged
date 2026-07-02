package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.paleimitations.schoolsofmagic.client.entity.model.ModelSphinx;
import com.paleimitations.schoolsofmagic.common.entity.EntitySphinx;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderSphinx extends MobRenderer<EntitySphinx, ModelSphinx<EntitySphinx>> {
   public static final ResourceLocation TEXTURES = new ResourceLocation("som", "textures/entity/sphinx.png");

   public RenderSphinx(EntityRendererProvider.Context context) {
      super(context, new ModelSphinx<>(context.bakeLayer(ModelSphinx.LAYER_LOCATION)), 0.3F);
   }

   @Override
   public ResourceLocation getTextureLocation(EntitySphinx entity) {
      return TEXTURES;
   }
}
