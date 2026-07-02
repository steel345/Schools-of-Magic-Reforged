package com.paleimitations.schoolsofmagic.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;

public class ParticleEmbers extends TextureSheetParticle implements IAnimatedParticle {
   private int ticksExisted;
   private SpriteSet sprites;

   @Override
   public void setSprites(SpriteSet s) {
      this.sprites = s;
      this.setSpriteFromAge(s);
   }

   public ParticleEmbers(ClientLevel world, double xCoordIn, double yCoordIn, double zCoordIn, double motionXIn, double motionYIn, double motionZIn) {
      super(world, xCoordIn, yCoordIn, zCoordIn, motionXIn, motionYIn, motionZIn);
      this.xd = this.xd * 0.01D + motionXIn;
      this.yd = this.yd * 0.01D + motionYIn;
      this.zd = this.zd * 0.01D + motionZIn;
      this.x += (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
      this.y += (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
      this.z += (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
      this.rCol = 1.0F;
      this.gCol = 1.0F;
      this.bCol = 1.0F;
      this.setSize(0.3F, 0.3F);

      this.quadSize = (this.random.nextFloat() * 0.5F + 0.5F) * 0.5F;
   }

   @Override
   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
   }

   @Override
   protected int getLightColor(float partialTick) {
      float f = ((float)this.age + partialTick) / (float)this.lifetime;
      f = Mth.clamp(f, 0.0F, 1.0F);
      int i = super.getLightColor(partialTick);
      int j = i & 0xFF;
      int k = i >> 16 & 0xFF;
      if ((j += (int)(f * 15.0F * 16.0F)) > 240) {
         j = 240;
      }
      return j | k << 16;
   }

   @Override
   public void move(double x, double y, double z) {
      this.setBoundingBox(this.getBoundingBox().move(x, y, z));
      this.setLocationFromBoundingbox();
   }

   @Override
   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      ++this.ticksExisted;
      if (this.ticksExisted % 2 == 0) {
         ++this.age;
      }
      if (this.age >= this.lifetime) {
         this.remove();
      }

      if (this.sprites != null) {
         this.setSpriteFromAge(this.sprites);
      }
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
