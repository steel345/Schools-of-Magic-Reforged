package com.paleimitations.schoolsofmagic.common.crafting;

import com.google.gson.JsonObject;
import com.paleimitations.schoolsofmagic.common.registries.RecipeSerializerRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraftforge.common.crafting.CraftingHelper;

public class NbtStonecuttingRecipe extends StonecutterRecipe {

   public NbtStonecuttingRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result) {
      super(id, group, ingredient, result);
   }

   @Override
   public RecipeSerializer<?> getSerializer() {
      return RecipeSerializerRegistry.NBT_STONECUTTING.get();
   }

   public static class Serializer implements RecipeSerializer<NbtStonecuttingRecipe> {
      @Override
      public NbtStonecuttingRecipe fromJson(ResourceLocation id, JsonObject json) {
         String group = GsonHelper.getAsString(json, "group", "");
         Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
         ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);
         return new NbtStonecuttingRecipe(id, group, ingredient, result);
      }

      @Override
      public NbtStonecuttingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
         String group = buf.readUtf();
         Ingredient ingredient = Ingredient.fromNetwork(buf);
         ItemStack result = buf.readItem();
         return new NbtStonecuttingRecipe(id, group, ingredient, result);
      }

      @Override
      public void toNetwork(FriendlyByteBuf buf, NbtStonecuttingRecipe recipe) {
         buf.writeUtf(recipe.getGroup());
         recipe.getIngredients().get(0).toNetwork(buf);
         buf.writeItem(recipe.getResultItem(null));
      }
   }
}
