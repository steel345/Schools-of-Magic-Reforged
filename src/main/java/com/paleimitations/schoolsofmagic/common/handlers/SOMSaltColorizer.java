package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SOMSaltColorizer {
   private static final int SALT_TINT = 0xF7F2E0;

   @SubscribeEvent
   public static void onBlockColors(RegisterColorHandlersEvent.Block event) {
      event.register((state, level, pos, tintIndex) -> SALT_TINT, BlockRegistry.salt_line.get());
   }
}
