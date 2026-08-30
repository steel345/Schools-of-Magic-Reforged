package com.paleimitations.schoolsofmagic.client.tileentity.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.paleimitations.schoolsofmagic.client.entity.model.ModelSolarOrb;
import com.paleimitations.schoolsofmagic.client.entity.model.SolarOrbAnimation;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySolarOrb;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class TileEntityRendererSolarOrb implements BlockEntityRenderer<TileEntitySolarOrb> {
   public static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/entity/solar_orb.png");
   private static final float SCALE = 0.7F;
   private static final float OPACITY = 0.7F;
   private static final float GLOW_R = 1.0F;
   private static final float GLOW_G = 0.86F;
   private static final float GLOW_B = 0.18F;

   private final ModelSolarOrb model;

   public TileEntityRendererSolarOrb(BlockEntityRendererProvider.Context context) {
      this.model = new ModelSolarOrb(context.bakeLayer(ModelSolarOrb.LAYER_LOCATION));
   }

   @Override
   public void render(TileEntitySolarOrb orb, float partialTicks, PoseStack pose,
                      MultiBufferSource buf, int packedLight, int packedOverlay) {
      float alpha = orb.getAlpha(partialTicks) * OPACITY;
      if (alpha <= 0.01F) return;

      float seconds = (orb.getPhaseTicks() + partialTicks) / 20.0F;

      pose.pushPose();
      pose.translate(0.5D, 0.5D, 0.5D);
      pose.scale(-SCALE, -SCALE, SCALE);
      pose.translate(0.0F, -0.75F, 0.0F);

      this.model.play(animationFor(orb.getPhase()), seconds);
      this.model.renderToBuffer(pose, buf.getBuffer(RenderType.entityTranslucentCull(TEXTURE)),
         LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, GLOW_R, GLOW_G, GLOW_B, alpha);
      pose.popPose();
   }

   private static AnimationDefinition animationFor(TileEntitySolarOrb.Phase phase) {
      return switch (phase) {
         case CHARGE -> SolarOrbAnimation.CHARGE;
         case ATTACK -> SolarOrbAnimation.ATTACK;
         case BURST, DISSOLVE -> SolarOrbAnimation.BURST;
         default -> SolarOrbAnimation.IDLE;
      };
   }

   @Override
   public boolean shouldRenderOffScreen(TileEntitySolarOrb orb) {
      return true;
   }

   @Override
   public int getViewDistance() {
      return 96;
   }
}
