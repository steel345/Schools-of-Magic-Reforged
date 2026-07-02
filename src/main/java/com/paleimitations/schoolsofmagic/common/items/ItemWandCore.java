package com.paleimitations.schoolsofmagic.common.items;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;

public class ItemWandCore extends Item implements IWoodType {
   public ItemWandCore(Item.Properties props) {
      super(props);
   }

   public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
      for (int i = 0; i < this.getDamage(); ++i) {
         items.add(i, new ItemStack(this));
      }
   }

   public String getDescriptionId(ItemStack stack) {
      return super.getDescriptionId(stack) + "_" + this.handleMeta(stack.getDamageValue());
   }
}
