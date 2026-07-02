package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SOMKeyBindings {

   @SubscribeEvent
   public static void onRegister(RegisterKeyMappingsEvent event) {

      event.register(ClientProxy.OPEN_SPELL_RING);
      event.register(ClientProxy.TALISMAN_ACTIVATE);
      event.register(ClientProxy.CHARM_ACTIVATE);
      event.register(ClientProxy.CASTING_TOGGLE);
      event.register(ClientProxy.PHOENIX_DESCEND);
      event.register(ClientProxy.PHOENIX_DISMOUNT);
   }
}
