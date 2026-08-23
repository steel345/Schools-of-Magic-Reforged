package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;

import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class AdvancementHelper {
   public static void grant(ServerPlayer player, String path, String criterion) {
      Advancement advancement = player.server.getAdvancements()
         .getAdvancement(new ResourceLocation(SchoolsOfMagic.MODID, path));
      if (advancement == null) return;
      if (player.getAdvancements().getOrStartProgress(advancement).isDone()) return;
      player.getAdvancements().award(advancement, criterion);
   }
}
