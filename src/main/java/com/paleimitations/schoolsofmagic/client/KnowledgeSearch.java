package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.books.PageElement;
import com.paleimitations.schoolsofmagic.common.books.PageElementParagraphs;
import com.paleimitations.schoolsofmagic.common.books.PageElementStandardText;
import com.paleimitations.schoolsofmagic.common.books.PageElementString;
import com.paleimitations.schoolsofmagic.common.items.ItemBookBase;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.page.CapabilityPage;
import com.paleimitations.schoolsofmagic.common.items.capabilities.page.IPage;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@OnlyIn(Dist.CLIENT)
public class KnowledgeSearch {
   public static final int RADIUS = 60;

   public static class Hit {
      public final ItemStack source;
      public final String title;
      public final String snippet;
      public final net.minecraft.core.BlockPos shelf;
      public final int slot;
      public final int pageIndex;

      public Hit(ItemStack source, String title, String snippet, net.minecraft.core.BlockPos shelf, int slot) {
         this(source, title, snippet, shelf, slot, -1);
      }

      public Hit(ItemStack source, String title, String snippet, net.minecraft.core.BlockPos shelf, int slot, int pageIndex) {
         this.source = source;
         this.title = title;
         this.snippet = snippet;
         this.shelf = shelf;
         this.slot = slot;
         this.pageIndex = pageIndex;
      }
   }

   public static List<Hit> searchBook(com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook book, String query) {
      List<Hit> hits = new ArrayList<>();
      if (book == null || query == null) return hits;
      String q = query.trim().toLowerCase(Locale.ROOT);
      if (q.isEmpty()) return hits;
      List<com.paleimitations.schoolsofmagic.common.books.BookPage> pages = book.getBookPages();
      for (int i = 0; i < pages.size(); i++) {
         com.paleimitations.schoolsofmagic.common.books.BookPage page = pages.get(i);
         if (isPageHidden(page)) continue;
         String title = pageTitle(page);
         List<String> texts = new ArrayList<>();
         if (!title.isEmpty()) texts.add(title);
         collectPageText(page, texts);
         for (String t : texts) {
            if (t == null || t.isEmpty()) continue;
            int idx = t.toLowerCase(Locale.ROOT).indexOf(q);
            if (idx >= 0) {
               hits.add(new Hit(ItemStack.EMPTY, title.isEmpty() ? "Page " + (i + 1) : title,
                  sentence(t, idx, q.length()), null, -1, i));
               break;
            }
         }
      }
      return hits;
   }

   public static boolean isPageHidden(com.paleimitations.schoolsofmagic.common.books.BookPage page) {
      return page instanceof com.paleimitations.schoolsofmagic.common.books.BookPageLocked bl && bl.isContentHidden();
   }

   public static String pageTitle(com.paleimitations.schoolsofmagic.common.books.BookPage page) {
      if (page == null || page.elements == null) return "";
      for (PageElement el : page.elements) {
         if (el instanceof com.paleimitations.schoolsofmagic.common.books.PageElementTitle t
               && t.text != null && t.text.length > 0) {
            return I18n.get(t.text[0]);
         }
         if (el instanceof PageElementStandardText st) {
            return I18n.get(st.textLocation);
         }
         if (el instanceof PageElementString s && s.text != null && s.text.length > 0) {
            return I18n.get(s.text[0]);
         }
      }
      return "";
   }

   public static boolean isWorkstationRenderable(ItemStack st) {
      if (st == null || st.isEmpty()) return false;
      if (st.getCapability(CapabilityBook.BOOK_CAPABILITY).isPresent()) return true;
      if (st.getCapability(CapabilityPage.PAGE_CAPABILITY).isPresent()) return true;
      if (st.getCapability(com.paleimitations.schoolsofmagic.common.items.capabilities.spell_modifier.CapabilitySpellModifier.SPELL_MODIFIER_CAPABILITY).isPresent()) return true;
      if (st.getCapability(com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.CapabilitySpellNotes.SPELL_NOTES_CAPABILITY).isPresent()) return true;
      if (st.getItem() == Items.WRITTEN_BOOK) return true;
      return st.hasTag() && st.getTag() != null && st.getTag().contains("pages");
   }

   public static List<Hit> matchAll(List<com.paleimitations.schoolsofmagic.common.handlers.KnowledgeGather.Found> found, String query) {
      List<Hit> hits = new ArrayList<>();
      if (found == null || query == null) return hits;
      String q = query.trim().toLowerCase(Locale.ROOT);
      if (q.isEmpty()) return hits;
      for (com.paleimitations.schoolsofmagic.common.handlers.KnowledgeGather.Found f : found) {
         if (f == null || f.stack == null || f.stack.isEmpty()) continue;
         Hit h = matchItem(f.stack, q, f.shelf, f.slot);
         if (h != null) hits.add(h);
      }
      return hits;
   }

   private static Hit matchItem(ItemStack st, String q, net.minecraft.core.BlockPos shelf, int slot) {
      String title = st.getHoverName().getString();
      List<String> texts = new ArrayList<>();
      texts.add(title);

      if (st.getItem() == Items.WRITTEN_BOOK || st.getItem() == Items.WRITABLE_BOOK) {
         addVanillaBookText(st, texts);
      }
      IBook book = st.getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
      IPage page = st.getCapability(CapabilityPage.PAGE_CAPABILITY).orElse(null);
      if (book != null) {
         try { ItemBookBase.ensureInitialized(st); } catch (Throwable ignored) {}
         for (BookPage bp : book.getBookPages()) collectPageText(bp, texts);
      } else if (page != null && page.getBookPage() != null) {
         collectPageText(page.getBookPage(), texts);
      }

      for (String t : texts) {
         if (t == null || t.isEmpty()) continue;
         int idx = t.toLowerCase(Locale.ROOT).indexOf(q);
         if (idx >= 0) {
            return new Hit(st.copy(), title, sentence(t, idx, q.length()), shelf, slot);
         }
      }
      return null;
   }

   private static void addVanillaBookText(ItemStack st, List<String> out) {
      CompoundTag tag = st.getTag();
      if (tag == null) return;
      if (tag.contains("title")) out.add(tag.getString("title"));
      if (tag.contains("author")) out.add(tag.getString("author"));
      if (tag.contains("pages", Tag.TAG_LIST)) {
         ListTag pages = tag.getList("pages", Tag.TAG_STRING);
         boolean written = st.getItem() == Items.WRITTEN_BOOK;
         for (int i = 0; i < pages.size(); i++) {
            String raw = pages.getString(i);
            if (written) {
               try {
                  Component c = Component.Serializer.fromJson(raw);
                  out.add(c != null ? c.getString() : raw);
               } catch (Exception e) {
                  out.add(raw);
               }
            } else {
               out.add(raw);
            }
         }
      }
   }

   private static void collectPageText(BookPage bp, List<String> out) {
      if (bp == null || bp.elements == null) return;
      for (PageElement el : bp.elements) {
         if (el instanceof PageElementParagraphs p) {
            try { out.addAll(p.getSearchLines()); } catch (Throwable ignored) {}
         } else if (el instanceof PageElementStandardText t) {
            out.add(I18n.get(t.textLocation));
         } else if (el instanceof PageElementString s) {
            if (s.text != null) {
               for (String line : s.text) {
                  out.add(line);
                  out.add(I18n.get(line));
               }
            }
         }
      }
   }

   private static String sentence(String text, int matchIdx, int matchLen) {
      int start = 0;
      int end = text.length();
      for (int i = matchIdx; i > 0; i--) {
         char c = text.charAt(i - 1);
         if (c == '.' || c == '!' || c == '?' || c == '\n') { start = i; break; }
      }
      for (int i = matchIdx + matchLen; i < text.length(); i++) {
         char c = text.charAt(i);
         if (c == '.' || c == '!' || c == '?' || c == '\n') { end = i + 1; break; }
      }
      String s = text.substring(start, end).trim();
      s = s.replaceAll("(?i)\\u00A7[0-9A-FK-OR]", "");
      return s;
   }
}
