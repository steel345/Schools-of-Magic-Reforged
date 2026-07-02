package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BlockOrefalling extends FallingBlock {

   private final Item drop;

   public BlockOrefalling(BlockBehaviour.Properties props, Item drop) {
      super(props);
      this.drop = drop;
   }

   public Item getDrop() {
      return this.drop;
   }
}
