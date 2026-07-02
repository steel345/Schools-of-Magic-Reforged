package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;

import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityCounterspell;

import net.minecraft.client.model.ShulkerBulletModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderCounterspell extends EntityRenderer<EntityCounterspell> {
   private static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/entity/spell_ice_shell.png");
   private final ShulkerBulletModel<EntityCounterspell> model;

   public RenderCounterspell(EntityRendererProvider.Context context) {
      super(context);
      this.model = new ShulkerBulletModel<>(context.bakeLayer(ModelLayers.SHULKER_BULLET));
   }

   private float rotLerp(float a, float b, float t) {
      float f = b - a;
      while (f < -180.0F) f += 360.0F;
      while (f >= 180.0F) f -= 360.0F;
      return a + t * f;
   }

   @Override
   public ResourceLocation getTextureLocation(EntityCounterspell entity) { return TEXTURE; }

   @Override
   public void render(EntityCounterspell entity, float entityYaw, float partialTicks,
                      PoseStack pose, MultiBufferSource buf, int packedLight) {
      pose.pushPose();
      float yaw = this.rotLerp(entity.yRotO, entity.getYRot(), partialTicks);
      float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
      float f2 = (float) entity.tickCount + partialTicks;
      pose.translate(0.0F, 0.15F, 0.0F);
      pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees(Mth.sin(f2 * 0.1F) * 180.0F));
      pose.mulPose(com.mojang.math.Axis.XP.rotationDegrees(Mth.cos(f2 * 0.1F) * 180.0F));
      pose.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(Mth.sin(f2 * 0.15F) * 360.0F));
      pose.scale(-1.0F, -1.0F, 1.0F);
      this.model.setupAnim(entity, 0.0F, 0.0F, 0.0F, yaw, pitch);
      var vc = buf.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
      this.model.renderToBuffer(pose, vc, packedLight, OverlayTexture.NO_OVERLAY,
         (float) entity.getColorColor().getRed() / 255.0F,
         (float) entity.getColorColor().getGreen() / 255.0F,
         (float) entity.getColorColor().getBlue() / 255.0F,
         1.0F);
      pose.scale(1.5F, 1.5F, 1.5F);
      var vcTrans = buf.getBuffer(RenderType.entityTranslucent(TEXTURE));
      this.model.renderToBuffer(pose, vcTrans, packedLight, OverlayTexture.NO_OVERLAY,
         (float) entity.getColorColor().getRed() / 255.0F,
         (float) entity.getColorColor().getGreen() / 255.0F,
         (float) entity.getColorColor().getBlue() / 255.0F,
         0.5F);
      pose.popPose();
      super.render(entity, entityYaw, partialTicks, pose, buf, packedLight);
   }
}
