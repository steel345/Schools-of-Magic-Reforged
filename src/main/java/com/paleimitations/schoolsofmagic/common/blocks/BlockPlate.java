package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPlate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockPlate extends BaseEntityBlock {

   protected static final VoxelShape[] STACKS = new VoxelShape[]{
      Block.box(1.0D, 0.0D, 1.0D, 15.0D, 1.75D, 15.0D),
      Block.box(1.0D, 0.0D, 1.0D, 15.0D, 3.0D, 15.0D),
      Block.box(1.0D, 0.0D, 1.0D, 15.0D, 4.25D, 15.0D),
      Block.box(1.0D, 0.0D, 1.0D, 15.0D, 5.5D, 15.0D),
      Block.box(1.0D, 0.0D, 1.0D, 15.0D, 6.75D, 15.0D)};

   public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
   public static final IntegerProperty COUNT = IntegerProperty.create("count", 1, 5);

   public BlockPlate(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(COUNT, 1));
   }

   @Override
   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.MODEL;
   }

   @Override
   public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
      if (!state.is(newState.getBlock())) {
         BlockEntity be = level.getBlockEntity(pos);
         if (be instanceof TileEntityPlate plate) {
            Containers.dropItemStack(level, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, plate.getItem());
            level.updateNeighbourForOutputSignal(pos, this);
         }
         super.onRemove(state, level, pos, newState, isMoving);
      }
   }

   @Override
   public java.util.List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder builder) {
      int count = state.getValue(COUNT);
      java.util.List<ItemStack> drops = new java.util.ArrayList<>();
      for (int i = 0; i < count; i++) {
         drops.add(new ItemStack(this));
      }
      return drops;
   }

   @Override
   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
      ItemStack stack = player.getItemInHand(hand);
      if (!(world.getBlockEntity(pos) instanceof TileEntityPlate plate)) {
         return InteractionResult.PASS;
      }

      if (stack.getItem() == this.asItem() && state.getValue(COUNT) < 5) {
         world.setBlockAndUpdate(pos, state.setValue(COUNT, state.getValue(COUNT) + 1));
         if (!player.isCreative()) stack.shrink(1);
         world.playSound(null, pos, net.minecraft.sounds.SoundEvents.STONE_PLACE, net.minecraft.sounds.SoundSource.BLOCKS, 0.7F, 1.4F);
         return InteractionResult.sidedSuccess(world.isClientSide);
      } else if (player.isShiftKeyDown() && !plate.getItem().isEmpty()) {
         if (!world.isClientSide) {
            ItemStack taken = plate.getItem();
            plate.setItem(ItemStack.EMPTY);
            if (!player.addItem(taken)) player.drop(taken, false);
         }
         return InteractionResult.sidedSuccess(world.isClientSide);
      } else if (!stack.isEmpty() && plate.getItem().isEmpty()) {
         if (!world.isClientSide) {
            plate.setItem(stack.split(1));
         } else if (!player.isCreative()) {
            stack.shrink(1);
         }
         return InteractionResult.sidedSuccess(world.isClientSide);
      } else if (stack.isEmpty() && !player.isShiftKeyDown() && !plate.getItem().isEmpty()) {
         return tryConsume(world, pos, player, plate);
      } else if (player.isShiftKeyDown() && state.getValue(COUNT) > 1) {
         world.setBlockAndUpdate(pos, state.setValue(COUNT, state.getValue(COUNT) - 1));
         if (!world.isClientSide) {
            ItemStack one = new ItemStack(this, 1);
            if (!player.addItem(one)) player.drop(one, false);
         }
         return InteractionResult.sidedSuccess(world.isClientSide);
      }
      return InteractionResult.PASS;
   }

   private InteractionResult tryConsume(Level world, BlockPos pos, Player player, TileEntityPlate plate) {
      ItemStack stack = plate.getItem();
      net.minecraft.world.item.Item item = stack.getItem();
      net.minecraft.world.food.FoodProperties food = item.getFoodProperties(stack, player);
      boolean drink = item.getUseAnimation(stack) == net.minecraft.world.item.UseAnim.DRINK;
      if (!drink && food == null) return InteractionResult.PASS;
      if (!drink && !player.canEat(food.canAlwaysEat())) return InteractionResult.PASS;

      if (!world.isClientSide) {
         ItemStack one = stack.copy();
         one.setCount(1);
         ItemStack remainder = item.finishUsingItem(one, world, player);
         plate.setItem(remainder.isEmpty() ? ItemStack.EMPTY : remainder);
         world.playSound(null, pos,
            drink ? net.minecraft.sounds.SoundEvents.GENERIC_DRINK : net.minecraft.sounds.SoundEvents.GENERIC_EAT,
            net.minecraft.sounds.SoundSource.BLOCKS, 0.7F, 1.0F);
      }
      return InteractionResult.sidedSuccess(world.isClientSide);
   }

   @Override
   public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return STACKS[state.getValue(COUNT) - 1];
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return STACKS[state.getValue(COUNT) - 1];
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
      builder.add(FACING, COUNT);
   }

   @Override
   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
   }

   @Override
   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return TileEntityRegistry.PLATE.get().create(pos, state);
   }
}
