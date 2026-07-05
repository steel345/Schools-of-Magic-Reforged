package com.paleimitations.schoolsofmagic.common.compat.jei.cauldron_brew;

import java.util.List;
import net.minecraft.world.item.ItemStack;

public class CauldronBrewRecipe {

   private final List<ItemStack> inputs;
   private final ItemStack output;

   public CauldronBrewRecipe(List<ItemStack> inputs, ItemStack output) {
      this.inputs = inputs;
      this.output = output;
   }

   public List<ItemStack> getInputs() {
      return this.inputs;
   }

   public ItemStack getOutput() {
      return this.output;
   }
}
