package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityMortNPest;
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
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class BlockMortnpest extends SOMBlock implements EntityBlock {

   public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

   protected static final VoxelShape MORT_SHAPE = Shapes.or(
      Block.box(6.0D,  0.0D,  6.0D, 10.0D, 1.0D, 10.0D),
      Block.box(5.0D,  1.0D,  5.0D, 11.0D, 2.0D, 11.0D),
      Block.box(5.0D,  2.0D,  5.0D, 11.0D, 5.0D,  6.0D),
      Block.box(5.0D,  2.0D, 10.0D, 11.0D, 5.0D, 11.0D),
      Block.box(5.0D,  2.0D,  6.0D,  6.0D, 5.0D, 10.0D),
      Block.box(10.0D, 2.0D,  6.0D, 11.0D, 5.0D, 10.0D)
   );

   public BlockMortnpest(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(FACING);
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return MORT_SHAPE;
   }

   @Override
   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
   }

   @Override
   public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
      if (placer != null) {
         level.setBlock(pos, state.setValue(FACING, placer.getDirection().getOpposite()), 2);
      }
   }

   public void decrHeld(Player playerIn) {
      ItemStack held = playerIn.getInventory().getSelected();
      held.setCount(held.getCount() - 1);
   }

   @Override
   public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
      if (!state.is(newState.getBlock())) {
         BlockEntity be = level.getBlockEntity(pos);
         if (be instanceof TileEntityMortNPest) {
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
      if (!world.isClientSide && player instanceof ServerPlayer sp) {
         BlockEntity be = world.getBlockEntity(pos);
         if (be instanceof MenuProvider provider) {
            NetworkHooks.openScreen(sp, provider, pos);
         }
      }
      return InteractionResult.SUCCESS;
   }

   @Override
   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {

      return new TileEntityMortNPest(pos, state);
   }

   @Override
   @Nullable
   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
      return type == TileEntityRegistry.MORT_N_PEST.get()
         ? (lvl, pos, st, be) -> ((TileEntityMortNPest) be).tick()
         : null;
   }
}
