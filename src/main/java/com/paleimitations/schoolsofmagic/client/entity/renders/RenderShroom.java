package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.client.entity.model.ModelShroom;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityShroom;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderShroom extends EntityRenderer<EntityShroom> {
   private static final ResourceLocation[] TEXTURES = {
      new ResourceLocation("som", "textures/entity/shroom/lime.png"),
      new ResourceLocation("som", "textures/entity/shroom/blue.png"),
      new ResourceLocation("som", "textures/entity/shroom/purple.png")
   };

   private static final float SCALE = 1.1F;
   private static final float SPIN_PER_TICK = 17.0F;
   private static final float WOBBLE = 22.0F;

   private final ModelShroom<EntityShroom> model;

   public RenderShroom(EntityRendererProvider.Context context) {
      super(context);
      this.model = new ModelShroom<>(context.bakeLayer(ModelShroom.LAYER_LOCATION));
   }

   @Override
   public ResourceLocation getTextureLocation(EntityShroom entity) {
      return TEXTURES[Math.floorMod(entity.getVariant(), TEXTURES.length)];
   }

   @Override
   public void render(EntityShroom entity, float entityYaw, float partialTicks,
                      PoseStack pose, MultiBufferSource buf, int packedLight) {
      float age = (float) entity.tickCount + partialTicks;
      // every shroom gets its own tumble out of its id so no two spin alike
      float seed = entity.getId() * 0.7F;
      float spin = (age * SPIN_PER_TICK + seed * 40.0F) % 360.0F;
      float wobbleX = Mth.sin(age * 0.31F + seed) * WOBBLE;
      float wobbleZ = Mth.cos(age * 0.23F + seed * 1.7F) * WOBBLE;

      pose.pushPose();
      pose.translate(0.0D, entity.getBbHeight() * 0.5D, 0.0D);
      pose.mulPose(Axis.YP.rotationDegrees(spin));
      pose.mulPose(Axis.XP.rotationDegrees(wobbleX));
      pose.mulPose(Axis.ZP.rotationDegrees(wobbleZ));
      pose.scale(-SCALE, -SCALE, SCALE);
      pose.translate(0.0F, -0.75F, 0.0F);

      ResourceLocation texture = this.getTextureLocation(entity);
      this.model.renderToBuffer(pose, buf.getBuffer(RenderType.entityCutoutNoCull(texture)),
         packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
      pose.popPose();

      super.render(entity, entityYaw, partialTicks, pose, buf, packedLight);
   }
}
