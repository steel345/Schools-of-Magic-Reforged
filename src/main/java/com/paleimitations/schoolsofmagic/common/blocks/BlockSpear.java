package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockSpear extends SOMBlock {

   public static final EnumProperty<DoubleBlockHalf> HALF =
      net.minecraft.world.level.block.state.properties.BlockStateProperties.DOUBLE_BLOCK_HALF;

   private static final VoxelShape SHAPE_LOWER = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);
   private static final VoxelShape SHAPE_UPPER = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 13.0D, 9.0D);

   public BlockSpear(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(HALF);
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return state.getValue(HALF) == DoubleBlockHalf.UPPER ? SHAPE_UPPER : SHAPE_LOWER;
   }

   @Override
   public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {

      return Shapes.empty();
   }

   @Override
   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      BlockPos pos = ctx.getClickedPos();
      Level level = ctx.getLevel();

      if (pos.getY() >= level.getMaxBuildHeight() - 1) return null;
      if (!level.getBlockState(pos.above()).canBeReplaced(ctx)) return null;
      return this.defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER);
   }

   @Override
   public void setPlacedBy(Level level, BlockPos pos, BlockState state,
                           LivingEntity placer, net.minecraft.world.item.ItemStack stack) {

      level.setBlock(pos.above(), this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER), 3);
   }

   @Override
   public BlockState updateShape(BlockState state, Direction dir, BlockState neighbour,
                                 LevelAccessor world, BlockPos pos, BlockPos neighbourPos) {
      DoubleBlockHalf half = state.getValue(HALF);

      boolean isPartnerDir = (half == DoubleBlockHalf.LOWER && dir == Direction.UP)
                          || (half == DoubleBlockHalf.UPPER && dir == Direction.DOWN);
      if (isPartnerDir) {
         boolean partnerStillSpear = neighbour.is(this) && neighbour.getValue(HALF) != half;
         if (!partnerStillSpear) return Blocks.AIR.defaultBlockState();
      }
      return state;
   }

   @Override
   public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
      if (!level.isClientSide() && player.isCreative()) {

         preventCreativeDropFromOtherHalf(level, pos, state, player);
      }
      super.playerWillDestroy(level, pos, state, player);
   }

   private void preventCreativeDropFromOtherHalf(Level level, BlockPos pos, BlockState state, Player player) {
      DoubleBlockHalf half = state.getValue(HALF);
      BlockPos partnerPos = (half == DoubleBlockHalf.UPPER) ? pos.below() : pos.above();
      BlockState partner = level.getBlockState(partnerPos);
      if (partner.is(this) && partner.getValue(HALF) != half) {
         level.setBlock(partnerPos, Blocks.AIR.defaultBlockState(), 35);
         level.levelEvent(player, 2001, partnerPos, Block.getId(partner));
      }
   }

   @Override
   public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
      if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {

         BlockState below = world.getBlockState(pos.below());
         return below.isFaceSturdy(world, pos.below(), Direction.UP);
      }

      BlockState below = world.getBlockState(pos.below());
      return below.is(this) && below.getValue(HALF) == DoubleBlockHalf.LOWER;
   }

   @Override
   public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
      if (!(entity instanceof LivingEntity)) return;

      entity.hurt(level.damageSources().cactus(), 4.0F);

      if (level instanceof net.minecraft.server.level.ServerLevel sl) {
         sl.sendParticles(net.minecraft.core.particles.ParticleTypes.CRIT,
            entity.getX(), entity.getY() + entity.getBbHeight() * 0.5, entity.getZ(),
            3, 0.1, 0.1, 0.1, 0.05);
      }
   }
}
