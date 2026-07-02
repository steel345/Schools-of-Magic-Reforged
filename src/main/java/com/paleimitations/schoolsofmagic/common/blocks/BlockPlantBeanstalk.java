package com.paleimitations.schoolsofmagic.common.blocks;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.BlockTags;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockPlantBeanstalk extends SOMPlant {

   public static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 2);
   public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
   protected static final VoxelShape TOP_SHAPE = Block.box(0.0D, 15.68D, 0.0D, 16.0D, 16.0D, 16.0D);
   protected static final VoxelShape FULL_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

   public BlockPlantBeanstalk(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(TYPE, 0).setValue(FACING, Direction.SOUTH));
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      int t = state.getValue(TYPE);
      return (t == 0 || t == 2) ? TOP_SHAPE : FULL_SHAPE;
   }

   @Override
   public boolean isRandomlyTicking(BlockState state) { return true; }

   @Override
   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
      super.randomTick(state, level, pos, rand);
      if ((level.getBlockState(pos.below()).getBlock() == BlockRegistry.plant_beanstalk.get() || canSurvive(state, level, pos))
          && level.isEmptyBlock(pos.above())) {
         int i = 1;
         while (level.getBlockState(pos.below(i)).getBlock() == this) i++;
         if (i < 6 && rand.nextInt(15 * i) == 0) {
            level.setBlockAndUpdate(pos.above(),
               this.defaultBlockState().setValue(FACING, randomFacing(rand)).setValue(TYPE, 0));
         }
      }
   }

   @Override
   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      RandomSource rand = ctx.getLevel().random;
      return this.defaultBlockState().setValue(FACING, randomFacing(rand)).setValue(TYPE, 0);
   }

   @Override
   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {

      return state.is(this)
          || (BlockRegistry.block_mud != null && state.is(BlockRegistry.block_mud.get()))
          || state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT) || state.is(Blocks.SAND);
   }

   @Override
   public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
      BlockState below = level.getBlockState(pos.below());
      return below.is(this) || mayPlaceOn(below, level, pos.below());
   }

   public static Direction randomFacing(RandomSource rand) {
      switch (rand.nextInt(5)) {
         case 1: return Direction.EAST;
         case 2: return Direction.NORTH;
         case 3: return Direction.WEST;
         default: return Direction.SOUTH;
      }
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(FACING, TYPE);
   }

   @Override
   public boolean isLadder(BlockState state, LevelReader level, BlockPos pos,
                           net.minecraft.world.entity.LivingEntity entity) {
      return true;
   }
}
