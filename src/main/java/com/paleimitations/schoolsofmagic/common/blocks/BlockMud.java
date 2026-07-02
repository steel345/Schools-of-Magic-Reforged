package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockMud extends SOMBlock {

   protected static final VoxelShape SOUL_SAND_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 10.4, 16.0);
   public static final BooleanProperty FULL = BooleanProperty.create("full");

   public BlockMud(BlockBehaviour.Properties props) {
      super(props);

      this.registerDefaultState(this.stateDefinition.any().setValue(FULL, true));
   }

   @Override
   public BlockState getStateForPlacement(net.minecraft.world.item.context.BlockPlaceContext ctx) {
      return this.defaultBlockState().setValue(FULL, this.computeFull(ctx.getLevel(), ctx.getClickedPos()));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(FULL);
   }

   private boolean computeFull(BlockGetter world, BlockPos pos) {
      BlockState above = world.getBlockState(pos.above());
      Block ab = above.getBlock();
      if (ab instanceof BlockPlantWaterplant) return false;
      if (ab instanceof BlockMagicPlant) {

         try {
            EnumMagicType t = above.getValue(BlockMagicPlant.TYPE);
            if (t == EnumMagicType.HYDROMANCY) return false;
         } catch (Throwable ignored) {}
      }
      return true;
   }

   @Override
   public BlockState updateShape(BlockState state, Direction dir, BlockState neighbor, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
      if (dir == Direction.UP) return state.setValue(FULL, this.computeFull(world, pos));
      return state;
   }

   @Override
   public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return state.getValue(FULL) ? super.getCollisionShape(state, level, pos, ctx) : SOUL_SAND_SHAPE;
   }

   @Override
   public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
      entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.01D, 1.0D, 0.01D));
      if (entity.isOnFire()) entity.clearFire();
   }
}
