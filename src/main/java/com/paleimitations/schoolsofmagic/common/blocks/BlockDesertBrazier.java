package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityRitualCenter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FireChargeItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockDesertBrazier extends SOMBlock implements EntityBlock {

   public static final IntegerProperty FLAME = IntegerProperty.create("flame", 0, 2);
   protected static final VoxelShape TEAPLATE_SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 5.0D, 14.0D);

   public BlockDesertBrazier(BlockBehaviour.Properties props) {
      super(props.lightLevel(s -> s.getValue(FLAME) > 0 ? 15 : 0));
      this.registerDefaultState(this.stateDefinition.any().setValue(FLAME, 0));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(FLAME);
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return TEAPLATE_SHAPE;
   }

   @Override
   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      ItemStack held = player.getItemInHand(hand);
      BlockEntity be = world.getBlockEntity(pos);
      if (!(be instanceof TileEntityRitualCenter center)) return InteractionResult.PASS;

      int flame = state.getValue(FLAME);
      if (flame == 0) {
         if (held.getItem() instanceof FlintAndSteelItem || held.getItem() instanceof FireChargeItem) {
            world.setBlockAndUpdate(pos, state.setValue(FLAME, 1));
            return InteractionResult.SUCCESS;
         }
      } else if (flame == 1) {
         if (held.getItem() == ItemRegistry.potion_crystal.get() && center.handler.getStackInSlot(0).isEmpty()) {
            center.handler.setStackInSlot(0, held.split(1));
            center.setActivated(true);
            center.sendUpdates();
            world.setBlockAndUpdate(pos, state.setValue(FLAME, 2));
            return InteractionResult.SUCCESS;
         }
         if (held.isEmpty()) {
            world.setBlockAndUpdate(pos, state.setValue(FLAME, 0));
            return InteractionResult.SUCCESS;
         }
      } else if (flame == 2 && center.handler.getStackInSlot(0).getItem() == ItemRegistry.potion_crystal.get()) {
         center.setActivated(false);
         player.getInventory().add(center.handler.getStackInSlot(0));
         center.handler.setStackInSlot(0, ItemStack.EMPTY);
         center.sendUpdates();
         world.setBlockAndUpdate(pos, state.setValue(FLAME, 1));
         return InteractionResult.SUCCESS;
      }
      return InteractionResult.PASS;
   }

   @Override
   public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
      super.setPlacedBy(level, pos, state, placer, stack);
      BlockEntity be = level.getBlockEntity(pos);
      if (be instanceof TileEntityRitualCenter crystal && placer != null) {
         crystal.setOwner(placer);
      }
   }

   @Override
   public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
      if (!state.is(newState.getBlock())) {
         BlockEntity be = level.getBlockEntity(pos);
         if (be instanceof TileEntityRitualCenter brazier) {
            for (int slot = 0; slot < brazier.handler.getSlots(); slot++) {
               net.minecraft.world.Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(),
                     brazier.handler.getStackInSlot(slot));
            }
         }
      }
      super.onRemove(state, level, pos, newState, isMoving);
   }

   @Override
   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new TileEntityRitualCenter(pos, state);
   }

   @Override
   @Nullable
   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(
         Level level, BlockState state, net.minecraft.world.level.block.entity.BlockEntityType<T> type) {
      return type == com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry.RITUAL_CENTER.get()
         ? (lvl, pos, st, be) -> ((TileEntityRitualCenter) be).tick()
         : null;
   }
}
