package com.paleimitations.schoolsofmagic.common.recipes;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import java.util.List;
import java.util.Map;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.items.IItemHandler;

public class RecipeRitualCrafting {
   private final int mana;
   private final int minMagicianLevel;
   private final int minRitualLevel;
   private final int[] minSchoolLevels = new int[MagicSchoolRegistry.SCHOOLS.size()];
   private final int[] minElementLevels = new int[MagicElementRegistry.ELEMENTS.size()];
   private final ItemStack output;
   private final List<Ingredient> inputs;

   public RecipeRitualCrafting(
      ItemStack output,
      int mana,
      int minMagicianLevelIn,
      int minRitualLevelIn,
      Map<MagicSchool, Integer> minSchoolLevelsIn,
      Map<MagicElement, Integer> minElementLevelsIn,
      Object... inputs
   ) {
      this.output = output;
      this.minMagicianLevel = minMagicianLevelIn;
      this.minRitualLevel = minRitualLevelIn;

      for (MagicSchool school : minSchoolLevelsIn.keySet()) {
         this.minSchoolLevels[school.getId()] = minSchoolLevelsIn.get(school);
      }

      for (MagicElement element : minElementLevelsIn.keySet()) {
         this.minElementLevels[element.getId()] = minElementLevelsIn.get(element);
      }

      this.mana = mana;
      List<Ingredient> inputsToSet = Lists.newArrayList();

      for (Object obj : inputs) {
         inputsToSet.add(IngredientFactory.of(obj));
      }

      this.inputs = inputsToSet;
   }

   public List<Ingredient> getInputs() {
      return this.inputs;
   }

   public ItemStack getOutput() {
      return this.output.copy();
   }

   public int getManaUsage() {
      return this.mana;
   }

   public int getMinMagicianLevel() {
      return this.minMagicianLevel;
   }

   public int getMinRitualLevel() {
      return this.minRitualLevel;
   }

   public int[] getMinElementLevels() {
      return this.minElementLevels;
   }

   public int[] getMinSchoolLevels() {
      return this.minSchoolLevels;
   }

   public boolean matches(IItemHandler inv) {
      List<ItemStack> inputsToSet = Lists.newArrayList();

      for (int i = 0; i < inv.getSlots(); i++) {
         if (!inv.getStackInSlot(i).isEmpty()) {
            inputsToSet.add(inv.getStackInSlot(i));
         }
      }

      if (inputsToSet.size() != this.inputs.size()) {
         return false;
      } else {
         boolean[] flags = new boolean[this.inputs.size()];

         for (ItemStack stack : inputsToSet) {
            for (int ix = 0; ix < this.inputs.size(); ix++) {
               if (this.compareStacks(this.inputs.get(ix), stack) && !flags[ix]) {
                  flags[ix] = true;
                  break;
               }
            }
         }

         for (boolean b : flags) {
            if (!b) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean compareStacks(Ingredient recipe, ItemStack supplied) {
      return IngredientFactory.compareStacks(recipe, supplied);
   }
}
