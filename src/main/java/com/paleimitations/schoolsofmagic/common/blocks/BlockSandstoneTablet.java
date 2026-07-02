package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySacrificialAltar;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySandstoneTablet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockSandstoneTablet extends SOMBlock implements EntityBlock {

   public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

   protected static final VoxelShape WEST_SHAPE  = Block.box(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 16.0D);
   protected static final VoxelShape EAST_SHAPE  = Block.box(14.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
   protected static final VoxelShape NORTH_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 2.0D);
   protected static final VoxelShape SOUTH_SHAPE = Block.box(0.0D, 0.0D, 14.0D, 16.0D, 16.0D, 16.0D);

   public BlockSandstoneTablet(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
   }

   @Override
   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new TileEntitySandstoneTablet(pos, state);
   }

   @Override
   @Nullable
   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(
         Level level, BlockState state, net.minecraft.world.level.block.entity.BlockEntityType<T> type) {
      return type == com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry.SANDSTONE_TABLET.get()
            ? (lvl, pos, st, be) -> ((TileEntitySandstoneTablet) be).tick() : null;
   }

   @Override
   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      BlockEntity be = world.getBlockEntity(pos);
      if (!(be instanceof TileEntitySandstoneTablet tablet)) return InteractionResult.PASS;
      BlockEntity altarBE = world.getBlockEntity(tablet.getAltarPos());
      if (altarBE instanceof TileEntitySacrificialAltar altar) {
         if (!world.isClientSide) {
            String name;
            try {
               Object e = altar.getEntity();
               if (e instanceof net.minecraft.world.entity.EntityType<?> et) {
                  name = et.getDescription().getString();
               } else {
                  name = String.valueOf(e);
               }
            } catch (Throwable t) {
               name = "creature";
            }
            player.sendSystemMessage(Component.literal("To pass this threshold, a " + name + " must be sacrificed upon the altar."));
         }
         return InteractionResult.SUCCESS;
      }
      return InteractionResult.PASS;
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {

      Direction f = state.getValue(FACING);
      if (f == Direction.SOUTH) return NORTH_SHAPE;
      if (f == Direction.WEST)  return EAST_SHAPE;
      if (f == Direction.NORTH) return SOUTH_SHAPE;
      if (f == Direction.EAST)  return WEST_SHAPE;
      return SOUTH_SHAPE;
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

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(FACING);
   }
}
