package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class BlockMeteorFire extends FireBlock {
   private static final IntegerProperty AGE = BlockStateProperties.AGE_15;

   public BlockMeteorFire(BlockBehaviour.Properties props) {
      super(props);
   }

   @Override
   public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
      level.scheduleTick(pos, this, 20 + random.nextInt(10));
      if (!this.canSurvive(state, level, pos)) {
         level.removeBlock(pos, false);
         return;
      }
      int age = state.getValue(AGE);
      if (age >= 15) {
         level.removeBlock(pos, false);
         return;
      }
      level.setBlock(pos, state.setValue(AGE, Integer.valueOf(age + 1)), 4);
   }
}
