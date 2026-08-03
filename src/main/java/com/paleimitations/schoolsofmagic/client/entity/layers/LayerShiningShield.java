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

// The ring of shields turning about the caster.
//
// The face is drawn straight from its own texture rather than through a baked model.
// A baked model has to be stitched into the block atlas and found again in the model
// registry, and anything wrong anywhere along that chain shows as a missing texture
// with nothing to say why. Pointing at the file leaves nothing to go wrong.
public class LayerShiningShield<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

   private static final ResourceLocation TEXTURE =
      new ResourceLocation("som", "textures/entity/shield.png");

   private static final float ORBIT_RADIUS = 0.8F;
   private static final float TURN_PER_TICK = 5.0F;
   private static final float SIZE = 1.35F;
   // Measured up from the caster's feet.
   private static final double RIDE_HEIGHT = 1.15D;
   // Where the layer's own origin sits, a block and a half up.
   private static final double MODEL_ORIGIN = 1.5D;
   // How solid the panels read. Fully opaque hides too much of the caster.
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

      // A fuller ring is drawn smaller, so it does not crowd itself.
      float size = SIZE / (1.0F + 0.13F * (count - 1));
      float spin = (entity.tickCount + partialTick) * TURN_PER_TICK;
      VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE));

      for (int i = 0; i < count; ++i) {
         float angle = spin + (360.0F / count) * i;
         pose.pushPose();

         // The layer arrives at the model's feet with the world upside down, so it is
         // turned upright before anything is placed.
         pose.mulPose(Axis.ZP.rotationDegrees(180.0F));
         pose.translate(0.0D, RIDE_HEIGHT - MODEL_ORIGIN, 0.0D);
         pose.mulPose(Axis.YP.rotationDegrees(angle));
         pose.translate(0.0D, 0.0D, ORBIT_RADIUS);
         // Faces turned back toward the caster.
         pose.mulPose(Axis.YP.rotationDegrees(180.0F));

         ShieldGeometry.render(vc, pose.last().pose(), pose.last().normal(), light, size, ALPHA);
         pose.popPose();
      }
   }

}
