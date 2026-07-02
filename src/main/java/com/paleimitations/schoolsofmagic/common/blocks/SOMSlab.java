package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SOMSlab extends SlabBlock {

   public SOMSlab(BlockBehaviour.Properties props) {
      super(props);
   }

   public static BlockBehaviour.Properties defaultProps() {
      return BlockBehaviour.Properties.of().strength(0.5F, 0.5F);
   }
}
