package com.paleimitations.schoolsofmagic.common.compat.jei;

import com.paleimitations.schoolsofmagic.common.items.BookDecorations;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;

public class BookDecorJeiRecipes {

   private static ItemStack ingot(int meta) {
      ItemStack s = new ItemStack(ItemRegistry.ingot.get());
      s.setDamageValue(meta);
      return s;
   }

   private static ItemStack grimoire() {
      ItemStack s = new ItemStack(ItemRegistry.spellbook.get());
      s.getOrCreateTag().putInt("BookColor", net.minecraft.world.item.DyeColor.BLACK.getId());
      s.getOrCreateTag().putInt("BookLinks", 6);
      return s;
   }

   private static ItemStack base() {
      ItemStack s = grimoire();
      return s;
   }

   public static List<SmithingRecipe> build() {
      List<SmithingRecipe> list = new ArrayList<>();
      Ingredient grimoire = Ingredient.of(grimoire());
      int[] n = {0};

      Map<String, ItemStack> metals = new LinkedHashMap<>();
      metals.put("iron", new ItemStack(Items.IRON_INGOT));
      metals.put("gold", new ItemStack(Items.GOLD_INGOT));
      metals.put("silver", ingot(0));
      metals.put("copper", ingot(2));
      metals.put("bronze", ingot(4));
      metals.put("brass", ingot(6));
      metals.put("netherite", ingot(8));
      metals.put("tenebrium", ingot(9));

      for (Map.Entry<String, ItemStack> e : metals.entrySet()) {
         String metal = e.getKey();
         ItemStack metalIngot = e.getValue();
         ItemStack facet = base();
         BookDecorations.setShape(facet, "facet", metal);
         list.add(rec(grimoire, metalIngot, new ItemStack(ItemRegistry.copper_key.get()), facet, n));

         ItemStack triangle = base();
         BookDecorations.setShape(triangle, "triangle", metal);
         list.add(rec(grimoire, metalIngot, new ItemStack(ItemRegistry.shard_netherstar.get()), triangle, n));

         ItemStack frame = base();
         BookDecorations.setFrame(frame, metal);
         list.add(rec(grimoire, metalIngot, new ItemStack(Items.ITEM_FRAME), frame, n));

         ItemStack jewel = base();
         BookDecorations.setJewel(jewel, metal);
         list.add(rec(grimoire, metalIngot, new ItemStack(ItemRegistry.magic_diamond.get()), jewel, n));

         for (int el = 0; el < 16; el++) {
            ItemStack dust = new ItemStack(ItemRegistry.gem_dust.get());
            dust.setDamageValue(el);
            if (BookDecorations.elementTex(dust) == null) {
               continue;
            }
            ItemStack element = base();
            BookDecorations.setElement(element, metal, el);
            list.add(rec(grimoire, metalIngot, dust, element, n));
         }
      }

      for (DyeColor color : DyeColor.values()) {
         ItemStack swirl = base();
         BookDecorations.setSwirl(swirl, color);
         list.add(rec(grimoire, new ItemStack(Items.WATER_BUCKET), new ItemStack(DyeItem.byColor(color)), swirl, n));
      }

      return list;
   }

   private static SmithingRecipe rec(Ingredient template, ItemStack base, ItemStack addition, ItemStack result, int[] n) {
      return new SmithingTransformRecipe(
         new ResourceLocation("som", "jei_book_decor_" + (n[0]++)),
         template,
         Ingredient.of(base),
         Ingredient.of(addition),
         result);
   }
}
