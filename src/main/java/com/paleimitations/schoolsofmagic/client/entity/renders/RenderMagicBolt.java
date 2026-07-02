package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicBolt;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class RenderMagicBolt extends EntityRenderer<EntityMagicBolt> {
   private static final ResourceLocation TEX = new ResourceLocation("som", "textures/entity/magic_bolt.png");

   public RenderMagicBolt(EntityRendererProvider.Context context) {
      super(context);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityMagicBolt entity) {
      return TEX;
   }

   @Override
   public void render(EntityMagicBolt entity, float entityYaw, float partialTicks,
                      PoseStack pose, MultiBufferSource buffer, int packedLight) {
      int color = entity.getColor();
      float r = ((color >> 16) & 0xFF) / 255.0F;
      float g = ((color >> 8) & 0xFF) / 255.0F;
      float b = (color & 0xFF) / 255.0F;

      pose.pushPose();
      pose.mulPose(Axis.YP.rotationDegrees(180.0F - entity.getYRot()));
      pose.mulPose(Axis.XP.rotationDegrees(entity.getXRot()));
      pose.mulPose(Axis.ZP.rotationDegrees((entity.tickCount + partialTicks) * 14.0F % 360.0F));
      VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucentCull(TEX));
      Matrix4f m = pose.last().pose();
      float s = 0.42F;
      int light = LightTexture.FULL_BRIGHT;

      quad(vc, m, light, r, g, b, -s, -s, 0, s, -s, 0, s, s, 0, -s, s, 0, 0, 1);
      quad(vc, m, light, r, g, b, -s, s, 0, s, s, 0, s, -s, 0, -s, -s, 0, 0, 1);
      quad(vc, m, light, r, g, b, 0, -s, -s, 0, -s, s, 0, s, s, 0, s, -s, 1, 0);
      quad(vc, m, light, r, g, b, 0, s, -s, 0, s, s, 0, -s, s, 0, -s, -s, 1, 0);

      pose.popPose();
      super.render(entity, entityYaw, partialTicks, pose, buffer, packedLight);
   }

   private static void quad(VertexConsumer vc, Matrix4f m, int light, float r, float g, float b,
                            float x0, float y0, float z0, float x1, float y1, float z1,
                            float x2, float y2, float z2, float x3, float y3, float z3,
                            float uLeft, float uRight) {
      v(vc, m, light, r, g, b, x0, y0, z0, uLeft, 1.0F);
      v(vc, m, light, r, g, b, x1, y1, z1, uRight, 1.0F);
      v(vc, m, light, r, g, b, x2, y2, z2, uRight, 0.0F);
      v(vc, m, light, r, g, b, x3, y3, z3, uLeft, 0.0F);
   }

   private static void v(VertexConsumer vc, Matrix4f m, int light, float r, float g, float b,
                         float x, float y, float z, float u, float vv) {
      vc.vertex(m, x, y, z).color(r, g, b, 1.0F).uv(u, vv)
         .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
   }
}
