package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.paleimitations.schoolsofmagic.client.entity.model.ModelTadpole;
import com.paleimitations.schoolsofmagic.common.entity.EntityTadpole;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderTadpole extends MobRenderer<EntityTadpole, ModelTadpole<EntityTadpole>> {
   private static final ResourceLocation[] TEX = new ResourceLocation[] {
      new ResourceLocation("som", "textures/entity/tadpole0.png"),
      new ResourceLocation("som", "textures/entity/tadpole1.png"),
      new ResourceLocation("som", "textures/entity/tadpole2.png"),
      new ResourceLocation("som", "textures/entity/tadpole3.png"),
      new ResourceLocation("som", "textures/entity/tadpole4.png"),
      new ResourceLocation("som", "textures/entity/tadpole5.png"),
      new ResourceLocation("som", "textures/entity/tadpole6.png"),
      new ResourceLocation("som", "textures/entity/tadpole7.png"),
      new ResourceLocation("som", "textures/entity/tadpole8.png"),
      new ResourceLocation("som", "textures/entity/tadpole9.png"),
      new ResourceLocation("som", "textures/entity/tadpole10.png"),
      new ResourceLocation("som", "textures/entity/tadpole11.png"),
   };

   public RenderTadpole(EntityRendererProvider.Context context) {
      super(context, new ModelTadpole<>(context.bakeLayer(ModelTadpole.LAYER_LOCATION)), 0.1F);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityTadpole entity) {
      int s = entity.getToadType();
      if (s >= 0 && s < TEX.length) return TEX[s];
      return TEX[0];
   }
}
