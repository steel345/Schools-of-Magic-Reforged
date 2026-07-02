package com.paleimitations.schoolsofmagic.common.world.features.trees;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

public class SOMTreeFeatureWillow extends SOMTreeFeature {

   @Override protected RegistryObject<Block> log() { return BlockRegistry.log_willow; }
   @Override protected RegistryObject<Block> leaves() { return BlockRegistry.leaves_willow; }

   private BlockState hangingLeaf() {
      return BlockRegistry.leaves_hanging_willow.get().defaultBlockState()
         .setValue(LeavesBlock.DISTANCE, 1).setValue(LeavesBlock.PERSISTENT, false);
   }

   private boolean placeTreeOfHeight(WorldGenLevel world, BlockPos pos, int iInt, int height) {
      int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();
      BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos();
      for (int l = 0; l <= height + 1; ++l) {
         int radius = l < iInt ? 0 : iInt;
         for (int j1 = -radius; j1 <= radius; ++j1) {
            for (int k1 = -radius; k1 <= radius; ++k1) {
               if (isReplaceable(world, m.set(i + j1, j + l, k + k1))) continue;
               return false;
            }
         }
      }
      return true;
   }

   @Override
   protected boolean generate(WorldGenLevel world, RandomSource rand, BlockPos position) {
      int d = rand.nextInt(3);
      int i = rand.nextInt(3) + 4 + d;
      int x = position.getX(); int y = position.getY(); int z = position.getZ();
      int treeSize = 2 + i;
      if (y >= world.getMinBuildHeight() + 1 && y + i + 1 < maxHeight(world)) {
         BlockPos down = position.below();
         if (!isSoil(world, down) || y >= maxHeight(world) - treeSize - 1) return false;
         if (!placeTreeOfHeight(world, position, d + 4, treeSize)) return false;
         if (isSoil(world, down) && y < maxHeight(world) - i - 1) {
            int a;
            onPlantGrow(world, down, position);
            placeBlock(world, new BlockPos(x, y + i, z), 2 + d, 3 + d, 1, 0);
            placeBlock(world, new BlockPos(x, y + i, z), 3 + d, 2 + d, 1, 0);
            placeBlock(world, new BlockPos(x, y + i, z), 1 + d, 4 + d, 0, 0);
            placeBlock(world, new BlockPos(x, y + i, z), 4 + d, 1 + d, 0, 0);
            placeBlock(world, new BlockPos(x, y + i, z), 0 + d, 4 + d, 0, 1);
            placeBlock(world, new BlockPos(x, y + i, z), 4 + d, 0 + d, 0, 1);
            placeBlock(world, new BlockPos(x, y + i, z), 1 + d, 3 + d, 0, 2);
            placeBlock(world, new BlockPos(x, y + i, z), 3 + d, 1 + d, 0, 2);
            placeBlock(world, new BlockPos(x, y + i, z), 2 + d, 2 + d, 0, 2);
            placeBlock(world, new BlockPos(x, y + i, z), 1 + d, 2 + d, 0, 3);
            placeBlock(world, new BlockPos(x, y + i, z), 2 + d, 1 + d, 0, 3);
            for (int e = 0; e <= 3 + d; ++e) {
               placeLog1At(world, new BlockPos(x + e, y + i + 1, z), trunkX());
               placeLog1At(world, new BlockPos(x - e, y + i + 1, z), trunkX());
               placeLog1At(world, new BlockPos(x, y + i + 1, z + e), trunkZ());
               placeLog1At(world, new BlockPos(x, y + i + 1, z - e), trunkZ());
            }
            for (a = 0; a <= 2 + i; ++a) {
               placeLogAt(world, new BlockPos(x, y + a, z));
            }
            if (d == 0) {
               placeFringeAt(world, new BlockPos(x + 4, y + i, z), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 4, y + i, z + 1), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 4, y + i, z - 1), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 3, y + i, z + 2), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 3, y + i, z - 2), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 2, y + i, z + 3), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 2, y + i, z - 3), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 1, y + i, z + 4), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 1, y + i, z - 4), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x, y + i, z + 4), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x, y + i, z - 4), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 1, y + i, z + 4), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 1, y + i, z - 4), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 2, y + i, z + 3), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 2, y + i, z - 3), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 3, y + i, z + 2), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 3, y + i, z - 2), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 4, y + i, z + 1), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 4, y + i, z - 1), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 4, y + i, z), rand.nextInt(i + d));
            }
            if (d == 1) {
               placeFringeAt(world, new BlockPos(x + 5, y + i, z + 2), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 5, y + i, z + 1), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 5, y + i, z), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 5, y + i, z - 1), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 5, y + i, z - 2), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 4, y + i, z + 3), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 4, y + i, z - 3), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 3, y + i, z + 4), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 3, y + i, z - 4), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 2, y + i, z + 5), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 2, y + i, z - 5), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 1, y + i, z + 5), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 1, y + i, z - 5), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x, y + i, z + 5), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x, y + i, z - 5), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 1, y + i, z + 5), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 1, y + i, z - 5), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 2, y + i, z + 5), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 2, y + i, z - 5), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 3, y + i, z + 4), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 3, y + i, z - 4), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 4, y + i, z + 3), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 4, y + i, z - 3), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 5, y + i, z + 2), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 5, y + i, z + 1), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 5, y + i, z), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 5, y + i, z - 1), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 5, y + i, z - 2), rand.nextInt(i + d));
            }
            if (d == 2) {
               placeFringeAt(world, new BlockPos(x + 6, y + i, z + 3), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 6, y + i, z + 2), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 6, y + i, z + 1), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 6, y + i, z), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 6, y + i, z - 1), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 6, y + i, z - 2), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 6, y + i, z - 3), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 5, y + i, z + 4), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 5, y + i, z - 4), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 4, y + i, z + 5), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 4, y + i, z - 5), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 3, y + i, z + 6), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 3, y + i, z - 6), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 2, y + i, z - 6), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 2, y + i, z + 6), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 1, y + i, z + 6), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x + 1, y + i, z - 6), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x, y + i, z + 6), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x, y + i, z - 6), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 1, y + i, z + 6), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 1, y + i, z - 6), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 2, y + i, z + 6), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 2, y + i, z - 6), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 3, y + i, z + 6), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 3, y + i, z - 6), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 4, y + i, z + 5), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 4, y + i, z - 5), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 5, y + i, z + 4), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 5, y + i, z - 4), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 6, y + i, z + 3), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 6, y + i, z + 2), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 6, y + i, z + 1), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 6, y + i, z), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 6, y + i, z - 1), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 6, y + i, z - 2), rand.nextInt(i + d));
               placeFringeAt(world, new BlockPos(x - 6, y + i, z - 3), rand.nextInt(i + d));
            }
            if (rand.nextInt(5) == 0) {
               for (a = -3; a <= rand.nextInt(2); ++a) placeLogMaybe(world, new BlockPos(x - 1, y + a, z));
            }
            if (rand.nextInt(5) == 0) {
               for (a = -3; a <= rand.nextInt(2); ++a) placeLogMaybe(world, new BlockPos(x + 1, y + a, z));
            }
            if (rand.nextInt(5) == 0) {
               for (a = -3; a <= rand.nextInt(2); ++a) placeLogMaybe(world, new BlockPos(x, y + a, z - 1));
            }
            if (rand.nextInt(5) == 0) {
               for (a = -3; a <= rand.nextInt(2); ++a) placeLogMaybe(world, new BlockPos(x, y + a, z + 1));
            }
            return true;
         }
         return false;
      }
      return false;
   }

   private void placeLeaf1At(WorldGenLevel world, BlockPos pos) {
      BlockState s = world.getBlockState(pos);
      if (s.isAir() || s.is(net.minecraft.tags.BlockTags.LEAVES)) {
         set(world, pos, hangingLeaf());
      }
   }

   private void placeFringeAt(WorldGenLevel world, BlockPos pos, int length) {
      for (int u = 1; u <= length; ++u) {
         if (!world.getBlockState(pos.offset(0, -u, 0).below()).isAir()) continue;
         placeLeaf1At(world, pos.offset(0, -u, 0));
      }
   }

   private void placeBlock(WorldGenLevel world, BlockPos pos, int xWidth, int zWidth, int yLength, int height) {
      for (int a = -xWidth; a <= xWidth; ++a) {
         for (int b = -zWidth; b <= zWidth; ++b) {
            for (int c = 0; c <= yLength; ++c) {
               placeLeafAt(world, new BlockPos(pos.getX() + a, pos.getY() + height + c, pos.getZ() + b));
            }
         }
      }
   }
}
