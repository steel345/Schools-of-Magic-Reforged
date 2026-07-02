package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class BlockZigMural extends SOMBlock {

   public static final IntegerProperty X = IntegerProperty.create("x", 1, 11);
   public static final IntegerProperty Y = IntegerProperty.create("y", 1, 6);

   public BlockZigMural(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(X, 1).setValue(Y, 1));
   }

   @Override
   public BlockState updateShape(BlockState state, net.minecraft.core.Direction dir, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
      return state;
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(X, Y);
   }
}
