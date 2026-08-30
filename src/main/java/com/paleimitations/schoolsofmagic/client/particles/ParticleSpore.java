package com.paleimitations.schoolsofmagic.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

// same timing, size and drift as goetys blast fungus puff, which is vanillas explosion particle
// with another texture. the incoming x speed is the seeds age ramp, it shrinks each wave
public class ParticleSpore extends TextureSheetParticle {
   private SpriteSet sprites;

   public ParticleSpore(ClientLevel level, double x, double y, double z, double scale, double g, double b) {
      super(level, x, y, z, 0.0D, 0.0D, 0.0D);
      this.lifetime = 11 + this.random.nextInt(5);
      this.quadSize = 1.0F * (1.0F - (float) scale * 0.5F);
      this.hasPhysics = false;
      this.alpha = 1.0F;
   }

   public void tint(float r, float g, float b) {
      this.rCol = r;
      this.gCol = g;
      this.bCol = b;
   }

   public void sprites(SpriteSet sprites) {
      this.sprites = sprites;
      this.setSpriteFromAge(sprites);
   }

   @Override
   public int getLightColor(float partialTicks) {
      return 15728880;
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
      } else if (this.sprites != null) {
         this.setSpriteFromAge(this.sprites);
      }
   }
}
