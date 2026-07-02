package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class BlockWallPyracantha extends BlockWalls {

   public BlockWallPyracantha(BlockBehaviour.Properties props) {
      super(props);
   }

   @Override
   public float getDestroyProgress(net.minecraft.world.level.block.state.BlockState state,
         net.minecraft.world.entity.player.Player player,
         net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos) {
      return BushBreak.progress(player);
   }

   @Override
   public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
      BushSound.rustle(level, entity);
      if (entity instanceof LivingEntity living && living.isAttackable()) {
         living.hurt(level.damageSources().cactus(), 1.0F);
         if (level.getRandom().nextInt(50) == 5) {
            living.setSecondsOnFire(2);
         }
      }
   }

   @Override public boolean isRandomlyTicking(BlockState state) { return true; }

   @Override
   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {

      level.destroyBlock(pos, true);
   }
}
