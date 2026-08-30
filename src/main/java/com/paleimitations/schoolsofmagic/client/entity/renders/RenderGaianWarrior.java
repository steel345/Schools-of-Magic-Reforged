package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.paleimitations.schoolsofmagic.client.entity.model.ModelGaianWarrior;
import com.paleimitations.schoolsofmagic.common.entity.EntityGaianWarrior;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderGaianWarrior extends MobRenderer<EntityGaianWarrior, ModelGaianWarrior> {
   private static final ResourceLocation[] TEXTURES = {
      new ResourceLocation("som", "textures/entity/gaian_warrior/clay.png"),
      new ResourceLocation("som", "textures/entity/gaian_warrior/dirt.png"),
      new ResourceLocation("som", "textures/entity/gaian_warrior/cobblestone.png"),
      new ResourceLocation("som", "textures/entity/gaian_warrior/stone.png"),
      new ResourceLocation("som", "textures/entity/gaian_warrior/deepslate.png")
   };

   public RenderGaianWarrior(EntityRendererProvider.Context context) {
      super(context, new ModelGaianWarrior(context.bakeLayer(ModelGaianWarrior.LAYER_LOCATION)), 0.7F);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityGaianWarrior entity) {
      return TEXTURES[Math.floorMod(entity.getVariant(), TEXTURES.length)];
   }
}
