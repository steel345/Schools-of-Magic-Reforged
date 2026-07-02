package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.paleimitations.schoolsofmagic.client.entity.layers.LayerSqueakardHeldItem;
import com.paleimitations.schoolsofmagic.client.entity.model.ModelSqueakard;
import com.paleimitations.schoolsofmagic.common.entity.EntitySqueakard;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderSqueakard extends MobRenderer<EntitySqueakard, ModelSqueakard<EntitySqueakard>> {

   private static final ResourceLocation FALLBACK = new ResourceLocation("som", "textures/entity/squeakard/fur0.png");

   public RenderSqueakard(EntityRendererProvider.Context context) {
      super(context, new ModelSqueakard<>(context.bakeLayer(ModelSqueakard.LAYER_LOCATION)), 0.3F);
      this.addLayer(new LayerSqueakardHeldItem(this));
   }

   @Override
   protected void scale(EntitySqueakard entity, PoseStack pose, float partialTick) {
      pose.translate(0.0D, 0.22D, 0.0D);
   }

   @Override
   public ResourceLocation getTextureLocation(EntitySqueakard entity) {
      return LayeredEntityTexture.get(entity.getSqueakardTexture(), entity.getVariantTexturePaths(), "squeakard", FALLBACK);
   }
}
