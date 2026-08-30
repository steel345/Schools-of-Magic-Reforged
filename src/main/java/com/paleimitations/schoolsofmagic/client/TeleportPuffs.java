package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.client.effects.EffectHelper;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

public class TeleportPuffs {
   private static final int END_PUFFS = 14;
   private static final double SPREAD = 0.4D;

   public static void spawn(double x, double y, double z, double tx, double ty, double tz) {
      ClientLevel level = Minecraft.getInstance().level;
      if (level == null) return;

      burst(level, x, y, z);
      burst(level, tx, ty, tz);
   }

   private static void burst(ClientLevel level, double x, double y, double z) {
      for (int i = 0; i < END_PUFFS; i++) {
         double dx = (level.random.nextDouble() - 0.5D) * SPREAD * 2.0D;
         double dz = (level.random.nextDouble() - 0.5D) * SPREAD * 2.0D;
         double dy = level.random.nextDouble() * 1.8D;
         EffectHelper.createPotionPuffParticle(level, x + dx, y + dy, z + dz, Color.GRAY);
      }
   }
}
