package com.paleimitations.schoolsofmagic.common.recipes;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import java.util.List;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.items.IItemHandler;

public class RecipeTea {
   private final Ingredient input1;
   private final Ingredient input2;
   private final Ingredient input3;
   public final Object inputO1;
   public final Object inputO2;
   public final Object inputO3;
   private final MobEffectInstance effect;
   private final String name;
   public int overlayColor = -1;

   public RecipeTea setOverlayColor(int color) {
      this.overlayColor = color;
      return this;
   }

   public RecipeTea(String name, EnumPlantType input1, EnumPlantType input2, EnumPlantType input3, MobEffectInstance effect) {
      this.name = name;
      this.inputO1 = input1;
      this.inputO2 = input2;
      this.inputO3 = input3;
      this.input1 = Ingredient.of(plantStack(input1));
      this.input2 = Ingredient.of(plantStack(input2));
      this.input3 = Ingredient.of(plantStack(input3));
      this.effect = effect;
   }

   public RecipeTea(String name, EnumPlantType input1, EnumPlantType input2, ItemStack input3, MobEffectInstance effect) {
      this.name = name;
      this.inputO1 = input1;
      this.inputO2 = input2;
      this.inputO3 = input3;
      this.input1 = Ingredient.of(plantStack(input1));
      this.input2 = Ingredient.of(plantStack(input2));
      this.input3 = Ingredient.of(input3);
      this.effect = effect;
   }

   public RecipeTea(String name, ItemStack input1, ItemStack input2, ItemStack input3, MobEffectInstance effect) {
      this.name = name;
      this.inputO1 = input1;
      this.inputO2 = input2;
      this.inputO3 = input3;
      this.input1 = Ingredient.of(input1);
      this.input2 = Ingredient.of(input2);
      this.input3 = Ingredient.of(input3);
      this.effect = effect;
   }

   public RecipeTea(String name, Object input1, Object input2, Object input3, MobEffectInstance effect) {
      this.name = name;
      this.inputO1 = input1;
      this.inputO2 = input2;
      this.inputO3 = input3;
      this.input1 = Ingredient.of(teaStack(input1));
      this.input2 = Ingredient.of(teaStack(input2));
      this.input3 = Ingredient.of(teaStack(input3));
      this.effect = effect;
   }

   private static ItemStack plantStack(EnumPlantType type) {
      ItemStack stack = new ItemStack(ItemRegistry.crushed_plant.get());
      stack.setDamageValue(type.getIndex());
      return stack;
   }

   private static ItemStack teaStack(Object obj) {
      if (obj instanceof ItemStack stack) {
         return stack;
      }
      if (obj instanceof EnumPlantType type) {
         return plantStack(type);
      }
      return ItemStack.EMPTY;
   }

   public boolean matches(IItemHandler inv) {
      List<ItemStack> inputsToSet = Lists.newArrayList();
      List<Ingredient> inputs = Lists.newArrayList(this.input1, this.input2, this.input3);

      for (int i = 0; i < inv.getSlots() - 1; i++) {
         if (!inv.getStackInSlot(i).isEmpty()) {
            inputsToSet.add(inv.getStackInSlot(i));
         }
      }

      if (inputsToSet.size() != inputs.size()) {
         return false;
      } else {
         boolean[] flags = new boolean[inputs.size()];

         for (ItemStack stack : inputsToSet) {
            for (int ix = 0; ix < inputs.size(); ix++) {
               if (this.compareStacks(inputs.get(ix), stack) && !flags[ix]) {
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

   public String getName() {
      return this.name;
   }

   public MobEffectInstance getEffect() {
      return this.effect;
   }

   public List<Ingredient> getInputs() {
      return Lists.newArrayList(this.input1, this.input2, this.input3);
   }
}
