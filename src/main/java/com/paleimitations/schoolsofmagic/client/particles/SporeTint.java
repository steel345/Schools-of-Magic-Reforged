package com.paleimitations.schoolsofmagic.client.particles;

// the puff only has room for a scale in its arguments, so the seed leaves the colour here for the
// puff it is about to make
public class SporeTint {
   private static float r = 1.0F;
   private static float g = 1.0F;
   private static float b = 1.0F;

   public static void next(float red, float green, float blue) {
      r = red;
      g = green;
      b = blue;
   }

   public static float red() {
      return r;
   }

   public static float green() {
      return g;
   }

   public static float blue() {
      return b;
   }
}
