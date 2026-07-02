package com.paleimitations.schoolsofmagic.common.world.features;

import com.paleimitations.schoolsofmagic.common.blocks.BlockMushroom;
import com.paleimitations.schoolsofmagic.common.blocks.BlockMushroomStalk;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class WildMushroomFeature extends Feature<NoneFeatureConfiguration> {

   public WildMushroomFeature() {
      super(NoneFeatureConfiguration.CODEC);
   }

   @Override
   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
      WorldGenLevel level = ctx.level();
      BlockPos pos = ctx.origin();
      RandomSource rand = ctx.random();

      if (!level.getBlockState(pos).isAir()) return false;
      BlockState ground = level.getBlockState(pos.below());
      if (!(ground.is(Blocks.GRASS_BLOCK) || ground.is(Blocks.MYCELIUM) || ground.is(Blocks.PODZOL))) return false;

      Block cap = pickCap(rand);

      int r = rand.nextInt(100);
      int stalks = r < 45 ? 0 : (r < 70 ? 1 : (r < 88 ? 2 : 3));

      for (int i = 1; i <= stalks; i++) {
         if (!level.getBlockState(pos.above(i)).isAir()) { stalks = i - 1; break; }
      }

      Block stalk = BlockRegistry.mushroom_stalk.get();
      for (int i = 0; i < stalks; i++) {
         int type = Math.min(2, stalks - i - 1);
         level.setBlock(pos.above(i), stalk.defaultBlockState().setValue(BlockMushroomStalk.TYPE, type), 2);
      }
      level.setBlock(pos.above(stalks),
         cap.defaultBlockState().setValue(BlockMushroom.AGE, 0).setValue(BlockMushroom.NATURAL, true), 2);
      return true;
   }

   private static Block pickCap(RandomSource rand) {
      int w = rand.nextInt(11);
      if (w < 4) return BlockRegistry.mushroom_pink.get();
      if (w < 7) return BlockRegistry.mushroom_white.get();
      if (w < 10) return BlockRegistry.mushroom_dark.get();
      return BlockRegistry.mushroom_grey.get();
   }
}
