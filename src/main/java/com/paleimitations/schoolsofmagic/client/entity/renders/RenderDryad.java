package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.paleimitations.schoolsofmagic.client.entity.model.ModelDryad;
import com.paleimitations.schoolsofmagic.common.entity.EntityDryad;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderDryad extends MobRenderer<EntityDryad, ModelDryad<EntityDryad>> {
   private static final ResourceLocation[] TEX = new ResourceLocation[] {
      new ResourceLocation("som", "textures/entity/dryad_ash.png"),
      new ResourceLocation("som", "textures/entity/dryad_elder.png"),
      new ResourceLocation("som", "textures/entity/dryad_pine.png"),
      new ResourceLocation("som", "textures/entity/dryad_willow.png"),
      new ResourceLocation("som", "textures/entity/dryad_yew.png"),
      new ResourceLocation("som", "textures/entity/dryad_verde.png"),
      new ResourceLocation("som", "textures/entity/dryad_oak.png"),
      new ResourceLocation("som", "textures/entity/dryad_birch.png"),
      new ResourceLocation("som", "textures/entity/dryad_spruce.png"),
      new ResourceLocation("som", "textures/entity/dryad_doak.png"),
      new ResourceLocation("som", "textures/entity/dryad_jungle.png"),
      new ResourceLocation("som", "textures/entity/dryad_acacia.png"),
   };

   public RenderDryad(EntityRendererProvider.Context context) {
      super(context, new ModelDryad<>(context.bakeLayer(ModelDryad.LAYER_LOCATION)), 0.5F);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityDryad entity) {
      int s = entity.getDryadType();
      if (s >= 0 && s < TEX.length) return TEX[s];
      return TEX[0];
   }
}
