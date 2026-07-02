package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.paleimitations.schoolsofmagic.client.entity.model.ModelTarantula;
import com.paleimitations.schoolsofmagic.common.entity.EntityTarantula;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderTarantula extends MobRenderer<EntityTarantula, ModelTarantula<EntityTarantula>> {
   public static final ResourceLocation TEXTURES = new ResourceLocation("som", "textures/entity/tarantula.png");

   public RenderTarantula(EntityRendererProvider.Context context) {
      super(context, new ModelTarantula<>(context.bakeLayer(ModelTarantula.LAYER_LOCATION)), 3.0F);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityTarantula entity) {
      return TEXTURES;
   }
}
