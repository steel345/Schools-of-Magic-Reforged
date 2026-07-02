package com.paleimitations.schoolsofmagic.client.effects.effects;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import com.paleimitations.imitationcore.client.ClientUtil;
import com.paleimitations.imitationcore.client.effects.ImitationSpiteEffect;
import com.paleimitations.schoolsofmagic.client.effects.EffectHelper;
import com.paleimitations.schoolsofmagic.client.effects.ImitationSpriteLibrary;

import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;

public class FlameRingEffect extends ImitationSpiteEffect {
   private double rotX;
   private double rotY;
   private double rotZ;
   private boolean colorized;

   public FlameRingEffect(Level world, double x, double y, double z, double rotX, double rotY, double rotZ) {
      super(ImitationSpriteLibrary.flame_ring, world, x, y, z, 0.0, 0.0, 0.0, 1.0f, 0, 0, Color.WHITE);
      this.rotX = rotX;
      this.rotY = rotY;
      this.rotZ = rotZ;
      this.colorized = false;
   }

   public FlameRingEffect(Level world, double x, double y, double z, double rotX, double rotY, double rotZ, float scale, int age, int maxAge) {
      super(ImitationSpriteLibrary.flame_ring, world, x, y, z, 0.0, 0.0, 0.0, scale, age, maxAge, Color.WHITE);
      this.rotX = rotX;
      this.rotY = rotY;
      this.rotZ = rotZ;
      this.colorized = false;
   }

   public FlameRingEffect(Level world, double x, double y, double z, double rotX, double rotY, double rotZ, double vX, double vY, double vZ, float scale, int age, int maxAge, Color color) {
      super(ImitationSpriteLibrary.flame_ring, world, x, y, z, vX, vY, vZ, scale, age, maxAge, color);
      this.rotX = rotX;
      this.rotY = rotY;
      this.rotZ = rotZ;
      this.colorized = false;
   }

   public FlameRingEffect(Level world, double x, double y, double z, double rotX, double rotY, double rotZ, double vX, double vY, double vZ, float scale, int age, int maxAge, Color color, boolean colorized) {
      super(ImitationSpriteLibrary.flame_ring, world, x, y, z, vX, vY, vZ, scale, age, maxAge, color);
      this.rotX = rotX;
      this.rotY = rotY;
      this.rotZ = rotZ;
      this.colorized = colorized;
   }

   @Override
   public void update() {
      super.update();

      if (!this.world.isClientSide && this.age == this.maxAge / 2) {
         EffectHelper.createColoredPuffParticle(this.world,
            this.x + Math.sin(Math.toRadians(this.rotY)) * (double) this.scale * 0.5,
            this.y,
            this.z + Math.cos(Math.toRadians(this.rotY)) * (double) this.scale * 0.5,
            Color.DARK_GRAY);
         EffectHelper.createColoredPuffParticle(this.world,
            this.x + Math.sin(Math.toRadians(this.rotY + 90.0)) * (double) this.scale * 1.5 + Math.sin(Math.toRadians(this.rotY)) * (double) this.scale * 0.5,
            this.y,
            this.z + Math.cos(Math.toRadians(this.rotY + 90.0)) * (double) this.scale * 1.5 + Math.cos(Math.toRadians(this.rotY)) * (double) this.scale * 0.5,
            Color.DARK_GRAY);
         EffectHelper.createColoredPuffParticle(this.world,
            this.x - Math.sin(Math.toRadians(this.rotY + 90.0)) * (double) this.scale * 1.5 + Math.sin(Math.toRadians(this.rotY)) * (double) this.scale * 0.5,
            this.y,
            this.z - Math.cos(Math.toRadians(this.rotY + 90.0)) * (double) this.scale * 1.5 + Math.cos(Math.toRadians(this.rotY)) * (double) this.scale * 0.5,
            Color.DARK_GRAY);
      }
   }

   @Override
   public int getBrightnessForRender(float partial) {
      float f = ((float) this.age + partial) / (float) this.maxAge;
      f = Mth.clamp(f, 0.0f, 1.0f);
      int i = super.getBrightnessForRender(partial);
      int j = i & 0xFF;
      int k = i >> 16 & 0xFF;
      if ((j += (int) (f * 15.0f * 16.0f)) > 240) {
         j = 240;
      }
      return j | k << 16;
   }

   @Override
   public void render(PoseStack pose, float partial) {
      pose.pushPose();
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(com.mojang.blaze3d.platform.GlStateManager.SourceFactor.SRC_ALPHA,
                             com.mojang.blaze3d.platform.GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
      RenderSystem.disableCull();

      RenderSystem.disableDepthTest();
      RenderSystem.depthMask(false);
      RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

      Tuple<Double, Double> uv = this.age < 0
         ? this.sprite.getUVOffset(0, this.colorized ? 1 : 0)
         : this.sprite.getUVOffset(this.age, this.colorized ? 1 : 0);
      this.sprite.bindTexture();
      double iX = Mth.lerp(partial, this.prevX, this.x);
      double iY = Mth.lerp(partial, this.prevY, this.y);
      double iZ = Mth.lerp(partial, this.prevZ, this.z);
      int light = this.getBrightnessForRender(partial);

      ClientUtil.drawSpriteWithScale(pose, iX, iY, iZ,
            Math.toRadians(this.rotX), Math.toRadians(this.rotY), Math.toRadians(this.rotZ),
            uv.getA(), uv.getB(), this.sprite.getWidth(), this.sprite.getHeight(),
            this.scale, 2.0f, 1.0f, partial,
            (float) this.color.getRed() / 255.0f,
            (float) this.color.getGreen() / 255.0f,
            (float) this.color.getBlue() / 255.0f,
            1.0f, light, false);

      RenderSystem.disableBlend();
      RenderSystem.depthMask(true);
      RenderSystem.enableDepthTest();
      RenderSystem.enableCull();
      pose.popPose();
   }
}
