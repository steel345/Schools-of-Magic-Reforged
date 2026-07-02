package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

public class BlockWallPlant extends BlockWalls implements IPlantable {

   public BlockWallPlant(BlockBehaviour.Properties props) {
      super(props);
   }

   @Override
   public float getDestroyProgress(net.minecraft.world.level.block.state.BlockState state,
         net.minecraft.world.entity.player.Player player,
         net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos) {
      return BushBreak.progress(player);
   }

   @Override public boolean isRandomlyTicking(BlockState state) { return true; }

   @Override
   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
      checkAndDropBlock(level, pos, state);
   }

   protected void checkAndDropBlock(Level level, BlockPos pos, BlockState state) {
      if (!this.canBlockStay(level, pos, state)) {
         level.destroyBlock(pos, true);
      }
   }

   public boolean canBlockStay(LevelReader level, BlockPos pos, BlockState state) {
      BlockState soil = level.getBlockState(pos.below());
      return soil.canSustainPlant(level, pos.below(), net.minecraft.core.Direction.UP, this)
         || soil.getBlock() == this
         || soil.getBlock() == Blocks.AIR;
   }

   @Override
   public PlantType getPlantType(net.minecraft.world.level.BlockGetter world, BlockPos pos) {
      return PlantType.PLAINS;
   }

   @Override
   public BlockState getPlant(net.minecraft.world.level.BlockGetter world, BlockPos pos) {
      return this.defaultBlockState();
   }

   @Override
   public void entityInside(net.minecraft.world.level.block.state.BlockState state, net.minecraft.world.level.Level world, net.minecraft.core.BlockPos pos, net.minecraft.world.entity.Entity entity) {
      super.entityInside(state, world, pos, entity);
      BushSound.rustle(world, entity);
   }
}
