package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.paleimitations.schoolsofmagic.common.entity.EntityFairy;
import net.minecraft.client.model.AllayModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderFairy extends MobRenderer<EntityFairy, EntityModel<EntityFairy>> {

   @SuppressWarnings("unchecked")
   public RenderFairy(EntityRendererProvider.Context ctx) {
      super(ctx, (EntityModel<EntityFairy>) (EntityModel<?>) new AllayModel(ctx.bakeLayer(ModelLayers.ALLAY)), 0.3F);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityFairy entity) {
      return new ResourceLocation("som", "textures/entity/fairy/fairy_" + entity.getVariant().id + ".png");
   }
}
