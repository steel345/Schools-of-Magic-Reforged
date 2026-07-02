package com.paleimitations.schoolsofmagic.common.world.capabilities.banishedblocks;

import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface IBanishedBlocks {
   boolean isBanished(BlockPos var1);

   BlockState getPreviousState(BlockPos var1);

   int getTimer(BlockPos var1);

   void setTimer(BlockPos var1, int var2);

   void clear();

   Map<BlockPos, BlockState> getPreviousStates();

   Map<BlockPos, Integer> getCountdowns();

   void addSet(BlockPos var1, BlockState var2, int var3);

   void removeSet(BlockPos var1);
}
