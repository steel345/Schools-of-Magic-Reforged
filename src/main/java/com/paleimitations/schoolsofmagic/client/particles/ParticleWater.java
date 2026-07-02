package com.paleimitations.schoolsofmagic.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class ParticleWater extends TextureSheetParticle implements IAnimatedParticle {
   private SpriteSet sprites;

   @Override
   public void setSprites(SpriteSet s) {
      this.sprites = s;
      this.setSpriteFromAge(s);
   }

   public ParticleWater(ClientLevel world, double xCoordIn, double yCoordIn, double zCoordIn, double motionXIn, double motionYIn, double motionZIn) {
      this(world, xCoordIn, yCoordIn, zCoordIn, motionXIn, motionYIn, motionZIn, 1.0F);
   }

   public ParticleWater(ClientLevel world, double xCoordIn, double yCoordIn, double zCoordIn, double motionXIn, double motionYIn, double motionZIn, float par14) {
      super(world, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
      this.xd *= 0.1D;
      this.yd *= 0.1D;
      this.zd *= 0.1D;
      this.xd += motionXIn;
      this.yd += motionYIn;
      this.zd += motionZIn;
      this.lifetime = (int)(8.0D / (Math.random() * 0.8D + 0.2D) * 8.0D);
      this.lifetime = (int)((float)this.lifetime * par14);
      this.age = world.random.nextInt(3);
      this.alpha = 1.0F;
      this.gravity = 0.04F;
      this.rCol = 1.0F;
      this.gCol = 1.0F;
      this.bCol = 1.0F;
      this.setSize(1.5F, 1.5F);
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
      this.yd -= (double)this.gravity;
      this.move(this.xd, this.yd, this.zd);
      this.xd *= 0.99D;
      this.yd *= 0.99D;
      this.zd *= 0.99D;
      if (this.sprites != null) {
         this.setSpriteFromAge(this.sprites);
      }
      if (this.onGround) {
         if (Math.random() < 0.5D) {
            this.remove();
         }
         this.xd *= 0.7D;
         this.zd *= 0.7D;
      }
      BlockPos blockpos = BlockPos.containing(this.x, this.y, this.z);
      BlockState state = this.level.getBlockState(blockpos);
      FluidState fluidState = state.getFluidState();
      boolean liquid = !fluidState.isEmpty();
      if (liquid || state.blocksMotion()) {
         double topOffset = liquid
            ? (double)fluidState.getHeight(this.level, blockpos)
            : state.getShape(this.level, blockpos).max(Direction.Axis.Y);
         double topY = (double)Mth.floor(this.y) + topOffset;
         if (this.y < topY) {
            this.remove();
         }
      }
   }
}
