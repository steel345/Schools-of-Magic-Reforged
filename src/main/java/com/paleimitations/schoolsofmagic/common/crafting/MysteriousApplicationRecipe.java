package com.paleimitations.schoolsofmagic.common.crafting;

import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.registries.RecipeSerializerRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class MysteriousApplicationRecipe extends CustomRecipe {

   public MysteriousApplicationRecipe(ResourceLocation id, CraftingBookCategory category) {
      super(id, category);
   }

   @Override
   public boolean matches(CraftingContainer inv, Level level) {
      boolean enabled = true;
      try {
         enabled = SOMConfig.enable_mysterious_application_recipe.get();
      } catch (Exception ignored) {
      }
      if (!enabled) return false;
      boolean paper = false, ink = false, red = false, lapis = false;
      for (int i = 0; i < inv.getContainerSize(); i++) {
         ItemStack s = inv.getItem(i);
         if (s.isEmpty()) continue;
         if (s.is(Items.PAPER) && !paper) paper = true;
         else if (s.is(Items.INK_SAC) && !ink) ink = true;
         else if (s.is(Items.RED_DYE) && !red) red = true;
         else if (s.is(Items.LAPIS_LAZULI) && !lapis) lapis = true;
         else return false;
      }
      return paper && ink && red && lapis;
   }

   @Override
   public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
      return new ItemStack(ItemRegistry.mysterious_application.get());
   }

   @Override
   public ItemStack getResultItem(RegistryAccess registryAccess) {
      return new ItemStack(ItemRegistry.mysterious_application.get());
   }

   @Override
   public boolean canCraftInDimensions(int width, int height) {
      return width * height >= 4;
   }

   @Override
   public boolean isSpecial() {
      return false;
   }

   @Override
   public NonNullList<Ingredient> getIngredients() {
      NonNullList<Ingredient> list = NonNullList.create();
      list.add(Ingredient.of(Items.PAPER));
      list.add(Ingredient.of(Items.INK_SAC));
      list.add(Ingredient.of(Items.RED_DYE));
      list.add(Ingredient.of(Items.LAPIS_LAZULI));
      return list;
   }

   @Override
   public RecipeSerializer<?> getSerializer() {
      return RecipeSerializerRegistry.MYSTERIOUS_APPLICATION.get();
   }
}
