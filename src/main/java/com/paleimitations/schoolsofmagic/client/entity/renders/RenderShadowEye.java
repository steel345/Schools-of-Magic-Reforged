package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;

import com.paleimitations.schoolsofmagic.client.entity.model.ModelShadowEye;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityShadowEye;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderShadowEye extends EntityRenderer<EntityShadowEye> {
   private static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/entity/shadow_eye.png");
   private final ModelShadowEye<EntityShadowEye> model;

   public RenderShadowEye(EntityRendererProvider.Context context) {
      super(context);
      this.model = new ModelShadowEye<>(context.bakeLayer(ModelShadowEye.LAYER_LOCATION));
   }

   private float rotLerp(float a, float b, float t) {
      float f = b - a;
      while (f < -180.0F) f += 360.0F;
      while (f >= 180.0F) f -= 360.0F;
      return a + t * f;
   }

   @Override
   public ResourceLocation getTextureLocation(EntityShadowEye entity) {
      return TEXTURE;
   }

   @Override
   public void render(EntityShadowEye entity, float entityYaw, float partialTicks,
                      PoseStack pose, MultiBufferSource buf, int packedLight) {
      pose.pushPose();
      float yaw = this.rotLerp(entity.yRotO, entity.getYRot(), partialTicks);
      float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
      pose.scale(-1.0F, -1.0F, 1.0F);
      pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees(yaw + 180.0F));
      pose.mulPose(com.mojang.math.Axis.XP.rotationDegrees(pitch));
      var vc = buf.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(entity)));
      this.model.renderToBuffer(pose, vc, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.85F);
      pose.popPose();
      super.render(entity, entityYaw, partialTicks, pose, buf, packedLight);
   }
}
