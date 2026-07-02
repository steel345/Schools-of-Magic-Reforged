package com.paleimitations.schoolsofmagic.common.crafting;

import com.paleimitations.schoolsofmagic.common.items.ItemBrassWhistle;
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
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class WhistleWaxRecipe extends CustomRecipe {

   public WhistleWaxRecipe(ResourceLocation id, CraftingBookCategory category) {
      super(id, category);
   }

   private static boolean isWhistle(ItemStack s) {
      return s.getItem() instanceof ItemBrassWhistle && !ItemBrassWhistle.isWaxed(s);
   }

   @Override
   public boolean matches(CraftingContainer inv, Level level) {
      boolean whistle = false;
      boolean wax = false;
      for (int i = 0; i < inv.getContainerSize(); i++) {
         ItemStack s = inv.getItem(i);
         if (s.isEmpty()) continue;
         if (isWhistle(s) && !whistle) {
            whistle = true;
         } else if (s.getItem() == Items.HONEYCOMB && !wax) {
            wax = true;
         } else {
            return false;
         }
      }
      return whistle && wax;
   }

   @Override
   public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
      for (int i = 0; i < inv.getContainerSize(); i++) {
         ItemStack s = inv.getItem(i);
         if (isWhistle(s)) return ItemBrassWhistle.waxed(s);
      }
      return ItemStack.EMPTY;
   }

   @Override
   public ItemStack getResultItem(RegistryAccess registryAccess) {
      return new ItemStack(ItemRegistry.brass_whistle.get());
   }

   @Override
   public boolean canCraftInDimensions(int width, int height) {
      return width * height >= 2;
   }

   @Override
   public NonNullList<net.minecraft.world.item.crafting.Ingredient> getIngredients() {
      NonNullList<net.minecraft.world.item.crafting.Ingredient> list = NonNullList.create();
      list.add(net.minecraft.world.item.crafting.Ingredient.of(ItemRegistry.brass_whistle.get()));
      list.add(net.minecraft.world.item.crafting.Ingredient.of(Items.HONEYCOMB));
      return list;
   }

   @Override
   public RecipeSerializer<?> getSerializer() {
      return RecipeSerializerRegistry.WHISTLE_WAX.get();
   }
}
