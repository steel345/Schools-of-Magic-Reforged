package com.paleimitations.schoolsofmagic.common.world.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

public class SOMDeadLogFeature extends Feature<BlockStateConfiguration> {

   public SOMDeadLogFeature() {
      super(BlockStateConfiguration.CODEC);
   }

   @Override
   public boolean place(FeaturePlaceContext<BlockStateConfiguration> ctx) {
      WorldGenLevel level = ctx.level();
      RandomSource rand = ctx.random();
      BlockPos origin = ctx.origin();
      BlockState log = ctx.config().state;

      BlockPos.MutableBlockPos p = origin.mutable();
      while (p.getY() > level.getMinBuildHeight() + 4 && level.isEmptyBlock(p.below())) {
         p.move(0, -1, 0);
      }
      if (!SOMStumpFeature.canStand(level.getBlockState(p.below()))) {
         return false;
      }

      Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(rand);
      Direction.Axis axis = dir.getAxis();
      BlockState logH = log.hasProperty(RotatedPillarBlock.AXIS)
            ? log.setValue(RotatedPillarBlock.AXIS, axis) : log;

      int cMinX = (origin.getX() >> 4) << 4;
      int cMinZ = (origin.getZ() >> 4) << 4;
      int cMaxX = cMinX + 15;
      int cMaxZ = cMinZ + 15;
      int baseY = p.getY();
      int len = 2 + rand.nextInt(4);
      for (int a = 0; a <= len; a++) {
         int x = origin.getX() + dir.getStepX() * a;
         int z = origin.getZ() + dir.getStepZ() * a;
         if (x < cMinX || x > cMaxX || z < cMinZ || z > cMaxZ) {
            break;
         }
         BlockPos lp = new BlockPos(x, baseY, z);
         SOMStumpFeature.tryLog(level, lp, logH);
         if (rand.nextInt(7) == 0) {
            BlockPos top = lp.above();
            if (level.isEmptyBlock(top)) {
               level.setBlock(top, (rand.nextBoolean() ? Blocks.BROWN_MUSHROOM : Blocks.RED_MUSHROOM).defaultBlockState(), 2);
            }
         }
      }
      return true;
   }
}
