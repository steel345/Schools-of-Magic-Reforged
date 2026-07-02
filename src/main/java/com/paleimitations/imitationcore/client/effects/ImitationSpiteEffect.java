package com.paleimitations.imitationcore.client.effects;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import com.paleimitations.imitationcore.client.ClientUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;

public class ImitationSpiteEffect extends AbstractImitationEffect {
   public final ImitationSprite sprite;
   public double x;
   public double y;
   public double z;
   public double vX;
   public double vY;
   public double vZ;
   public Level world;
   public Color color;
   public double prevX;
   public double prevY;
   public double prevZ;
   public final float scale;
   public int variant;

   public ImitationSpiteEffect(ImitationSprite sprite, Level world, double x, double y, double z) {
      this(sprite, world, x, y, z, 0.0, 0.0, 0.0, 1.0F, 0, 0, Color.WHITE);
   }

   public ImitationSpiteEffect(ImitationSprite sprite, Level world, double x, double y, double z, float scale, int age, int maxAge) {
      this(sprite, world, x, y, z, 0.0, 0.0, 0.0, scale, age, maxAge, Color.WHITE);
   }

   public ImitationSpiteEffect(
      ImitationSprite sprite, Level world, double x, double y, double z, double vX, double vY, double vZ, float scale, int age, int maxAge, Color color
   ) {
      this(sprite, world, x, y, z, vX, vY, vZ, scale, age, maxAge, 0, color);
   }

   public ImitationSpiteEffect(
      ImitationSprite sprite,
      Level world,
      double x,
      double y,
      double z,
      double vX,
      double vY,
      double vZ,
      float scale,
      int age,
      int maxAge,
      int variant,
      Color color
   ) {
      this.sprite = sprite;
      this.x = x;
      this.y = y;
      this.z = z;
      this.prevX = x;
      this.prevY = y;
      this.prevZ = z;
      this.vX = vX;
      this.vY = vY;
      this.vZ = vZ;
      this.scale = scale;
      this.age = age;
      this.variant = variant;
      this.maxAge = maxAge <= 0 ? sprite.frameCount * sprite.ticksPerFrame : maxAge;
      this.world = world;
      this.color = color;
   }

   public void setPosition(double x, double y, double z) {
      this.prevX = this.x;
      this.prevY = this.y;
      this.prevZ = this.z;
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public void updatePosition() {
      this.prevX = this.x;
      this.prevY = this.y;
      this.prevZ = this.z;
      this.x = this.x + this.vX;
      this.y = this.y + this.vY;
      this.z = this.z + this.vZ;
   }

   @Override
   public void update() {
      super.update();
      this.updatePosition();
   }

   public int getBrightnessForRender(float partial) {

      return net.minecraft.client.renderer.LightTexture.FULL_BRIGHT;
   }

   @Override
   public void render(PoseStack pose, float partial) {
      pose.pushPose();
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(
         com.mojang.blaze3d.platform.GlStateManager.SourceFactor.SRC_ALPHA,
         com.mojang.blaze3d.platform.GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
      RenderSystem.disableCull();
      RenderSystem.depthMask(false);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

      Tuple<Double, Double> uv = this.age < 0
         ? this.sprite.getUVOffset(0, this.variant)
         : this.sprite.getUVOffset(this.age, this.variant);
      this.sprite.bindTexture();

      double iX = Mth.lerp(partial, this.prevX, this.x);
      double iY = Mth.lerp(partial, this.prevY, this.y);
      double iZ = Mth.lerp(partial, this.prevZ, this.z);
      int light = this.getBrightnessForRender(partial);

      ClientUtil.drawSprite(
         pose,
         iX,
         iY,
         iZ,
         0.0,
         0.0,
         0.0,
         uv.getA(),
         uv.getB(),
         this.sprite.getWidth(),
         this.sprite.getHeight(),
         this.scale,
         partial,
         (float) this.color.getRed() / 255.0F,
         (float) this.color.getGreen() / 255.0F,
         (float) this.color.getBlue() / 255.0F,
         (float) this.color.getAlpha() / 255.0F,
         light,
         true
      );

      RenderSystem.disableBlend();
      RenderSystem.depthMask(true);
      RenderSystem.enableCull();
      pose.popPose();
   }
}
