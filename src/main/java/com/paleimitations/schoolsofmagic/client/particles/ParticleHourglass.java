package com.paleimitations.schoolsofmagic.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

public class ParticleHourglass extends TextureSheetParticle implements IAnimatedParticle {
   private static final int FRAMES = 6;

   private SpriteSet sprites;

   public ParticleHourglass(ClientLevel level, double x, double y, double z, double mx, double my, double mz) {
      super(level, x, y, z, 0.0D, 0.0D, 0.0D);
      this.xd = mx;
      this.yd = my;
      this.zd = mz;
      this.quadSize = 0.22F + this.random.nextFloat() * 0.08F;
      this.lifetime = 30 + this.random.nextInt(10);
      this.hasPhysics = false;
      this.gravity = 0.0F;
      this.alpha = 1.0F;
   }

   @Override
   public void setSprites(SpriteSet sprites) {
      this.sprites = sprites;
      this.pickSprite();
   }

   private void pickSprite() {
      if (this.sprites == null) return;
      int frame = Math.min(this.age * FRAMES / Math.max(1, this.lifetime), FRAMES - 1);
      this.setSprite(this.sprites.get(frame, FRAMES - 1));
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
      this.pickSprite();
      this.move(this.xd, this.yd, this.zd);
      this.xd *= 0.93D;
      this.yd *= 0.93D;
      this.zd *= 0.93D;
      this.alpha = Math.min(1.0F, 2.5F * (1.0F - (float) this.age / (float) this.lifetime));
   }
}
