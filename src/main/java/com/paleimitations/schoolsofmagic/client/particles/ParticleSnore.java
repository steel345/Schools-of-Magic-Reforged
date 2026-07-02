package com.paleimitations.schoolsofmagic.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;

public class ParticleSnore extends TextureSheetParticle {
   private float smokeParticleScale;

   public ParticleSnore(ClientLevel world, double xCoordIn, double yCoordIn, double zCoordIn, double motionXIn, double motionYIn, double motionZIn) {
      this(world, xCoordIn, yCoordIn, zCoordIn, motionXIn, motionYIn, motionZIn, 1.0F);
   }

   public ParticleSnore(ClientLevel world, double xCoordIn, double yCoordIn, double zCoordIn, double motionXIn, double motionYIn, double motionZIn, float par14) {
      super(world, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
      this.xd *= 0.1D;
      this.yd *= 0.1D;
      this.zd *= 0.1D;
      this.xd += motionXIn;
      this.yd += motionYIn;
      this.zd += motionZIn;
      float f = (float)(Math.random() * 0.3D) + 0.3F;
      this.rCol = f;
      this.gCol = f;
      this.bCol = f;
      this.quadSize *= 0.75F;
      this.quadSize *= par14;
      this.smokeParticleScale = this.quadSize;
      this.lifetime = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
      this.lifetime = (int)((float)this.lifetime * par14);
   }

   @Override
   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
   }

   @Override
   public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {

      if (this.sprite == null) return;
      float f = ((float)this.age + partialTicks) / (float)this.lifetime * 32.0F;
      f = Mth.clamp(f, 0.0F, 1.0F);
      this.quadSize = this.smokeParticleScale * f;
      super.render(buffer, renderInfo, partialTicks);
   }

   @Override
   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      if (this.age++ >= this.lifetime) {
         this.remove();
      }
      this.yd += 0.004D;
      this.move(this.xd, this.yd, this.zd);
      if (this.y == this.yo) {
         this.xd *= 1.1D;
         this.zd *= 1.1D;
      }
      this.xd *= 0.96D;
      this.yd *= 0.96D;
      this.zd *= 0.96D;
      if (this.onGround) {
         this.xd *= 0.7D;
         this.zd *= 0.7D;
      }
   }
}
