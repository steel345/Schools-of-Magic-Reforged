package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.paleimitations.schoolsofmagic.common.entity.projectile.IBoltTrail;
import net.minecraft.world.entity.Entity;
import java.util.List;
import net.minecraft.client.model.ShulkerBulletModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class RenderPrecisionBolt<T extends Entity & IBoltTrail> extends EntityRenderer<T> {
   private static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/entity/spell_ice_shell.png");
   private static final ResourceLocation TRAIL = new ResourceLocation("minecraft", "textures/entity/beacon_beam.png");
   private static final float TRAIL_WIDTH = 0.14F;

   private final ShulkerBulletModel<T> model;

   public RenderPrecisionBolt(EntityRendererProvider.Context context) {
      super(context);
      this.model = new ShulkerBulletModel<>(context.bakeLayer(ModelLayers.SHULKER_BULLET));
   }

   @Override
   public ResourceLocation getTextureLocation(T entity) {
      return TEXTURE;
   }

   private float rotLerp(float a, float b, float t) {
      float f = b - a;
      while (f < -180.0F) f += 360.0F;
      while (f >= 180.0F) f -= 360.0F;
      return a + t * f;
   }

   @Override
   public void render(T entity, float entityYaw, float partialTicks,
                      PoseStack pose, MultiBufferSource buf, int packedLight) {
      float r = entity.getColorColor().getRed() / 255.0F;
      float g = entity.getColorColor().getGreen() / 255.0F;
      float b = entity.getColorColor().getBlue() / 255.0F;

      this.renderTrail(entity, partialTicks, pose, buf, r, g, b);

      pose.pushPose();
      float yaw = this.rotLerp(entity.yRotO, entity.getYRot(), partialTicks);
      float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
      float age = (float) entity.tickCount + partialTicks;
      pose.translate(0.0F, 0.15F, 0.0F);
      pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees(Mth.sin(age * 0.1F) * 180.0F));
      pose.mulPose(com.mojang.math.Axis.XP.rotationDegrees(Mth.cos(age * 0.1F) * 180.0F));
      pose.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(Mth.sin(age * 0.15F) * 360.0F));
      pose.scale(-0.8F, -0.8F, 0.8F);
      this.model.setupAnim(entity, 0.0F, 0.0F, 0.0F, yaw, pitch);
      this.model.renderToBuffer(pose, buf.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)),
         LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F);
      pose.scale(1.5F, 1.5F, 1.5F);
      this.model.renderToBuffer(pose, buf.getBuffer(RenderType.entityTranslucent(TEXTURE)),
         LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, r, g, b, 0.5F);
      pose.popPose();

      super.render(entity, entityYaw, partialTicks, pose, buf, packedLight);
   }

   private void renderTrail(T entity, float partialTicks, PoseStack pose,
                            MultiBufferSource buf, float r, float g, float b) {
      List<Vec3> points = entity.boltTrail();
      if (points.size() < 2) return;

      Vec3 base = new Vec3(
         Mth.lerp(partialTicks, entity.xo, entity.getX()),
         Mth.lerp(partialTicks, entity.yo, entity.getY()),
         Mth.lerp(partialTicks, entity.zo, entity.getZ()));
      Vec3 camera = this.entityRenderDispatcher.camera.getPosition();

      VertexConsumer vc = buf.getBuffer(RenderType.entityTranslucent(TRAIL));
      Matrix4f matrix = pose.last().pose();
      int segments = points.size() - 1;

      for (int i = 0; i < segments; i++) {
         Vec3 head = points.get(i);
         Vec3 tail = points.get(i + 1);
         Vec3 along = tail.subtract(head);
         if (along.lengthSqr() < 1.0E-8D) continue;

         Vec3 toCamera = head.add(tail).scale(0.5D).subtract(camera);
         Vec3 side = along.cross(toCamera);
         if (side.lengthSqr() < 1.0E-8D) continue;
         side = side.normalize();

         float headTaper = 1.0F - (float) i / segments;
         float tailTaper = 1.0F - (float) (i + 1) / segments;
         Vec3 headOff = side.scale(TRAIL_WIDTH * headTaper);
         Vec3 tailOff = side.scale(TRAIL_WIDTH * tailTaper);
         Vec3 h = head.subtract(base);
         Vec3 t = tail.subtract(base);

         quad(vc, matrix, h.subtract(headOff), h.add(headOff), t.add(tailOff), t.subtract(tailOff),
            r, g, b, 0.75F * headTaper, 0.75F * tailTaper);
         quad(vc, matrix, t.subtract(tailOff), t.add(tailOff), h.add(headOff), h.subtract(headOff),
            r, g, b, 0.75F * tailTaper, 0.75F * headTaper);
      }
   }

   private static void quad(VertexConsumer vc, Matrix4f m, Vec3 v0, Vec3 v1, Vec3 v2, Vec3 v3,
                            float r, float g, float b, float alphaNear, float alphaFar) {
      vertex(vc, m, v0, 0.0F, 0.0F, r, g, b, alphaNear);
      vertex(vc, m, v1, 1.0F, 0.0F, r, g, b, alphaNear);
      vertex(vc, m, v2, 1.0F, 1.0F, r, g, b, alphaFar);
      vertex(vc, m, v3, 0.0F, 1.0F, r, g, b, alphaFar);
   }

   private static void vertex(VertexConsumer vc, Matrix4f m, Vec3 pos, float u, float v,
                              float r, float g, float b, float a) {
      vc.vertex(m, (float) pos.x, (float) pos.y, (float) pos.z)
         .color(r, g, b, a)
         .uv(u, v)
         .overlayCoords(OverlayTexture.NO_OVERLAY)
         .uv2(LightTexture.FULL_BRIGHT)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
   }
}
