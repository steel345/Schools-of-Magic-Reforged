package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.client.entity.model.FireBallAnimation;
import com.paleimitations.schoolsofmagic.client.entity.model.ModelFireBall;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityFireBall;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class RenderFireBall extends EntityRenderer<EntityFireBall> {
   public static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/entity/fireball.png");
   private static final float SCALE = 4.0F;
   private static final float SPIN_PER_TICK = 22.0F;
   private static final float GLOW_R = 1.0F;
   private static final float GLOW_G = 0.45F;
   private static final float GLOW_B = 0.12F;
   private final ModelFireBall<EntityFireBall> model;

   public RenderFireBall(EntityRendererProvider.Context context) {
      super(context);
      this.model = new ModelFireBall<>(context.bakeLayer(ModelFireBall.LAYER_LOCATION));
   }

   @Override
   public ResourceLocation getTextureLocation(EntityFireBall entity) {
      return TEXTURE;
   }

   @Override
   public void render(EntityFireBall entity, float entityYaw, float partialTicks,
                      PoseStack pose, MultiBufferSource buf, int packedLight) {
      Vec3 motion = entity.getDeltaMovement();
      float yaw;
      float pitch;
      if (motion.lengthSqr() > 1.0E-6D) {
         yaw = (float) (Mth.atan2(motion.x, motion.z) * (180.0D / Math.PI));
         pitch = (float) (Mth.atan2(motion.y, motion.horizontalDistance()) * (180.0D / Math.PI));
      } else {
         yaw = entity.getYRot();
         pitch = entity.getXRot();
      }

      float spin = ((float) entity.tickCount + partialTicks) * SPIN_PER_TICK % 360.0F;

      pose.pushPose();
      pose.translate(0.0D, entity.getBbHeight() * 0.5D, 0.0D);
      pose.mulPose(Axis.YP.rotationDegrees(180.0F + yaw));
      pose.mulPose(Axis.XP.rotationDegrees(pitch));
      pose.mulPose(Axis.ZP.rotationDegrees(spin));
      pose.scale(-SCALE, -SCALE, SCALE);
      pose.translate(0.0F, -1.0F, 0.0F);

      this.model.play(FireBallAnimation.FLY, (float) entity.tickCount + partialTicks);
      this.model.renderToBuffer(pose, buf.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)),
         LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
      this.model.renderToBuffer(pose, buf.getBuffer(RenderType.eyes(TEXTURE)),
         LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, GLOW_R, GLOW_G, GLOW_B, 1.0F);
      pose.popPose();

      super.render(entity, entityYaw, partialTicks, pose, buf, packedLight);
   }
}
