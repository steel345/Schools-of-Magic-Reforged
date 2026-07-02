package com.paleimitations.schoolsofmagic.common.books;

import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;

public class BookPageStandardTitled extends BookPage {
   public BookPageStandardTitled(String name) {
      super(name, Lists.newArrayList(new PageElement[]{
         new PageElementStandardText("page." + name + ".title", 72, 58, 99, 16, 0, true),
         new PageElementParagraphs(name, 0.75F, 0, 2,
            new ParagraphBox(23, 65, 0, 99, 125),
            new ParagraphBox(134, 50, 0, 99, 140),
            new ParagraphBox(23, 50, 1, 99, 140),
            new ParagraphBox(134, 50, 1, 99, 140),
            new ParagraphBox(23, 50, 2, 99, 140),
            new ParagraphBox(134, 50, 2, 99, 140))
      }));
   }

   public BookPageStandardTitled(String name, ResourceLocation loc) {
      this(name);
   }
}
