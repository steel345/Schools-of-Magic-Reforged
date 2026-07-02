package com.paleimitations.schoolsofmagic.client.effects.effects;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import com.paleimitations.imitationcore.client.ClientUtil;
import com.paleimitations.imitationcore.client.effects.ImitationSpiteEffect;
import com.paleimitations.schoolsofmagic.client.effects.ImitationSpriteLibrary;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CloudEffect extends ImitationSpiteEffect {
   private boolean flip;
   private float startRot;

   public CloudEffect(Level world, double x, double y, double z, boolean flipped, float startRot) {
      super(ImitationSpriteLibrary.cloud3, world, x, y, z, 0.0, 0.0, 0.0, 1.0f, 0, 0, Color.WHITE);
      this.flip = flipped;
      this.startRot = startRot;
   }

   public CloudEffect(Level world, double x, double y, double z, float scale, int age, int maxAge, boolean flipped, float startRot) {
      super(ImitationSpriteLibrary.cloud3, world, x, y, z, 0.0, 0.0, 0.0, scale, age, maxAge, Color.WHITE);
      this.flip = flipped;
      this.startRot = startRot;
   }

   public CloudEffect(Level world, double x, double y, double z, double vX, double vY, double vZ, float scale, int age, int maxAge, Color color, boolean flipped, float startRot) {
      super(ImitationSpriteLibrary.cloud3, world, x, y, z, vX, vY, vZ, scale, age, maxAge, color);
      this.flip = flipped;
      this.startRot = startRot;
   }

   @Override
   public void render(PoseStack pose, float partial) {
      pose.pushPose();
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(com.mojang.blaze3d.platform.GlStateManager.SourceFactor.SRC_ALPHA,
                             com.mojang.blaze3d.platform.GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
      RenderSystem.disableCull();
      RenderSystem.depthMask(false);

      RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

      Tuple<Double, Double> uv = this.age < 0 ? this.sprite.getUVOffset(0, 0) : this.sprite.getUVOffset(this.age, 0);
      double iX = Mth.lerp(partial, this.prevX, this.x);
      double iY = Mth.lerp(partial, this.prevY, this.y);
      double iZ = Mth.lerp(partial, this.prevZ, this.z);
      int light = this.getBrightnessForRender(partial);
      BlockPos blockpos = BlockPos.containing(this.x, this.y, this.z);
      BlockState state = this.world.getBlockState(blockpos);
      if (this.world.isLoaded(blockpos) && !state.isSolidRender(this.world, blockpos)) {
         this.sprite.bindResource();
         double rotZ = (this.flip ? -0.5 : 0.5) * Math.PI * (((double) this.age + (double) partial) / (double) this.maxAge) + (double) this.startRot;
         float alpha = ((1.0f - (float) (Math.max((double) this.age, 0.0) / (double) this.maxAge)) * 0.6f + 0.2f) * (float) this.color.getAlpha() / 255.0f;
         ClientUtil.drawSprite(pose, iX, iY, iZ, 0.0, 0.0, rotZ,
               uv.getA(), uv.getB(), this.sprite.getWidth(), this.sprite.getHeight(),
               this.scale, partial,
               (float) this.color.getRed() / 255.0f,
               (float) this.color.getGreen() / 255.0f,
               (float) this.color.getBlue() / 255.0f,
               alpha, light, true);
      }

      RenderSystem.disableBlend();
      RenderSystem.depthMask(true);
      RenderSystem.enableCull();
      pose.popPose();
   }
}
