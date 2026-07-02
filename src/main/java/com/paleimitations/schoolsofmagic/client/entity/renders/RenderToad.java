package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.paleimitations.schoolsofmagic.client.entity.model.ModelToad;
import com.paleimitations.schoolsofmagic.common.entity.EntityToad;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderToad extends MobRenderer<EntityToad, ModelToad> {
   private static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
      new ResourceLocation("som", "textures/entity/toad0.png"),
      new ResourceLocation("som", "textures/entity/toad1.png"),
      new ResourceLocation("som", "textures/entity/toad2.png"),
      new ResourceLocation("som", "textures/entity/toad3.png"),
      new ResourceLocation("som", "textures/entity/toad4.png"),
      new ResourceLocation("som", "textures/entity/toad5.png"),
      new ResourceLocation("som", "textures/entity/toad6.png"),
      new ResourceLocation("som", "textures/entity/toad7.png"),
      new ResourceLocation("som", "textures/entity/toad8.png"),
      new ResourceLocation("som", "textures/entity/toad9.png"),
      new ResourceLocation("som", "textures/entity/toad10.png"),
      new ResourceLocation("som", "textures/entity/toad11.png")
   };

   public RenderToad(EntityRendererProvider.Context context) {
      super(context, new ModelToad(context.bakeLayer(ModelToad.LAYER_LOCATION)), 0.2F);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityToad entity) {
      int s = entity.getToadType();
      if (s >= 0 && s < TEXTURES.length) return TEXTURES[s];
      return TEXTURES[0];
   }
}
