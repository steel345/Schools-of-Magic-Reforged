package com.paleimitations.schoolsofmagic.common.crafting;

import com.google.gson.JsonObject;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.registries.RecipeSerializerRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class SpellbookVariantRecipe extends ShapedRecipe {

   public SpellbookVariantRecipe(ResourceLocation id, ShapedRecipe base, ItemStack result) {
      super(id, base.getGroup(), base.category(), base.getWidth(), base.getHeight(),
            base.getIngredients(), result, base.showNotification());
   }

   @Override
   public RecipeSerializer<?> getSerializer() {
      return RecipeSerializerRegistry.SPELLBOOK_VARIANT.get();
   }

   public static ItemStack buildGrimoire(String color, int links) {
      ItemStack stack = new ItemStack(ItemRegistry.spellbook.get());
      IBook book = CapabilityBook.getCapability(stack);
      int colorId = -1;
      if (color != null && !color.isEmpty()) {
         DyeColor c = DyeColor.byName(color, null);
         if (c != null) colorId = c.getId();
      }
      if (book != null) {
         book.setLinks(links);
         if (colorId >= 0) book.setColor(DyeColor.byId(colorId));
      }
      stack.getOrCreateTag().putInt("BookLinks", links);
      stack.getOrCreateTag().putInt("BookColor", colorId);
      return stack;
   }

   public static class Serializer implements RecipeSerializer<SpellbookVariantRecipe> {
      @Override
      public SpellbookVariantRecipe fromJson(ResourceLocation id, JsonObject json) {

         ShapedRecipe base = RecipeSerializer.SHAPED_RECIPE.fromJson(id, json);
         int links = GsonHelper.getAsInt(json, "links", 0);
         String color = GsonHelper.getAsString(json, "color", "");
         return new SpellbookVariantRecipe(id, base, buildGrimoire(color, links));
      }

      @Override
      public SpellbookVariantRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {

         ShapedRecipe base = RecipeSerializer.SHAPED_RECIPE.fromNetwork(id, buf);
         return new SpellbookVariantRecipe(id, base, base.getResultItem(null));
      }

      @Override
      public void toNetwork(FriendlyByteBuf buf, SpellbookVariantRecipe recipe) {
         RecipeSerializer.SHAPED_RECIPE.toNetwork(buf, recipe);
      }
   }
}
