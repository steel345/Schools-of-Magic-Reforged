package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class OtherPlayerHandler {

   public static final String ALIAS = "Other Player";

   @SubscribeEvent
   public static void onName(PlayerEvent.NameFormat event) {
      if (event.getEntity().hasEffect(PotionRegistry.other_player.get())) {
         event.setDisplayname(Component.literal(ALIAS));
      }
   }
}
