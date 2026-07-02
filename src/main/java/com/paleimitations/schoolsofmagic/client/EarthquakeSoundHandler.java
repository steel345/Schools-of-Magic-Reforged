package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellEarthquake;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellTremor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EarthquakeSoundHandler {
   private static EarthquakeLoopSound activeLoop;

   @SubscribeEvent
   public static void onClientTick(TickEvent.ClientTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      LocalPlayer player = Minecraft.getInstance().player;
      if (player == null) return;
      if (isQuakeCasting(player)) {
         if (activeLoop == null || activeLoop.isStopped()) {
            activeLoop = new EarthquakeLoopSound(player, () -> isQuakeCasting(Minecraft.getInstance().player));
            Minecraft.getInstance().getSoundManager().play(activeLoop);
         }
      }
   }

   private static boolean isQuakeCasting(net.minecraft.world.entity.player.Player player) {
      if (player == null) return false;
      IManaData mana = player.getCapability(CapabilityManaData.CAP).orElse(null);
      if (mana == null) return false;
      Spell s = mana.getCurrentSpell();
      return (s instanceof SpellTremor t && t.casting) || (s instanceof SpellEarthquake e && e.casting);
   }
}
