package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class BlockPlantSage extends SOMPlant {

   public BlockPlantSage(BlockBehaviour.Properties props) {
      super(props);
   }

   @Override
   public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
   }
}
