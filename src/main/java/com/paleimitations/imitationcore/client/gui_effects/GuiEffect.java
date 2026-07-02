package com.paleimitations.imitationcore.client.gui_effects;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import com.paleimitations.imitationcore.client.ClientUtil;
import com.paleimitations.imitationcore.client.effects.IImitationEffect;
import com.paleimitations.imitationcore.client.effects.ImitationSprite;

import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;

public class GuiEffect implements IImitationEffect {
   public final ImitationSprite sprite;
   private float x;
   private float y;
   public float prevX;
   public float prevY;
   public float vX;
   public float vY;
   private Color color;
   private float alpha;
   private float scale;
   public int variant;
   protected int age;
   protected int maxAge;
   protected boolean dead;

   public GuiEffect(ImitationSprite sprite, float x, float y, float vX, float vY, float scale, int age, int maxAge, int variant, Color color) {
      this.sprite = sprite;
      this.x = x;
      this.y = y;
      this.prevX = x;
      this.prevY = y;
      this.vX = vX;
      this.vY = vY;
      this.scale = scale;
      this.age = age;
      this.variant = variant;
      this.maxAge = maxAge <= 0 ? sprite.frameCount * sprite.ticksPerFrame : maxAge;
      this.color = color;
   }

   public void setPosition(float x, float y) {
      this.prevX = this.x;
      this.prevY = this.y;
      this.x = x;
      this.y = y;
   }

   public void updatePosition() {
      this.prevX = this.x;
      this.prevY = this.y;
      this.x = this.x + this.vX;
      this.y = this.y + this.vY;
   }

   @Override
   public void setDead() {
      this.dead = true;
   }

   @Override
   public boolean isDead() {
      return this.dead;
   }

   public void setAge(int age) {
      this.age = age;
   }

   public int getAge() {
      return this.age;
   }

   public void setMaxAge(int maxAge) {
      this.maxAge = maxAge;
   }

   public int getMaxAge() {
      return this.maxAge;
   }

   @Override
   public void update() {
      this.age++;
      if (this.age >= this.maxAge) {
         this.setDead();
      }
   }

   @Override
   public void render(PoseStack pose, float partial) {
      pose.pushPose();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      Tuple<Double, Double> uv = this.age < 0
         ? this.sprite.getUVOffset(0, this.variant)
         : this.sprite.getUVOffset(this.age, this.variant);
      this.sprite.bindTexture();
      double iX = Mth.lerp(partial, (double) this.prevX, (double) this.x);
      double iY = Mth.lerp(partial, (double) this.prevY, (double) this.y);
      ClientUtil.drawGuiSprite(
         pose,
         iX,
         iY,
         0.0,
         0.0F,
         0.0F,
         0.0F,
         uv.getA(),
         uv.getB(),
         this.sprite.getWidth(),
         this.sprite.getHeight(),
         this.scale,
         partial,
         (float) this.color.getRed() / 255.0F,
         (float) this.color.getGreen() / 255.0F,
         (float) this.color.getBlue() / 255.0F,
         1.0F - (float) this.age / (float) this.maxAge
      );
      pose.popPose();
   }
}
