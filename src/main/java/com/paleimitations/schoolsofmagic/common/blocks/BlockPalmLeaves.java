package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockPalmLeaves extends HorizontalDirectionalBlock {

   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
   protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

   public BlockPalmLeaves(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(FACING);
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return SHAPE;
   }

   private static boolean hasPalmSupport(BlockGetter level, BlockPos pos) {
      for (Direction d : Direction.values()) {
         Block b = level.getBlockState(pos.relative(d)).getBlock();
         if (b == BlockRegistry.log_palm.get() || b == BlockRegistry.palm_top.get()) return true;
      }
      return false;
   }

   public boolean canBlockStay(Level worldIn, BlockPos pos, BlockState state) {
      return hasPalmSupport(worldIn, pos);
   }

   @Override
   public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean moving) {
      if (!level.isClientSide) {
         level.scheduleTick(pos, this, 1);
      }
   }

   @Override
   public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
      if (!hasPalmSupport(level, pos)) {
         level.destroyBlock(pos, true);
      }
   }

   @Override
   public boolean isRandomlyTicking(BlockState state) {
      return true;
   }

   @Override
   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
      if (!hasPalmSupport(level, pos)) {
         level.destroyBlock(pos, true);
      }
   }

   @Override
   public BlockState rotate(BlockState state, Rotation rot) {
      return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
   }
}
