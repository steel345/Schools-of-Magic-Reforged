package com.paleimitations.schoolsofmagic.common.registries;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.books.BookPageChapter;
import com.paleimitations.schoolsofmagic.common.books.PageElement;
import com.paleimitations.schoolsofmagic.common.books.PageElementTitle;
import com.paleimitations.schoolsofmagic.common.books.PageElementCraftingRecipe;
import com.paleimitations.schoolsofmagic.common.books.PageElementImage;
import net.minecraft.resources.ResourceLocation;
import com.paleimitations.schoolsofmagic.common.books.PageElementItemStack;
import com.paleimitations.schoolsofmagic.common.books.PageElementItemStackScaled;
import com.paleimitations.schoolsofmagic.common.books.PageElementParagraphs;
import com.paleimitations.schoolsofmagic.common.books.PageElementStandardText;
import com.paleimitations.schoolsofmagic.common.books.ParagraphBox;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeTea;
import com.paleimitations.schoolsofmagic.common.util.TeaUtils;
import net.minecraft.world.item.ItemStack;

public class BookTea {

   private static final float TEXT_SCALE = 0.75F;
   private static final ResourceLocation TEAPOT_BOOK_TEMPLATE = new ResourceLocation("som", "textures/gui/teapot_book.png");

   private static ItemStack porcelain() {
      return new ItemStack(ItemRegistry.item_porcelain.get());
   }

   private static ItemStack ingredient(Object o) {
      if (o instanceof ItemStack s) return s.copy();
      if (o instanceof EnumPlantType t) {
         ItemStack s = new ItemStack(ItemRegistry.crushed_plant.get());
         s.setDamageValue(t.getIndex());
         return s;
      }
      return ItemStack.EMPTY;
   }

   public static void init() {
      ItemStack e = ItemStack.EMPTY;

      new com.paleimitations.schoolsofmagic.common.books.BookPageTableContent(null).addToList(BookPageRegistry.TEA_BOOK);

      BookPageChapter introChapter = new BookPageChapter(null);
      introChapter.elements.add(new PageElementTitle("page.tea_ch_intro.title", 72, 58, 99, 16, 0, true));
      introChapter.addToList(BookPageRegistry.TEA_BOOK);

      new BookPage("tea_title", Lists.newArrayList(new PageElement[]{
         new PageElementImage(new ResourceLocation("som", "textures/gui/books/images/tea_page_initial.png"), 0, 0, 0, 0, 256, 256, 1.0F, false),
         new PageElementItemStackScaled(new ItemStack(ItemRegistry.teacup_empty.get()), 32, 100, 5.0F)
      })).addToList(BookPageRegistry.TEA_BOOK);

      new BookPage("tea_intro_1", Lists.newArrayList(new PageElement[]{
         new PageElementItemStackScaled(new ItemStack(ItemRegistry.bi_teapot.get()), 50, 76, 2.5F),
         new PageElementStandardText("page.tea_intro_1.title", 184, 58, 92, 14, 0, true),
         new PageElementParagraphs("tea_intro_1", TEXT_SCALE, 0, 0,
            new ParagraphBox(136, 72, 0, 92, 110))
      })).addToList(BookPageRegistry.TEA_BOOK);

      new BookPage("tea_teapot", Lists.newArrayList(new PageElement[]{
         new PageElementCraftingRecipe(Lists.newArrayList(new ItemStack[]{
            porcelain(), e, porcelain(),
            porcelain(), e, porcelain(),
            e, porcelain(), e
         }), new ItemStack(ItemRegistry.bi_teapot.get()), 44, 80),
         new PageElementStandardText("page.tea_teapot.label", 184, 58, 92, 14, 0, true),
         new PageElementParagraphs("tea_teapot", TEXT_SCALE, 0, 0,
            new ParagraphBox(136, 72, 0, 92, 110))
      })).addToList(BookPageRegistry.TEA_BOOK);

      new BookPage("tea_teacup_page", Lists.newArrayList(new PageElement[]{
         new PageElementCraftingRecipe(Lists.newArrayList(new ItemStack[]{
            porcelain(), e, porcelain(),
            e, porcelain(), e,
            e, e, e
         }), new ItemStack(ItemRegistry.teacup_empty.get()), 44, 80),
         new PageElementStandardText("page.tea_teacup.label", 184, 58, 92, 14, 0, true),
         new PageElementParagraphs("tea_teacup_page", TEXT_SCALE, 0, 0,
            new ParagraphBox(136, 72, 0, 92, 110))
      })).addToList(BookPageRegistry.TEA_BOOK);

      new BookPage("tea_intro_2", Lists.newArrayList(new PageElement[]{
         new PageElementCraftingRecipe(Lists.newArrayList(new ItemStack[]{
            porcelain(), porcelain(), porcelain(),
            e, e, e,
            e, e, e
         }), new ItemStack(ItemRegistry.bi_plate.get()), 44, 80),
         new PageElementStandardText("page.tea_plate.label", 184, 58, 92, 14, 0, true),
         new PageElementParagraphs("tea_intro_2", TEXT_SCALE, 0, 0,
            new ParagraphBox(136, 72, 0, 92, 110))
      })).addToList(BookPageRegistry.TEA_BOOK);

      BookPageChapter recipesChapter = new BookPageChapter(null);
      recipesChapter.elements.add(new PageElementTitle("page.tea_ch_recipes.title", 72, 58, 99, 16, 0, true));
      recipesChapter.addToList(BookPageRegistry.TEA_BOOK);

      for (RecipeTea tea : RecipeRegistry.teaRecipes) {
         String name = tea.getName();
         ItemStack result = TeaUtils.appendEffects(new ItemStack(ItemRegistry.teacup.get()), tea.getEffect());

         new BookPage(name, Lists.newArrayList(new PageElement[]{
            new PageElementImage(TEAPOT_BOOK_TEMPLATE, 24, 62, 0, 0, 116, 98, 0.85F, false),
            new PageElementItemStackScaled(ingredient(tea.inputO1), 75, 80, 0.8F),
            new PageElementItemStackScaled(ingredient(tea.inputO2), 75, 98, 0.8F),
            new PageElementItemStackScaled(ingredient(tea.inputO3), 75, 115, 0.8F),
            new PageElementItemStackScaled(result, 30, 98, 0.8F),
            new PageElementStandardText("page." + name + ".title", 184, 58, 92, 14, 0, true),
            new PageElementParagraphs(name, TEXT_SCALE, 0, 0,
               new ParagraphBox(136, 68, 0, 92, 110))
         })).addToList(BookPageRegistry.TEA_BOOK);
      }

      BookPageChapter customChapter = new BookPageChapter(null);
      customChapter.elements.add(new PageElementTitle("page.tea_ch_custom.title", 72, 58, 99, 16, 0, true));
      customChapter.addToList(BookPageRegistry.TEA_BOOK);

      new BookPage("tea_custom_intro", Lists.newArrayList(new PageElement[]{
         new PageElementStandardText("page.tea_custom_intro.title", 72, 58, 99, 16, 0, true),
         new PageElementParagraphs("tea_custom_intro", TEXT_SCALE, 0, 0,
            new ParagraphBox(23, 70, 0, 99, 120),
            new ParagraphBox(134, 50, 0, 99, 140))
      })).addToList(BookPageRegistry.TEA_BOOK);

      new BookPage("tea_custom_modifiers", Lists.newArrayList(new PageElement[]{
         new PageElementStandardText("page.tea_custom_modifiers.title", 72, 58, 99, 16, 0, true),
         new PageElementParagraphs("tea_custom_modifiers", TEXT_SCALE, 0, 0,
            new ParagraphBox(23, 70, 0, 99, 120),
            new ParagraphBox(134, 50, 0, 99, 140))
      })).addToList(BookPageRegistry.TEA_BOOK);
   }
}
