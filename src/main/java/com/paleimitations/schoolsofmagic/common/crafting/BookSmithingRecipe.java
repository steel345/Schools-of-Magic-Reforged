package com.paleimitations.schoolsofmagic.common.crafting;

import com.google.gson.JsonObject;
import com.paleimitations.schoolsofmagic.common.items.BookDecorations;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.registries.RecipeSerializerRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.level.Level;

public class BookSmithingRecipe implements SmithingRecipe {

   private final ResourceLocation id;

   public BookSmithingRecipe(ResourceLocation id) {
      this.id = id;
   }

   private static boolean isGrimoire(ItemStack s) {
      return s.getItem() == ItemRegistry.spellbook.get();
   }

   @Override
   public boolean isTemplateIngredient(ItemStack s) {
      return isGrimoire(s);
   }

   @Override
   public boolean isBaseIngredient(ItemStack s) {
      return s.is(Items.WATER_BUCKET) || BookDecorations.ingotMetal(s) != null;
   }

   @Override
   public boolean isAdditionIngredient(ItemStack s) {
      Item i = s.getItem();
      return i == ItemRegistry.copper_key.get()
         || i == ItemRegistry.shard_netherstar.get()
         || s.is(Items.ITEM_FRAME)
         || i == ItemRegistry.magic_diamond.get()
         || i == ItemRegistry.gem_dust.get()
         || i instanceof DyeItem;
   }

   private static ItemStack resolve(ItemStack book, ItemStack base, ItemStack add) {
      if (!isGrimoire(book) || base.isEmpty() || add.isEmpty()) {
         return null;
      }
      ItemStack out = book.copy();
      out.setCount(1);
      if (base.is(Items.WATER_BUCKET)) {
         if (!(add.getItem() instanceof DyeItem dye)) {
            return null;
         }
         BookDecorations.setSwirl(out, dye.getDyeColor());
         out.getOrCreateTag().putBoolean("__swirlBucket", true);
         return out;
      }
      String metal = BookDecorations.ingotMetal(base);
      if (metal == null) {
         return null;
      }
      Item m = add.getItem();
      if (m == ItemRegistry.copper_key.get()) {
         if ("triangle".equals(BookDecorations.getShape(out))) {
            return null;
         }
         BookDecorations.setShape(out, "facet", metal);
         return out;
      }
      if (m == ItemRegistry.shard_netherstar.get()) {
         if ("facet".equals(BookDecorations.getShape(out))) {
            return null;
         }
         BookDecorations.setShape(out, "triangle", metal);
         return out;
      }
      if (add.is(Items.ITEM_FRAME)) {
         BookDecorations.setFrame(out, metal);
         return out;
      }
      if (m == ItemRegistry.magic_diamond.get()) {
         if ("element".equals(BookDecorations.getEmblem(out))) {
            return null;
         }
         BookDecorations.setJewel(out, metal);
         return out;
      }
      if (m == ItemRegistry.gem_dust.get()) {
         String el = BookDecorations.elementTex(add);
         if (el == null) {
            return null;
         }
         if ("jewel".equals(BookDecorations.getEmblem(out))) {
            return null;
         }
         BookDecorations.setElement(out, metal, add.getDamageValue());
         return out;
      }
      return null;
   }

   @Override
   public boolean matches(Container c, Level level) {
      return resolve(c.getItem(0), c.getItem(1), c.getItem(2)) != null;
   }

   @Override
   public ItemStack assemble(Container c, RegistryAccess ra) {
      ItemStack out = resolve(c.getItem(0), c.getItem(1), c.getItem(2));
      return out == null ? ItemStack.EMPTY : out;
   }

   @Override
   public ItemStack getResultItem(RegistryAccess ra) {
      return new ItemStack(ItemRegistry.spellbook.get());
   }

   @Override
   public ResourceLocation getId() {
      return this.id;
   }

   @Override
   public RecipeSerializer<?> getSerializer() {
      return RecipeSerializerRegistry.BOOK_SMITHING.get();
   }

   public static class Serializer implements RecipeSerializer<BookSmithingRecipe> {
      @Override
      public BookSmithingRecipe fromJson(ResourceLocation id, JsonObject json) {
         return new BookSmithingRecipe(id);
      }

      @Override
      public BookSmithingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
         return new BookSmithingRecipe(id);
      }

      @Override
      public void toNetwork(FriendlyByteBuf buf, BookSmithingRecipe recipe) {
      }
   }
}
