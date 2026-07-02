package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.paleimitations.schoolsofmagic.client.entity.model.ModelOrchidFae;
import com.paleimitations.schoolsofmagic.common.entity.EntityFlowerFae;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderFlowerFae extends MobRenderer<EntityFlowerFae, ModelOrchidFae<EntityFlowerFae>> {

   private static final ResourceLocation[] TEXTURES = buildTextures();

   private static ResourceLocation[] buildTextures() {
      ResourceLocation[] locs = new ResourceLocation[EntityFlowerFae.VARIANTS.length];
      for (int i = 0; i < locs.length; i++) {
         locs[i] = new ResourceLocation("som", "textures/entity/orchid_fae_" + EntityFlowerFae.VARIANTS[i] + ".png");
      }
      return locs;
   }

   public RenderFlowerFae(EntityRendererProvider.Context context) {
      super(context, new ModelOrchidFae<>(context.bakeLayer(ModelOrchidFae.LAYER_LOCATION)), 0.3F);
   }

   @Override
   protected void scale(EntityFlowerFae entity, com.mojang.blaze3d.vertex.PoseStack pose, float partialTick) {
      if (entity.isBaby()) {
         pose.scale(0.5F, 0.5F, 0.5F);
      }
   }

   @Override
   public ResourceLocation getTextureLocation(EntityFlowerFae entity) {
      int v = entity.getVariant();
      if (v < 0 || v >= TEXTURES.length) v = 0;
      return TEXTURES[v];
   }
}
