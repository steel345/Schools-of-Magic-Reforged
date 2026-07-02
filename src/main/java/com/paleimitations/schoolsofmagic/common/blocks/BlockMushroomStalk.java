package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockMushroomStalk extends SOMPlant {

   public static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 2);

   private static final VoxelShape SHAPE_TYPE0 = Shapes.or(
      Block.box(6.0D, 0.0D, 6.0D, 10.0D, 7.0D, 10.0D),
      Block.box(6.5D, 7.0D, 6.5D, 9.5D, 16.0D, 9.5D));
   private static final VoxelShape SHAPE_TYPE1 = Shapes.or(
      Block.box(5.5D, 0.0D, 5.5D, 10.5D, 8.0D, 10.5D),
      Block.box(6.0D, 8.0D, 6.0D, 10.0D, 16.0D, 10.0D));
   private static final VoxelShape SHAPE_TYPE2 = Shapes.or(
      Block.box(4.5D, 0.0D, 4.5D, 11.5D, 9.0D, 11.5D),
      Block.box(5.0D, 9.0D, 5.0D, 11.0D, 16.0D, 11.0D));

   public BlockMushroomStalk(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(TYPE, 0));
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      switch (state.getValue(TYPE)) {
         case 0: return SHAPE_TYPE0;
         case 1: return SHAPE_TYPE1;
         default: return SHAPE_TYPE2;
      }
   }

   @Override
   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      var level = ctx.getLevel();
      BlockPos pos = ctx.getClickedPos();
      if (level.getBlockState(pos.below(1)).getBlock() == this) {
         level.setBlockAndUpdate(pos.below(1), this.defaultBlockState().setValue(TYPE, 1));
      }
      if (level.getBlockState(pos.below(2)).getBlock() == this) {
         level.setBlockAndUpdate(pos.below(2), this.defaultBlockState().setValue(TYPE, 2));
      }
      return this.defaultBlockState().setValue(TYPE, 0);
   }

   @Override
   public boolean isRandomlyTicking(BlockState state) { return true; }

   @Override
   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
      super.randomTick(state, level, pos, rand);
      if (!this.canSurvive(state, level, pos)) level.destroyBlock(pos, true);
   }

   @Override
   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
      return state.is(Blocks.MYCELIUM) || state.is(Blocks.PODZOL) || state.is(Blocks.GRASS_BLOCK)
          || state.getBlock() == BlockRegistry.mushroom_stalk.get();
   }

   @Override
   public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
      BlockPos below = pos.below();
      return this.mayPlaceOn(level.getBlockState(below), level, below);
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(TYPE);
   }
}
