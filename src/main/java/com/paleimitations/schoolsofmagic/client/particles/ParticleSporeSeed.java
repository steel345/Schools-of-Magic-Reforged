package com.paleimitations.schoolsofmagic.client.particles;

import com.paleimitations.schoolsofmagic.common.registries.ParticleTypeRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;

// goetys seed: eight ticks, six puffs a tick, scattered in a four block cube, each puff told how
// far through the burst it is so the wave shrinks as it goes
public class ParticleSporeSeed extends NoRenderParticle {
   private static final int LIFE_TIME = 1;
   private static final double SPREAD = 1.2D;
   private static final int PUFFS = 4;

   private final float r;
   private final float g;
   private final float b;
   private int life;

   public ParticleSporeSeed(ClientLevel level, double x, double y, double z, double r, double g, double b) {
      super(level, x, y, z, 0.0D, 0.0D, 0.0D);
      this.r = (float) r;
      this.g = (float) g;
      this.b = (float) b;
      this.lifetime = LIFE_TIME;
   }

   @Override
   public void tick() {
      for (int i = 0; i < PUFFS; ++i) {
         double x = this.x + (this.random.nextDouble() - this.random.nextDouble()) * SPREAD;
         double y = this.y + (this.random.nextDouble() - this.random.nextDouble()) * SPREAD;
         double z = this.z + (this.random.nextDouble() - this.random.nextDouble()) * SPREAD;
         SporeTint.next(this.r, this.g, this.b);
         this.level.addParticle(ParticleTypeRegistry.SPORE.get(), x, y, z,
            (double) ((float) this.life / (float) LIFE_TIME), 0.0D, 0.0D);
      }

      if (++this.life == LIFE_TIME) {
         this.remove();
      }
   }
}
