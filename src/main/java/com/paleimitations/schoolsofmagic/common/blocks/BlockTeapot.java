package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityTeapot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockTeapot extends SOMBlock implements EntityBlock {

   protected static final VoxelShape SHAPE_N = Shapes.or(
      Block.box(5.0D,  0.0D,  5.0D, 11.0D,  2.0D, 11.0D),
      Block.box(4.0D,  2.0D,  4.0D, 12.0D, 15.0D, 12.0D),
      Block.box(7.0D, 15.0D,  7.0D,  9.0D, 16.5D,  9.0D),
      Block.box(0.0D,  4.0D,  6.5D,  4.0D, 13.0D,  9.5D),
      Block.box(12.0D, 3.0D,  7.5D, 16.0D, 11.8D,  8.5D)
   );
   protected static final VoxelShape SHAPE_E = VoxelShapeUtils.rotateY(SHAPE_N, 1);
   protected static final VoxelShape SHAPE_S = VoxelShapeUtils.rotateY(SHAPE_N, 2);
   protected static final VoxelShape SHAPE_W = VoxelShapeUtils.rotateY(SHAPE_N, 3);

   public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
   public static final BooleanProperty BOILING = BooleanProperty.create("boiling");

   public BlockTeapot(BlockBehaviour.Properties props) {
      super(props.lightLevel(s -> s.getValue(BOILING) ? 3 : 0));
      this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(BOILING, false));
   }

   @Override
   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(BOILING, false);
   }

   @Override
   public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
      if (placer != null) {
         level.setBlock(pos, state.setValue(FACING, placer.getDirection().getOpposite()), 2);
      }
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      switch (state.getValue(FACING)) {
         case EAST: return SHAPE_E;
         case WEST: return SHAPE_W;
         case SOUTH: return SHAPE_S;
         case NORTH:
         default: return SHAPE_N;
      }
   }

   @Override
   public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {

      if (!com.paleimitations.schoolsofmagic.common.tileentity.TileEntityTeapot.isHeatSourceBelow(world, pos)) {
         return;
      }
      BlockEntity tbe = world.getBlockEntity(pos);
      if (!(tbe instanceof TileEntityTeapot teapot) || teapot.waterLevel <= 0) {
         return;
      }
      if (rand.nextInt(80) == 0) {
         world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
            SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 0.1F, rand.nextFloat() * 0.4F + 2.8F, false);
      }
      double x = pos.getX() + 0.5, y = pos.getY() + 0.85, z = pos.getZ() + 0.5;
      switch (state.getValue(FACING)) {
         case NORTH: x = pos.getX() + 0.0625; break;
         case SOUTH: x = pos.getX() + 0.9375; break;
         case EAST:  z = pos.getZ() + 0.0625; break;
         case WEST:  z = pos.getZ() + 0.9375; break;
         default: break;
      }

      if (rand.nextInt(2) == 0) {
         world.addAlwaysVisibleParticle(net.minecraft.core.particles.ParticleTypes.SMOKE,
            x + (rand.nextDouble() - 0.5) * 0.05, y, z + (rand.nextDouble() - 0.5) * 0.05,
            0.0D, 0.02D + rand.nextDouble() * 0.02D, 0.0D);
      }
   }

   @Override
   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      BlockEntity be = world.getBlockEntity(pos);
      if (!(be instanceof TileEntityTeapot teapot)) return InteractionResult.PASS;
      ItemStack held = player.getItemInHand(hand);
      RandomSource rand = world.getRandom();

      if (ItemStack.isSameItem(held, new ItemStack(Items.WATER_BUCKET)) && teapot.waterLevel < 3 && teapot.getTea().isEmpty()) {
         teapot.waterLevel = 3;
         if (!player.getAbilities().instabuild) {
            held.shrink(1);
            player.getInventory().add(new ItemStack(Items.BUCKET));
         }
         playPour(world, pos, rand);
         return InteractionResult.SUCCESS;
      }

      if (ItemStack.isSameItem(player.getInventory().getSelected(), TileEntityTeapot.WATER_BOTTLE) && teapot.waterLevel < 3 && teapot.getTea().isEmpty()) {
         teapot.waterLevel++;
         if (!player.getAbilities().instabuild) {
            held.shrink(1);
            player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
         }
         playPour(world, pos, rand);
         return InteractionResult.SUCCESS;
      }

      if (ItemStack.isSameItem(held, new ItemStack(Items.BUCKET)) && teapot.waterLevel == 3 && teapot.getTea().isEmpty()) {
         teapot.waterLevel = 0;
         teapot.setTea(null);
         if (!player.getAbilities().instabuild) {
            held.shrink(1);
            player.getInventory().add(new ItemStack(Items.WATER_BUCKET));
         }
         playPour(world, pos, rand);
         return InteractionResult.SUCCESS;
      }

      if (ItemStack.isSameItem(player.getInventory().getSelected(), new ItemStack(Items.GLASS_BOTTLE)) && teapot.waterLevel > 0 && teapot.getTea().isEmpty()) {
         teapot.waterLevel--;
         if (teapot.waterLevel == 0) {
            teapot.setTea(null);
         }
         if (!player.getAbilities().instabuild) {
            held.shrink(1);
            player.getInventory().add(TileEntityTeapot.WATER_BOTTLE.copy());
         }
         playPour(world, pos, rand);
         return InteractionResult.SUCCESS;
      }

      if (held.getItem() == ItemRegistry.teacup.get() && teapot.waterLevel < 3) {
         com.paleimitations.schoolsofmagic.common.recipes.RecipeTea back =
            TileEntityTeapot.recipeForEffect(com.paleimitations.schoolsofmagic.common.util.TeaUtils.getEffect(held));
         if (back != null && (teapot.tea == null || teapot.tea == back)) {
            teapot.setTea(back);
            teapot.waterLevel++;
            if (!player.getAbilities().instabuild) {
               held.shrink(1);
               player.getInventory().add(new ItemStack(ItemRegistry.teacup_empty.get()));
            }
            playPour(world, pos, rand);
            return InteractionResult.SUCCESS;
         }
      }

      if (held.getItem() == ItemRegistry.teacup_empty.get()) {
         ItemStack tea = teapot.extractTea();
         if (!tea.isEmpty()) {
            if (!player.getAbilities().instabuild) {
               held.shrink(1);
            }
            if (!player.getInventory().add(tea)) {
               player.drop(tea, false);
            }
            playPour(world, pos, rand);
            return InteractionResult.SUCCESS;
         }
      }

      if (held.getItem() == ItemRegistry.crushed_plant.get() && teapot.waterLevel > 0) {
         teapot.addItem(held);
         return InteractionResult.SUCCESS;
      }

      if (ItemStack.isSameItem(held, new ItemStack(ItemRegistry.teacup_empty.get())) && !teapot.getTea().isEmpty() && teapot.waterLevel > 0) {
         if (!player.getAbilities().instabuild) held.shrink(1);
         player.getInventory().add(teapot.getTea());
         teapot.waterLevel--;
         if (teapot.waterLevel == 0) {
            teapot.setTea(null);
         }
         world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
            SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 0.1F, rand.nextFloat() * 0.4F + 2.8F, false);
         return InteractionResult.SUCCESS;
      }

      if (!world.isClientSide && player instanceof net.minecraft.server.level.ServerPlayer sp) {
         net.minecraftforge.network.NetworkHooks.openScreen(sp,
            new net.minecraft.world.MenuProvider() {
               @Override public net.minecraft.network.chat.Component getDisplayName() {
                  return net.minecraft.network.chat.Component.translatable("block.som.teapot");
               }
               @Override public net.minecraft.world.inventory.AbstractContainerMenu createMenu(
                     int id, net.minecraft.world.entity.player.Inventory inv, Player p) {
                  return new com.paleimitations.schoolsofmagic.common.containers.ContainerTeapot(id, inv, teapot);
               }
            },
            buf -> buf.writeBlockPos(pos));
      }
      return InteractionResult.SUCCESS;
   }

   private static void playPour(Level world, BlockPos pos, RandomSource rand) {
      world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
         SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 0.1F, rand.nextFloat() * 0.4F + 2.8F, false);
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(BOILING, FACING);
   }

   @Override
   public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
      if (!state.is(newState.getBlock())) {
         BlockInventoryDrops.drop(level, pos);
      }
      super.onRemove(state, level, pos, newState, isMoving);
   }

   @Override
   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new TileEntityTeapot(pos, state);
   }

   @Override
   @Nullable
   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(
         Level level, BlockState state, net.minecraft.world.level.block.entity.BlockEntityType<T> type) {
      return type == com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry.TEAPOT.get()
         ? (lvl, pos, st, be) -> ((TileEntityTeapot) be).tick()
         : null;
   }
}
