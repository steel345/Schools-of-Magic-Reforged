package com.paleimitations.schoolsofmagic.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

// a bank of it, sat still and drifting barely at all. the length is handed in through the speed
// argument so the spell can make the whole bank thin out together at the end
public class ParticleFog extends TextureSheetParticle implements IAnimatedParticle {
   private static final int FRAMES = 8;

   private SpriteSet sprites;
   private final int frame;

   public ParticleFog(ClientLevel level, double x, double y, double z, double mx, double my, double mz) {
      super(level, x, y, z, 0.0D, 0.0D, 0.0D);
      this.xd = mx * 0.02D;
      this.yd = 0.0D;
      this.zd = mz * 0.02D;

      this.quadSize = 2.6F + this.random.nextFloat() * 2.2F;
      this.lifetime = 60 + this.random.nextInt(20);
      this.hasPhysics = false;
      this.gravity = 0.0F;
      this.alpha = 0.0F;

      float shade = 0.82F + this.random.nextFloat() * 0.14F;
      this.rCol = shade;
      this.gCol = shade;
      this.bCol = shade * 1.02F;

      // each puff keeps one frame rather than running the strip, so the bank does not shimmer
      this.frame = this.random.nextInt(FRAMES);
   }

   public void setLifetime(int ticks) {
      this.lifetime = Math.max(4, ticks);
   }

   @Override
   public void setSprites(SpriteSet sprites) {
      this.sprites = sprites;
      if (sprites != null) this.setSprite(sprites.get(this.frame, FRAMES - 1));
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

      if (this.age++ >= this.lifetime) {
         this.remove();
         return;
      }

      this.move(this.xd, this.yd, this.zd);
      this.xd *= 0.96D;
      this.yd *= 0.96D;
      this.zd *= 0.96D;

      // rolls in over the first second and thins away over the last five, so the bank clears
      // rather than blinking out
      int fadeOut = Math.min(100, this.lifetime / 2);
      if (this.age < 20) {
         this.alpha = this.age / 20.0F * 0.85F;
      } else if (this.age > this.lifetime - fadeOut) {
         this.alpha = (this.lifetime - this.age) / (float) fadeOut * 0.85F;
      } else {
         this.alpha = 0.85F;
      }
   }
}
