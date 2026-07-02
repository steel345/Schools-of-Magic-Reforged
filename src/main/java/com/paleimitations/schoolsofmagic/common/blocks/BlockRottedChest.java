package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityRottedChest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockRottedChest extends SOMBlockContainer {

   public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
   protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

   public BlockRottedChest(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
   }

   @Override
   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.ENTITYBLOCK_ANIMATED;
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return SHAPE;
   }

   @Override
   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
   }

   @Override
   public net.minecraft.world.InteractionResult use(BlockState state, Level level, BlockPos pos,
         net.minecraft.world.entity.player.Player player, net.minecraft.world.InteractionHand hand,
         net.minecraft.world.phys.BlockHitResult hit) {
      return net.minecraft.world.InteractionResult.PASS;
   }

   @Override
   public void playerWillDestroy(Level level, BlockPos pos, BlockState state, net.minecraft.world.entity.player.Player player) {
      if (!level.isClientSide && level instanceof net.minecraft.server.level.ServerLevel sl
            && level.getBlockEntity(pos) instanceof TileEntityRottedChest chest) {

         boolean silk = net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(
               net.minecraft.world.item.enchantment.Enchantments.SILK_TOUCH, player.getMainHandItem()) > 0;
         if (!silk) {

            try {
               chest.unpackLootTable(player);
               java.util.List<ItemStack> drops = new java.util.ArrayList<>();
               for (int i = 0; i < chest.handler.getSlots(); i++) {
                  ItemStack s = chest.handler.getStackInSlot(i);
                  if (!s.isEmpty()) drops.add(s.copy());
               }
               if (drops.isEmpty()) {
                  net.minecraft.world.level.storage.loot.LootTable loot = sl.getServer().getLootData().getLootTable(
                     new net.minecraft.resources.ResourceLocation("som", "chests/rotted_chest"));
                  net.minecraft.world.level.storage.loot.LootParams params =
                     new net.minecraft.world.level.storage.loot.LootParams.Builder(sl)
                        .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ORIGIN, net.minecraft.world.phys.Vec3.atCenterOf(pos))
                        .withOptionalParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.THIS_ENTITY, player)
                        .create(net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.CHEST);
                  drops = loot.getRandomItems(params);
               }
               for (ItemStack s : drops) {
                  if (!s.isEmpty()) net.minecraft.world.Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, s);
               }
               for (int i = 0; i < chest.handler.getSlots(); i++) chest.handler.setStackInSlot(i, ItemStack.EMPTY);
            } catch (Throwable ignored) {}
         }
      }
      super.playerWillDestroy(level, pos, state, player);
   }

   @Override
   public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
      if (placer != null) {
         level.setBlock(pos, state.setValue(FACING, placer.getDirection().getOpposite()), 3);
      }
      if (stack.hasCustomHoverName()) {
         BlockEntity be = level.getBlockEntity(pos);
         if (be instanceof TileEntityRottedChest chest) {

            chest.setCustomName(stack.getHoverName().getString());
         }
      }
   }

   @Override
   public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
      if (!state.is(newState.getBlock())) {
         BlockEntity be = level.getBlockEntity(pos);
         if (be instanceof TileEntityRottedChest chest) {
            for (int i = 0; i < chest.getContainerSize(); i++) {
               Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, chest.handler.getStackInSlot(i));
            }
         }
         super.onRemove(state, level, pos, newState, isMoving);
      }
   }

   @Override
   public BlockState rotate(BlockState state, Rotation rot) {
      return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
   }

   @Override
   public BlockState mirror(BlockState state, Mirror mir) {
      return state.rotate(mir.getRotation(state.getValue(FACING)));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(FACING);
   }

   @Override
   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new TileEntityRottedChest(pos, state);
   }
}
