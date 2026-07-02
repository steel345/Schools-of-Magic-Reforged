package com.paleimitations.schoolsofmagic.common.books;

import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;

public class BookPageStandardIllustrated extends BookPage {
   public BookPageStandardIllustrated(String name, ResourceLocation illustrationTexture) {
      super(name, Lists.newArrayList(new PageElement[]{
         new PageElementImage(illustrationTexture, 0, 0, 0, 0, 256, 256, 1.0F, false),
         new PageElementWrappedText(name + "_left", 23, 50, 99, 0.75F, 0),
         new PageElementWrappedText(name + "_right", 134, 50, 99, 0.75F, 0)
      }));
   }
}
