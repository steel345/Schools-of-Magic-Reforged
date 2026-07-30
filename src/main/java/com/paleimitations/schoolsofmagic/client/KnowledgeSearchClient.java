package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.client.guis.GuiStandardBook;
import com.paleimitations.schoolsofmagic.client.guis.podium.GuiPodiumRead;
import com.paleimitations.schoolsofmagic.common.handlers.KnowledgeGather;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class KnowledgeSearchClient {

   // Called on the client when the server returns the nearby books. Hands them to
   // whichever knowledge-book screen is open so it can search against them.
   public static void onCandidates(List<KnowledgeGather.Found> found) {
      Screen s = Minecraft.getInstance().screen;
      if (s instanceof GuiPodiumRead p) {
         p.acceptCandidates(found);
      } else if (s instanceof GuiStandardBook b) {
         b.acceptCandidates(found);
      }
   }
}
