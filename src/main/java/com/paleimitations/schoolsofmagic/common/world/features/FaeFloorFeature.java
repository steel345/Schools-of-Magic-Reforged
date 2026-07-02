package com.paleimitations.schoolsofmagic.common.world.features;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class FaeFloorFeature extends Feature<NoneFeatureConfiguration> {

   public FaeFloorFeature() {
      super(NoneFeatureConfiguration.CODEC);
   }

   @Override
   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
      WorldGenLevel level = ctx.level();
      RandomSource rand = ctx.random();
      BlockState bedrock = Blocks.BEDROCK.defaultBlockState();
      BlockState faeStone = com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.fae_stone.get().defaultBlockState();
      int minY = level.getMinBuildHeight();
      int x0 = (ctx.origin().getX() >> 4) << 4;
      int z0 = (ctx.origin().getZ() >> 4) << 4;
      BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
      for (int dx = 0; dx < 16; dx++) {
         for (int dz = 0; dz < 16; dz++) {
            int wx = x0 + dx, wz = z0 + dz;
            level.setBlock(pos.set(wx, minY, wz), bedrock, 2);
            int top = minY;
            for (int y = 1; y <= 3; y++) {
               if (rand.nextInt(y + 1) == 0) {
                  level.setBlock(pos.set(wx, minY + y, wz), bedrock, 2);
                  top = minY + y;
               }
            }

            for (int y = top + 1; y <= top + 2; y++) {
               BlockState s = level.getBlockState(pos.set(wx, y, wz));
               if (s.isAir() || !s.getFluidState().isEmpty()) {
                  level.setBlock(pos, faeStone, 2);
               }
            }

            for (int y = minY; y <= minY + 24; y++) {
               BlockState s = level.getBlockState(pos.set(wx, y, wz));
               if (s.is(Blocks.LAVA) || s.getFluidState().is(net.minecraft.tags.FluidTags.LAVA)) {
                  level.setBlock(pos, faeStone, 2);
               }
            }
         }
      }
      return true;
   }
}
