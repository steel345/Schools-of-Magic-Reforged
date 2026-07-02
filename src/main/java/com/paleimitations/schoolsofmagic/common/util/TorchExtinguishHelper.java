package com.paleimitations.schoolsofmagic.common.util;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class TorchExtinguishHelper {

   public static boolean relight(Level level, BlockPos pos) {
      BlockState oldState = level.getBlockState(pos);
      if (oldState.hasProperty(BlockStateProperties.WATERLOGGED) && oldState.getValue(BlockStateProperties.WATERLOGGED)) {
         return false;
      }
      BlockState newState;
      if (oldState.is(BlockRegistry.unlit_torch.get())) {
         newState = Blocks.TORCH.defaultBlockState();
      } else if (oldState.is(BlockRegistry.unlit_wall_torch.get())) {
         newState = Blocks.WALL_TORCH.defaultBlockState()
            .setValue(WallTorchBlock.FACING, oldState.getValue(WallTorchBlock.FACING));
      } else {
         return false;
      }
      if (level instanceof ServerLevel serverLevel) {
         level.setBlock(pos, newState, 3);
         level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
         serverLevel.sendParticles(ParticleTypes.FLAME, pos.getX() + 0.5D, pos.getY() + 0.7D, pos.getZ() + 0.5D, 6, 0.1D, 0.1D, 0.1D, 0.01D);
      }
      return true;
   }

   public static boolean extinguish(Level level, BlockPos pos) {
      BlockState oldState = level.getBlockState(pos);
      BlockState newState;
      if (oldState.is(Blocks.TORCH) || oldState.is(Blocks.SOUL_TORCH)) {
         newState = BlockRegistry.unlit_torch.get().defaultBlockState();
      } else if (oldState.is(Blocks.WALL_TORCH) || oldState.is(Blocks.SOUL_WALL_TORCH)) {
         newState = BlockRegistry.unlit_wall_torch.get().defaultBlockState()
            .setValue(WallTorchBlock.FACING, oldState.getValue(WallTorchBlock.FACING));
      } else {
         return false;
      }
      if (level instanceof ServerLevel serverLevel) {
         level.setBlock(pos, newState, 3);
         level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.6F, 1.2F);
         serverLevel.sendParticles(ParticleTypes.SMOKE, pos.getX() + 0.5D, pos.getY() + 0.7D, pos.getZ() + 0.5D, 8, 0.1D, 0.1D, 0.1D, 0.01D);
      }
      return true;
   }

   public static void extinguishArea(Level level, BlockPos center, int radius) {
      for (int dx = -radius; dx <= radius; dx++) {
         for (int dy = -radius; dy <= radius; dy++) {
            for (int dz = -radius; dz <= radius; dz++) {
               extinguish(level, center.offset(dx, dy, dz));
            }
         }
      }
   }
}
