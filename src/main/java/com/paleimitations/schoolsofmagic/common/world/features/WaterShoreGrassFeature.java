package com.paleimitations.schoolsofmagic.common.world.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class WaterShoreGrassFeature extends Feature<NoneFeatureConfiguration> {

   public WaterShoreGrassFeature() {
      super(NoneFeatureConfiguration.CODEC);
   }

   @Override
   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
      WorldGenLevel level = ctx.level();
      BlockPos origin = ctx.origin();
      int minX = origin.getX() & ~15;
      int minZ = origin.getZ() & ~15;
      BlockState grass = Blocks.GRASS_BLOCK.defaultBlockState();
      BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos();
      boolean changed = false;

      for (int dx = 0; dx < 16; dx++) {
         for (int dz = 0; dz < 16; dz++) {
            int wx = minX + dx, wz = minZ + dz;
            int top = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, wx, wz);
            int y = top - 1;
            p.set(wx, y, wz);
            BlockState s = level.getBlockState(p);
            if (!isShoreDirt(s)) continue;

            BlockState above = level.getBlockState(p.above());
            if (!above.isAir() && !above.getFluidState().is(FluidTags.WATER)) continue;

            boolean nearWater = false;
            for (Direction d : Direction.Plane.HORIZONTAL) {
               if (level.getBlockState(p.relative(d)).getFluidState().is(FluidTags.WATER)) {
                  nearWater = true;
                  break;
               }
            }
            if (nearWater) {
               level.setBlock(p, grass, 2);
               changed = true;
            }
         }
      }
      return changed;
   }

   private static boolean isShoreDirt(BlockState s) {
      return s.is(Blocks.DIRT) || s.is(Blocks.COARSE_DIRT) || s.is(Blocks.PODZOL);
   }
}
