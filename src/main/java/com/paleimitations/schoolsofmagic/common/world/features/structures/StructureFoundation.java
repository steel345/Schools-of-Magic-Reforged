package com.paleimitations.schoolsofmagic.common.world.features.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class StructureFoundation {

   private StructureFoundation() {}

   public static void fill(WorldGenLevel level, int x0, int y0, int z0, int x1, int y1, int z1) {
      int[][] base = new int[x1 - x0 + 1][z1 - z0 + 1];
      int globalMin = Integer.MAX_VALUE;
      BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos();

      for (int x = x0; x <= x1; x++) {
         for (int z = z0; z <= z1; z++) {
            int found = Integer.MIN_VALUE;
            for (int y = y0; y <= y1; y++) {
               m.set(x, y, z);
               if (!level.getBlockState(m).isAir()) { found = y; break; }
            }
            base[x - x0][z - z0] = found;
            if (found != Integer.MIN_VALUE && found < globalMin) globalMin = found;
         }
      }
      if (globalMin == Integer.MAX_VALUE) return;

      BlockState dirt = Blocks.DIRT.defaultBlockState();
      int limit = y0 - 12;
      for (int x = x0; x <= x1; x++) {
         for (int z = z0; z <= z1; z++) {
            int baseY = base[x - x0][z - z0];
            if (baseY == Integer.MIN_VALUE || baseY > globalMin + 2) continue;
            for (int y = baseY - 1; y >= limit; y--) {
               m.set(x, y, z);
               BlockState s = level.getBlockState(m);
               if (s.isAir() || s.canBeReplaced()) {
                  level.setBlock(m, dirt, 2);
               } else {
                  break;
               }
            }
         }
      }
   }
}
