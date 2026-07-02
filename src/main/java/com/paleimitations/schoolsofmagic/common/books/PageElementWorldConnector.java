package com.paleimitations.schoolsofmagic.common.books;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class PageElementWorldConnector extends PageElement {
   public PageElementWorldConnector() {
      super(0, 0);
   }

   public boolean connects(Level world, BlockState state, BlockPos pos) {
      return false;
   }
}
