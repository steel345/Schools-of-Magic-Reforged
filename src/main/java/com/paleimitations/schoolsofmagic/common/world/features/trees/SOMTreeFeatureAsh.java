package com.paleimitations.schoolsofmagic.common.world.features.trees;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

public class SOMTreeFeatureAsh extends SOMTreeFeature {

   @Override protected RegistryObject<Block> log() { return BlockRegistry.log_ash; }
   @Override protected RegistryObject<Block> leaves() { return BlockRegistry.leaves_ash; }

   private boolean placeTreeOfHeight(WorldGenLevel world, BlockPos pos, int iInt, int height) {
      int i = pos.getX(); int j = pos.getY(); int k = pos.getZ();
      BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos();
      for (int l = 0; l <= height + 1; ++l) {
         int radius = iInt;
         if (l <= iInt) radius = 0;
         if (l > iInt) radius = iInt + 2;
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
      int i = rand.nextInt(3);
      int x = position.getX(); int y = position.getY(); int z = position.getZ();
      int treeSize = i + 13;
      if (y >= world.getMinBuildHeight() + 1 && y + i + 1 < maxHeight(world)) {
         BlockPos down = position.below();
         if (!isSoil(world, down) || y >= maxHeight(world) - treeSize - 1) return false;
         if (!placeTreeOfHeight(world, position, i, treeSize)) return false;
         if (isSoil(world, down) && y < maxHeight(world) - i - 1) {
            int a;
            onPlantGrow(world, down, position);
            placeBase2(world, new BlockPos(x, y + 7 + i, z));
            for (a = 0; a <= rand.nextInt(5) + 3; ++a) {
               int xDif = -2 - i + rand.nextInt(4 + i * 2);
               int zDif = -2 - i + rand.nextInt(4 + i * 2);
               int yDif = 4 + i + rand.nextInt(5);
               int limbLength = rand.nextInt(1) + i;
               int d = 0;
               if (xDif == 0 || zDif == 0) continue;
               placeBase2(world, new BlockPos(x + xDif, y + yDif, z + zDif));
               while (xDif != 0 || zDif != 0) {
                  BlockState log = trunkY();
                  if (d < limbLength) {
                     log = trunkY();
                  } else {
                     if (xDif > 0 && zDif > 0) log = trunkX();
                     if (xDif > 0 && zDif < 0) log = trunkZ();
                     if (xDif < 0 && zDif > 0) log = trunkZ();
                     if (xDif < 0 && zDif < 0) log = trunkX();
                     if (xDif == 0) log = trunkZ();
                     if (zDif == 0) log = trunkX();
                  }
                  if (d <= limbLength) ++d;
                  placeLog1At(world, new BlockPos(x + xDif, y + yDif - d, z + zDif), log);
                  if (xDif != 0) xDif -= xDif / Math.abs(xDif);
                  if (zDif == 0) continue;
                  zDif -= zDif / Math.abs(zDif);
               }
            }
            for (a = 0; a <= 6 + i; ++a) {
               placeLogAt(world, new BlockPos(x, y + a, z));
            }
            return true;
         }
         return false;
      }
      return false;
   }

   private void placeFringeAt(WorldGenLevel world, BlockPos pos) {
      placeLeafAt(world, pos);
      placeLeafAt(world, pos.below());
      placeLeafAt(world, pos.north());
      placeLeafAt(world, pos.south());
      placeLeafAt(world, pos.east());
      placeLeafAt(world, pos.west());
   }

   private void placeBase2(WorldGenLevel world, BlockPos pos) {
      int c, a;
      int x = pos.getX(); int y = pos.getY(); int z = pos.getZ();
      for (a = -1; a <= 1; ++a) {
         for (int b = 0; b <= 2; ++b) {
            for (int c2 = -2; c2 <= 2; ++c2) {
               placeLeafAt(world, new BlockPos(x + a, y + b, z + c2));
               placeLeafAt(world, new BlockPos(x + c2, y + b, z + a));
            }
         }
      }
      for (a = -1; a <= 1; ++a) {
         for (c = -3; c <= 3; ++c) {
            placeLeafAt(world, new BlockPos(x + a, y + 1, z + c));
            placeLeafAt(world, new BlockPos(x + c, y + 1, z + a));
         }
      }
      for (a = -2; a <= 2; ++a) {
         for (c = -2; c <= 2; ++c) {
            placeLeafAt(world, new BlockPos(x + a, y + 1, z + c));
            placeLeafAt(world, new BlockPos(x + c, y + 1, z + a));
         }
      }
      placeFringeAt(world, new BlockPos(x, y + 3, z));
      for (int k = 0; k <= 2; ++k) {
         set(world, new BlockPos(x, y + k, z), trunkY());
      }
   }
}
