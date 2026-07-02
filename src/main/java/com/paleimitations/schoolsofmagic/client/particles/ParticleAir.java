package com.paleimitations.schoolsofmagic.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;

public class ParticleAir extends TextureSheetParticle {

   public ParticleAir(ClientLevel world, double xCoordIn, double yCoordIn, double zCoordIn, int particleAge, int particleMaxAge, float red, float green, float blue, float alpha) {
      super(world, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
      this.rCol = red;
      this.bCol = blue;
      this.gCol = green;
      this.alpha = alpha;
      this.lifetime = particleMaxAge;
      this.age = particleAge;
      this.hasPhysics = false;
      this.setSize(0.3F, 0.3F);
   }

   @Override
   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
   }

   @Override
   public void move(double x, double y, double z) {

   }

   @Override
   public void tick() {
      if (this.age++ >= this.lifetime) {
         this.remove();
      }
   }
}
