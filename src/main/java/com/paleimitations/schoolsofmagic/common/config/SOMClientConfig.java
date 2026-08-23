package com.paleimitations.schoolsofmagic.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class SOMClientConfig {
   public static final ForgeConfigSpec SPEC;

   public static ForgeConfigSpec.DoubleValue BOOK_TEXT_SCALE;

   public static ForgeConfigSpec.BooleanValue COLORED_LIGHTING;
   public static ForgeConfigSpec.DoubleValue COLORED_LIGHT_STRENGTH;
   public static ForgeConfigSpec.DoubleValue COLORED_LIGHT_RADIUS;

   static {
      ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();
      b.comment("Schools of Magic - Client / book settings").push("books");

      BOOK_TEXT_SCALE = b.comment("Multiplier applied to book body text. 1.0 is the default size;",
            "larger values reflow longer passages onto extra pages.")
         .defineInRange("book_text_scale", 1.0D, 0.5D, 2.0D);

      b.pop();
      b.comment("Coloured lighting cast by tinted flames.").push("lighting");

      COLORED_LIGHTING = b.comment("Whether coloured flames light the world around them in their own colour.",
            "Turn this off if it misbehaves with your graphics card or another rendering mod.")
         .define("colored_lighting", true);

      COLORED_LIGHT_STRENGTH = b.comment("How strongly a coloured flame lights nearby surfaces.")
         .defineInRange("colored_light_strength", 0.55D, 0.0D, 2.0D);

      COLORED_LIGHT_RADIUS = b.comment("How far, in blocks, a coloured flame reaches.")
         .defineInRange("colored_light_radius", 9.0D, 1.0D, 32.0D);

      b.pop();
      SPEC = b.build();
   }

   private SOMClientConfig() {
   }

   public static float bookTextScale() {
      try {
         return BOOK_TEXT_SCALE.get().floatValue();
      } catch (IllegalStateException notLoaded) {
         return 1.0F;
      }
   }

   public static boolean coloredLighting() {
      try {
         return COLORED_LIGHTING.get();
      } catch (IllegalStateException notLoaded) {
         return true;
      }
   }

   public static float coloredLightStrength() {
      try {
         return COLORED_LIGHT_STRENGTH.get().floatValue();
      } catch (IllegalStateException notLoaded) {
         return 0.55F;
      }
   }

   public static float coloredLightRadius() {
      try {
         return COLORED_LIGHT_RADIUS.get().floatValue();
      } catch (IllegalStateException notLoaded) {
         return 9.0F;
      }
   }
}
