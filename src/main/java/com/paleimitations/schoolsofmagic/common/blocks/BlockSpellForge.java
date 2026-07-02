package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.SpellNoteHelper;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityRitualCenter;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySpellForge;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class BlockSpellForge extends SOMBlock implements EntityBlock {

   protected static final VoxelShape LOWER_SHAPE = Block.box(1.0D, 1.0D, 1.0D, 15.0D, 15.0D, 15.0D);
   protected static final VoxelShape MIDDLE_SHAPE = net.minecraft.world.phys.shapes.Shapes.or(
      Block.box(4.5D, -1.0D, 4.5D, 11.5D, 9.0D, 11.5D),
      Block.box(0.5D, 9.0D, 0.5D, 15.5D, 16.0D, 15.5D));
   protected static final VoxelShape UPPER_SHAPE = Block.box(0.5D, 0.0D, 0.5D, 15.5D, 7.0D, 15.5D);

   public static final EnumProperty<EnumBlockHalf> HALF = EnumProperty.create("half", EnumBlockHalf.class);
   public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

   public BlockSpellForge(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(HALF, EnumBlockHalf.LOWER).setValue(FACING, Direction.NORTH));
   }

   @Override
   public RenderShape getRenderShape(BlockState state) {
      return state.getValue(HALF) == EnumBlockHalf.LOWER ? RenderShape.ENTITYBLOCK_ANIMATED : RenderShape.INVISIBLE;
   }

   @Override
   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      BlockPos pos = ctx.getClickedPos();
      Level level = ctx.getLevel();
      if (pos.getY() >= level.getMaxBuildHeight() - 2) return null;
      if (!level.getBlockState(pos.above()).canBeReplaced(ctx) || !level.getBlockState(pos.above(2)).canBeReplaced(ctx)) return null;
      return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(HALF, EnumBlockHalf.LOWER);
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      switch (state.getValue(HALF)) {
         case LOWER: return LOWER_SHAPE;
         case MIDDLE: return MIDDLE_SHAPE;
         case UPPER: return UPPER_SHAPE;
         default: return net.minecraft.world.phys.shapes.Shapes.empty();
      }
   }

   private static BlockPos baseOf(BlockState state, BlockPos pos) {
      switch (state.getValue(HALF)) {
         case UPPER: return pos.below(2);
         case MIDDLE: return pos.below();
         default: return pos;
      }
   }

   @Override public boolean isRandomlyTicking(BlockState state) { return true; }

   @Override
   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
      this.checkAndDropBlock(level, pos, state);
   }

   @Override
   public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
      Direction facing = placer != null ? placer.getDirection().getOpposite() : state.getValue(FACING);
      level.setBlock(pos.above(), this.defaultBlockState().setValue(HALF, EnumBlockHalf.MIDDLE).setValue(FACING, facing), 2);
      level.setBlock(pos.above(2), this.defaultBlockState().setValue(HALF, EnumBlockHalf.UPPER).setValue(FACING, facing), 2);
   }

   public boolean canBlockStay(Level level, BlockPos pos, BlockState state) {
      BlockPos base = baseOf(state, pos);
      return level.getBlockState(base).getBlock() == this
         && level.getBlockState(base.above()).getBlock() == this
         && level.getBlockState(base.above(2)).getBlock() == this;
   }

   protected void checkAndDropBlock(Level level, BlockPos pos, BlockState state) {
      if (!this.canBlockStay(level, pos, state)) {
         BlockPos base = baseOf(state, pos);
         level.destroyBlock(base, true);
         removeIfForge(level, base.above());
         removeIfForge(level, base.above(2));
      }
   }

   private void removeIfForge(Level level, BlockPos pos) {
      if (level.getBlockState(pos).getBlock() == this) {
         level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
      }
   }

   @Override
   public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
      BlockPos base = baseOf(state, pos);
      for (BlockPos other : new BlockPos[]{base, base.above(), base.above(2)}) {
         if (other.equals(pos)) continue;
         if (level.getBlockState(other).getBlock() == this) {
            level.setBlock(other, Blocks.AIR.defaultBlockState(), 2);
         }
      }
      super.playerWillDestroy(level, pos, state, player);
   }

   @Override
   public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
      if (!state.is(newState.getBlock())) {
         BlockEntity be = level.getBlockEntity(pos);
         if (be instanceof TileEntitySpellForge) {
            be.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
               for (int slot = 0; slot < handler.getSlots(); slot++) {
                  Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), handler.getStackInSlot(slot));
               }
            });
         }
         super.onRemove(state, level, pos, newState, isMoving);
      }
   }

   @Override
   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (state.getValue(HALF) != EnumBlockHalf.LOWER) {
         BlockPos base = baseOf(state, pos);
         BlockState baseState = world.getBlockState(base);
         if (baseState.getBlock() == this) return this.use(baseState, world, base, player, hand, hit);
         return InteractionResult.PASS;
      }
      BlockEntity forge = world.getBlockEntity(pos);
      if (!(forge instanceof TileEntitySpellForge sf)) return InteractionResult.PASS;

      boolean onBrazier = world.getBlockEntity(pos.below()) instanceof TileEntityRitualCenter;
      if (onBrazier && player.getMainHandItem().getItem() == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.spell_parchment.get()) {
         if (sf.active) return InteractionResult.SUCCESS;
         if (!world.isClientSide && sf.startDissolve(true)) {
            if (!player.getAbilities().instabuild) player.getMainHandItem().shrink(1);
         }
         return InteractionResult.SUCCESS;
      }
      if (onBrazier && player.getMainHandItem().getItem() == Items.PAPER) {
         if (sf.active) return InteractionResult.SUCCESS;
         if (!world.isClientSide && sf.startDissolve()) {
            if (!player.getAbilities().instabuild) player.getMainHandItem().shrink(1);
         }
         return InteractionResult.SUCCESS;
      }

      if (!world.isClientSide && player instanceof ServerPlayer sp) {

         NetworkHooks.openScreen(sp, sf, pos);
      }
      return InteractionResult.SUCCESS;
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(FACING, HALF);
   }

   @Override
   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {

      return state.getValue(HALF) == EnumBlockHalf.LOWER
         ? new TileEntitySpellForge(pos, state)
         : new com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySpellForgeProxy(pos, state);
   }

   @Override
   @Nullable
   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(
         Level level, BlockState state, net.minecraft.world.level.block.entity.BlockEntityType<T> type) {
      if (level.isClientSide || state.getValue(HALF) != EnumBlockHalf.LOWER) return null;
      return (lvl, pos, st, be) -> { if (be instanceof TileEntitySpellForge sf) sf.tick(); };
   }

   public static enum EnumBlockHalf implements StringRepresentable {
      UPPER, MIDDLE, LOWER;
      @Override public String toString() { return this.getSerializedName(); }
      @Override public String getSerializedName() { return this == UPPER ? "upper" : (this == MIDDLE ? "middle" : "lower"); }
   }
}
