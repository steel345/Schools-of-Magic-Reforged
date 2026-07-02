package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

public class BlockConnectedBush extends SOMBlock implements IPlantable, BonemealableBlock {

   public static final BooleanProperty UP = BooleanProperty.create("up");
   public static final BooleanProperty DOWN = BooleanProperty.create("down");
   public static final BooleanProperty NORTH = BooleanProperty.create("north");
   public static final BooleanProperty SOUTH = BooleanProperty.create("south");
   public static final BooleanProperty EAST = BooleanProperty.create("east");
   public static final BooleanProperty WEST = BooleanProperty.create("west");
   public static final IntegerProperty LEAVES = IntegerProperty.create("leaves", 0, 2);
   public static final BooleanProperty FLOWERING = BooleanProperty.create("flowering");

   public BlockConnectedBush(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any()
         .setValue(UP, false).setValue(DOWN, false)
         .setValue(NORTH, false).setValue(SOUTH, false).setValue(EAST, false).setValue(WEST, false)
         .setValue(LEAVES, 2).setValue(FLOWERING, false));
   }

   private static final net.minecraft.world.phys.shapes.VoxelShape PLANT_SHAPE =
      net.minecraft.world.level.block.Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);

   @Override
   public net.minecraft.world.phys.shapes.VoxelShape getShape(net.minecraft.world.level.block.state.BlockState state, net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext ctx) {
      return PLANT_SHAPE;
   }

   @Override
   public float getDestroyProgress(net.minecraft.world.level.block.state.BlockState state,
         net.minecraft.world.entity.player.Player player,
         net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos) {
      return BushBreak.progress(player);
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(UP, DOWN, NORTH, SOUTH, EAST, WEST, LEAVES, FLOWERING);
   }

   @Override
   public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state, boolean isClient) {
      if (state.getValue(FLOWERING)) return false;

      if (state.getValue(LEAVES) < 2) return true;

      BlockState cs = this.computeConnections(state, world, pos);
      return !cs.getValue(UP) || !cs.getValue(EAST) || !cs.getValue(WEST) || !cs.getValue(NORTH) || !cs.getValue(SOUTH);
   }

   @Override
   public boolean isBonemealSuccess(Level world, RandomSource rand, BlockPos pos, BlockState state) {
      return true;
   }

   @Override
   public void performBonemeal(ServerLevel world, RandomSource rand, BlockPos pos, BlockState state) {
      if (state.getValue(LEAVES) < 2) {
         world.setBlockAndUpdate(pos, this.computeConnections(state, world, pos).setValue(LEAVES, state.getValue(LEAVES) + 1));
      } else {
         world.setBlockAndUpdate(pos, state.setValue(FLOWERING, true));
      }
   }

   @Override
   public boolean isRandomlyTicking(BlockState state) { return true; }

   @Override
   public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rand) {
      super.randomTick(state, world, pos, rand);

      if (!state.getValue(FLOWERING) && state.getValue(LEAVES) < 2) {
         if (rand.nextInt(8) == 0) {
            world.setBlockAndUpdate(pos, this.computeConnections(state, world, pos).setValue(LEAVES, state.getValue(LEAVES) + 1));
         }
      } else if (rand.nextInt(20) == 0 && !state.getValue(FLOWERING) && state.getValue(LEAVES) >= 2) {
         BlockState cs = this.computeConnections(state, world, pos);
         if (!(cs.getValue(UP) && cs.getValue(EAST) && cs.getValue(WEST) && cs.getValue(NORTH) && cs.getValue(SOUTH))) {
            world.setBlockAndUpdate(pos, cs.setValue(FLOWERING, true));
         }
      } else if (state.getValue(FLOWERING) && rand.nextInt(10) == 0) {
         world.setBlockAndUpdate(pos, state.setValue(FLOWERING, false));
      }
   }

   @Override
   public BlockState getStateForPlacement(net.minecraft.world.item.context.BlockPlaceContext ctx) {
      BlockState base = this.defaultBlockState().setValue(LEAVES, 0).setValue(FLOWERING, false);
      return this.computeConnections(base, ctx.getLevel(), ctx.getClickedPos());
   }

   @Override
   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (state.getValue(FLOWERING)) {

         if (this == BlockRegistry.bush.get()) {
            world.setBlockAndUpdate(pos, state.setValue(FLOWERING, false));
            if (!player.getInventory().add(new ItemStack(ItemRegistry.brambleberry.get())) && !world.isClientSide) {
               Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ItemRegistry.brambleberry.get()));
            }
            return InteractionResult.SUCCESS;
         }
         if (this == BlockRegistry.hydrangea.get()) {
            world.setBlockAndUpdate(pos, state.setValue(FLOWERING, false));

            ItemStack drop = new ItemStack(ItemRegistry.item.get(), 1);
            drop.setDamageValue(EnumMisc.HYDRANGEA_FLOWERS.getIndex());
            if (!player.getInventory().add(drop) && !world.isClientSide) {
               Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), drop);
            }
            return InteractionResult.SUCCESS;
         }
      }
      if (player.getItemInHand(hand).getItem() == Items.SHEARS) {

         int leaves = state.getValue(LEAVES);
         if (leaves <= 0) {
            return InteractionResult.PASS;
         }
         if (!player.isCreative()) player.getItemInHand(hand).hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
         world.setBlockAndUpdate(pos, state.setValue(LEAVES, leaves - 1));
         return InteractionResult.SUCCESS;
      }
      return InteractionResult.PASS;
   }

   @Override
   public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
      super.entityInside(state, world, pos, entity);
      BushSound.rustle(world, entity);
      entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.01D, 0.01D, 0.01D));
      if (this == BlockRegistry.bush.get() && entity instanceof LivingEntity le && le.getRandom().nextInt(100) == 0) {
         entity.hurt(world.damageSources().cactus(), 0.5F);
      }
   }

   protected boolean canSustainBush(BlockState state) {
      return state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT) || state.is(Blocks.SAND) || state.is(this);
   }

   public BlockState computeConnections(BlockState state, BlockGetter world, BlockPos pos) {
      return state
         .setValue(UP,    world.getBlockState(pos.above()).is(this))
         .setValue(DOWN,  this.canSustainBush(world.getBlockState(pos.below())))
         .setValue(EAST,  world.getBlockState(pos.east()).is(this))
         .setValue(WEST,  world.getBlockState(pos.west()).is(this))
         .setValue(NORTH, world.getBlockState(pos.north()).is(this))
         .setValue(SOUTH, world.getBlockState(pos.south()).is(this));
   }

   @Override
   public BlockState updateShape(BlockState state, Direction dir, BlockState neighbor, net.minecraft.world.level.LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
      return this.computeConnections(state, world, pos);
   }

   @Override
   public PlantType getPlantType(BlockGetter world, BlockPos pos) {
      return PlantType.PLAINS;
   }

   @Override
   public BlockState getPlant(BlockGetter world, BlockPos pos) {
      return this.defaultBlockState();
   }

}
