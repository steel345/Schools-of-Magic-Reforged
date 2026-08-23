package com.paleimitations.schoolsofmagic.common.books.editor;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public class BookPageLayout {
   public enum Kind {
      TEXT,
      TITLE,
      ITEM,
      SLOT,
      TEMPLATE,

      ENTITY;

      public static Kind byName(String name) {
         for (Kind k : values()) {
            if (k.name().equalsIgnoreCase(name)) return k;
         }
         return TEXT;
      }
   }

   public static class Element {
      public Kind kind = Kind.TEXT;
      public int x;
      public int y;
      public int w;
      public int h;

      public String value = "";
      public float scale = 1.0F;

      public int color = 0;

      public final List<String> slots = new ArrayList<>();

      public Element() {}

      public static Element of(Kind kind, int x, int y) {
         Element e = new Element();
         e.kind = kind;
         e.x = x;
         e.y = y;
         switch (kind) {
            case TITLE -> { e.w = 99; e.h = 16; e.value = "Title"; }
            case TEXT -> { e.w = 99; e.h = 115; e.value = "Text"; }
            case ITEM, ENTITY -> { e.w = 16; e.h = 16; }
            case SLOT -> { e.w = 18; e.h = 18; }
            case TEMPLATE -> { e.w = 54; e.h = 76; }
         }
         return e;
      }

      public static Element atHome(Kind kind) {
         return switch (kind) {
            case TITLE -> of(kind, 72, 58);
            case TEXT -> of(kind, 23, 75);
            default -> of(kind, 60, 90);
         };
      }

      public CompoundTag save() {
         CompoundTag tag = new CompoundTag();
         tag.putString("Kind", this.kind.name());
         tag.putInt("X", this.x);
         tag.putInt("Y", this.y);
         tag.putInt("W", this.w);
         tag.putInt("H", this.h);
         tag.putString("Value", this.value);
         tag.putFloat("Scale", this.scale);
         tag.putInt("Color", this.color);
         ListTag filled = new ListTag();
         for (String s : this.slots) {
            CompoundTag entry = new CompoundTag();
            entry.putString("Id", s == null ? "" : s);
            filled.add(entry);
         }
         tag.put("Slots", filled);
         return tag;
      }

      public static Element load(CompoundTag tag) {
         Element e = new Element();
         e.kind = Kind.byName(tag.getString("Kind"));
         e.x = tag.getInt("X");
         e.y = tag.getInt("Y");
         e.w = tag.getInt("W");
         e.h = tag.getInt("H");
         e.value = tag.getString("Value");
         e.scale = tag.contains("Scale") ? tag.getFloat("Scale") : 1.0F;
         e.color = tag.getInt("Color");
         ListTag filled = tag.getList("Slots", Tag.TAG_COMPOUND);
         for (int i = 0; i < filled.size(); i++) {
            e.slots.add(filled.getCompound(i).getString("Id"));
         }
         return e;
      }

      public Element copy() {
         return load(save());
      }
   }

   public final List<Element> elements = new ArrayList<>();

   public boolean finished;

   public boolean isEmpty() {
      return this.elements.isEmpty() && !this.finished;
   }

   public BookPageLayout copy() {
      return load(save());
   }

   public CompoundTag save() {
      CompoundTag tag = new CompoundTag();
      ListTag list = new ListTag();
      for (Element e : this.elements) {
         list.add(e.save());
      }
      tag.put("Elements", list);
      tag.putBoolean("Finished", this.finished);
      return tag;
   }

   public static BookPageLayout load(CompoundTag tag) {
      BookPageLayout layout = new BookPageLayout();
      ListTag list = tag.getList("Elements", Tag.TAG_COMPOUND);
      for (int i = 0; i < list.size(); i++) {
         layout.elements.add(Element.load(list.getCompound(i)));
      }
      layout.finished = tag.getBoolean("Finished");
      return layout;
   }
}
