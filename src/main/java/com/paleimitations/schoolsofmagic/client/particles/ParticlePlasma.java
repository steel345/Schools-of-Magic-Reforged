package com.paleimitations.schoolsofmagic.client.particles;

import com.paleimitations.schoolsofmagic.common.particles.PlasmaParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import org.jetbrains.annotations.Nullable;

public class ParticlePlasma extends TextureSheetParticle {

   protected ParticlePlasma(ClientLevel level, double x, double y, double z, double vx, double vy, double vz,
                            float r, float g, float b, float scale) {
      super(level, x, y, z);
      this.rCol = r;
      this.gCol = g;
      this.bCol = b;
      this.alpha = 1.0F;
      this.xd = vx;
      this.yd = vy;
      this.zd = vz;
      this.gravity = 0.0F;
      this.hasPhysics = false;
      this.friction = 0.90F;
      this.lifetime = 7 + this.random.nextInt(6);
      this.quadSize = scale * (0.7F + this.random.nextFloat() * 0.5F);
   }

   @Override
   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
   }

   @Override
   public int getLightColor(float partialTick) {
      return 0xF000F0;
   }

   @Override
   public float getQuadSize(float partialTicks) {
      float f = 1.0F - (this.age + partialTicks) / (float) this.lifetime;
      return this.quadSize * Math.max(0.0F, f);
   }

   @Override
   public void tick() {
      super.tick();
      this.alpha = Math.max(0.0F, 1.0F - (float) this.age / (float) this.lifetime);
   }

   public static class Provider implements ParticleProvider<PlasmaParticleOptions> {
      private final SpriteSet sprites;

      public Provider(SpriteSet sprites) {
         this.sprites = sprites;
      }

      @Nullable
      @Override
      public Particle createParticle(PlasmaParticleOptions options, ClientLevel level,
                                     double x, double y, double z, double vx, double vy, double vz) {
         ParticlePlasma p = new ParticlePlasma(level, x, y, z, vx, vy, vz,
            options.r, options.g, options.b, options.scale);
         p.pickSprite(this.sprites);
         return p;
      }
   }
}
