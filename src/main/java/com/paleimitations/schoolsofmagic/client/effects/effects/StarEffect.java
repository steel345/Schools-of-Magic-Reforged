package com.paleimitations.schoolsofmagic.client.effects.effects;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import com.paleimitations.imitationcore.client.ClientUtil;
import com.paleimitations.imitationcore.client.effects.ImitationSpiteEffect;
import com.paleimitations.schoolsofmagic.client.effects.ImitationSpriteLibrary;

import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;

public class StarEffect extends ImitationSpiteEffect {

   public StarEffect(Level world, double x, double y, double z) {
      super(ImitationSpriteLibrary.star, world, x, y, z, 0.0, 0.0, 0.0, 1.0f, 0, 0, Color.WHITE);
   }

   public StarEffect(Level world, double x, double y, double z, float scale, int age, int maxAge) {
      super(ImitationSpriteLibrary.star, world, x, y, z, 0.0, 0.0, 0.0, scale, age, maxAge, Color.WHITE);
   }

   public StarEffect(Level world, double x, double y, double z, double vX, double vY, double vZ, float scale, int age, int maxAge, Color color) {
      super(ImitationSpriteLibrary.star, world, x, y, z, vX, vY, vZ, scale, age, maxAge, color);
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
      RenderSystem.depthMask(false);
      RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

      Tuple<Double, Double> uv = this.age < 0 ? this.sprite.getUVOffset(0, 0) : this.sprite.getUVOffset(this.age, 0);
      this.sprite.bindTexture();
      double iX = Mth.lerp(partial, this.prevX, this.x);
      double iY = Mth.lerp(partial, this.prevY, this.y);
      double iZ = Mth.lerp(partial, this.prevZ, this.z);
      int light = this.getBrightnessForRender(partial);
      float alpha = 1.0f - Math.abs((float) this.maxAge / 2.0f - (float) this.age) / ((float) this.maxAge / 2.0f);
      float r = (float) this.color.getRed() / 255.0f;
      float g = (float) this.color.getGreen() / 255.0f;
      float b = (float) this.color.getBlue() / 255.0f;

      ClientUtil.drawSprite(pose, iX, iY, iZ, 0.0, Math.toRadians(45.0), 0.0,
            uv.getA(), uv.getB(), this.sprite.getWidth(), this.sprite.getHeight(),
            this.scale, partial, r, g, b, alpha, light, false);
      ClientUtil.drawSprite(pose, iX, iY, iZ, 0.0, Math.toRadians(-45.0), 0.0,
            uv.getA(), uv.getB(), this.sprite.getWidth(), this.sprite.getHeight(),
            this.scale, partial, r, g, b, alpha, light, false);
      ClientUtil.drawSprite(pose, iX, iY, iZ, Math.toRadians(90.0), 0.0, 0.0,
            uv.getA(), uv.getB(), this.sprite.getWidth(), this.sprite.getHeight(),
            this.scale, partial, r, g, b, alpha, light, false);

      RenderSystem.disableBlend();
      RenderSystem.depthMask(true);
      RenderSystem.enableCull();
      pose.popPose();
   }
}
