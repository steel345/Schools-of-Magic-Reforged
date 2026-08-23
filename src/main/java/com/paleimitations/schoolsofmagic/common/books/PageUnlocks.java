package com.paleimitations.schoolsofmagic.common.books;

import java.util.Set;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PageUnlocks {
   public static final String SALT = "salt";
   public static final String COPPER_KEY = "copper_key";
   public static final String THUNDERBIRD = "thunderbird";
   public static final String UNICORN = "unicorn";
   public static final String ENCHANT_BOTTLE = "enchant_bottle";
   public static final String ECLIPSE = "eclipse";
   public static final String MAGIC_MIRROR = "magic_mirror";

   public static final String[] ALL = {SALT, COPPER_KEY, THUNDERBIRD, UNICORN, ENCHANT_BOTTLE, ECLIPSE};

   private static final String TAG = "som_page_unlocks";
   private static final String UNREAD_TAG = "som_page_unread";

   private static CompoundTag root(Player player) {
      CompoundTag data = player.getPersistentData();
      CompoundTag persisted = data.getCompound(Player.PERSISTED_NBT_TAG);
      if (!data.contains(Player.PERSISTED_NBT_TAG)) data.put(Player.PERSISTED_NBT_TAG, persisted);
      return persisted;
   }

   private static Set<String> read(Player player, String tag) {
      Set<String> out = new java.util.HashSet<>();
      ListTag list = root(player).getList(tag, Tag.TAG_STRING);
      for (int i = 0; i < list.size(); i++) out.add(list.getString(i));
      return out;
   }

   private static void write(Player player, String tag, Set<String> keys) {
      ListTag list = new ListTag();
      for (String s : keys) list.add(StringTag.valueOf(s));
      root(player).put(tag, list);
   }

   public static Set<String> get(Player player) {
      return read(player, TAG);
   }

   public static Set<String> getUnread(Player player) {
      return read(player, UNREAD_TAG);
   }

   public static boolean has(Player player, String key) {
      return get(player).contains(key);
   }

   public static boolean add(ServerPlayer player, String key) {
      Set<String> current = get(player);
      if (!current.add(key)) return false;
      write(player, TAG, current);
      Set<String> unread = getUnread(player);
      unread.add(key);
      write(player, UNREAD_TAG, unread);
      return true;
   }

   public static boolean markRead(ServerPlayer player, String key) {
      Set<String> unread = getUnread(player);
      if (!unread.remove(key)) return false;
      write(player, UNREAD_TAG, unread);
      return true;
   }

   private static final String REVISION_TAG = "som_book_revision";

   public static int getRevision(Player player) {
      return root(player).getInt(REVISION_TAG);
   }

   public static void setRevision(ServerPlayer player, int revision) {
      root(player).putInt(REVISION_TAG, revision);
   }

   public static boolean flagChanged(ServerPlayer player, String pageName) {
      Set<String> unread = getUnread(player);
      if (!unread.add(BookUpdates.key(pageName))) return false;
      write(player, UNREAD_TAG, unread);
      return true;
   }

   public static int unlockAll(ServerPlayer player) {
      Set<String> current = get(player);
      Set<String> unread = getUnread(player);
      int before = current.size();
      for (String key : ALL) {
         if (current.add(key)) unread.add(key);
      }
      write(player, TAG, current);
      write(player, UNREAD_TAG, unread);
      return current.size() - before;
   }

   public static int lockAll(ServerPlayer player) {
      int before = get(player).size();
      write(player, TAG, new java.util.HashSet<>());
      write(player, UNREAD_TAG, new java.util.HashSet<>());
      return before;
   }
}
