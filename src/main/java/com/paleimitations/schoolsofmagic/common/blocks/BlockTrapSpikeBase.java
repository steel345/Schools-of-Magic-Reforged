package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySpearTrap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

public class BlockTrapSpikeBase extends SOMBlock implements EntityBlock {

   public static final DirectionProperty FACING = BlockStateProperties.FACING;

   public BlockTrapSpikeBase(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
   }

   @Override
   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      return this.defaultBlockState().setValue(FACING, ctx.getClickedFace());
   }

   @Override
   public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
      if (placer != null) {
         level.setBlock(pos, state.setValue(FACING, Direction.orderedByNearest(placer)[0].getOpposite()), 2);
      }

      if (placer instanceof net.minecraft.world.entity.player.Player
            && level.getBlockEntity(pos) instanceof TileEntitySpearTrap trap) {
         trap.setHasSpear(false);
      }
   }

   @Override
   public net.minecraft.world.InteractionResult use(BlockState state, Level level, BlockPos pos,
         net.minecraft.world.entity.player.Player player, net.minecraft.world.InteractionHand hand,
         net.minecraft.world.phys.BlockHitResult hit) {
      ItemStack held = player.getItemInHand(hand);
      boolean isSpear = held.getItem() == BlockRegistry.trap_spike.get().asItem()
            || held.getItem() == BlockRegistry.spear.get().asItem();
      if (isSpear && level.getBlockEntity(pos) instanceof TileEntitySpearTrap trap && !trap.hasSpear()) {
         if (!level.isClientSide) {
            trap.setHasSpear(true);
            if (!player.getAbilities().instabuild) held.shrink(1);
            level.playSound(null, pos, net.minecraft.sounds.SoundEvents.ANVIL_LAND, net.minecraft.sounds.SoundSource.BLOCKS, 0.6F, 1.4F);
         }
         return net.minecraft.world.InteractionResult.sidedSuccess(level.isClientSide);
      }
      return net.minecraft.world.InteractionResult.PASS;
   }

   @Override
   public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
      if (!state.is(newState.getBlock())) {
         BlockEntity be = level.getBlockEntity(pos);
         if (be instanceof TileEntitySpearTrap chest && chest.hasSpear()) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(BlockRegistry.trap_spike.get()));
         }
         Direction facing = state.getValue(FACING);
         removeExtendedSpike(level, pos.relative(facing));
         removeExtendedSpike(level, pos.relative(facing, 2));
         super.onRemove(state, level, pos, newState, isMoving);
      }
   }

   private static void removeExtendedSpike(Level level, BlockPos p) {
      if (level.getBlockState(p).getBlock() == BlockRegistry.trap_spike.get()) {
         level.removeBlock(p, false);
      }
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(FACING);
   }

   @Override
   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new TileEntitySpearTrap(pos, state);
   }

   @Override
   @Nullable
   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(
         net.minecraft.world.level.Level level, BlockState state, net.minecraft.world.level.block.entity.BlockEntityType<T> type) {
      return type == com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry.SPEAR_TRAP.get()
            ? (lvl, pos, st, be) -> ((TileEntitySpearTrap) be).tick() : null;
   }
}
