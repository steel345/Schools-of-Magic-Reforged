package com.paleimitations.schoolsofmagic.common.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ItemBlockCauldron extends BlockItem implements ICauldronType {
   public ItemBlockCauldron(Block block, Item.Properties props) {
      super(block, props);
   }
}
