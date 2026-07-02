package com.paleimitations.schoolsofmagic.client.effects.effects;

import java.awt.Color;
import java.util.Random;

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

public class WindEffect extends ImitationSpiteEffect {
   private double rotX;
   private double rotY;
   private double rotZ;

   public WindEffect(Level world, double x, double y, double z, double angle, double speed, int age, int maxAge, float scale, Color color) {
      super(ImitationSpriteLibrary.wind, world, x, y, z,
            Math.sin(Math.toRadians(angle)) * speed,
            Math.sin(Math.toRadians(-20.0)) * speed,
            Math.cos(Math.toRadians(angle)) * speed,
            scale, age, maxAge, color);
      this.rotY = angle;
      this.rotZ = -20.0;
      this.rotX = new Random().nextDouble() * 10.0 - 5.0;
   }

   public WindEffect(Level world, double x, double y, double z) {
      super(ImitationSpriteLibrary.wind, world, x, y, z, 0.0, 0.0, 0.0, 1.0f, 0, 0, Color.LIGHT_GRAY);
   }

   public WindEffect(Level world, double x, double y, double z, float scale, int age, int maxAge) {
      super(ImitationSpriteLibrary.wind, world, x, y, z, 0.0, 0.0, 0.0, scale, age, maxAge, Color.LIGHT_GRAY);
   }

   public WindEffect(Level world, double x, double y, double z, double vX, double vY, double vZ, float scale, int age, int maxAge, Color color) {
      super(ImitationSpriteLibrary.wind, world, x, y, z, vX, vY, vZ, scale, age, maxAge, color);
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

      Tuple<Double, Double> uv = this.age < 0 ? this.sprite.getUVOffset(0, this.variant) : this.sprite.getUVOffset(this.age, this.variant);
      this.sprite.bindTexture();
      double iX = Mth.lerp(partial, this.prevX, this.x);
      double iY = Mth.lerp(partial, this.prevY, this.y);
      double iZ = Mth.lerp(partial, this.prevZ, this.z);
      int light = this.getBrightnessForRender(partial);
      BlockPos blockpos = BlockPos.containing(this.x, this.y, this.z);
      BlockState state = this.world.getBlockState(blockpos);
      if (this.world.isLoaded(blockpos) && !state.isSolidRender(this.world, blockpos)) {
         float alpha = ((1.0f - (float) (Math.max((double) this.age, 0.0) / (double) this.maxAge)) * 0.2f + 0.4f) * (float) this.color.getAlpha() / 255.0f;
         ClientUtil.drawSprite(pose, iX, iY, iZ,
               Math.toRadians(this.rotX),
               Math.toRadians(-90.0 + this.rotY),
               Math.toRadians(40.0 + this.rotZ),
               uv.getA(), uv.getB(), this.sprite.getWidth(), this.sprite.getHeight(),
               this.scale, partial,
               (float) this.color.getRed() / 255.0f,
               (float) this.color.getGreen() / 255.0f,
               (float) this.color.getBlue() / 255.0f,
               alpha, light, false);
      }

      RenderSystem.disableBlend();
      RenderSystem.depthMask(true);
      RenderSystem.enableCull();
      pose.popPose();
   }
}
