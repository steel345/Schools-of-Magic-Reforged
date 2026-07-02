package com.paleimitations.schoolsofmagic.common.books;

import net.minecraft.ChatFormatting;

public class PageElementDescription extends PageElement {
   public final String description;
   public final ChatFormatting formatting;

   public PageElementDescription(String description) {
      super(0, 0);
      this.description = description;
      this.formatting = ChatFormatting.GRAY;
   }

   public PageElementDescription(String description, ChatFormatting formatting) {
      super(0, 0);
      this.description = description;
      this.formatting = formatting;
   }
}
