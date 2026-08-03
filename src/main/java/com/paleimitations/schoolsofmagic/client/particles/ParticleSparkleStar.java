package com.paleimitations.schoolsofmagic.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

// The ten sparkle stars, run straight through in order. Whatever colour is asked of
// it, since the frames themselves are plain.
public class ParticleSparkleStar extends TextureSheetParticle implements IAnimatedParticle {

   private static final int FRAMES = 10;

   private SpriteSet sprites;

   public ParticleSparkleStar(ClientLevel world, double x, double y, double z,
                              double r, double g, double b) {
      super(world, x, y, z, 0.0D, 0.0D, 0.0D);
      // The constructor above scatters every particle by up to 0.4 a tick of its own
      // accord. These are meant to hang exactly where they are put, so that is undone.
      this.xd = 0.0D;
      this.yd = 0.0D;
      this.zd = 0.0D;
      this.quadSize *= 0.9F;
      // Short lived, so the trail stays a line rather than piling up into a cloud.
      this.lifetime = 10;
      this.hasPhysics = false;
      this.rCol = (float) r;
      this.gCol = (float) g;
      this.bCol = (float) b;
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
      this.xd *= 0.94D;
      this.yd *= 0.94D;
      this.zd *= 0.94D;
   }
}
