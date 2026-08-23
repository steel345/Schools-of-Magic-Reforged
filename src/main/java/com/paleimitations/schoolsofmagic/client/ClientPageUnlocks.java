package com.paleimitations.schoolsofmagic.client;

import java.util.HashSet;
import java.util.Set;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientPageUnlocks {
   private static final Set<String> UNLOCKED = new HashSet<>();
   private static final Set<String> UNREAD = new HashSet<>();

   public static void set(Set<String> keys, Set<String> unread) {
      UNLOCKED.clear();
      UNLOCKED.addAll(keys);
      UNREAD.clear();
      UNREAD.addAll(unread);
   }

   public static boolean has(String key) {
      return UNLOCKED.contains(key);
   }

   public static boolean isUnread(String key) {
      return UNREAD.contains(key);
   }

   public static boolean clearUnread(String key) {
      return UNREAD.remove(key);
   }
}
