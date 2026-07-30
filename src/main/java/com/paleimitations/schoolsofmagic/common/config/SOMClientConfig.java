package com.paleimitations.schoolsofmagic.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class SOMClientConfig {

   public static final ForgeConfigSpec SPEC;

   public static ForgeConfigSpec.DoubleValue BOOK_TEXT_SCALE;

   static {
      ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();
      b.comment("Schools of Magic - Client / book settings").push("books");

      BOOK_TEXT_SCALE = b.comment("Multiplier applied to book body text. 1.0 is the default size;",
            "larger values reflow longer passages onto extra pages.")
         .defineInRange("book_text_scale", 1.0D, 0.5D, 2.0D);

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
}
