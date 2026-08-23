package com.paleimitations.schoolsofmagic.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

public class ParticleSculkBloom extends TextureSheetParticle implements IAnimatedParticle {
   private static final int FRAMES = 4;

   private SpriteSet sprites;

   public ParticleSculkBloom(ClientLevel world, double x, double y, double z,
                             double r, double g, double b) {
      super(world, x, y, z, 0.0D, 0.0D, 0.0D);
      this.xd = 0.0D;
      this.yd = 0.02D;
      this.zd = 0.0D;
      this.quadSize *= 0.8F;
      this.lifetime = 16 + this.random.nextInt(8);
      this.hasPhysics = false;
      this.rCol = (float) r;
      this.gCol = (float) g;
      this.bCol = (float) b;
      this.alpha = 0.9F;
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
      this.alpha = 0.9F * (1.0F - (float) this.age / (float) this.lifetime);
      this.move(this.xd, this.yd, this.zd);
      this.xd *= 0.92D;
      this.yd *= 0.92D;
      this.zd *= 0.92D;
   }
}
