package com.paleimitations.schoolsofmagic.client.astral;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralClientSetup {
   @SubscribeEvent
   public static void onRegisterEffects(RegisterDimensionSpecialEffectsEvent event) {
      event.register(new ResourceLocation(SchoolsOfMagic.MODID, "astral_plane_rift"), new AstralSkyEffects());
   }

   @SubscribeEvent
   public static void onRegisterListeners(RegisterClientReloadListenersEvent event) {
      event.registerReloadListener(new AstralScene());
   }
}
