package com.paleimitations.schoolsofmagic.common.books;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.items.capabilities.IBookPageHolder;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.Book;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;

public class BookPageTableContent extends BookPage {
   public IBookPageHolder pageHolder;

   public BookPageTableContent(IBookPageHolder pageHolder) {
      super("table_content", Lists.newArrayList());
      this.pageHolder = pageHolder;
   }

   @Override
   public void addPageToRegistry() {
   }

   public void buildTableContent() {
      if (this.pageHolder instanceof IBook || this.pageHolder instanceof Book) {
         IBook book = (IBook)this.pageHolder;
         this.elements.clear();
         this.elements.add(new PageElementString("page.table_content.element", 72, 58, 99, 16, 0, true));

         for (int i = 0; i < book.getChapters().size(); i++) {
            BookPageChapter chapter = book.getChapters().get(i);
            String[] title = new String[]{"page.chapter.element", String.valueOf(chapter.chapterNumber)};
            for (PageElement element : chapter.elements) {
               if (element instanceof PageElementTitle) {
                  title = ((PageElementTitle)element).text;
               }
            }
            int segment = 7;
            int xi = i / segment % 2 == 0 ? 23 : 134;
            int yi = 65 + i % segment * 18;
            int targeti = i / (segment * 2);
            PageElementChapterEntry entry = new PageElementChapterEntry(title, "", chapter.page, xi, yi, targeti, 99, 8);

            java.util.List<BookPage> inChapter = Lists.newArrayList();
            for (int p = chapter.page + 1; p < book.getBookPages().size(); p++) {
               BookPage bp = book.getBookPage(p);
               if (bp instanceof BookPageChapter) break;
               inChapter.add(bp);
            }
            entry.chapterPages = inChapter;
            this.elements.add(entry);
         }
      }
   }
}
