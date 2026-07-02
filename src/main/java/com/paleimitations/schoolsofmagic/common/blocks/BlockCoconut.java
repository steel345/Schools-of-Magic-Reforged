package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockCoconut extends HorizontalDirectionalBlock {

   protected static final VoxelShape COCOA_SOUTH_SHAPE = Block.box(5.0D, 5.0D, 11.0D, 11.0D, 11.0D, 16.0D);
   protected static final VoxelShape COCOA_NORTH_SHAPE = Block.box(5.0D, 5.0D, 0.0D, 11.0D, 11.0D, 5.0D);
   protected static final VoxelShape COCOA_WEST_SHAPE  = Block.box(0.0D, 5.0D, 5.0D, 5.0D, 11.0D, 11.0D);
   protected static final VoxelShape COCOA_EAST_SHAPE  = Block.box(11.0D, 5.0D, 5.0D, 16.0D, 11.0D, 11.0D);

   public BlockCoconut(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
   }

   public boolean canBlockStay(LevelReader level, BlockPos pos, BlockState state) {
      BlockPos at = pos.relative(state.getValue(FACING));
      BlockState neighbor = level.getBlockState(at);
      return neighbor.getBlock() == BlockRegistry.log_palm.get() || neighbor.getBlock() == BlockRegistry.palm_top.get();
   }

   protected static boolean canPlaceBlockOn(LevelReader level, BlockPos pos, Direction direction) {
      if (direction == Direction.UP || direction == Direction.DOWN) return false;
      BlockState st = level.getBlockState(pos.relative(direction.getOpposite()));
      Block b = st.getBlock();
      return b == BlockRegistry.log_palm.get() || b == BlockRegistry.palm_top.get();
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      switch (state.getValue(FACING)) {
         case SOUTH: return COCOA_SOUTH_SHAPE;
         case WEST: return COCOA_WEST_SHAPE;
         case EAST: return COCOA_EAST_SHAPE;
         default: return COCOA_NORTH_SHAPE;
      }
   }

   @Override
   public BlockState rotate(BlockState state, Rotation rot) {
      return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
   }

   @Override
   public BlockState mirror(BlockState state, Mirror mir) {
      return state.rotate(mir.getRotation(state.getValue(FACING)));
   }

   @Override
   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      Direction face = ctx.getClickedFace();
      if (face.getAxis() == Direction.Axis.Y) face = Direction.NORTH;
      return this.defaultBlockState().setValue(FACING, face.getOpposite());
   }

   @Override
   public BlockState updateShape(BlockState state, Direction dir, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
      if (!canBlockStay(level, pos, state)) {
         return Blocks.AIR.defaultBlockState();
      }
      return super.updateShape(state, dir, neighbor, level, pos, neighborPos);
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(FACING);
   }
}
