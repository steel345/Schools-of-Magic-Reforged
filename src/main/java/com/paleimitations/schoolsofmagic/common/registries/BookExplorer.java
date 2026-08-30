package com.paleimitations.schoolsofmagic.common.registries;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.books.BookPageChapter;
import com.paleimitations.schoolsofmagic.common.books.BookPageStandardTitled;
import com.paleimitations.schoolsofmagic.common.books.BookPageTableContent;
import com.paleimitations.schoolsofmagic.common.books.PageElement;
import com.paleimitations.schoolsofmagic.common.books.PageElementEntity;
import com.paleimitations.schoolsofmagic.common.books.PageElementImage;
import com.paleimitations.schoolsofmagic.common.books.PageElementMultiblock;
import com.paleimitations.schoolsofmagic.common.books.PageElementParagraphs;
import com.paleimitations.schoolsofmagic.common.books.PageElementStandardText;
import com.paleimitations.schoolsofmagic.common.books.ParagraphBox;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BookExplorer {
   private static final ResourceLocation COVER = new ResourceLocation("som", "textures/gui/books/images/adventurers_codex_image.png");

   private static BookPage figurePage(String name, PageElement figure) {
      return new BookPage(name, Lists.newArrayList(new PageElement[]{
         new PageElementStandardText("page." + name + ".title", 72, 58, 99, 16, 0, true),
         new PageElementParagraphs(name, 0.75F, 0, 2,
            new ParagraphBox(23, 65, 0, 99, 125),
            new ParagraphBox(23, 50, 1, 99, 140),
            new ParagraphBox(134, 50, 1, 99, 140),
            new ParagraphBox(23, 50, 2, 99, 140),
            new ParagraphBox(134, 50, 2, 99, 140)),
         figure
      }));
   }

   private static PageElement acolytePortal() {
      java.util.Map<Character, BlockState> legend = com.google.common.collect.Maps.newHashMap();
      legend.put('W', BlockRegistry.wood_ash.get().defaultBlockState());

      String[][] layers = new String[][]{ {"WWWW"}, {"W  W"}, {"W  W"}, {"WWWW"} };
      return new PageElementMultiblock(182, 96, 0, 14.0F, 22.0F, true, true, legend, layers);
   }

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
      new BookPageStandardTitled("bce_extinct").addToList(BookPageRegistry.EXPLORER_CODEX);
      BookPage thunderbird = figurePage("bce_thunderbird",
         new PageElementEntity(EntityRegistry.THUNDER_BIRD.get(), 182, 118, 30, 0));

      BookPageRegistry.PAGES.remove(thunderbird);
      new com.paleimitations.schoolsofmagic.common.books.BookPageLocked(
            "bce_thunderbird", com.paleimitations.schoolsofmagic.common.books.PageUnlocks.THUNDERBIRD, thunderbird.elements)
         .addToList(BookPageRegistry.EXPLORER_CODEX);

      new BookPageChapter(null).addToList(BookPageRegistry.EXPLORER_CODEX);
      new BookPageStandardTitled("bce_zig1").addToList(BookPageRegistry.EXPLORER_CODEX);
      new BookPageStandardTitled("bce_zig2").addToList(BookPageRegistry.EXPLORER_CODEX);
      new BookPageStandardTitled("bce_zig3").addToList(BookPageRegistry.EXPLORER_CODEX);
      new BookPageStandardTitled("bce_zig4").addToList(BookPageRegistry.EXPLORER_CODEX);

      new BookPageChapter(null).addToList(BookPageRegistry.EXPLORER_CODEX);
      new BookPageStandardTitled("bce_fae1").addToList(BookPageRegistry.EXPLORER_CODEX);
      figurePage("bce_portal", acolytePortal()).addToList(BookPageRegistry.EXPLORER_CODEX);
      figurePage("bce_fae2",
         new PageElementEntity(EntityRegistry.FAIRY.get(), 182, 112, 70, 0))
         .addToList(BookPageRegistry.EXPLORER_CODEX);
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
      figurePage("bce_fae5",
         new PageElementEntity(EntityRegistry.FLOWER_FAE.get(), 182, 120, 34, 0))
         .addToList(BookPageRegistry.EXPLORER_CODEX);
      new BookPageStandardTitled("bce_fae6").addToList(BookPageRegistry.EXPLORER_CODEX);
   }
}
