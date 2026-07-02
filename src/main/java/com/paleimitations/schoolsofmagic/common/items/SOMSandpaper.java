package com.paleimitations.schoolsofmagic.common.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SOMSandpaper extends Item {
   public SOMSandpaper(Item.Properties props) {
      super(props);
   }

   @Override
   public boolean hasCraftingRemainingItem(ItemStack itemStack) {
      return true;
   }

   @Override
   public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
      ItemStack stack = itemStack.copy();
      stack.setDamageValue(stack.getDamageValue() + 1);
      if (stack.getDamageValue() == this.getMaxDamage(stack)) {
         stack = ItemStack.EMPTY;
      }

      return stack;
   }
}
