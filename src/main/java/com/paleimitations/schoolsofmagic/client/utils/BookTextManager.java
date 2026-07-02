package com.paleimitations.schoolsofmagic.client.utils;

import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.books.PageElement;
import com.paleimitations.schoolsofmagic.common.books.PageElementParagraphs;
import com.paleimitations.schoolsofmagic.common.registries.BookPageRegistry;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class BookTextManager implements ResourceManagerReloadListener {
   public static BookTextManager resReloadInstance = new BookTextManager();

   public BookTextManager() {
   }

   @Override
   public void onResourceManagerReload(ResourceManager resourceManager) {
      System.out.println("Book Text reloaded");
      loadText();
   }

   public static void loadText() {
      for (BookPage page : BookPageRegistry.PAGES) {
         for (PageElement element : page.elements) {
            if (element instanceof PageElementParagraphs) {
               ((PageElementParagraphs)element).loadText();
            }
         }
      }
   }

   public static void loadText(BookPage page) {
      for (PageElement element : page.elements) {
         if (element instanceof PageElementParagraphs) {
            ((PageElementParagraphs)element).loadText();
         }
      }
   }
}
