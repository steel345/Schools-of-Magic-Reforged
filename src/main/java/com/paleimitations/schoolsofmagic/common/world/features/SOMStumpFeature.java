package com.paleimitations.schoolsofmagic.common.world.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

public class SOMStumpFeature extends Feature<BlockStateConfiguration> {

   public SOMStumpFeature() {
      super(BlockStateConfiguration.CODEC);
   }

   @Override
   public boolean place(FeaturePlaceContext<BlockStateConfiguration> ctx) {
      WorldGenLevel level = ctx.level();
      RandomSource rand = ctx.random();
      BlockPos origin = ctx.origin();
      BlockState log = ctx.config().state;
      BlockState logY = log.hasProperty(RotatedPillarBlock.AXIS)
            ? log.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y) : log;

      BlockPos.MutableBlockPos p = origin.mutable();
      while (p.getY() > level.getMinBuildHeight() + 4 && level.isEmptyBlock(p.below())) {
         p.move(0, -1, 0);
      }
      if (!canStand(level.getBlockState(p.below()))) {
         return false;
      }

      int cMinX = (origin.getX() >> 4) << 4;
      int cMinZ = (origin.getZ() >> 4) << 4;
      int cMaxX = cMinX + 15;
      int cMaxZ = cMinZ + 15;
      int baseY = p.getY();
      int h = 2 + rand.nextInt(2);
      for (int i = 0; i <= h; i++) {
         tryLog(level, new BlockPos(origin.getX(), baseY + i, origin.getZ()), logY);
      }
      for (Direction d : Direction.Plane.HORIZONTAL) {
         if (rand.nextInt(3) != 0) {
            continue;
         }
         int x = origin.getX() + d.getStepX();
         int z = origin.getZ() + d.getStepZ();
         if (x < cMinX || x > cMaxX || z < cMinZ || z > cMaxZ) {
            continue;
         }
         int top = rand.nextInt(h + 1);
         for (int i = -1; i <= top; i++) {
            tryLog(level, new BlockPos(x, baseY + i, z), logY);
         }
      }
      for (int k = 0; k < 4; k++) {
         Direction d = Direction.Plane.HORIZONTAL.getRandomDirection(rand);
         int x = origin.getX() + d.getStepX();
         int z = origin.getZ() + d.getStepZ();
         if (x < cMinX || x > cMaxX || z < cMinZ || z > cMaxZ) {
            continue;
         }
         BlockPos mp = new BlockPos(x, baseY, z);
         if (rand.nextInt(2) == 0 && level.isEmptyBlock(mp) && canStand(level.getBlockState(mp.below()))) {
            level.setBlock(mp, (rand.nextBoolean() ? Blocks.BROWN_MUSHROOM : Blocks.RED_MUSHROOM).defaultBlockState(), 2);
         }
      }
      return true;
   }

   static boolean canStand(BlockState s) {
      return s.is(Blocks.GRASS_BLOCK) || s.is(BlockTags.DIRT) || s.is(BlockTags.BASE_STONE_OVERWORLD)
            || s.is(Blocks.MUD) || s.is(Blocks.COARSE_DIRT);
   }

   static void tryLog(WorldGenLevel level, BlockPos pos, BlockState log) {
      BlockState cur = level.getBlockState(pos);
      if (cur.isAir() || cur.canBeReplaced() || cur.is(BlockTags.LEAVES) || cur.is(BlockTags.REPLACEABLE)) {
         level.setBlock(pos, log, 2);
      }
   }
}
