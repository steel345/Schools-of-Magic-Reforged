package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityWitherVase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockVaseLarge extends BlockVaseSmall {

   public static final EnumProperty<EnumBlockHalf> HALF = EnumProperty.create("half", EnumBlockHalf.class);
   protected static final VoxelShape FULL_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

   public BlockVaseLarge(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(HALF, EnumBlockHalf.LOWER));
   }

   @Override
   public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
      if (state.is(newState.getBlock())) return;
      if (state.getValue(HALF) == EnumBlockHalf.LOWER && !level.isClientSide && level instanceof ServerLevel server) {
         BlockEntity be = level.getBlockEntity(pos);
         if (be instanceof TileEntityWitherVase vase && !vase.isEmpty()) {
            WitherSkeleton skel = EntityType.WITHER_SKELETON.create(server);
            if (skel != null) {
               skel.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
               level.addFreshEntity(skel);
            }
         }
      }

      level.removeBlockEntity(pos);
   }

   @Override
   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {

      return state.getValue(HALF) == EnumBlockHalf.LOWER ? new TileEntityWitherVase(pos, state) : null;
   }

   @Override
   @Nullable
   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(
         Level level, BlockState state, net.minecraft.world.level.block.entity.BlockEntityType<T> type) {
      return type == com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry.WITHER_VASE.get()
            ? (lvl, pos, st, be) -> ((TileEntityWitherVase) be).tick() : null;
   }

   @Override
   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
      if (rand.nextInt(40) == 0 && state.getValue(HALF) == EnumBlockHalf.LOWER) {
         level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
            SOMSoundHandler.VASE_AMBIENT.get(), SoundSource.BLOCKS,
            rand.nextFloat() * 0.5F, rand.nextFloat() * 0.4F + 2.8F, false);
      }
   }

   @Override public boolean isRandomlyTicking(BlockState state) { return true; }

   @Override
   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
      this.checkAndDropBlock(level, pos, state);
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return FULL_SHAPE;
   }

   protected void checkAndDropBlock(Level level, BlockPos pos, BlockState state) {
      if (!this.canBlockStay(level, pos, state)) {
         if (state.getValue(HALF) == EnumBlockHalf.LOWER) {
            level.destroyBlock(pos, true);
         }
         BlockState above = level.getBlockState(pos.above());
         if (above.getBlock() == this && (above.getValue(HALF) == EnumBlockHalf.UPPER || above.getValue(HALF) == EnumBlockHalf.MIDDLE)) {
            level.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), 2);
         }
         BlockState below = level.getBlockState(pos.below());
         if (below.getBlock() == this && (below.getValue(HALF) == EnumBlockHalf.LOWER || below.getValue(HALF) == EnumBlockHalf.MIDDLE)) {
            if (below.getValue(HALF) == EnumBlockHalf.LOWER) {
               level.destroyBlock(pos, true);
            }
            level.setBlock(pos.below(), Blocks.AIR.defaultBlockState(), 2);
         }
      }
   }

   public boolean canBlockStay(Level level, BlockPos pos, BlockState state) {

      return true;
   }

   @Override
   public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
      if (placer != null) {
         level.setBlock(pos.above(),
            this.defaultBlockState().setValue(HALF, EnumBlockHalf.MIDDLE).setValue(FACING, placer.getDirection()), 2);
         level.setBlock(pos.above(2),
            this.defaultBlockState().setValue(HALF, EnumBlockHalf.UPPER).setValue(FACING, placer.getDirection()), 2);
      }
   }

   @Override
   public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
      EnumBlockHalf h = state.getValue(HALF);
      if (h == EnumBlockHalf.UPPER && level.getBlockState(pos.below(2)).getBlock() == this) {
         if (player.getAbilities().instabuild || level.isClientSide) {
            level.removeBlock(pos.below(2), false);
         } else {
            level.destroyBlock(pos.below(2), true);
         }
      }
      if (h == EnumBlockHalf.LOWER && level.getBlockState(pos.above(2)).getBlock() == this) {
         level.removeBlock(pos.above(2), false);
      }
      if (level.getBlockState(pos.above()).getBlock() == this) {
         level.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), 2);
      }
      if (level.getBlockState(pos.below()).getBlock() == this) {
         if (level.isClientSide) {
            level.removeBlock(pos.below(), false);
         } else {
            level.destroyBlock(pos.below(), true);
         }
      }
      super.playerWillDestroy(level, pos, state, player);
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(FACING, HALF);
   }

   public static enum EnumBlockHalf implements StringRepresentable {
      UPPER, MIDDLE, LOWER;
      @Override public String toString() { return this.getSerializedName(); }
      @Override public String getSerializedName() {
         if (this == UPPER) return "upper";
         if (this == LOWER) return "lower";
         return "middle";
      }
   }
}
