package com.paleimitations.schoolsofmagic.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class TransferRoute {
   public final BlockPos sourceLocation;
   public final Direction sourceFacing;
   public final BlockPos destinationLocation;
   public final Direction destinationFacing;

   public TransferRoute(BlockPos sourceLocation, Direction sourceFacing, BlockPos destinationLocation, Direction destinationFacing) {
      this.sourceLocation = sourceLocation;
      this.sourceFacing = sourceFacing;
      this.destinationLocation = destinationLocation;
      this.destinationFacing = destinationFacing;
   }

   public boolean isValid() {
      return this.sourceLocation != null && this.sourceFacing != null && this.destinationLocation != null && this.destinationFacing != null;
   }
}
