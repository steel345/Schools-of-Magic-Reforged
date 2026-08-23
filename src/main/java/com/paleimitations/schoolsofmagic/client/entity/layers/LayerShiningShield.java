package com.paleimitations.schoolsofmagic.client.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.client.ClientShiningShields;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class LayerShiningShield<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
   private static final ResourceLocation TEXTURE =
      new ResourceLocation("som", "textures/entity/shield.png");

   private static final float ORBIT_RADIUS = 0.8F;
   private static final float TURN_PER_TICK = 5.0F;
   private static final float SIZE = 1.35F;

   private static final double RIDE_HEIGHT = 1.15D;

   private static final double MODEL_ORIGIN = 1.5D;

   private static final int ALPHA = 210;

   public LayerShiningShield(RenderLayerParent<T, M> parent) {
      super(parent);
   }

   @Override
   public void render(PoseStack pose, MultiBufferSource buffer, int light, T entity,
                      float limbSwing, float limbSwingAmount, float partialTick,
                      float age, float netHeadYaw, float headPitch) {
      int count = ClientShiningShields.get(entity.getUUID());
      if (count <= 0) return;

      float size = SIZE / (1.0F + 0.13F * (count - 1));
      float spin = (entity.tickCount + partialTick) * TURN_PER_TICK;
      VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE));

      for (int i = 0; i < count; ++i) {
         float angle = spin + (360.0F / count) * i;
         pose.pushPose();

         pose.mulPose(Axis.ZP.rotationDegrees(180.0F));
         pose.translate(0.0D, RIDE_HEIGHT - MODEL_ORIGIN, 0.0D);
         pose.mulPose(Axis.YP.rotationDegrees(angle));
         pose.translate(0.0D, 0.0D, ORBIT_RADIUS);

         pose.mulPose(Axis.YP.rotationDegrees(180.0F));

         ShieldGeometry.render(vc, pose.last().pose(), pose.last().normal(), light, size, ALPHA);
         pose.popPose();
      }
   }
}
