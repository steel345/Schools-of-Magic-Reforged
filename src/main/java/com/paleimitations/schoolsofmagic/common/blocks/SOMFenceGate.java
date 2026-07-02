package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;

public class SOMFenceGate extends FenceGateBlock {

   public SOMFenceGate(BlockBehaviour.Properties props) {
      super(props, WoodType.OAK);
   }

   public SOMFenceGate(BlockBehaviour.Properties props, WoodType type) {
      super(props, type);
   }

   public static BlockBehaviour.Properties defaultProps() {
      return BlockBehaviour.Properties.of().strength(0.5F, 0.5F);
   }
}
