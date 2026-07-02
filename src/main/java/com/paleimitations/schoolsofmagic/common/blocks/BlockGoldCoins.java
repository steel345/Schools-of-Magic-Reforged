package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockGoldCoins extends Block {

   public static final IntegerProperty LAYERS = IntegerProperty.create("layers", 1, 8);
   protected static final VoxelShape[] SHAPE_BY_LAYER = new VoxelShape[]{
      Block.box(0.0, 0.0, 0.0, 16.0, 0.0, 16.0),
      Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
      Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
      Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
      Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
      Block.box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0),
      Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
      Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0),
      Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)
   };

   public BlockGoldCoins(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(LAYERS, 1));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(LAYERS);
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return SHAPE_BY_LAYER[state.getValue(LAYERS)];
   }

   @Override
   public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return SHAPE_BY_LAYER[state.getValue(LAYERS) - 1];
   }

   @Override
   public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
      return state.getValue(LAYERS) < 5;
   }

   @Override
   public boolean canBeReplaced(BlockState state, BlockPlaceContext ctx) {
      int layers = state.getValue(LAYERS);
      if (ctx.getItemInHand().getItem() == this.asItem() && layers < 8) {
         return !ctx.replacingClickedOnBlock() || ctx.getClickedFace() == net.minecraft.core.Direction.UP;
      }
      return layers == 1;
   }

   @Override
   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      BlockState existing = ctx.getLevel().getBlockState(ctx.getClickedPos());
      if (existing.is(this)) {
         int cur = existing.getValue(LAYERS);
         return existing.setValue(LAYERS, Math.min(8, cur + 1));
      }
      return this.defaultBlockState();
   }
}
