package com.paleimitations.schoolsofmagic.common.books;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;

public final class BookUpdates {
   public static final int REVISION = 2;

   public static final String PREFIX = "upd:";

   private BookUpdates() {}

   public static String key(String pageName) {
      return PREFIX + pageName;
   }

   public static final Map<String, List<String>> CHANGED = Map.of(
      "basic_spellbook", Lists.newArrayList(
         "bmb_wand_looks",
         "bmb_page6",
         "bmb_podium_scribing",
         "bmb_podium_write",
         "bmb_book_editor",
         "bmb_book_editor2",
         "bmb_tree_items"),
      "intermediate_spellbook", Lists.newArrayList(
         "bmi_looking_glass",
         "bmi_magic_mirror"),
      "advanced_spellbook", Lists.newArrayList(
         "bma_title")
   );

   public static List<String> changedIn(String bookItemPath) {
      List<String> pages = CHANGED.get(bookItemPath);
      return pages == null ? List.of() : pages;
   }
}
