package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockTrapSpike extends SOMBlock {

   public static final DirectionProperty FACING = BlockStateProperties.FACING;
   public static final EnumProperty<EnumBlockHalf> HALF = EnumProperty.create("half", EnumBlockHalf.class);

   protected static final VoxelShape Y_AXIS_SHAPE = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);
   protected static final VoxelShape X_AXIS_SHAPE = Block.box(0.0D, 7.0D, 7.0D, 16.0D, 9.0D, 9.0D);
   protected static final VoxelShape Z_AXIS_SHAPE = Block.box(7.0D, 7.0D, 0.0D, 9.0D, 9.0D, 16.0D);

   public BlockTrapSpike(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(HALF, EnumBlockHalf.LOWER));
   }

   @Override public boolean isRandomlyTicking(BlockState state) { return true; }

   @Override
   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
      super.randomTick(state, level, pos, rand);
      this.checkAndDropBlock(level, pos, state);
   }

   @Override
   public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
      if (entity instanceof LivingEntity living) {
         living.hurt(level.damageSources().cactus(), 3.5F);
      }
   }

   private static boolean isTrapExtended(BlockGetter level, BlockPos pos, BlockState state) {
      Direction back = state.getValue(FACING).getOpposite();
      return level.getBlockState(pos.relative(back)).getBlock() instanceof BlockTrapSpikeBase
          || level.getBlockState(pos.relative(back, 2)).getBlock() instanceof BlockTrapSpikeBase;
   }

   @Override
   public java.util.List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder builder) {
      net.minecraft.world.phys.Vec3 origin = builder.getOptionalParameter(
         net.minecraft.world.level.storage.loot.parameters.LootContextParams.ORIGIN);
      if (origin != null && isTrapExtended(builder.getLevel(), BlockPos.containing(origin), state)) {
         return java.util.Collections.emptyList();
      }
      return super.getDrops(state, builder);
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      Direction f = state.getValue(FACING);
      if (f == Direction.UP || f == Direction.DOWN) return Y_AXIS_SHAPE;
      if (f == Direction.EAST || f == Direction.WEST) return X_AXIS_SHAPE;
      return Z_AXIS_SHAPE;
   }

   @Override
   public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return net.minecraft.world.phys.shapes.Shapes.empty();
   }

   protected void checkAndDropBlock(Level level, BlockPos pos, BlockState state) {
      if (!this.canBlockStay(level, pos, state)) {
         boolean isUpper = state.getValue(HALF) == EnumBlockHalf.UPPER;
         Direction facing = state.getValue(FACING);
         BlockPos other = isUpper ? pos.relative(facing.getOpposite()) : pos.relative(facing);
         if (!isUpper) level.destroyBlock(pos, true);
         if (level.getBlockState(other).getBlock() == this) {
            level.setBlock(other, Blocks.AIR.defaultBlockState(), 2);
         }
      }
   }

   public boolean canBlockStay(Level level, BlockPos pos, BlockState state) {

      return true;
   }

   @Override
   public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
      Direction facing = state.getValue(FACING);
      BlockPos ahead = pos.relative(facing);
      if (level.isEmptyBlock(ahead)) {
         level.setBlock(ahead, this.defaultBlockState().setValue(HALF, EnumBlockHalf.UPPER).setValue(FACING, facing), 2);
      }
   }

   @Override
   public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
      Direction facing = state.getValue(FACING);
      if (state.getValue(HALF) == EnumBlockHalf.UPPER) {
         BlockPos other = pos.relative(facing.getOpposite());
         if (level.getBlockState(other).getBlock() == this) {
            if (player.getAbilities().instabuild || level.isClientSide) {
               level.removeBlock(other, false);
            } else {
               level.destroyBlock(other, true);
            }
         }
      } else {
         BlockPos other = pos.relative(facing);
         if (level.getBlockState(other).getBlock() == this) {
            level.setBlock(other, Blocks.AIR.defaultBlockState(), 2);
         }
      }
      super.playerWillDestroy(level, pos, state, player);
   }

   @Override
   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      return this.defaultBlockState().setValue(FACING, ctx.getClickedFace()).setValue(HALF, EnumBlockHalf.LOWER);
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(FACING, HALF);
   }

   public static enum EnumBlockHalf implements StringRepresentable {
      UPPER, LOWER;
      @Override public String toString() { return this.getSerializedName(); }
      @Override public String getSerializedName() { return this == UPPER ? "upper" : "lower"; }
   }
}
