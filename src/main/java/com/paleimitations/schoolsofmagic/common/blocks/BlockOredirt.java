package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BlockOredirt extends SOMBlock {

   private final Item drop;

   public BlockOredirt(BlockBehaviour.Properties props, Item drop) {
      super(props);
      this.drop = drop;
   }

   public Item getDrop() {
      return this.drop;
   }
}
