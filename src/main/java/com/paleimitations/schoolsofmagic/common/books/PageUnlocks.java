package com.paleimitations.schoolsofmagic.common.books;

import java.util.Set;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

// Which book pages a player has unlocked, and which of those they have not opened
// yet (so the book can flag them as new). Stored in the player's persistent data (so
// it survives death) and mirrored to the client, because advancements without a
// display are never sent to clients and so cannot gate anything client-side.
public class PageUnlocks {
   public static final String SALT = "salt";
   public static final String COPPER_KEY = "copper_key";
   public static final String THUNDERBIRD = "thunderbird";
   public static final String UNICORN = "unicorn";
   public static final String ENCHANT_BOTTLE = "enchant_bottle";
   public static final String ECLIPSE = "eclipse";

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

   // Records an unlock, flagging it unread. Returns true only the first time, so
   // callers can fire the toast exactly once.
   public static boolean add(ServerPlayer player, String key) {
      Set<String> current = get(player);
      if (!current.add(key)) return false;
      write(player, TAG, current);
      Set<String> unread = getUnread(player);
      unread.add(key);
      write(player, UNREAD_TAG, unread);
      return true;
   }

   // Clears the "new" flag once the player has opened the page.
   public static boolean markRead(ServerPlayer player, String key) {
      Set<String> unread = getUnread(player);
      if (!unread.remove(key)) return false;
      write(player, UNREAD_TAG, unread);
      return true;
   }

   // Unlocks every page. Returns how many were newly unlocked.
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

   // Relocks every page. Returns how many were removed.
   public static int lockAll(ServerPlayer player) {
      int before = get(player).size();
      write(player, TAG, new java.util.HashSet<>());
      write(player, UNREAD_TAG, new java.util.HashSet<>());
      return before;
   }
}
