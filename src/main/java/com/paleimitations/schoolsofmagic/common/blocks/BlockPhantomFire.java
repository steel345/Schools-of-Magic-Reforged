package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;

public class BlockPhantomFire extends FireBlock {
   private static final IntegerProperty AGE = BlockStateProperties.AGE_15;

   public BlockPhantomFire(BlockBehaviour.Properties props) {
      super(props);
   }

   @Override
   public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
         net.minecraft.world.level.LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
      return this.canSurvive(state, level, pos) ? state : net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
   }

   @Override
   public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {

   }

   @Override
   public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
      return 0.0F;
   }

   @Override
   public boolean canBeReplaced(BlockState state, Fluid fluid) {
      return false;
   }

   @Override
   public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {

      level.scheduleTick(pos, this, 30 + random.nextInt(10));
      if (!this.canSurvive(state, level, pos)) {
         level.removeBlock(pos, false);
         return;
      }

      int age = state.getValue(AGE);
      int newAge = Math.min(15, age + random.nextInt(3) / 2);
      if (age != newAge) {
         state = state.setValue(AGE, Integer.valueOf(newAge));
         level.setBlock(pos, state, 4);
      }
   }
}
