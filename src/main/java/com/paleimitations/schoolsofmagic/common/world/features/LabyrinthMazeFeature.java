package com.paleimitations.schoolsofmagic.common.world.features;

import java.util.Random;

import com.paleimitations.schoolsofmagic.common.blocks.BlockFaeStone;
import com.paleimitations.schoolsofmagic.common.blocks.EnumFaeStone;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class LabyrinthMazeFeature extends Feature<NoneFeatureConfiguration> {

   private static final int WALL_HEIGHT = 5;

   public LabyrinthMazeFeature() {
      super(NoneFeatureConfiguration.CODEC);
   }

   @Override
   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
      WorldGenLevel level = ctx.level();
      BlockPos origin = ctx.origin();
      RandomSource rand = ctx.random();
      int chunkX = origin.getX() >> 4;
      int chunkZ = origin.getZ() >> 4;
      int baseX = chunkX << 4;
      int baseZ = chunkZ << 4;

      boolean[] map = LabyrinthByteMaps.getLabyrinthMap(
         new Random((long) chunkX * 341873128712L + (long) chunkZ * 132897987541L));

      BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
      for (int dx = 0; dx < 16; dx++) {
         for (int dz = 0; dz < 16; dz++) {

            int num = (dz / 2) + (dx / 2) * 8;
            boolean pathCell = map[num];
            int wx = baseX + dx, wz = baseZ + dz;
            int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, wx, wz) - 1;

            if (!isFaeSurface(level, pos.set(wx, surfaceY, wz))) continue;

            if (pathCell) {

               for (int h = 1; h <= WALL_HEIGHT + 1; h++) {
                  pos.set(wx, surfaceY + h, wz);
                  BlockState s = level.getBlockState(pos);
                  if (!s.isAir() && s.getFluidState().isEmpty()) {
                     level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                  }
               }
            } else {

               level.setBlock(pos.set(wx, surfaceY, wz), wallVariant(rand), 2);
               for (int h = 1; h <= WALL_HEIGHT; h++) {
                  if (h > 3 && rand.nextFloat() < 0.30F) continue;
                  pos.set(wx, surfaceY + h, wz);
                  level.setBlock(pos, wallVariant(rand), 2);
               }
            }
         }
      }
      return true;
   }

   private static boolean isFaeSurface(WorldGenLevel level, BlockPos pos) {
      BlockState s = level.getBlockState(pos);
      return !s.isAir() && s.getFluidState().isEmpty();
   }

   private static BlockState wallVariant(RandomSource r) {
      switch (r.nextInt(13)) {
         case 11: case 12:               return fae(EnumFaeStone.COBBLE);
         case 0: case 1:                 return fae(EnumFaeStone.MOSSY_COBBLE);
         case 2: case 3: case 4: case 10: return fae(EnumFaeStone.CRACKED_BRICKS);
         case 5: case 6:                 return fae(EnumFaeStone.MOSSY_BRICKS);
         case 7: case 8:                 return fae(EnumFaeStone.BRICKS);
         case 9:                         return Blocks.LIGHT_GRAY_TERRACOTTA.defaultBlockState();
         default:                        return fae(EnumFaeStone.BRICKS);
      }
   }

   private static BlockState fae(EnumFaeStone variant) {
      return BlockRegistry.fae_stone.get().defaultBlockState().setValue(BlockFaeStone.VARIANT, variant);
   }
}
