package com.paleimitations.schoolsofmagic.common.compat.config;

public class ConfigBiome {

   public static final String CATEGORY_NAME_BIOMES = "biomes";

   public static int acolyteWoods_rarity = 6;
   public static int autumnWoods_rarity = 6;
   public static int bandedDesert_rarity = 4;
   public static int desertCanyons_rarity = 4;
   public static int desertUplands_rarity = 4;
   public static int labyrinth_rarity = 3;
   public static int mountainousJungle_rarity = 4;
   public static int sinisterSwamp_rarity = 4;
   public static int strangeHills_rarity = 4;
   public static int vermilionGrove_rarity = 5;
   public static int wetlands_rarity = 3;

   public ConfigBiome() {
   }

   public static void syncConfig(boolean loadFromConfigFile, boolean readFieldsFromConfig) {

      try {
         acolyteWoods_rarity = com.paleimitations.schoolsofmagic.common.compat.SOMConfig.biome_acolyte_woods_weight.get();
         autumnWoods_rarity = com.paleimitations.schoolsofmagic.common.compat.SOMConfig.biome_autumn_woods_weight.get();
         vermilionGrove_rarity = com.paleimitations.schoolsofmagic.common.compat.SOMConfig.biome_vermilion_grove_weight.get();
         sinisterSwamp_rarity = com.paleimitations.schoolsofmagic.common.compat.SOMConfig.biome_sinister_swamp_weight.get();
      } catch (IllegalStateException ignored) {

      }

   }
}
