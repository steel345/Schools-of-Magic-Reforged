package com.paleimitations.schoolsofmagic.client.particles;

import java.awt.Color;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.client.renderer.BiomeColors;

public class ParticleLeaf extends TextureSheetParticle implements IAnimatedParticle {
   private int texture = 0;
   private int counter = 0;
   private SpriteSet sprites;

   @Override
   public void setSprites(SpriteSet s) {
      this.sprites = s;
      this.setSpriteFromAge(s);
   }

   public ParticleLeaf(ClientLevel world, double xCoordIn, double yCoordIn, double zCoordIn, double motionXIn, double motionYIn, double motionZIn) {
      this(world, xCoordIn, yCoordIn, zCoordIn, motionXIn, motionYIn, motionZIn, 1.0F);
   }

   public ParticleLeaf(ClientLevel world, double xCoordIn, double yCoordIn, double zCoordIn, double motionXIn, double motionYIn, double motionZIn, float par14) {
      super(world, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
      Color color = new Color(BiomeColors.getAverageFoliageColor((BlockAndTintGetter)world, new BlockPos((int)xCoordIn, (int)yCoordIn, (int)zCoordIn)));
      this.rCol = (float)color.getRed() / 255.0F;
      this.bCol = (float)color.getBlue() / 255.0F;
      this.gCol = (float)color.getGreen() / 255.0F;
      this.xd *= 0.1D;
      this.yd *= 0.1D;
      this.zd *= 0.1D;
      this.xd += motionXIn;
      this.yd += motionYIn;
      this.zd += motionZIn;
      this.lifetime = (int)(8.0D / (Math.random() * 0.8D + 0.2D) * 8.0D);
      this.lifetime = (int)((float)this.lifetime * par14);
      this.age = world.random.nextInt(3);
      this.alpha = 1.0F;
      this.gravity = 0.003F;
      this.hasPhysics = true;
      this.setSize(0.3F, 0.3F);
   }

   @Override
   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
   }

   @Override
   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      ++this.age;
      ++this.counter;
      if (this.age >= this.lifetime) {
         this.remove();
      }
      this.counter %= 6;
      if (this.counter == 0) {
         ++this.texture;
      }
      this.texture %= 6;
      this.yd -= (double)this.gravity;
      this.move(this.xd, this.yd, this.zd);
      this.xd *= 0.99D + (0.04D - 0.08D * this.random.nextDouble());
      this.yd *= 0.99D;
      this.zd *= 0.99D + (0.04D - 0.08D * this.random.nextDouble());
      if (this.onGround) {
         this.texture = 0;
         this.xd *= 0.7D;
         this.zd *= 0.7D;
      }
      if (this.sprites != null) {
         this.setSprite(this.sprites.get(this.texture, 5));
      }
   }
}
