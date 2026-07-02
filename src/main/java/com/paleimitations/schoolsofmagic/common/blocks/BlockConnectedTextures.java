package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class BlockConnectedTextures extends SOMBlock {

   public static final BooleanProperty NORTH = BooleanProperty.create("north");
   public static final BooleanProperty SOUTH = BooleanProperty.create("south");
   public static final BooleanProperty EAST = BooleanProperty.create("east");
   public static final BooleanProperty WEST = BooleanProperty.create("west");
   public static final BooleanProperty UP = BooleanProperty.create("up");
   public static final BooleanProperty DOWN = BooleanProperty.create("down");

   public BlockConnectedTextures(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any()
         .setValue(NORTH, false).setValue(SOUTH, false)
         .setValue(EAST, false).setValue(WEST, false)
         .setValue(UP, false).setValue(DOWN, false));
   }

   @Override
   public BlockState updateShape(BlockState state, net.minecraft.core.Direction dir, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
      return state
         .setValue(NORTH, level.getBlockState(pos.north()).getBlock() == this)
         .setValue(EAST,  level.getBlockState(pos.east()).getBlock() == this)
         .setValue(SOUTH, level.getBlockState(pos.south()).getBlock() == this)
         .setValue(WEST,  level.getBlockState(pos.west()).getBlock() == this)
         .setValue(UP,    level.getBlockState(pos.above()).getBlock() == this)
         .setValue(DOWN,  level.getBlockState(pos.below()).getBlock() == this);
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(NORTH, EAST, WEST, SOUTH, UP, DOWN);
   }
}
