package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemBottle extends Item implements IBottleType {
   public ItemBottle(Item.Properties props) {
      super(props);
   }

   @Override
   public Component getName(ItemStack stack) {
      int meta = Math.max(0, Math.min(stack.getDamageValue(), this.getDamage() - 1));
      return Component.translatable(this.getDescriptionId() + "_" + this.handleMeta(meta));
   }

   public boolean hasCraftingRemainingItem(ItemStack stack) {
      return true;
   }

   public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
      return new ItemStack(ItemRegistry.bottle_empty.get());
   }
}
