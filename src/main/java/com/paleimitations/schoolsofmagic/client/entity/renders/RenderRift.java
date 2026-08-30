package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.common.entity.EntityRift;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

// the strips are animated by hand rather than through mcmeta, entity textures are not stitched so
// nothing would advance them. open runs once, the loop cycles, close plays out at the end
public class RenderRift extends EntityRenderer<EntityRift> {
   private static final ResourceLocation OPEN = new ResourceLocation("som", "textures/entity/rift/open.png");
   private static final ResourceLocation LOOP = new ResourceLocation("som", "textures/entity/rift/loop.png");
   private static final ResourceLocation CLOSE = new ResourceLocation("som", "textures/entity/rift/close.png");

   private static final int OPEN_FRAMES = 24;
   private static final int LOOP_FRAMES = 32;
   private static final int CLOSE_FRAMES = 20;
   // every strip runs at the opens frame rate, that is the one that looks right
   private static final float RATE = (float) OPEN_FRAMES / (float) EntityRift.OPEN_TICKS;

   private static final float SIZE = 2.2F;

   public RenderRift(EntityRendererProvider.Context context) {
      super(context);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityRift entity) {
      return LOOP;
   }

   @Override
   public void render(EntityRift entity, float entityYaw, float partialTicks,
                      PoseStack pose, MultiBufferSource buf, int packedLight) {
      float time = entity.tickCount + partialTicks;
      int left = entity.getLife();

      ResourceLocation texture;
      int frames;
      int frame;
      if (time < EntityRift.OPEN_TICKS) {
         texture = OPEN;
         frames = OPEN_FRAMES;
         frame = Math.min(OPEN_FRAMES - 1, (int) (time * RATE));
      } else if (left <= EntityRift.CLOSE_TICKS) {
         texture = CLOSE;
         frames = CLOSE_FRAMES;
         float done = EntityRift.CLOSE_TICKS - left + partialTicks;
         frame = Mth.clamp((int) (done * RATE), 0, CLOSE_FRAMES - 1);
      } else {
         texture = LOOP;
         frames = LOOP_FRAMES;
         frame = (int) ((time - EntityRift.OPEN_TICKS) * RATE) % LOOP_FRAMES;
      }

      float v0 = (float) frame / (float) frames;
      float v1 = (float) (frame + 1) / (float) frames;

      // the way home is drawn through everything with a halo round it, so it can be found from
      // anywhere down the corridor
      boolean marked = entity.isHomeward();
      if (marked) RenderSystem.disableDepthTest();

      pose.pushPose();
      pose.translate(0.0D, entity.getBbHeight() * 0.5D, 0.0D);
      pose.mulPose(this.entityRenderDispatcher.cameraOrientation());
      pose.scale(SIZE, SIZE, SIZE);

      Matrix4f matrix = pose.last().pose();
      if (marked) {
         VertexConsumer halo = buf.getBuffer(RenderType.entityTranslucentEmissive(texture));
         pose.pushPose();
         pose.scale(1.45F, 1.45F, 1.45F);
         Matrix4f wide = pose.last().pose();
         glow(halo, wide, -0.5F, 0.5F, 0.0F, 1.0F, v0, v1);
         glow(halo, wide, 0.5F, -0.5F, 1.0F, 0.0F, v0, v1);
         pose.popPose();
      }

      VertexConsumer vc = buf.getBuffer(RenderType.entityCutoutNoCull(texture));
      quad(vc, matrix, -0.5F, 0.5F, 0.0F, 1.0F, v0, v1);
      quad(vc, matrix, 0.5F, -0.5F, 1.0F, 0.0F, v0, v1);
      pose.popPose();
      if (marked) RenderSystem.enableDepthTest();

      super.render(entity, entityYaw, partialTicks, pose, buf, packedLight);
   }

   private static void glow(VertexConsumer vc, Matrix4f matrix, float x0, float x1,
                            float u0, float u1, float v0, float v1) {
      int light = LightTexture.FULL_BRIGHT;
      float[][] corners = {{x0, -0.5F, u0, v1}, {x1, -0.5F, u1, v1}, {x1, 0.5F, u1, v0}, {x0, 0.5F, u0, v0}};
      for (float[] c : corners) {
         vc.vertex(matrix, c[0], c[1], 0.0F).color(150, 210, 255, 130).uv(c[2], c[3])
            .overlayCoords(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY)
            .uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
      }
   }

   private static void quad(VertexConsumer vc, Matrix4f matrix, float x0, float x1,
                            float u0, float u1, float v0, float v1) {
      int light = LightTexture.FULL_BRIGHT;
      vc.vertex(matrix, x0, -0.5F, 0.0F).color(255, 255, 255, 255).uv(u0, v1)
         .overlayCoords(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY)
         .uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
      vc.vertex(matrix, x1, -0.5F, 0.0F).color(255, 255, 255, 255).uv(u1, v1)
         .overlayCoords(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY)
         .uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
      vc.vertex(matrix, x1, 0.5F, 0.0F).color(255, 255, 255, 255).uv(u1, v0)
         .overlayCoords(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY)
         .uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
      vc.vertex(matrix, x0, 0.5F, 0.0F).color(255, 255, 255, 255).uv(u0, v0)
         .overlayCoords(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY)
         .uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
   }
}
