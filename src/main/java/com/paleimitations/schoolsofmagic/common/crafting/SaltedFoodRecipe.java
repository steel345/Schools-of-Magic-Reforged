package com.paleimitations.schoolsofmagic.common.crafting;

import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.registries.RecipeSerializerRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class SaltedFoodRecipe extends CustomRecipe {
   public SaltedFoodRecipe(ResourceLocation id, CraftingBookCategory category) {
      super(id, category);
   }

   private static boolean isSalt(ItemStack s) {
      return s.getItem() == ItemRegistry.salt.get();
   }

   public static boolean isSaltableFood(ItemStack s) {
      return s.getItem().isEdible()
         && s.getItem().getFoodProperties(s, null) != null
         && s.getUseAnimation() != UseAnim.DRINK
         && !(s.hasTag() && s.getTag() != null && s.getTag().getBoolean("Salted"));
   }

   @Override
   public boolean matches(CraftingContainer inv, Level level) {
      boolean salt = false;
      ItemStack food = ItemStack.EMPTY;
      for (int i = 0; i < inv.getContainerSize(); i++) {
         ItemStack s = inv.getItem(i);
         if (s.isEmpty()) {
            continue;
         }
         if (isSalt(s) && !salt) {
            salt = true;
         } else if (food.isEmpty() && isSaltableFood(s) && !isSalt(s)) {
            food = s;
         } else {
            return false;
         }
      }
      return salt && !food.isEmpty();
   }

   @Override
   public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
      ItemStack food = ItemStack.EMPTY;
      for (int i = 0; i < inv.getContainerSize(); i++) {
         ItemStack s = inv.getItem(i);
         if (!s.isEmpty() && !isSalt(s) && isSaltableFood(s)) {
            food = s;
            break;
         }
      }
      if (food.isEmpty()) {
         return ItemStack.EMPTY;
      }
      ItemStack out = food.copyWithCount(1);
      out.getOrCreateTag().putBoolean("Salted", true);
      out.setHoverName(Component.translatable("item.som.salted_prefix")
         .append(food.getHoverName()).withStyle(st -> st.withItalic(false).withColor(ChatFormatting.WHITE)));
      return out;
   }

   @Override
   public boolean canCraftInDimensions(int width, int height) {
      return width * height >= 2;
   }

   @Override
   public ItemStack getResultItem(RegistryAccess registryAccess) {
      return ItemStack.EMPTY;
   }

   @Override
   public RecipeSerializer<?> getSerializer() {
      return RecipeSerializerRegistry.SALTED_FOOD.get();
   }
}
