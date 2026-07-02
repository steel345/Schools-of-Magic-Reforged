package com.paleimitations.schoolsofmagic.common.compat.config;

public class ConfigOre {

   public static final String CATEGORY_NAME_ORES = "ores";

   public static int[] magicGemOreRarity = new int[16];
   public static int[] magicGemOreMinVein = new int[16];
   public static int[] magicGemOreMaxVein = new int[16];

   public static int silverRarity = 8;
   public static int copperRarity = 8;
   public static int silverMinVein = 1;
   public static int copperMinVein = 1;
   public static int silverMaxVein = 8;
   public static int copperMaxVein = 8;

   static {

      for (int i = 0; i < 16; i++) {
         magicGemOreRarity[i] = 2;
         magicGemOreMinVein[i] = 1;
         magicGemOreMaxVein[i] = 8;
      }
   }

   public ConfigOre() {
   }

   public static void syncConfig(boolean loadFromConfigFile, boolean readFieldsFromConfig) {
      try {
         copperRarity = com.paleimitations.schoolsofmagic.common.compat.SOMConfig.ore_copper_rarity.get();
         silverRarity = com.paleimitations.schoolsofmagic.common.compat.SOMConfig.ore_silver_rarity.get();
         int min = com.paleimitations.schoolsofmagic.common.compat.SOMConfig.ore_gem_min_vein.get();
         int max = com.paleimitations.schoolsofmagic.common.compat.SOMConfig.ore_gem_max_vein.get();
         for (int i = 0; i < 16; i++) {
            magicGemOreMinVein[i] = min;
            magicGemOreMaxVein[i] = max;
         }
      } catch (IllegalStateException ignored) {

      }
   }
}
