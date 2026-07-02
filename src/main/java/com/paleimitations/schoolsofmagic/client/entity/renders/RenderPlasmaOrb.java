package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityPlasmaOrb;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class RenderPlasmaOrb extends EntityRenderer<EntityPlasmaOrb> {
   private static final ResourceLocation TEX = new ResourceLocation("som", "textures/entity/magic_orb.png");

   public RenderPlasmaOrb(EntityRendererProvider.Context context) {
      super(context);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityPlasmaOrb entity) {
      return TEX;
   }

   @Override
   public void render(EntityPlasmaOrb entity, float entityYaw, float partialTicks,
                      PoseStack pose, MultiBufferSource buffer, int packedLight) {
      super.render(entity, entityYaw, partialTicks, pose, buffer, packedLight);
   }
}
