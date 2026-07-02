package com.paleimitations.schoolsofmagic.common.world.features.trees;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class SOMTreeFeaturePine extends SOMTreeFeature {

   @Override protected RegistryObject<Block> log() { return BlockRegistry.log_pine; }
   @Override protected RegistryObject<Block> leaves() { return BlockRegistry.leaves_pine; }

   private boolean placeTreeOfHeight(WorldGenLevel world, BlockPos pos, int iInt, int height, int canopy) {
      int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();
      BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos();
      for (int l = 0; l <= height + 1; ++l) {
         int radius = 1;
         if (l <= iInt) radius = 0;
         if (l > iInt) radius = canopy == 0 ? 4 : 5;
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
      int i = rand.nextInt(3) + 3;
      int x = position.getX(); int y = position.getY(); int z = position.getZ();
      int num = rand.nextInt(4);
      int treeSize = num * 4 + 13 + i;
      int sizeSize = y + i;
      if (y >= world.getMinBuildHeight() + 1 && y + i + 1 < maxHeight(world)) {
         BlockPos down = position.below();
         if (!isSoil(world, down) || y >= maxHeight(world) - treeSize - 1) return false;
         if (!placeTreeOfHeight(world, position, i, treeSize, num)) return false;
         if (isSoil(world, down) && y < maxHeight(world) - i - 1) {
            onPlantGrow(world, down, position);
            for (int b = -2; b <= 2; ++b) {
               for (int c = -1; c <= 1; ++c) {
                  placeLeafAt(world, new BlockPos(x + b, sizeSize, z + c));
                  placeLeafAt(world, new BlockPos(x + c, sizeSize, z + b));
                  placeLeafAt(world, new BlockPos(x + 3, sizeSize, z));
                  placeLeafAt(world, new BlockPos(x - 3, sizeSize, z));
                  placeLeafAt(world, new BlockPos(x, sizeSize, z - 3));
                  placeLeafAt(world, new BlockPos(x, sizeSize, z + 3));
                  placeLeafAt(world, new BlockPos(x + 1, sizeSize - 1, z));
                  placeLeafAt(world, new BlockPos(x - 1, sizeSize - 1, z));
                  placeLeafAt(world, new BlockPos(x, sizeSize - 1, z - 1));
                  placeLeafAt(world, new BlockPos(x, sizeSize - 1, z + 1));
               }
            }
            for (int a = 0; a <= i; ++a) {
               placeLogAt(world, new BlockPos(x, y + a, z));
            }
            for (int r = 0; r < num; ++r) {
               int oh = sizeSize + r * 4 + 1;
               placeTeir1(world, new BlockPos(x, oh, z));
            }
            int or = sizeSize + num * 4 + 1;
            placeTeir2(world, new BlockPos(x, or, z));
            return true;
         }
         return false;
      }
      return false;
   }

   private void placeFringeAt(WorldGenLevel world, BlockPos pos) {
      placeLeafAt(world, pos);
      placeLeafAt(world, pos.above());
      placeLeafAt(world, pos.below());
      placeLeafAt(world, pos.north());
      placeLeafAt(world, pos.south());
      placeLeafAt(world, pos.east());
      placeLeafAt(world, pos.west());
   }

   private void placeTeir1(WorldGenLevel world, BlockPos pos) {
      int x = pos.getX(); int y = pos.getY(); int z = pos.getZ();
      for (int a = -1; a <= 1; ++a) {
         for (int b = 0; b <= 3; ++b) {
            for (int c = -1; c <= 1; ++c) {
               placeLeafAt(world, new BlockPos(x + a, y + b, z + c));
            }
         }
      }
      for (int d = -2; d <= 2; ++d) {
         for (int e = -2; e <= 2; ++e) {
            placeLeafAt(world, new BlockPos(x + d, y, z + e));
         }
      }
      for (int f = -1; f <= 1; ++f) {
         for (int g = 0; g <= 3; ++g) {
            for (int h = -1; h <= 1; ++h) {
               placeLeafAt(world, new BlockPos(x + f + 3 - g, y + g, z + h));
               placeLeafAt(world, new BlockPos(x + 5 - g, y + g, z));
               placeLeafAt(world, new BlockPos(x + f - 3 + g, y + g, z + h));
               placeLeafAt(world, new BlockPos(x - 5 + g, y + g, z));
               placeLeafAt(world, new BlockPos(x + f, y + g, z + h + 3 - g));
               placeLeafAt(world, new BlockPos(x, y + g, z + 5 - g));
               placeLeafAt(world, new BlockPos(x + f, y + g, z + h - 3 + g));
               placeLeafAt(world, new BlockPos(x, y + g, z - 5 + g));
            }
         }
      }
      set(world, pos, trunkY());
      set(world, new BlockPos(x, y + 1, z), trunkY());
      set(world, new BlockPos(x, y + 2, z), trunkY());
      set(world, new BlockPos(x, y + 3, z), trunkY());
      set(world, new BlockPos(x - 1, y, z), trunkX());
      set(world, new BlockPos(x - 2, y, z), trunkX());
      set(world, new BlockPos(x + 1, y, z), trunkX());
      set(world, new BlockPos(x + 2, y, z), trunkX());
      set(world, new BlockPos(x, y, z - 1), trunkZ());
      set(world, new BlockPos(x, y, z - 2), trunkZ());
      set(world, new BlockPos(x, y, z + 1), trunkZ());
      set(world, new BlockPos(x, y, z + 2), trunkZ());
   }

   private void placeTeir2(WorldGenLevel world, BlockPos pos) {
      int x = pos.getX(); int y = pos.getY(); int z = pos.getZ();
      for (int a = -1; a <= 1; ++a) {
         for (int b = 0; b <= 4; ++b) {
            for (int c = -1; c <= 1; ++c) {
               placeLeafAt(world, new BlockPos(x + a, y + b, z + c));
            }
         }
      }
      placeFringeAt(world, new BlockPos(x + 2, y + 3, z));
      placeFringeAt(world, new BlockPos(x - 2, y + 3, z));
      placeFringeAt(world, new BlockPos(x, y + 3, z + 2));
      placeFringeAt(world, new BlockPos(x, y + 3, z - 2));
      placeFringeAt(world, new BlockPos(x + 1, y + 6, z));
      placeFringeAt(world, new BlockPos(x - 1, y + 6, z));
      placeFringeAt(world, new BlockPos(x, y + 6, z + 1));
      placeFringeAt(world, new BlockPos(x, y + 6, z - 1));
      placeFringeAt(world, new BlockPos(x, y + 9, z));
      placeLeafAt(world, new BlockPos(x, y + 11, z));
      for (int f = -1; f <= 1; ++f) {
         for (int g = 0; g <= 2; ++g) {
            for (int h = -1; h <= 1; ++h) {
               placeLeafAt(world, new BlockPos(x + f + 2 - g, y + g, z + h));
               placeLeafAt(world, new BlockPos(x + 4 - g, y + g, z));
               placeLeafAt(world, new BlockPos(x + f - 2 + g, y + g, z + h));
               placeLeafAt(world, new BlockPos(x - 4 + g, y + g, z));
               placeLeafAt(world, new BlockPos(x + f, y + g, z + h + 2 - g));
               placeLeafAt(world, new BlockPos(x, y + g, z + 4 - g));
               placeLeafAt(world, new BlockPos(x + f, y + g, z + h - 2 + g));
               placeLeafAt(world, new BlockPos(x, y + g, z - 4 + g));
            }
         }
      }
      for (int k = 0; k <= 7; ++k) {
         set(world, new BlockPos(x, y + k, z), trunkY());
      }
      set(world, new BlockPos(x - 1, y, z), trunkX());
      set(world, new BlockPos(x + 1, y, z), trunkX());
      set(world, new BlockPos(x, y, z - 1), trunkZ());
      set(world, new BlockPos(x, y, z + 1), trunkZ());
   }
}
