package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.paleimitations.schoolsofmagic.client.entity.model.ModelPetSpider;
import com.paleimitations.schoolsofmagic.common.entity.EntityPetSpider;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderPetSpider extends MobRenderer<EntityPetSpider, ModelPetSpider<EntityPetSpider>> {
   private static final ResourceLocation[] TEX = new ResourceLocation[] {
      new ResourceLocation("som", "textures/entity/pet_spider_0.png"),
      new ResourceLocation("som", "textures/entity/pet_spider_1.png"),
      new ResourceLocation("som", "textures/entity/pet_spider_2.png"),
      new ResourceLocation("som", "textures/entity/pet_spider_3.png"),
      new ResourceLocation("som", "textures/entity/pet_spider_4.png"),
   };

   public RenderPetSpider(EntityRendererProvider.Context context) {
      super(context, new ModelPetSpider<>(context.bakeLayer(ModelPetSpider.LAYER_LOCATION)), 0.5F);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityPetSpider entity) {
      int s = entity.getPetSpiderType();
      if (s >= 0 && s < TEX.length) return TEX[s];
      return TEX[0];
   }
}
