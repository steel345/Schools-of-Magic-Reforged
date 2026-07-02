package com.paleimitations.schoolsofmagic.common.config;

import com.paleimitations.schoolsofmagic.common.entity.FairyVariant;
import java.util.HashMap;
import java.util.Map;
import net.minecraftforge.common.ForgeConfigSpec;

public final class SOMFairyConfig {

   public static final ForgeConfigSpec SPEC;

   public static ForgeConfigSpec.DoubleValue PERK_RADIUS;
   public static ForgeConfigSpec.IntValue PERK_COOLDOWN;
   public static ForgeConfigSpec.BooleanValue NATURAL_SPAWNING;
   public static final Map<String, ForgeConfigSpec.BooleanValue> PERK_ENABLED = new HashMap<>();

   static {
      ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();
      b.comment("Schools of Magic - Fairy settings").push("fairies");

      PERK_RADIUS = b.comment("Radius in blocks within which fairy perks act on the world/entities.")
         .defineInRange("perk_radius", 6.0D, 2.0D, 24.0D);
      PERK_COOLDOWN = b.comment("Ticks between each fairy perk activation (20 ticks = 1 second).")
         .defineInRange("perk_cooldown", 60, 20, 600);
      NATURAL_SPAWNING = b.comment("Whether fairies may spawn naturally in the Faegrove (spawn eggs always work).")
         .define("natural_spawning", true);

      b.comment("Enable or disable each fairy's perk individually.").push("perks");
      for (FairyVariant v : FairyVariant.values()) {
         PERK_ENABLED.put(v.id, b.comment(v.title + " perk").define(v.id, true));
      }
      b.pop();

      b.pop();
      SPEC = b.build();
   }

   private SOMFairyConfig() {
   }

   public static boolean perkEnabled(FairyVariant v) {
      ForgeConfigSpec.BooleanValue val = PERK_ENABLED.get(v.id);
      return val == null || val.get();
   }
}
