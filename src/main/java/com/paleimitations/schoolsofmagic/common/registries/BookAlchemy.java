package com.paleimitations.schoolsofmagic.common.registries;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.books.BookPageChapter;
import com.paleimitations.schoolsofmagic.common.books.PageElement;
import com.paleimitations.schoolsofmagic.common.books.PageElementCatalystBasinRecipe;
import com.paleimitations.schoolsofmagic.common.books.PageElementImage;
import com.paleimitations.schoolsofmagic.common.books.PageElementMortarRecipe;
import com.paleimitations.schoolsofmagic.common.books.PageElementParagraphs;
import com.paleimitations.schoolsofmagic.common.books.PageElementTitle;
import com.paleimitations.schoolsofmagic.common.books.ParagraphBox;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeCatalystBasin;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeMortNPest;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;

public class BookAlchemy {

   private static final int[][] MORTAR_POS = {{28, 50}, {139, 50}, {28, 126}, {139, 126}};
   private static final int[][] BASIN_POS = {{21, 52}, {132, 52}};

   public static void init() {
      new com.paleimitations.schoolsofmagic.common.books.BookPageTableContent(null).addToList(BookPageRegistry.ALCHEMY_BOOK);

      BookPageChapter introChapter = new BookPageChapter(null);
      introChapter.elements.add(new PageElementTitle("page.alchemy_intro.title", 72, 58, 99, 16, 0, true));
      introChapter.addToList(BookPageRegistry.ALCHEMY_BOOK);

      new BookPage("alchemy_intro", Lists.newArrayList(new PageElement[]{
         new PageElementImage(new ResourceLocation("som", "textures/gui/books/images/bmb_alchemy.png"), 0, 0, 0, 0, 256, 256, 1.0F, false),
         new PageElementParagraphs("alchemy_intro", 0.75F, 0, 0,
            new ParagraphBox(134, 50, 0, 99, 140))
      })).addToList(BookPageRegistry.ALCHEMY_BOOK);

      BookPageChapter mortarChapter = new BookPageChapter(null);
      mortarChapter.elements.add(new PageElementTitle("page.alchemy_mortar.title", 72, 58, 99, 16, 0, true));
      mortarChapter.addToList(BookPageRegistry.ALCHEMY_BOOK);

      List<RecipeMortNPest> mortar = RecipeRegistry.mortnpestRecipes;
      for (int i = 0; i < mortar.size(); i += 4) {
         List<PageElement> els = new ArrayList<>();
         for (int j = 0; j < 4 && i + j < mortar.size(); j++) {
            els.add(new PageElementMortarRecipe(mortar.get(i + j), MORTAR_POS[j][0], MORTAR_POS[j][1]));
         }
         new BookPage("alchemy_mortar_" + (i / 4), els).addToList(BookPageRegistry.ALCHEMY_BOOK);
      }

      BookPageChapter basinChapter = new BookPageChapter(null);
      basinChapter.elements.add(new PageElementTitle("page.alchemy_catalyst.title", 72, 58, 99, 16, 0, true));
      basinChapter.addToList(BookPageRegistry.ALCHEMY_BOOK);

      List<RecipeCatalystBasin> basin = RecipeRegistry.catalystRecipes;
      for (int i = 0; i < basin.size(); i += 2) {
         List<PageElement> els = new ArrayList<>();
         for (int j = 0; j < 2 && i + j < basin.size(); j++) {
            els.add(new PageElementCatalystBasinRecipe(basin.get(i + j), BASIN_POS[j][0], BASIN_POS[j][1], 0));
         }
         new BookPage("alchemy_catalyst_" + (i / 2), els).addToList(BookPageRegistry.ALCHEMY_BOOK);
      }
   }
}
