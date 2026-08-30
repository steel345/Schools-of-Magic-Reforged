package com.paleimitations.schoolsofmagic.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;

public class ParticleCastCircle extends TextureSheetParticle {
   public ParticleCastCircle(ClientLevel level, double x, double y, double z, double r, double g, double b) {
      super(level, x, y, z, 0.0D, 0.0D, 0.0D);
      this.xd = 0.0D;
      this.yd = 0.006D;
      this.zd = 0.0D;
      this.quadSize = 0.14F + this.random.nextFloat() * 0.05F;
      this.lifetime = 14 + this.random.nextInt(6);
      this.hasPhysics = false;
      this.gravity = 0.0F;
      this.rCol = (float) r;
      this.gCol = (float) g;
      this.bCol = (float) b;
      this.alpha = 1.0F;
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
      // holds its size the whole way, only the alpha goes
      this.alpha = 1.0F - (float) this.age / (float) this.lifetime;
      this.move(this.xd, this.yd, this.zd);
   }
}
