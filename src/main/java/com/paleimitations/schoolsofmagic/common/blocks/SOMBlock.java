package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SOMBlock extends Block {

   public SOMBlock(BlockBehaviour.Properties props) {
      super(props);
   }

   public static BlockBehaviour.Properties defaultProps() {
      return BlockBehaviour.Properties.of().strength(0.5F, 0.5F);
   }

   public static BlockBehaviour.Properties defaultProps(SoundType sound) {
      return BlockBehaviour.Properties.of().strength(0.5F, 0.5F).sound(sound);
   }
}
