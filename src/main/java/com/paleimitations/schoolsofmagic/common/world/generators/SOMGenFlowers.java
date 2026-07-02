package com.paleimitations.schoolsofmagic.common.world.generators;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SOMGenFlowers {
   private BlockState state;
   private final int num;
   private final int radius;

   public SOMGenFlowers(BlockState type, int num) {
      this.setGeneratedBlock(type);
      this.num = num;
      this.radius = 10;
   }

   public SOMGenFlowers(BlockState type, int num, int radius) {
      this.setGeneratedBlock(type);
      this.num = num;
      this.radius = radius;
   }

   public void setGeneratedBlock(BlockState typeIn) {
      this.state = typeIn;
   }

   public BlockState getGeneratedBlock() {
      return this.state;
   }

   public boolean place(Level worldIn, RandomSource rand, BlockPos position) {
      for (int i = 0; i < this.num; i++) {
         BlockPos blockpos = position.offset(
            rand.nextInt(this.radius) - rand.nextInt(this.radius),
            rand.nextInt(4) - rand.nextInt(4),
            rand.nextInt(this.radius) - rand.nextInt(this.radius)
         );
         if (worldIn.isEmptyBlock(blockpos)
            && (!worldIn.dimensionType().hasCeiling() || blockpos.getY() < 255)
            && this.state.canSurvive(worldIn, blockpos)) {
            worldIn.setBlock(blockpos, this.state, 2);
         }
      }

      return true;
   }
}
