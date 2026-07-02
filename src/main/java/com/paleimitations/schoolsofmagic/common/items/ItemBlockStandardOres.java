package com.paleimitations.schoolsofmagic.common.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ItemBlockStandardOres extends BlockItem implements IStandardOre {
   public ItemBlockStandardOres(Block block, Item.Properties props) {
      super(block, props);
   }
}
