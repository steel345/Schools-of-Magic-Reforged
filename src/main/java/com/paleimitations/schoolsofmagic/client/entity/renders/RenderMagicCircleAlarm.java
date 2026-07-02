package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;

import com.paleimitations.schoolsofmagic.client.entity.model.ModelMagicCircle;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicCircleAlarm;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderMagicCircleAlarm extends EntityRenderer<EntityMagicCircleAlarm> {
   public static final ResourceLocation TEXTURES = new ResourceLocation("som", "textures/entity/magiccircle.png");
   private final ModelMagicCircle<EntityMagicCircleAlarm> model;

   public RenderMagicCircleAlarm(EntityRendererProvider.Context context) {
      super(context);
      this.model = new ModelMagicCircle<>(context.bakeLayer(ModelMagicCircle.LAYER_LOCATION));
   }

   private float rotLerp(float a, float b, float t) {
      float f = b - a;
      while (f < -180.0F) f += 360.0F;
      while (f >= 180.0F) f -= 360.0F;
      return a + t * f;
   }

   @Override
   public ResourceLocation getTextureLocation(EntityMagicCircleAlarm entity) {
      return TEXTURES;
   }

   @Override
   public void render(EntityMagicCircleAlarm entity, float entityYaw, float partialTicks,
                      PoseStack pose, MultiBufferSource buf, int packedLight) {
      if (entity.isInvisible()) return;
      pose.pushPose();
      float yaw = this.rotLerp(entity.yRotO, entity.getYRot(), partialTicks);
      float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
      float f2 = (float) entity.tickCount + partialTicks;
      pose.translate(0.0F, 0.15F, 0.0F);
      pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees(f2));
      pose.scale(-1.0F, -1.0F, 1.0F);
      this.model.setupAnim(entity, 0.0F, 0.0F, 0.0F, yaw, pitch);
      var vc = buf.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(entity)));
      this.model.renderToBuffer(pose, vc, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
      pose.scale(1.5F, 1.5F, 1.5F);
      var vcTrans = buf.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(entity)));
      this.model.renderToBuffer(pose, vcTrans, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.5F);
      pose.popPose();
      super.render(entity, entityYaw, partialTicks, pose, buf, packedLight);
   }
}
