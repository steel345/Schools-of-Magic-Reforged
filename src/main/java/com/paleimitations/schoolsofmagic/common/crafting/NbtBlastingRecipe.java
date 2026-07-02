package com.paleimitations.schoolsofmagic.common.crafting;

import com.google.gson.JsonObject;
import com.paleimitations.schoolsofmagic.common.registries.RecipeSerializerRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;

public class NbtBlastingRecipe extends BlastingRecipe {

   public NbtBlastingRecipe(ResourceLocation id, String group, CookingBookCategory category,
                            Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
      super(id, group, category, ingredient, result, experience, cookingTime);
   }

   @Override
   public RecipeSerializer<?> getSerializer() {
      return RecipeSerializerRegistry.NBT_BLASTING.get();
   }

   public static class Serializer implements RecipeSerializer<NbtBlastingRecipe> {
      @Override
      public NbtBlastingRecipe fromJson(ResourceLocation id, JsonObject json) {
         String group = GsonHelper.getAsString(json, "group", "");
         Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
         ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);
         float xp = GsonHelper.getAsFloat(json, "experience", 0.0F);
         int time = GsonHelper.getAsInt(json, "cookingtime", 100);
         return new NbtBlastingRecipe(id, group, CookingBookCategory.MISC, ingredient, result, xp, time);
      }

      @Override
      public NbtBlastingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
         String group = buf.readUtf();
         Ingredient ingredient = Ingredient.fromNetwork(buf);
         ItemStack result = buf.readItem();
         float xp = buf.readFloat();
         int time = buf.readVarInt();
         return new NbtBlastingRecipe(id, group, CookingBookCategory.MISC, ingredient, result, xp, time);
      }

      @Override
      public void toNetwork(FriendlyByteBuf buf, NbtBlastingRecipe recipe) {
         buf.writeUtf(recipe.getGroup());
         recipe.getIngredients().get(0).toNetwork(buf);
         buf.writeItem(recipe.getResultItem(null));
         buf.writeFloat(recipe.getExperience());
         buf.writeVarInt(recipe.getCookingTime());
      }
   }
}
