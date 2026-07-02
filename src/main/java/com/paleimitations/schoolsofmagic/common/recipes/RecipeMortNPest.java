package com.paleimitations.schoolsofmagic.common.recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.items.IItemHandler;

public class RecipeMortNPest {
   private final ItemStack output;
   private final Ingredient input;
   private final ItemStack outputSecondary;
   private final Ingredient inputSecondary;
   private final Object inputO;
   private final Object inputSecondaryO;
   private final int crush;

   public RecipeMortNPest(ItemStack output, ItemStack outputSecondary, int crush, Object input, Object inputSecondary) {
      this.output = output;
      this.outputSecondary = outputSecondary;
      this.inputO = input;
      this.inputSecondaryO = inputSecondary;
      this.input = IngredientFactory.of(input);
      this.inputSecondary = IngredientFactory.of(inputSecondary);
      this.crush = crush;
   }

   public boolean matches(IItemHandler inv) {
      return this.compareStacks(this.input, inv.getStackInSlot(0)) && this.compareStacks(this.inputSecondary, inv.getStackInSlot(1));
   }

   public boolean compareStacks(Ingredient recipe, ItemStack supplied) {
      return IngredientFactory.compareStacks(recipe, supplied);
   }

   public int getCrush() {
      return this.crush;
   }

   public Ingredient getInput() {
      return this.input;
   }

   public ItemStack getOutput() {
      return this.output.copy();
   }

   public Ingredient getInputSecondary() {
      return this.inputSecondary;
   }

   public ItemStack getOutputSecondary() {
      return this.outputSecondary.copy();
   }

   public Object getInputO() {
      return this.inputO;
   }

   public Object getInputSecondaryO() {
      return this.inputSecondaryO;
   }
}
