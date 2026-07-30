package com.paleimitations.schoolsofmagic.common.registries;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.books.PageElement;

public class BookKnowledge {

   public static void init() {
      new BookPage("book_of_knowledge_home", Lists.newArrayList(new PageElement[]{})).addToList(BookPageRegistry.KNOWLEDGE_BOOK);
   }
}
