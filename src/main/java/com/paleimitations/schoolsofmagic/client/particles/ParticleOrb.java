package com.paleimitations.schoolsofmagic.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

// The orb's own light, run straight through its seven frames in order and held on
// the last so the glow sits rather than flickering back to the start.
public class ParticleOrb extends TextureSheetParticle implements IAnimatedParticle {

   private final boolean core;
   private float baseSize;

   private static final int FRAMES = 7;
   private static final int TICKS_PER_FRAME = 3;

   private SpriteSet sprites;

   public ParticleOrb(ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
      this(world, x, y, z, mx, my, mz, false);
   }

   public ParticleOrb(ClientLevel world, double x, double y, double z, double mx, double my, double mz, boolean core) {
      super(world, x, y, z, 0.0D, 0.0D, 0.0D);
      this.xd = mx;
      this.yd = my;
      this.zd = mz;
      this.core = core;
      this.quadSize *= core ? 5.5F : 2.2F;
      this.baseSize = this.quadSize;
      this.lifetime = core ? 4 : 26;
      this.hasPhysics = false;
      this.alpha = 1.0F;
      // Yellow, as sunlight is.
      this.rCol = 1.0F;
      this.gCol = 0.85F;
      this.bCol = 0.30F;
   }

   @Override
   public void setSprites(SpriteSet sprites) {
      this.sprites = sprites;
      this.pickSprite();
   }

   private void pickSprite() {
      if (this.sprites == null) return;
      int frame = this.core
         ? Math.min(this.age * FRAMES / Math.max(1, this.lifetime), FRAMES - 1)
         : Math.min(this.age / TICKS_PER_FRAME, FRAMES - 1);
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
      this.xd *= 0.9D;
      this.zd *= 0.9D;
      if (!this.core) {
         // The lesser lights drift up off the body of the orb, thinning as they go.
         float left = 1.0F - this.age / (float) this.lifetime;
         this.quadSize = this.baseSize * left;
         this.alpha = left;
      }
   }
}
