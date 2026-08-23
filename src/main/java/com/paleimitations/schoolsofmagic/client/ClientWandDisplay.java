package com.paleimitations.schoolsofmagic.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientWandDisplay {
   private static boolean smallIcons = false;
   private static boolean flatModel = false;

   private ClientWandDisplay() {}

   public static boolean smallIcons() {
      return smallIcons;
   }

   public static boolean flatModel() {
      return flatModel;
   }

   public static void receive(boolean small, boolean flat) {
      boolean changed = small != smallIcons || flat != flatModel;
      smallIcons = small;
      flatModel = flat;
      if (changed) {
         Minecraft mc = Minecraft.getInstance();
         if (mc.levelRenderer != null) {
            mc.levelRenderer.allChanged();
         }
      }
   }
}
