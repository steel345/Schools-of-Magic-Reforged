package com.paleimitations.schoolsofmagic.common.world.features.trees;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

public class SOMTreeFeatureElder extends SOMTreeFeature {

   @Override protected RegistryObject<Block> log() { return BlockRegistry.log_elder; }
   @Override protected RegistryObject<Block> leaves() { return BlockRegistry.leaves_elder; }

   @Override
   protected boolean generate(WorldGenLevel world, RandomSource rand, BlockPos position) {
      int i = rand.nextInt(3) + rand.nextInt(3) + 5;
      boolean flag = true;
      int baseY = position.getY();
      if (baseY >= world.getMinBuildHeight() + 1 && baseY + i + 1 <= maxHeight(world)) {
         for (int j = baseY; j <= baseY + 1 + i; ++j) {
            int k = 1;
            if (j == baseY) k = 0;
            if (j >= baseY + 1 + i - 2) k = 2;
            BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos();
            for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l) {
               for (int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1) {
                  if (j >= world.getMinBuildHeight() && j < maxHeight(world)) {
                     if (isReplaceable(world, m.set(l, j, i1))) continue;
                     flag = false;
                  } else {
                     flag = false;
                  }
               }
            }
         }
         if (!flag) return false;
         BlockPos down = position.below();
         if (isSoil(world, down) && baseY < maxHeight(world) - i - 1) {
            for (int r = 0; r <= 1; ++r) {
               onPlantGrow(world, down, position);
               Direction enumfacing = Direction.Plane.HORIZONTAL.getRandomDirection(rand);
               int k2 = i - rand.nextInt(4) - 1;
               int l2 = 3 - rand.nextInt(3);
               int i3 = position.getX();
               int j1 = position.getZ();
               int k1 = 0;
               for (int l1 = 0; l1 < i; ++l1) {
                  int i2 = baseY + l1;
                  if (l1 >= k2 && l2 > 0) {
                     i3 += enumfacing.getStepX();
                     j1 += enumfacing.getStepZ();
                     --l2;
                  }
                  BlockPos bp = new BlockPos(i3, i2, j1);
                  BlockState st = world.getBlockState(bp);
                  if (!st.isAir() && !st.is(net.minecraft.tags.BlockTags.LEAVES)) continue;
                  placeLogAt(world, bp);
                  k1 = i2;
               }
               BlockPos blockpos2 = new BlockPos(i3, k1, j1);
               for (int j3 = -3; j3 <= 3; ++j3) {
                  for (int i4 = -3; i4 <= 3; ++i4) {
                     if (Math.abs(j3) == 3 && Math.abs(i4) == 3) continue;
                     placeLeafAt(world, blockpos2.offset(j3, 0, i4));
                  }
               }
               blockpos2 = blockpos2.above();
               for (int k3 = -1; k3 <= 1; ++k3) {
                  for (int j4 = -1; j4 <= 1; ++j4) {
                     placeLeafAt(world, blockpos2.offset(k3, 0, j4));
                  }
               }
               placeLeafAt(world, blockpos2.offset(2, 0, 0));
               placeLeafAt(world, blockpos2.offset(-2, 0, 0));
               placeLeafAt(world, blockpos2.offset(0, 0, 2));
               placeLeafAt(world, blockpos2.offset(0, 0, -2));
               i3 = position.getX();
               j1 = position.getZ();
               Direction enumfacing1 = Direction.Plane.HORIZONTAL.getRandomDirection(rand);
               if (enumfacing1 == enumfacing) continue;
               int l3 = k2 - rand.nextInt(2) - 1;
               int k4 = 1 + rand.nextInt(3);
               k1 = 0;
               for (int l4 = l3; l4 < i && k4 > 0; ++l4, --k4) {
                  if (l4 < 1) continue;
                  int j2 = baseY + l4;
                  i3 += enumfacing1.getStepX();
                  j1 += enumfacing1.getStepZ();
                  BlockPos blockpos1 = new BlockPos(i3, j2, j1);
                  BlockState st = world.getBlockState(blockpos1);
                  if (!st.isAir() && !st.is(net.minecraft.tags.BlockTags.LEAVES)) continue;
                  placeLogAt(world, blockpos1);
                  k1 = j2;
               }
               if (k1 <= 0) continue;
               BlockPos blockpos3 = new BlockPos(i3, k1, j1);
               for (int i5 = -2; i5 <= 2; ++i5) {
                  for (int k5 = -2; k5 <= 2; ++k5) {
                     if (Math.abs(i5) == 2 && Math.abs(k5) == 2) continue;
                     placeLeafAt(world, blockpos3.offset(i5, 0, k5));
                  }
               }
               blockpos3 = blockpos3.above();
               for (int j5 = -1; j5 <= 1; ++j5) {
                  for (int l5 = -1; l5 <= 1; ++l5) {
                     placeLeafAt(world, blockpos3.offset(j5, 0, l5));
                  }
               }
            }
            return true;
         }
         return false;
      }
      return false;
   }
}
