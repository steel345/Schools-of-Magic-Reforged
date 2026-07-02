package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;

import com.paleimitations.schoolsofmagic.client.entity.model.ModelWebProjectile;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityWebProjectile;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderWebProjectile extends EntityRenderer<EntityWebProjectile> {
   public static final ResourceLocation TEXTURES = new ResourceLocation("som", "textures/entity/web_projectile.png");
   private final ModelWebProjectile<EntityWebProjectile> model;

   public RenderWebProjectile(EntityRendererProvider.Context context) {
      super(context);
      this.model = new ModelWebProjectile<>(context.bakeLayer(ModelWebProjectile.LAYER_LOCATION));
   }

   private float rotLerp(float a, float b, float t) {
      float f = b - a;
      while (f < -180.0F) f += 360.0F;
      while (f >= 180.0F) f -= 360.0F;
      return a + t * f;
   }

   @Override
   public ResourceLocation getTextureLocation(EntityWebProjectile entity) { return TEXTURES; }

   @Override
   public void render(EntityWebProjectile entity, float entityYaw, float partialTicks,
                      PoseStack pose, MultiBufferSource buf, int packedLight) {
      pose.pushPose();
      float yaw = this.rotLerp(entity.yRotO, entity.getYRot(), partialTicks);
      float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());

      float angle = (float) (entity.tickCount % 360 * 10);
      pose.mulPose(com.mojang.math.Axis.XP.rotationDegrees(angle));
      pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees(angle));
      pose.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(angle));
      pose.scale(3.0F, 3.0F, 3.0F);
      this.model.setupAnim(entity, 0.0F, 0.0F, 0.0F, yaw, pitch);
      var vc = buf.getBuffer(RenderType.entityCutoutNoCull(TEXTURES));
      this.model.renderToBuffer(pose, vc, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
      var vcTrans = buf.getBuffer(RenderType.entityTranslucent(TEXTURES));
      this.model.renderToBuffer(pose, vcTrans, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.5F);
      pose.popPose();
      super.render(entity, entityYaw, partialTicks, pose, buf, packedLight);
   }
}
