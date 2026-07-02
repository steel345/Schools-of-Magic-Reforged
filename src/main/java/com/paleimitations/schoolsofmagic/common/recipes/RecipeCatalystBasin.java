package com.paleimitations.schoolsofmagic.common.recipes;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.items.IItemHandler;

public class RecipeCatalystBasin {
   private final Ingredient substrate;
   private final Ingredient catalyst;
   private final Ingredient solution;
   private final Ingredient reactant;
   private final float manaCost;
   private final int[] schoolLevels;
   private final int[] elementLevels;
   private final ItemStack output;
   private final ItemStack leftover;
   private final boolean requirementOr;

   public RecipeCatalystBasin(
      Object substrate, Object catalyst, Object solution, Object reactant, float manaCost, int[] schoolLevels, int[] elementLevels, ItemStack output
   ) {
      this(substrate, catalyst, solution, reactant, manaCost, schoolLevels, elementLevels, output, ItemStack.EMPTY, false);
   }

   public RecipeCatalystBasin(
      Object substrate, Object catalyst, Object solution, Object reactant, float manaCost, int[] schoolLevels, int[] elementLevels, ItemStack output, ItemStack leftover
   ) {
      this(substrate, catalyst, solution, reactant, manaCost, schoolLevels, elementLevels, output, leftover, false);
   }

   public RecipeCatalystBasin(
      Object substrate, Object catalyst, Object solution, Object reactant, float manaCost, int[] schoolLevels, int[] elementLevels, ItemStack output, ItemStack leftover, boolean requirementOr
   ) {
      this.substrate = IngredientFactory.of(substrate);
      this.catalyst = IngredientFactory.of(catalyst);
      this.solution = IngredientFactory.of(solution);
      this.reactant = IngredientFactory.of(reactant);
      this.manaCost = manaCost;
      this.schoolLevels = schoolLevels;
      this.elementLevels = elementLevels;
      this.output = output;
      this.leftover = leftover == null ? ItemStack.EMPTY : leftover;
      this.requirementOr = requirementOr;
   }

   public ItemStack getLeftover() {
      return this.leftover;
   }

   public boolean isRequirementOr() {
      return this.requirementOr;
   }

   public boolean matches(IItemHandler inv) {
      return this.compareStacks(this.substrate, inv.getStackInSlot(0))
         && this.compareStacks(this.catalyst, inv.getStackInSlot(1))
         && this.compareStacks(this.solution, inv.getStackInSlot(2))
         && this.compareStacks(this.reactant, inv.getStackInSlot(3));
   }

   private boolean compareStacks(Ingredient recipe, ItemStack supplied) {
      return IngredientFactory.compareStacks(recipe, supplied);
   }

   public Ingredient getSubstrate() {
      return this.substrate;
   }

   public Ingredient getCatalyst() {
      return this.catalyst;
   }

   public Ingredient getSolution() {
      return this.solution;
   }

   public Ingredient getReactant() {
      return this.reactant;
   }

   public int[] getElementLevels() {
      return this.elementLevels;
   }

   public List<MagicElement> getElementList() {
      List<MagicElement> list = Lists.newArrayList();

      for (int i = 0; i < 16; i++) {
         if (this.elementLevels[i] > 0) {
            list.add(MagicElementRegistry.getElementFromId(i));
         }
      }

      return list;
   }

   public int[] getSchoolLevels() {
      return this.schoolLevels;
   }

   public List<MagicSchool> getSchoolList() {
      List<MagicSchool> list = Lists.newArrayList();

      for (int i = 0; i < 6; i++) {
         if (this.schoolLevels[i] > 0) {
            list.add(MagicSchoolRegistry.getSchoolFromId(i));
         }
      }

      return list;
   }

   public ItemStack getOutput() {
      return this.output;
   }

   public float getManaCost() {
      return this.manaCost;
   }
}
