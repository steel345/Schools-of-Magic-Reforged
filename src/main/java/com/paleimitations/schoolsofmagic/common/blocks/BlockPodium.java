package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.items.ItemBaseWand;
import com.paleimitations.schoolsofmagic.common.items.ItemBookBase;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class BlockPodium extends SOMBlock implements EntityBlock {

   public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
   public static final BooleanProperty IS_LEFT = BooleanProperty.create("is_left");
   public static final EnumProperty<EnumWoodType> TYPE = EnumProperty.create("type", EnumWoodType.class);

   public BlockPodium(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(IS_LEFT, true).setValue(TYPE, EnumWoodType.values()[0]));
   }

   @Override
   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return state.getValue(IS_LEFT) ? new TileEntityPodium(pos, state) : null;
   }

   @Override
   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      Direction face = ctx.getHorizontalDirection();

      BlockPos rightPos = ctx.getClickedPos().relative(face.getClockWise());
      if (!ctx.getLevel().getBlockState(rightPos).canBeReplaced(ctx)) {
         return null;
      }
      return this.defaultBlockState().setValue(FACING, face).setValue(IS_LEFT, true);
   }

   @Override
   public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {

      Direction face = state.getValue(FACING);

      EnumWoodType wood = EnumWoodType.values()[0];
      if (stack.hasTag() && stack.getTag().contains("BlockStateTag")) {
         String t = stack.getTag().getCompound("BlockStateTag").getString("type");
         for (EnumWoodType w : EnumWoodType.values()) {
            if (w.getSerializedName().equals(t)) { wood = w; break; }
         }
      }
      level.setBlock(pos, state.setValue(FACING, face).setValue(IS_LEFT, true).setValue(TYPE, wood), 2);
      BlockEntity be = level.getBlockEntity(pos);
      if (be instanceof TileEntityPodium tp) tp.setWood(wood);
      level.setBlock(pos.relative(face.getClockWise()), this.defaultBlockState().setValue(FACING, face).setValue(IS_LEFT, false).setValue(TYPE, wood), 2);
   }

   @Override
   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      ItemStack held = player.getItemInHand(hand);
      if (held.getItem() instanceof ItemBaseWand) return InteractionResult.PASS;
      BlockPos leftPos = state.getValue(IS_LEFT) ? pos : pos.relative(state.getValue(FACING).getCounterClockWise());
      BlockEntity be = world.getBlockEntity(leftPos);
      if (!(be instanceof TileEntityPodium tb)) return InteractionResult.PASS;
      var handler = tb.handler;
      ItemStack slot0 = handler.getStackInSlot(0);

      if (player.isShiftKeyDown() && !slot0.isEmpty()) {
         if (!world.isClientSide) {
            ItemStack out = handler.extractItem(0, 1, false);
            if (!player.getInventory().add(out)) player.drop(out, false);
            tb.sendUpdates();
         }
         return InteractionResult.sidedSuccess(world.isClientSide);
      }

      if (slot0.isEmpty()) {
         boolean isBook = held.getItem() instanceof ItemBookBase
            || held.getItem() == ItemRegistry.magic_book.get()
            || held.getItem() == Items.WRITTEN_BOOK
            || (held.hasTag() && held.getTag() != null && held.getTag().contains("pages"));
         if (isBook) {
            if (!world.isClientSide) {
               handler.setStackInSlot(0, held.copyWithCount(1));
               if (!player.getAbilities().instabuild) held.shrink(1);

               tb.sendUpdates();
               world.playSound(null, leftPos, net.minecraft.sounds.SoundEvents.BOOK_PAGE_TURN,
                  net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
         }

      }

      if (!slot0.isEmpty() && (hit.getLocation().y - pos.getY()) > 0.5D) {
         Direction facing = state.getValue(FACING);
         double along = (facing.getAxis() == Direction.Axis.X)
            ? hit.getLocation().z - pos.getZ()
            : hit.getLocation().x - pos.getX();

         boolean leftHalf = state.getValue(IS_LEFT);
         boolean axisFlipped = facing == Direction.SOUTH || facing == Direction.WEST;
         double norm = axisFlipped ? 1.0D - along : along;
         final double CORNER = 0.22D;
         if (leftHalf && norm < CORNER) {
            if (world.isClientSide) tb.turnPage(false);
            return InteractionResult.sidedSuccess(world.isClientSide);
         }
         if (!leftHalf && norm > (1.0D - CORNER)) {
            if (world.isClientSide) tb.turnPage(true);
            return InteractionResult.sidedSuccess(world.isClientSide);
         }
      }

      if (!world.isClientSide && player instanceof ServerPlayer sp) {

         if (!slot0.isEmpty()) ItemBookBase.refreshIfPristine(slot0);
         NetworkHooks.openScreen(sp, tb, leftPos);
      }
      return InteractionResult.sidedSuccess(world.isClientSide);
   }

   private static final VoxelShape SHAPE = Shapes.or(
      Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D),
      Block.box(0.0D, 14.0D, 0.0D, 16.0D, 16.0D, 16.0D));

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return SHAPE;
   }

   @Override
   @Nullable
   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(
         Level level, BlockState state, net.minecraft.world.level.block.entity.BlockEntityType<T> type) {
      return type == com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry.PODIUM.get()
         ? (lvl, p, st, bz) -> ((TileEntityPodium) bz).tick()
         : null;
   }

   @Override
   public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
      if (!state.is(newState.getBlock())) {
         Direction facing = state.getValue(FACING);
         BlockPos leftPos = state.getValue(IS_LEFT) ? pos : pos.relative(facing.getCounterClockWise());
         BlockPos otherPos = state.getValue(IS_LEFT) ? pos.relative(facing.getClockWise()) : pos.relative(facing.getCounterClockWise());
         BlockEntity be = level.getBlockEntity(leftPos);
         if (be instanceof TileEntityPodium) {
            be.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
               for (int slot = 0; slot < handler.getSlots(); slot++) {
                  Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), handler.getStackInSlot(slot));
               }
            });
         }
         level.removeBlock(otherPos, false);
         super.onRemove(state, level, pos, newState, isMoving);
      }
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(FACING, TYPE, IS_LEFT);
   }

   @Override
   public net.minecraft.world.item.ItemStack getCloneItemStack(
         net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos,
         net.minecraft.world.level.block.state.BlockState state) {
      return VariantDrop.variantStack(this, state, TYPE);
   }

   @Override
   public java.util.List<net.minecraft.world.item.ItemStack> getDrops(
         net.minecraft.world.level.block.state.BlockState state,
         net.minecraft.world.level.storage.loot.LootParams.Builder builder) {
      return java.util.Collections.singletonList(VariantDrop.variantStack(this, state, TYPE));
   }

}
