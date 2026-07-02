package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityStarfallCloud;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderStarfallCloud extends EntityRenderer<EntityStarfallCloud> {
   private static final ResourceLocation TEX = new ResourceLocation("som", "textures/entity/magic_meteor.png");

   public RenderStarfallCloud(EntityRendererProvider.Context context) {
      super(context);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityStarfallCloud entity) {
      return TEX;
   }

   @Override
   public void render(EntityStarfallCloud entity, float entityYaw, float partialTicks,
                      PoseStack pose, MultiBufferSource buffer, int packedLight) {
   }
}
