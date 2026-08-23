package com.paleimitations.schoolsofmagic.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LookingGlassRefresh {
   private LookingGlassRefresh() {}

   public static void rebuild(BlockPos pos) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.levelRenderer == null) {
         return;
      }
      mc.levelRenderer.setSectionDirtyWithNeighbors(
         SectionPos.blockToSectionCoord(pos.getX()),
         SectionPos.blockToSectionCoord(pos.getY()),
         SectionPos.blockToSectionCoord(pos.getZ()));
   }
}
