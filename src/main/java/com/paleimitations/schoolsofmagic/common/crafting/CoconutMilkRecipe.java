package com.paleimitations.schoolsofmagic.common.crafting;

import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.registries.RecipeSerializerRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class CoconutMilkRecipe extends CustomRecipe {

   public CoconutMilkRecipe(ResourceLocation id, CraftingBookCategory category) {
      super(id, category);
   }

   private static boolean isCoconut(ItemStack s) {
      return s.getItem() == ItemRegistry.bi_coconut.get();
   }

   @Override
   public boolean matches(CraftingContainer inv, Level level) {
      boolean bucket = false;
      boolean coconut = false;
      for (int i = 0; i < inv.getContainerSize(); i++) {
         ItemStack s = inv.getItem(i);
         if (s.isEmpty()) continue;
         if (s.getItem() == Items.BUCKET && !bucket) {
            bucket = true;
         } else if (isCoconut(s) && !coconut) {
            coconut = true;
         } else {
            return false;
         }
      }
      return bucket && coconut;
   }

   public static ItemStack coconutMilk() {
      ItemStack milk = new ItemStack(Items.MILK_BUCKET);
      milk.setHoverName(Component.translatable("item.som.coconut_milk").withStyle(st -> st.withItalic(false)));
      return milk;
   }

   @Override
   public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
      return coconutMilk();
   }

   @Override
   public ItemStack getResultItem(RegistryAccess registryAccess) {
      return coconutMilk();
   }

   @Override
   public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
      NonNullList<ItemStack> remainders = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
      for (int i = 0; i < inv.getContainerSize(); i++) {
         if (isCoconut(inv.getItem(i))) {

            remainders.set(i, new ItemStack(Items.BOWL, 2));
         }
      }
      return remainders;
   }

   @Override
   public boolean canCraftInDimensions(int width, int height) {
      return width * height >= 2;
   }

   @Override
   public boolean isSpecial() {
      return false;
   }

   @Override
   public NonNullList<net.minecraft.world.item.crafting.Ingredient> getIngredients() {
      NonNullList<net.minecraft.world.item.crafting.Ingredient> list = NonNullList.create();
      list.add(net.minecraft.world.item.crafting.Ingredient.of(Items.BUCKET));
      list.add(net.minecraft.world.item.crafting.Ingredient.of(ItemRegistry.bi_coconut.get()));
      return list;
   }

   @Override
   public RecipeSerializer<?> getSerializer() {
      return RecipeSerializerRegistry.COCONUT_MILK.get();
   }
}
