package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandPersonality;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Bus.FORGE)
public class WandPersonalityHandler {

   @SubscribeEvent
   public static void onSleepFinished(SleepFinishedTimeEvent event) {
      if (!(event.getLevel() instanceof ServerLevel level)) return;
      for (Player player : level.players()) {
         if (player.isSleeping()) {
            WandPersonality.onSlept(player);
         }
      }
   }
}
