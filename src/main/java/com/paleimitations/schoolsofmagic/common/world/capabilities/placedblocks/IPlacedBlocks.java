package com.paleimitations.schoolsofmagic.common.world.capabilities.placedblocks;

import net.minecraft.core.BlockPos;

public interface IPlacedBlocks {
   boolean isPlaced(BlockPos var1);

   void add(BlockPos var1);

   void remove(BlockPos var1);

   int size();
}
