package com.paleimitations.schoolsofmagic.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

public class ParticleFly extends TextureSheetParticle implements IAnimatedParticle {
   private final double portalPosX;
   private final double portalPosY;
   private final double portalPosZ;
   private int texture = 0;
   private SpriteSet sprites;

   @Override
   public void setSprites(SpriteSet s) {
      this.sprites = s;
      this.setSpriteFromAge(s);
   }

   public ParticleFly(ClientLevel world, double xCoordIn, double yCoordIn, double zCoordIn, double motionXIn, double motionYIn, double motionZIn) {
      super(world, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
      this.xd = motionXIn;
      this.yd = motionYIn;
      this.zd = motionZIn;
      this.portalPosX = this.x;
      this.portalPosY = this.y;
      this.portalPosZ = this.z;
      this.lifetime = (int)(Math.random() * 10.0D) + 40;
      this.age = this.random.nextInt(6);
      this.hasPhysics = false;
      this.setSize(0.3F, 0.3F);
   }

   @Override
   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
   }

   @Override
   public void move(double x, double y, double z) {
      this.setBoundingBox(this.getBoundingBox().move(x, y, z));
      this.setLocationFromBoundingbox();
   }

   @Override
   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      ++this.age;
      ++this.texture;
      if (this.age >= this.lifetime) {
         this.remove();
      }
      this.texture %= 6;
      this.move(this.xd, this.yd, this.zd);
      this.xd *= 1.0D + (0.5D - this.random.nextDouble());
      this.yd *= 1.0D + (0.5D - this.random.nextDouble());
      this.zd *= 1.0D + (0.5D - this.random.nextDouble());
      float f = (float)this.age / (float)this.lifetime;
      float f1 = -f + f * f * 2.0F;
      float f2 = 1.0F - f1;
      this.x = this.portalPosX + this.xd * (double)f2;
      this.y = this.portalPosY + this.yd * (double)f2 + (double)(1.0F - f);
      this.z = this.portalPosZ + this.zd * (double)f2;
      if (this.sprites != null) {
         this.setSprite(this.sprites.get(this.texture, 5));
      }
   }
}
