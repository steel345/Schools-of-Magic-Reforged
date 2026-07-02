package com.paleimitations.schoolsofmagic.common.books;

import com.paleimitations.schoolsofmagic.common.registries.BookPageRegistry;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BookPage {
   public final String name;
   public final List<PageElement> elements;

   public BookPage(String name, List<PageElement> elements) {
      this.name = name;
      this.elements = elements;
      this.addPageToRegistry();
   }

   public void addPageToRegistry() {
      BookPageRegistry.PAGES.add(this);
   }

   @OnlyIn(Dist.CLIENT)
   public void drawPage(GuiGraphics gg, float mouseX, float mouseY, int x, int y, boolean isGUI, int subpage) {
      for (PageElement element : this.elements) {
         if (!element.isTarget(subpage)) continue;
         element.drawElement(gg, mouseX, mouseY, x, y, isGUI, subpage);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public boolean isSubPageBlank(int subpage) {
      for (PageElement element : this.elements) {
         if (element.hasSubpage(subpage)) return false;
      }
      return true;
   }

   public int getSubPages() {
      int i = 0;
      for (PageElement element : this.elements) {
         if (element.subpage > i) i = element.subpage;
      }
      return i + 1;
   }

   public String getName() {
      return this.name;
   }

   public BookPage addToList(List<BookPage> pages) {
      pages.add(this);
      return this;
   }

   public BookPage addElement(PageElement element) {
      this.elements.add(element);
      return this;
   }

   public BookPage addElements(List<PageElement> element) {
      this.elements.addAll(element);
      return this;
   }
}
