package com.paleimitations.schoolsofmagic.common.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemIngredient extends Item implements IIngredientType {
   public ItemIngredient(Item.Properties props) {
      super(props.food(new FoodProperties.Builder().nutrition(4).saturationMod(0.1F).build()));
   }

   @Override
   public Component getName(ItemStack stack) {
      int meta = Math.max(0, Math.min(stack.getDamageValue(), this.getDamage() - 1));
      return Component.translatable(this.getDescriptionId() + "_" + this.handleMeta(meta));
   }
}
