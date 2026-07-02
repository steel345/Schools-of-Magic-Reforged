package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Bus.FORGE)
public class WandSoulboundHandler {

   private static final String KEY = "som_soulbound";

   @SubscribeEvent
   public static void onClone(PlayerEvent.Clone event) {
      Player np = event.getEntity();
      CompoundTag persisted = np.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
      if (persisted.contains(KEY)) {
         persisted.remove(KEY);
         np.getPersistentData().put(Player.PERSISTED_NBT_TAG, persisted);
      }
   }
}
