package com.paleimitations.schoolsofmagic.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;

public class ParticleFlame extends TextureSheetParticle {
   private final float flameScale;

   public ParticleFlame(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
      super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
      this.xd = this.xd * 0.01D + xSpeedIn;
      this.yd = this.yd * 0.01D + ySpeedIn;
      this.zd = this.zd * 0.01D + zSpeedIn;
      this.x += (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
      this.y += (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
      this.z += (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
      this.flameScale = this.quadSize;
      this.rCol = 1.0F;
      this.gCol = 1.0F;
      this.bCol = 1.0F;
      this.lifetime = (int)(8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
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
   public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {

      if (this.sprite == null) return;
      float f = ((float)this.age + partialTicks) / (float)this.lifetime;
      this.quadSize = this.flameScale * (1.0F - f * f * 0.5F);
      super.render(buffer, renderInfo, partialTicks);
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
   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      if (this.age++ >= this.lifetime) {
         this.remove();
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
