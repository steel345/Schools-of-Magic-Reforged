package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicBeam;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class RenderMagicBeam extends EntityRenderer<EntityMagicBeam> {
   private static final ResourceLocation TEX = new ResourceLocation("minecraft", "textures/entity/beacon_beam.png");

   public RenderMagicBeam(EntityRendererProvider.Context context) {
      super(context);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityMagicBeam entity) {
      return TEX;
   }

   @Override
   public void render(EntityMagicBeam entity, float entityYaw, float partialTicks,
                      PoseStack pose, MultiBufferSource buffer, int packedLight) {
      int color = entity.getColor();
      float r = ((color >> 16) & 0xFF) / 255.0F;
      float g = ((color >> 8) & 0xFF) / 255.0F;
      float b = (color & 0xFF) / 255.0F;
      float a = 0.6F;
      float len = Math.max(1, entity.getLength());
      float s = 0.12F;

      Vec3 L = entity.look();
      Vec3 up0 = Math.abs(L.y) > 0.99D ? new Vec3(1, 0, 0) : new Vec3(0, 1, 0);
      Vec3 p = L.cross(up0).normalize();
      Vec3 u = p.cross(L).normalize();
      double roll = (entity.tickCount + partialTicks) * 0.16D;
      double cr = Math.cos(roll), sr = Math.sin(roll);
      Vec3 p2 = p.scale(cr).add(u.scale(sr));
      Vec3 u2 = p.scale(-sr).add(u.scale(cr));

      VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucent(TEX));
      Matrix4f m = pose.last().pose();
      Vec3 d = L.scale(len);

      face(vc, m, d, p2.scale(s), u2.scale(s), len, r, g, b, a, packedLight);
      face(vc, m, d, p2.scale(-s), u2.scale(s), len, r, g, b, a, packedLight);
      face(vc, m, d, u2.scale(s), p2.scale(s), len, r, g, b, a, packedLight);
      face(vc, m, d, u2.scale(-s), p2.scale(s), len, r, g, b, a, packedLight);

      super.render(entity, entityYaw, partialTicks, pose, buffer, packedLight);
   }

   private static void face(VertexConsumer vc, Matrix4f m, Vec3 d, Vec3 off, Vec3 span, float vt,
                            float r, float g, float b, float a, int light) {
      Vec3 v0 = off.subtract(span);
      Vec3 v1 = off.add(span);
      Vec3 v2 = v1.add(d);
      Vec3 v3 = v0.add(d);
      v(vc, m, v0, 0.0F, vt, r, g, b, a, light);
      v(vc, m, v1, 1.0F, vt, r, g, b, a, light);
      v(vc, m, v2, 1.0F, 0.0F, r, g, b, a, light);
      v(vc, m, v3, 0.0F, 0.0F, r, g, b, a, light);
      v(vc, m, v3, 0.0F, 0.0F, r, g, b, a, light);
      v(vc, m, v2, 1.0F, 0.0F, r, g, b, a, light);
      v(vc, m, v1, 1.0F, vt, r, g, b, a, light);
      v(vc, m, v0, 0.0F, vt, r, g, b, a, light);
   }

   private static void v(VertexConsumer vc, Matrix4f m, Vec3 pos, float u, float vv,
                         float r, float g, float b, float a, int light) {
      vc.vertex(m, (float) pos.x, (float) pos.y, (float) pos.z).color(r, g, b, a).uv(u, vv)
         .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
   }
}
