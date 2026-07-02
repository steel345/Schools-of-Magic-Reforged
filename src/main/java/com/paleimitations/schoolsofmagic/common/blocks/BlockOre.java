package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BlockOre extends SOMBlock {

   private final Item drop;

   public BlockOre(BlockBehaviour.Properties props) {
      this(props, null);
   }

   public BlockOre(BlockBehaviour.Properties props, Item drop) {
      super(props);
      this.drop = drop;
   }

   public Item getDrop() {
      return this.drop;
   }
}
