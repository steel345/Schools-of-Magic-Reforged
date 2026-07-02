package com.paleimitations.schoolsofmagic.common.books;

import com.google.common.collect.Lists;
import net.minecraft.world.item.ItemStack;

public class BookPageItemDescription extends BookPage {
   public final ItemStack stack;

   public BookPageItemDescription(final ItemStack stack) {
      super(
         "item_" + descKey(stack),
         Lists.newArrayList(new PageElement[]{
            new PageElementStandardText(stack.getDescriptionId(), 72, 55, 99, 10, 0, true),
            new PageElementItemStack(stack, 64, 62),
            new PageElementParagraphs("item_" + descKey(stack), 0.75F, 0, 1,
               new ParagraphBox(23, 82, 0, 99, 108),
               new ParagraphBox(134, 50, 0, 99, 140),
               new ParagraphBox(23, 50, 1, 99, 140),
               new ParagraphBox(134, 50, 1, 99, 140)),
            new PageElementWorldConnector()
         })
      );
      this.stack = stack;
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
