package com.paleimitations.schoolsofmagic.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// Whether the inventory is currently showing the charm/garment slots instead of the
// ordinary armour column. Purely a view toggle; nothing worn is ever cleared.
@OnlyIn(Dist.CLIENT)
public class CharmScreenState {
   private static boolean open = false;

   public static boolean isOpen() {
      return open;
   }

   public static void toggle() {
      open = !open;
   }

   public static void close() {
      open = false;
   }
}
