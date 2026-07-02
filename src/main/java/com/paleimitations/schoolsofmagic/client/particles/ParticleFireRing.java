package com.paleimitations.schoolsofmagic.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;

public class ParticleFireRing extends TextureSheetParticle {
   private int life;
   private final int lifeTime;
   private final float rotX;
   private final float rotY;
   private final float rotZ;

   public ParticleFireRing(ClientLevel worldIn, double x, double y, double z, double xSpeedIn, double ySpeedIn, double zSpeedIn, float rotationX, float rotationY, float rotationZ) {
      super(worldIn, x, y, z, xSpeedIn, ySpeedIn, zSpeedIn);
      this.lifeTime = 7;
      this.quadSize = 1.0F;
      this.hasPhysics = false;
      this.gravity = 0.0F;
      this.rCol = 1.0F;
      this.gCol = 1.0F;
      this.bCol = 1.0F;
      this.rotX = rotationX;
      this.rotY = rotationY;
      this.rotZ = rotationZ;
      this.xd = xSpeedIn;
      this.yd = ySpeedIn;
      this.zd = zSpeedIn;
   }

   @Override
   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
   }

   @Override
   public void tick() {
      ++this.life;
      if (this.life == this.lifeTime) {
         this.remove();
      }
      super.tick();
   }
}
