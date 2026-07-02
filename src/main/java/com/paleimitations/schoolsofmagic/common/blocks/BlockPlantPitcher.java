package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.particles.SOMParticleType;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public class BlockPlantPitcher extends SOMPlant {

   public static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 2);
   public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

   public BlockPlantPitcher(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TYPE, 0));
   }

   @Override
   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
      if (rand.nextInt(30) == 0) {
         double alfa = rand.nextDouble() * 2.0 * Math.PI;
         double beta = rand.nextDouble() * 2.0 * Math.PI;
         double gamma = rand.nextDouble() * 2.0 * Math.PI;
         double distance = 1.5 * Math.pow(rand.nextDouble(), 2.4);
         double x = pos.getX() + 0.5 + distance * Math.cos(alfa);
         double z = pos.getZ() + 0.5 + distance * Math.cos(beta);
         double y = pos.getY() + 0.5 + distance * Math.cos(gamma);
         SchoolsOfMagic.proxy.spawnParticle(SOMParticleType.BUG, x, y, z, 0.5 - rand.nextDouble(), 0.7 - rand.nextDouble(), 0.5 - rand.nextDouble());
      }
   }

   @Override
   public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
      if (entity instanceof LivingEntity le && le.getMobType() == MobType.ARTHROPOD) {
         entity.hurt(level.damageSources().sweetBerryBush(), 1.0F);
      }
   }

   @Override
   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      RandomSource rand = ctx.getLevel().random;
      return this.defaultBlockState().setValue(FACING, randomHorizontal(rand)).setValue(TYPE, rand.nextInt(3));
   }

   public static Direction randomHorizontal(RandomSource rand) {
      return Direction.from2DDataValue(rand.nextInt(4));
   }

   @Override
   public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      return InteractionResult.PASS;
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(FACING, TYPE);
   }

}
