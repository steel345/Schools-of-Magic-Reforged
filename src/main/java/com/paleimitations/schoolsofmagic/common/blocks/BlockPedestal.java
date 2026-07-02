package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockPedestal extends SOMBlock implements net.minecraft.world.level.block.EntityBlock {

   public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

   private static final VoxelShape SHAPE = Shapes.or(
      Block.box(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D),
      Block.box(1.0D, 3.0D, 1.0D, 15.0D, 6.0D, 15.0D),
      Block.box(0.0D, 6.0D, 0.0D, 16.0D, 16.0D, 16.0D));

   public BlockPedestal(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return SHAPE;
   }

   @Override
   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
   }

   @Override
   public BlockState rotate(BlockState state, Rotation rot) {
      return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
   }

   @Override
   public BlockState mirror(BlockState state, Mirror mirror) {
      return state.rotate(mirror.getRotation(state.getValue(FACING)));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(FACING);
   }

   @Override
   public net.minecraft.world.level.block.entity.BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPedestal(pos, state);
   }

   @javax.annotation.Nullable
   @Override
   public <T extends net.minecraft.world.level.block.entity.BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(
         net.minecraft.world.level.Level level, BlockState state, net.minecraft.world.level.block.entity.BlockEntityType<T> type) {
      return type == com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry.PEDESTAL.get()
         ? (lvl, p, st, bz) -> ((com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPedestal) bz).tick()
         : null;
   }

   @Override
   public net.minecraft.world.InteractionResult use(BlockState state, net.minecraft.world.level.Level level, BlockPos pos,
         net.minecraft.world.entity.player.Player player, net.minecraft.world.InteractionHand hand,
         net.minecraft.world.phys.BlockHitResult hit) {
      if (!(level.getBlockEntity(pos) instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPedestal pedestal)) {
         return net.minecraft.world.InteractionResult.PASS;
      }
      net.minecraft.world.item.ItemStack held = player.getItemInHand(hand);
      boolean sneaking = player.isShiftKeyDown();

      if (pedestal.getItem().isEmpty()) {
         if (sneaking || held.isEmpty()) return net.minecraft.world.InteractionResult.PASS;
         if (!level.isClientSide) {
            pedestal.setItem(held.copyWithCount(1));
            if (!player.getAbilities().instabuild) held.shrink(1);
            level.playSound(null, pos, net.minecraft.sounds.SoundEvents.ITEM_FRAME_ADD_ITEM,
               net.minecraft.sounds.SoundSource.BLOCKS, 0.8F, 1.0F);
         }
         return net.minecraft.world.InteractionResult.sidedSuccess(level.isClientSide);
      } else {
         com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook book =
            pedestal.getItem().getCapability(
               com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook.BOOK_CAPABILITY).orElse(null);
         if (!sneaking && book != null) {
            net.minecraft.world.phys.Vec3 look = player.getLookAngle();
            double hx = hit.getLocation().x - (pos.getX() + 0.5D);
            double hz = hit.getLocation().z - (pos.getZ() + 0.5D);
            boolean right = (hx * -look.z + hz * look.x) > 0.0D;
            if (!level.isClientSide) {
               int pages = book.getBookPages().size();
               int cur = book.getPage();
               int next = right ? Math.min(pages - 1, cur + 1) : Math.max(0, cur - 1);
               if (next != cur) {
                  book.setPage(next);
                  book.setSubPage(0);
                  pedestal.setItem(pedestal.getItem());
               }
            }
            return net.minecraft.world.InteractionResult.sidedSuccess(level.isClientSide);
         }
         com.paleimitations.schoolsofmagic.common.spells.Spell bound = pedestal.getBoundSpell();
         com.paleimitations.schoolsofmagic.common.entity.capabilities.spell_button.ISpellButton btn =
            player.getCapability(com.paleimitations.schoolsofmagic.common.entity.capabilities.spell_button.CapabilitySpellButton.CAP).orElse(null);
         boolean bindCtx = (btn != null && btn.isPressed())
            || com.paleimitations.schoolsofmagic.common.handlers.RingCastHandler.isRingActive(player);
         if (bound != null && bindCtx) {
            if (!level.isClientSide) {
               com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData md =
                  player.getCapability(com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData.CAP).orElse(null);
               if (md != null) md.setCurrentSpell(bound);
               level.playSound(null, pos, net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_CHIME,
                  net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return net.minecraft.world.InteractionResult.sidedSuccess(level.isClientSide);
         }
         if (!sneaking) {
            return net.minecraft.world.InteractionResult.PASS;
         }
         if (!level.isClientSide) {
            net.minecraft.world.item.ItemStack taken = pedestal.getItem().copy();
            pedestal.setItem(net.minecraft.world.item.ItemStack.EMPTY);
            if (!player.addItem(taken)) player.drop(taken, false);
            level.playSound(null, pos, net.minecraft.sounds.SoundEvents.ITEM_FRAME_REMOVE_ITEM,
               net.minecraft.sounds.SoundSource.BLOCKS, 0.8F, 1.0F);
         }
         return net.minecraft.world.InteractionResult.sidedSuccess(level.isClientSide);
      }
   }

   @Override
   public void onRemove(BlockState state, net.minecraft.world.level.Level level, BlockPos pos, BlockState newState, boolean isMoving) {
      if (!state.is(newState.getBlock())) {
         if (!level.isClientSide
               && level.getBlockEntity(pos) instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPedestal pedestal
               && !pedestal.getItem().isEmpty()) {
            net.minecraft.world.Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), pedestal.getItem());
            pedestal.setItem(net.minecraft.world.item.ItemStack.EMPTY);
         }
         super.onRemove(state, level, pos, newState, isMoving);
      }
   }
}
