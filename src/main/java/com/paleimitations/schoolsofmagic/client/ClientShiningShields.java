package com.paleimitations.schoolsofmagic.client;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// What the client knows of everyone's shield rings.
public class ClientShiningShields {

   private static final Map<UUID, Integer> SHIELDS = new HashMap<>();
   private static final Map<UUID, Long> SUMMONED = new HashMap<>();
   private static final Map<UUID, Long> STRUCK = new HashMap<>();

   public static void set(UUID player, int count) {
      int before = get(player);
      long now = now();
      if (count > before) {
         SUMMONED.put(player, now);
      } else if (count < before && count > 0) {
         // One was just broken, so the rest flinch.
         STRUCK.put(player, now);
      }
      if (count <= 0) {
         SHIELDS.remove(player);
         SUMMONED.remove(player);
         STRUCK.remove(player);
      } else {
         SHIELDS.put(player, count);
      }
   }

   private static long now() {
      net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
      return mc.level == null ? 0L : mc.level.getGameTime();
   }

   // Ticks since the ring was called up, or -1 if it never was.
   public static float sinceSummon(UUID player, float partial) {
      Long at = SUMMONED.get(player);
      return at == null ? -1.0F : (now() - at) + partial;
   }

   public static float sinceStruck(UUID player, float partial) {
      Long at = STRUCK.get(player);
      return at == null ? -1.0F : (now() - at) + partial;
   }

   public static int get(UUID player) {
      return SHIELDS.getOrDefault(player, 0);
   }
}
