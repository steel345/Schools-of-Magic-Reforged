package com.paleimitations.schoolsofmagic.common.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemPlant extends Item implements IPlantType {
   public ItemPlant(Item.Properties props) {
      super(props);
   }

   @Override
   public Component getName(ItemStack stack) {
      int meta = Math.max(0, Math.min(stack.getDamageValue(), this.getDamage() - 1));
      return Component.translatable(this.getDescriptionId() + "_" + this.handleMeta(meta));
   }
}
