package com.paleimitations.schoolsofmagic.common.blocks;

import java.util.function.Supplier;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class SOMStair extends StairBlock {

   public SOMStair(Supplier<BlockState> baseState, BlockBehaviour.Properties props) {
      super(baseState, props);
   }

   public static BlockBehaviour.Properties defaultProps() {
      return BlockBehaviour.Properties.of().strength(0.5F, 0.5F);
   }
}
