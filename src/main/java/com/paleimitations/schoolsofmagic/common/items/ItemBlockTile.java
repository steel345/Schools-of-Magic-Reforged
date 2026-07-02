package com.paleimitations.schoolsofmagic.common.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ItemBlockTile extends BlockItem implements ITileStyles {
   public ItemBlockTile(Block block, Item.Properties props) {
      super(block, props);
   }
}
