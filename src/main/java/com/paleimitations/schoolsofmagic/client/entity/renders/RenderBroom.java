package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.paleimitations.schoolsofmagic.client.entity.model.ModelBroom;
import com.paleimitations.schoolsofmagic.common.entity.EntityBroom;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderBroom extends MobRenderer<EntityBroom, ModelBroom> {
   private static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/entity/broom.png");

   public RenderBroom(EntityRendererProvider.Context context) {
      super(context, new ModelBroom(context.bakeLayer(ModelBroom.LAYER_LOCATION)), 0.3F);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityBroom entity) {
      return TEXTURE;
   }
}
