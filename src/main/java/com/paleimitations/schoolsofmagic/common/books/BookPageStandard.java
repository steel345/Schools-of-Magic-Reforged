package com.paleimitations.schoolsofmagic.common.books;

import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;

public class BookPageStandard extends BookPage {
   public BookPageStandard(String name, ResourceLocation pageTexture) {
      super(name, Lists.newArrayList(new PageElement[]{
         new PageElementWrappedText(name + "_left", 23, 50, 99, 0.75F, 0),
         new PageElementWrappedText(name + "_right", 134, 50, 99, 0.75F, 0)
      }));
   }

   public BookPageStandard(String name) {
      super(name, Lists.newArrayList(new PageElement[]{
         new PageElementWrappedText(name + "_left", 23, 50, 99, 0.75F, 0),
         new PageElementWrappedText(name + "_right", 134, 50, 99, 0.75F, 0)
      }));
   }
}
