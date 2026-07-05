package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.paleimitations.schoolsofmagic.client.entity.model.ModelMagicBroom;
import com.paleimitations.schoolsofmagic.common.entity.EntityMagicBroom;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderMagicBroom extends MobRenderer<EntityMagicBroom, ModelMagicBroom> {
   private static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/entity/magic_broom.png");

   public RenderMagicBroom(EntityRendererProvider.Context context) {
      super(context, new ModelMagicBroom(context.bakeLayer(ModelMagicBroom.LAYER_LOCATION)), 0.4F);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityMagicBroom entity) {
      return TEXTURE;
   }
}
