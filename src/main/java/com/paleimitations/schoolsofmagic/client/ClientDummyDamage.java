package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.common.entity.EntityTargetDummy;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

public class ClientDummyDamage {
   public static final long LIFETIME_MS = 2000L;

   public static class Popup {
      public final float damage;
      public final float dps;
      public final float offX;
      public final float offY;
      public final long spawnMs;

      Popup(float damage, float dps, float offX, float offY, long spawnMs) {
         this.damage = damage;
         this.dps = dps;
         this.offX = offX;
         this.offY = offY;
         this.spawnMs = spawnMs;
      }

      public float elapsedSeconds() {
         return (System.currentTimeMillis() - this.spawnMs) / 1000.0F;
      }
   }

   private static final java.util.Map<Integer, List<Popup>> POPUPS = new java.util.HashMap<>();

   public static void receive(int entityId, float damage, float dps, float yaw, float wobble) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.level == null) {
         return;
      }
      Entity e = mc.level.getEntity(entityId);
      if (e instanceof EntityTargetDummy dummy) {
         dummy.hitTicks = 10;
         dummy.hitYaw = yaw;
         dummy.wobbleStrength = wobble;
      }
      if (damage <= 0.0F) {
         return;
      }
      java.util.Random rand = new java.util.Random();
      float offX = -0.85F + rand.nextFloat() * 0.45F;
      float offY = 1.85F + rand.nextFloat() * 0.55F;
      List<Popup> list = POPUPS.computeIfAbsent(entityId, k -> new ArrayList<>());
      list.clear();
      list.add(new Popup(damage, dps, offX, offY, System.currentTimeMillis()));
   }

   public static List<Popup> get(int entityId) {
      List<Popup> list = POPUPS.get(entityId);
      return list == null ? java.util.Collections.emptyList() : list;
   }

   public static void tick() {
      long now = System.currentTimeMillis();
      java.util.Iterator<java.util.Map.Entry<Integer, List<Popup>>> it = POPUPS.entrySet().iterator();
      while (it.hasNext()) {
         java.util.Map.Entry<Integer, List<Popup>> e = it.next();
         e.getValue().removeIf(p -> now - p.spawnMs > LIFETIME_MS);
         if (e.getValue().isEmpty()) {
            it.remove();
         }
      }
   }

   public static void clear() {
      POPUPS.clear();
   }
}
