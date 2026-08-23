package com.paleimitations.schoolsofmagic.common.books;

import net.minecraft.nbt.CompoundTag;

public class BookTextOverride {
   public String title;
   public String body;
   public String originalTitle;
   public String originalBody;

   public BookTextOverride() {
      this("", "", "", "");
   }

   public BookTextOverride(String title, String body, String originalTitle, String originalBody) {
      this.title = title == null ? "" : title;
      this.body = body == null ? "" : body;
      this.originalTitle = originalTitle == null ? "" : originalTitle;
      this.originalBody = originalBody == null ? "" : originalBody;
   }

   public boolean stillMatches(String currentTitle, String currentBody) {
      return this.originalTitle.equals(currentTitle == null ? "" : currentTitle)
         && this.originalBody.equals(currentBody == null ? "" : currentBody);
   }

   public CompoundTag save() {
      CompoundTag tag = new CompoundTag();
      tag.putString("Title", this.title);
      tag.putString("Body", this.body);
      tag.putString("OrigTitle", this.originalTitle);
      tag.putString("OrigBody", this.originalBody);
      return tag;
   }

   public static BookTextOverride load(CompoundTag tag) {
      return new BookTextOverride(tag.getString("Title"), tag.getString("Body"),
         tag.getString("OrigTitle"), tag.getString("OrigBody"));
   }

   private static BookTextOverride active;
   private static BookTextOverride preview;

   public static void setPreview(BookTextOverride typing) {
      preview = typing;
   }

   private static boolean titleTaken;
   private static boolean bodyTaken;

   public static void beginPage(BookTextOverride override) {
      active = preview != null ? preview : override;
      titleTaken = false;
      bodyTaken = false;
   }

   public static void endPage() {
      active = null;
      titleTaken = false;
      bodyTaken = false;
   }

   public static String titleOr(String fallback) {
      if (active == null || titleTaken || active.title.isEmpty()) {
         return fallback;
      }
      titleTaken = true;
      return active.title;
   }

   public static String bodyOr(String fallback) {
      if (active == null || bodyTaken || active.body.isEmpty()) {
         return fallback;
      }
      bodyTaken = true;
      return active.body;
   }

   public static boolean hasActive() {
      return active != null;
   }
}
