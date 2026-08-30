package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class EarthenRideSound {
   private static boolean riding;
   private static EarthquakeLoopSound loop;

   private static int ticks;
   private static int max;

   public static void setActive(boolean active, int left, int total) {
      riding = active;
      ticks = left;
      max = total;
      com.paleimitations.schoolsofmagic.common.handlers.EarthenElevatorHandler.setLocalBar(
         active && total > 0 ? (float) left / (float) total : 1.0F);
   }

   @SubscribeEvent
   public static void onClientTick(TickEvent.ClientTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      LocalPlayer player = Minecraft.getInstance().player;
      if (player == null) {
         riding = false;
         return;
      }
      if (!riding) {
         com.paleimitations.schoolsofmagic.common.handlers.EarthenElevatorHandler.setLocalBar(1.0F);
         return;
      }
      if (ticks > 0 && max > 0) {
         ticks--;
         com.paleimitations.schoolsofmagic.common.handlers.EarthenElevatorHandler.setLocalBar((float) ticks / (float) max);
      }
      if (loop == null || loop.isStopped()) {
         loop = new EarthquakeLoopSound(player, () -> riding);
         Minecraft.getInstance().getSoundManager().play(loop);
      }
   }
}
