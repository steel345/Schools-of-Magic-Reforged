package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.paleimitations.schoolsofmagic.client.entity.model.ModelDemon;
import com.paleimitations.schoolsofmagic.common.entity.EntityDemon;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderDemon extends MobRenderer<EntityDemon, ModelDemon<EntityDemon>> {
   public static final ResourceLocation TEXTURES = new ResourceLocation("som", "textures/entity/demon.png");

   public RenderDemon(EntityRendererProvider.Context context) {
      super(context, new ModelDemon<>(context.bakeLayer(ModelDemon.LAYER_LOCATION)), 0.5F);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityDemon entity) {
      return TEXTURES;
   }
}
