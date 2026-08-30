package com.paleimitations.schoolsofmagic.common.compat.jei.cauldron_brew;

import java.util.List;
import net.minecraft.world.item.ItemStack;

public class CauldronBrewRecipe {
   private final List<ItemStack> inputs;
   private final ItemStack output;
   private final int potionSkill;

   public CauldronBrewRecipe(List<ItemStack> inputs, ItemStack output) {
      this(inputs, output, 0);
   }

   public CauldronBrewRecipe(List<ItemStack> inputs, ItemStack output, int potionSkill) {
      this.inputs = inputs;
      this.output = output;
      this.potionSkill = potionSkill;
   }

   public List<ItemStack> getInputs() {
      return this.inputs;
   }

   public ItemStack getOutput() {
      return this.output;
   }

   public int getPotionSkill() {
      return this.potionSkill;
   }
}
