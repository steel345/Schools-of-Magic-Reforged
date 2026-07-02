package com.paleimitations.schoolsofmagic.common.registries;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.books.BookPageChapter;
import com.paleimitations.schoolsofmagic.common.books.BookPageStandardTitled;
import com.paleimitations.schoolsofmagic.common.books.BookPageTableContent;
import com.paleimitations.schoolsofmagic.common.books.PageElement;
import com.paleimitations.schoolsofmagic.common.books.PageElementImage;
import com.paleimitations.schoolsofmagic.common.books.PageElementParagraphs;
import com.paleimitations.schoolsofmagic.common.books.ParagraphBox;
import net.minecraft.resources.ResourceLocation;

public class BookExplorer {

   private static final ResourceLocation COVER = new ResourceLocation("som", "textures/gui/books/images/adventurers_codex_image.png");

   public static void init() {
      new BookPageTableContent(null).addToList(BookPageRegistry.EXPLORER_CODEX);

      new BookPageChapter(null).addToList(BookPageRegistry.EXPLORER_CODEX);
      new BookPage("bce_intro", Lists.newArrayList(new PageElement[]{
         new PageElementImage(COVER, -13, 0, 0, 0, 256, 256, 1.0F, false),
         new PageElementParagraphs("bce_intro", 0.7F, 0, 0,
            new ParagraphBox(23, 98, 0, 99, 88),
            new ParagraphBox(134, 50, 0, 99, 138))
      })).addToList(BookPageRegistry.EXPLORER_CODEX);
      new BookPageStandardTitled("bce_trees").addToList(BookPageRegistry.EXPLORER_CODEX);

      new BookPageChapter(null).addToList(BookPageRegistry.EXPLORER_CODEX);
      new BookPageStandardTitled("bce_zig1").addToList(BookPageRegistry.EXPLORER_CODEX);
      new BookPageStandardTitled("bce_zig2").addToList(BookPageRegistry.EXPLORER_CODEX);
      new BookPageStandardTitled("bce_zig3").addToList(BookPageRegistry.EXPLORER_CODEX);
      new BookPageStandardTitled("bce_zig4").addToList(BookPageRegistry.EXPLORER_CODEX);

      new BookPageChapter(null).addToList(BookPageRegistry.EXPLORER_CODEX);
      new BookPageStandardTitled("bce_fae1").addToList(BookPageRegistry.EXPLORER_CODEX);
      new BookPageStandardTitled("bce_portal").addToList(BookPageRegistry.EXPLORER_CODEX);
      new BookPageStandardTitled("bce_fae2").addToList(BookPageRegistry.EXPLORER_CODEX);
      new BookPage("bce_fae3", Lists.newArrayList(new PageElement[]{
         new PageElementParagraphs("bce_fae3", 0.75F, 0, 2,
            new ParagraphBox(23, 50, 0, 99, 140),
            new ParagraphBox(134, 50, 0, 99, 140),
            new ParagraphBox(23, 50, 1, 99, 140),
            new ParagraphBox(134, 50, 1, 99, 140),
            new ParagraphBox(23, 50, 2, 99, 140),
            new ParagraphBox(134, 50, 2, 99, 140))
      })).addToList(BookPageRegistry.EXPLORER_CODEX);
      new BookPageStandardTitled("bce_fae4").addToList(BookPageRegistry.EXPLORER_CODEX);
      new BookPageStandardTitled("bce_fae5").addToList(BookPageRegistry.EXPLORER_CODEX);
      new BookPageStandardTitled("bce_fae6").addToList(BookPageRegistry.EXPLORER_CODEX);
   }
}
