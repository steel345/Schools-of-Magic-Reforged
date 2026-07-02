package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SOMPlanks extends SOMBlock {

   public SOMPlanks(BlockBehaviour.Properties props) {
      super(props);
   }

   public static BlockBehaviour.Properties defaultProps() {
      return BlockBehaviour.Properties.of().strength(0.5F, 0.8F).sound(SoundType.WOOD);
   }
}
