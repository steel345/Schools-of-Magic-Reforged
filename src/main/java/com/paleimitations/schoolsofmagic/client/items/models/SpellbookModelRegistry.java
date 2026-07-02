package com.paleimitations.schoolsofmagic.client.items.models;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SpellbookModelRegistry {

   @SubscribeEvent
   public static void onRegisterLoaders(ModelEvent.RegisterGeometryLoaders event) {
      event.register("book", new SpellbookDecorModel.Loader());
   }
}
