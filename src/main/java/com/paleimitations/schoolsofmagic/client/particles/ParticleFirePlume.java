package com.paleimitations.schoolsofmagic.client.particles;

import java.util.Random;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;

public class ParticleFirePlume extends TextureSheetParticle {
   private int life;
   private int ticksExisted;
   private final int lifeTime;

   public ParticleFirePlume(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
      super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
      this.xd = this.xd * 0.01D + xSpeedIn;
      this.yd = this.yd * 0.01D + ySpeedIn;
      this.zd = this.zd * 0.01D + zSpeedIn;
      this.x += (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
      this.y += (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
      this.z += (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
      this.rCol = 1.0F;
      this.gCol = 1.0F;
      this.bCol = 1.0F;
      this.lifeTime = 7;
      this.ticksExisted = new Random().nextInt(3);
      this.life = new Random().nextInt(3);
   }

   @Override
   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
   }

   @Override
   public void move(double x, double y, double z) {
      this.setBoundingBox(this.getBoundingBox().move(x, y, z));
      this.setLocationFromBoundingbox();
   }

   @Override
   public void tick() {
      ++this.ticksExisted;
      if (this.ticksExisted % 2 == 0) {
         ++this.life;
      }
      if (this.life == this.lifeTime) {
         this.remove();
      }
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      this.move(this.xd, this.yd, this.zd);
      this.xd *= 0.96D;
      this.yd *= 0.96D;
      this.zd *= 0.96D;
      if (this.onGround) {
         this.xd *= 0.7D;
         this.zd *= 0.7D;
      }
   }
}
