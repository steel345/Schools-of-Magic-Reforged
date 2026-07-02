package com.paleimitations.schoolsofmagic.common.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ItemBlockWood extends BlockItem implements IWoodType {
   public ItemBlockWood(Block block, Item.Properties props) {
      super(block, props);
   }
}
