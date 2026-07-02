package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;

public class BlockUnlitTorch extends TorchBlock implements SimpleWaterloggedBlock {

   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

   public BlockUnlitTorch(Properties properties) {
      super(properties, ParticleTypes.FLAME);
      this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.FALSE));
   }

   @Override
   public void animateTick(BlockState state, net.minecraft.world.level.Level level, BlockPos pos, RandomSource rand) {
   }

   @Override
   public net.minecraft.world.InteractionResult use(BlockState state, net.minecraft.world.level.Level level, BlockPos pos,
         net.minecraft.world.entity.player.Player player, net.minecraft.world.InteractionHand hand, net.minecraft.world.phys.BlockHitResult hit) {
      net.minecraft.world.item.ItemStack held = player.getItemInHand(hand);
      if (held.getItem() instanceof net.minecraft.world.item.FlintAndSteelItem
            && !state.getValue(WATERLOGGED)
            && com.paleimitations.schoolsofmagic.common.util.TorchExtinguishHelper.relight(level, pos)) {
         held.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
         return net.minecraft.world.InteractionResult.sidedSuccess(level.isClientSide);
      }
      return super.use(state, level, pos, player, hand, hit);
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
      builder.add(WATERLOGGED);
   }

   @Override
   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      BlockState state = super.getStateForPlacement(ctx);
      if (state == null) {
         state = this.defaultBlockState();
      }
      boolean water = ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER;
      return state.setValue(WATERLOGGED, water);
   }

   @Override
   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
   }

   @Override
   public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
      if (state.getValue(WATERLOGGED)) {
         level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
      }
      return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
   }
}
