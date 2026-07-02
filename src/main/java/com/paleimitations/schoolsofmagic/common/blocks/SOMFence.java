package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class SOMFence extends FenceBlock {

   public SOMFence(BlockBehaviour.Properties props) {
      super(props);
   }

   public static BlockBehaviour.Properties defaultProps(MapColor color) {
      return BlockBehaviour.Properties.of().mapColor(color).strength(0.5F, 0.5F);
   }
}
