package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.paleimitations.schoolsofmagic.client.entity.model.ModelTornado;
import com.paleimitations.schoolsofmagic.common.entity.EntityTornado;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RenderTornado extends EntityRenderer<EntityTornado> {
   private final ModelTornado model;

   public RenderTornado(EntityRendererProvider.Context context) {
      super(context);
      this.model = new ModelTornado(context.bakeLayer(ModelTornado.LAYER_LOCATION));
      this.shadowRadius = 1.6F;
   }

   @Override
   public ResourceLocation getTextureLocation(EntityTornado entity) {
      return ModelTornado.TEXTURE;
   }

   @Override
   public void render(EntityTornado entity, float yaw, float partialTicks,
                      PoseStack pose, MultiBufferSource buf, int light) {
      pose.pushPose();
      pose.translate(0.0D, 0.0D, 0.0D);
      pose.scale(-1.0F, -1.0F, 1.0F);
      pose.translate(0.0D, -1.501D, 0.0D);

      this.model.setupAnim(entity, 0.0F, 0.0F, entity.tickCount + partialTicks, 0.0F, 0.0F);
      VertexConsumer out = buf.getBuffer(RenderType.entityTranslucent(ModelTornado.TEXTURE));
      this.model.renderToBuffer(pose, out, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.75F);

      pose.popPose();
      super.render(entity, yaw, partialTicks, pose, buf, light);
   }
}
