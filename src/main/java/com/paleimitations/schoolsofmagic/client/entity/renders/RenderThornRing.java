package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;

import com.paleimitations.schoolsofmagic.client.entity.model.ModelThornRing;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityThornRing;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RenderThornRing extends EntityRenderer<EntityThornRing> {
   public static final ResourceLocation TEXTURES = new ResourceLocation("som", "textures/entity/thorn_ring.png");
   private final ModelThornRing<EntityThornRing> model;

   public RenderThornRing(EntityRendererProvider.Context context) {
      super(context);
      this.model = new ModelThornRing<>(context.bakeLayer(ModelThornRing.LAYER_LOCATION));
   }

   @Override
   public ResourceLocation getTextureLocation(EntityThornRing entity) { return TEXTURES; }

   @Override
   public void render(EntityThornRing entity, float entityYaw, float partialTicks,
                      PoseStack pose, MultiBufferSource buf, int packedLight) {
      float f = entity.getAnimationProgress(partialTicks);
      if (f != 0.0F) {

         float f1 = 1.0F;
         if (f > 0.9F) f1 = (float) ((double) f1 * ((1.0 - (double) f) / 0.1F));
         pose.pushPose();
         pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees(90.0F - entity.getYRot()));
         pose.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(((float) entity.tickCount + partialTicks) * 10.0F % 360.0F));
         pose.scale(-f1, -f1, f1);

         pose.translate(0.0F, -0.4F, 0.0F);
         this.model.setupAnim(entity, f, 0.0F, 0.0F, entity.getYRot(), entity.getXRot());
         var vc = buf.getBuffer(RenderType.entityCutoutNoCull(TEXTURES));
         this.model.renderToBuffer(pose, vc, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
         pose.popPose();
         super.render(entity, entityYaw, partialTicks, pose, buf, packedLight);
      }
   }
}
