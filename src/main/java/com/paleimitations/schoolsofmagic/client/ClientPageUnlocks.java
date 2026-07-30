package com.paleimitations.schoolsofmagic.client;

import java.util.HashSet;
import java.util.Set;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// The client's mirror of the local player's unlocked pages (and which of those are
// still flagged new), filled by PacketSyncPageUnlocks.
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

   // Clears the flag locally the moment the page is opened, so the marker vanishes
   // immediately and the server is only told once.
   public static boolean clearUnread(String key) {
      return UNREAD.remove(key);
   }
}
