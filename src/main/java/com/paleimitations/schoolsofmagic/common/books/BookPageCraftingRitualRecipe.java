package com.paleimitations.schoolsofmagic.common.books;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.paleimitations.schoolsofmagic.common.IMagicType;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeRitualCrafting;
import java.util.Map;
import net.minecraft.world.item.ItemStack;

public class BookPageCraftingRitualRecipe extends BookPage {
   public final RecipeRitualCrafting recipe;
   public final Map<IMagicType, Integer> map = Maps.newHashMap();

   public BookPageCraftingRitualRecipe(final RecipeRitualCrafting recipe) {
      super(
         "crafting_ritual_" + descKey(recipe.getOutput()),
         Lists.newArrayList(new PageElement[]{
            new PageElementStandardText(recipe.getOutput().getDescriptionId(), 72, 55, 99, 10, 0, true),
            new PageElementItemStack(recipe.getOutput(), 64, 62),
            new PageElementParagraphs("crafting_ritual_" + descKey(recipe.getOutput()), 0.75F, 0, 1,
               new ParagraphBox(23, 82, 0, 99, 108),
               new ParagraphBox(23, 50, 1, 99, 140),
               new ParagraphBox(134, 50, 1, 99, 140),
               new ParagraphBox(23, 50, 2, 99, 140)),
            new PageElementRitualRecipe(recipe, 132, 47, 0),
            new PageElementWorldConnector()
         })
      );
      this.recipe = recipe;
   }

   public BookPageCraftingRitualRecipe(final RecipeRitualCrafting recipe, String pageName, String titleKey, String textLocation) {
      super(pageName, Lists.newArrayList(new PageElement[]{
         new PageElementStandardText(titleKey, 72, 55, 99, 10, 0, true),
         new PageElementItemStack(recipe.getOutput(), 64, 62),
         new PageElementParagraphs(textLocation, 0.75F, 0, 1,
            new ParagraphBox(23, 82, 0, 99, 108),
            new ParagraphBox(23, 50, 1, 99, 140),
            new ParagraphBox(134, 50, 1, 99, 140),
            new ParagraphBox(23, 50, 2, 99, 140)),
         new PageElementRitualRecipe(recipe, 132, 47, 0),
         new PageElementWorldConnector()
      }));
      this.recipe = recipe;
   }

   private static String descKey(ItemStack stack) {

      String id = stack.getDescriptionId();
      String tail;
      String[] a = id.split("item\\.");
      if (a.length > 1) tail = a[1];
      else {
         String[] b = id.split("block\\.");
         tail = b.length > 1 ? b[1] : id;
      }
      int dot = tail.indexOf('.');
      return dot >= 0 ? tail.substring(dot + 1) : tail;
   }
}
