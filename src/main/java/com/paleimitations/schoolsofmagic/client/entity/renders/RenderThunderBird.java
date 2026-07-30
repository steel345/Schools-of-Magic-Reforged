package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.paleimitations.schoolsofmagic.common.entity.EntityPhoenix;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderThunderBird extends RenderPhoenix {
   public static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/entity/thunder_bird_texture.png");

   public RenderThunderBird(EntityRendererProvider.Context context) {
      super(context);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityPhoenix entity) {
      return TEXTURE;
   }
}
