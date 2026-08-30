package com.paleimitations.schoolsofmagic.common.spells.spells;

import com.paleimitations.schoolsofmagic.common.network.PacketGaianWarriorBar;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

// the golem carries its own clock, the caster just gets told how much is left of it
public class GaianWarriorBar {
   private static int ticks;
   private static int max = 1;

   public static void set(Player player, int life) {
      if (player instanceof ServerPlayer server) {
         PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> server),
            new PacketGaianWarriorBar(life, life));
      }
   }

   public static void client(int left, int total) {
      ticks = left;
      max = Math.max(1, total);
   }

   public static void tickClient() {
      if (ticks > 0) ticks--;
   }

   public static float ratio() {
      return ticks <= 0 ? 1.0F : Math.min(1.0F, (float) ticks / (float) max);
   }
}
