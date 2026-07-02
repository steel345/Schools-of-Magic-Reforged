package com.paleimitations.schoolsofmagic.common.books;

import com.google.common.collect.Lists;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BookPageWriteable extends BookPage {
   private int edit;
   private int selector;
   public boolean selecting = false;

   public BookPageWriteable() {
      super("writeable", Lists.newArrayList());
   }

   public BookPageWriteable(String title, String text) {
      super("writeable", Lists.newArrayList(new PageElement[]{
         new PageElementTitle(title, 72, 58, 99, 16, 0, true),
         new PageElementWriteableParagraphs(text, 0.75F, 0)
      }));
   }

   public void editText(char typedChar, int keyCode) {
      PageElementWriteableParagraphs paragraphs = null;
      for (PageElement element : this.elements) {
         if (element instanceof PageElementWriteableParagraphs) {
            paragraphs = (PageElementWriteableParagraphs)element;
         }
      }
      if (paragraphs != null && this.edit >= 0) {
         String s = paragraphs.text.substring(0, this.edit);
         String end = paragraphs.text.substring(this.edit);

         if (keyCode == 0 && typedChar == 0) {
            String clip = Minecraft.getInstance().keyboardHandler.getClipboard();
            s = s + clip + end;
            this.edit += clip.length();
         } else {
            switch (keyCode) {
               case 14:
                  if (!s.isEmpty()) {
                     s = s.substring(0, s.length() - 1) + end;
                     this.edit--;
                  }
                  break;
               case 28:
               case 156:
                  s = s + '\n' + end;
                  this.edit++;
                  break;
               default:
                  if (SharedConstants.isAllowedChatCharacter(typedChar)) {
                     s = s + typedChar + end;
                     this.edit++;
                  }
            }
         }
         paragraphs.text = s;
      }
   }

   @Override
   @OnlyIn(Dist.CLIENT)
   public void drawPage(GuiGraphics gg, float mouseX, float mouseY, int x, int y, boolean isGUI, int subpage) {
      super.drawPage(gg, mouseX, mouseY, x, y, isGUI, subpage);
   }

   @Override
   public String getName() {
      String s = "writeable";
      for (PageElement element : this.elements) {
         if (element instanceof PageElementTitle) {
            s = s + "<title>" + ((PageElementTitle)element).text;
         }
      }
      for (PageElement element : this.elements) {
         if (element instanceof PageElementWriteableParagraphs) {
            s = s + "<paragraph>" + ((PageElementWriteableParagraphs)element).text;
         }
      }
      return s;
   }

   @Override
   public void addPageToRegistry() {
   }
}
