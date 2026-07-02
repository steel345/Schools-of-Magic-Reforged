package com.paleimitations.schoolsofmagic.common.compat.jei.spell_forge;

import net.minecraft.world.item.ItemStack;

public class SpellForgePointsRecipe {
   private final ItemStack input;
   private final ItemStack note;

   public SpellForgePointsRecipe(ItemStack input, ItemStack note) {
      this.input = input;
      this.note = note;
   }

   public ItemStack getInput() {
      return this.input;
   }

   public ItemStack getNote() {
      return this.note;
   }
}
