package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SOMLeaves1 extends LeavesBlock {

   public SOMLeaves1(BlockBehaviour.Properties props) {
      super(props);
   }

   public static BlockBehaviour.Properties defaultProps() {
      return BlockBehaviour.Properties.of()
         .strength(0.2F)
         .randomTicks()
         .sound(SoundType.GRASS)
         .noOcclusion();
   }

   @Override
   public SoundType getSoundType(net.minecraft.world.level.block.state.BlockState state) {
      return SoundType.GRASS;
   }
}
