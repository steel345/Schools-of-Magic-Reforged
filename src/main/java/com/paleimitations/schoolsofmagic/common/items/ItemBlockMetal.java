package com.paleimitations.schoolsofmagic.common.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ItemBlockMetal extends BlockItem implements IMetalType {
   public ItemBlockMetal(Block block, Item.Properties props) {
      super(block, props);
   }
}
