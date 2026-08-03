package com.paleimitations.schoolsofmagic.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

public class ParticleSkull extends TextureSheetParticle implements IAnimatedParticle {

   private static final int[] FRAMES = {3, 4, 3, 2, 1, 0};
   // The sprites are drawn at different sizes but every quad is the same, so the
   // later, smaller skulls would be stretched up to fill it. These shrink each step
   // instead, so the puff only ever dwindles.
   private static final float[] FRAME_SCALE = {1.0F, 0.94F, 0.84F, 0.70F, 0.55F, 0.40F};
   private static final int TICKS_PER_FRAME = 4;
   private static final int SPRITE_COUNT = 5;

   private SpriteSet sprites;
   private float baseSize;

   public ParticleSkull(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ) {
      super(world, x, y, z, 0.0D, 0.0D, 0.0D);
      this.xd = motionX;
      this.yd = motionY;
      this.zd = motionZ;
      this.quadSize *= 0.45F + this.random.nextFloat() * 0.2F;
      this.baseSize = this.quadSize;
      this.lifetime = FRAMES.length * TICKS_PER_FRAME;
      this.hasPhysics = false;
      this.rCol = 1.0F;
      this.gCol = 1.0F;
      this.bCol = 1.0F;
      this.alpha = 1.0F;
   }

   @Override
   public void setSprites(SpriteSet sprites) {
      this.sprites = sprites;
      this.pickSprite();
   }

   private void pickSprite() {
      if (this.sprites == null) return;
      int frame = Math.min(this.age / TICKS_PER_FRAME, FRAMES.length - 1);
      this.setSprite(this.sprites.get(FRAMES[frame], SPRITE_COUNT - 1));
      this.quadSize = this.baseSize * FRAME_SCALE[frame];
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
      this.xd *= 0.91D;
      this.yd *= 0.91D;
      this.zd *= 0.91D;
      this.yd += 0.002D;
      float tail = (float) this.age / (float) this.lifetime;
      this.alpha = tail < 0.6F ? 1.0F : 1.0F - (tail - 0.6F) / 0.4F;
   }
}
