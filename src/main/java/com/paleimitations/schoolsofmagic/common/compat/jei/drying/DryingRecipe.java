package com.paleimitations.schoolsofmagic.common.compat.jei.drying;

import net.minecraft.world.item.ItemStack;

public class DryingRecipe {
   private final ItemStack input;
   private final ItemStack output;

   public DryingRecipe(ItemStack input, ItemStack output) {
      this.input = input;
      this.output = output;
   }

   public ItemStack getInput() {
      return this.input;
   }

   public ItemStack getOutput() {
      return this.output;
   }
}
