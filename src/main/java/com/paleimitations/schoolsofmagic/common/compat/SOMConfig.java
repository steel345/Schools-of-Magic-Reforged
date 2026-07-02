package com.paleimitations.schoolsofmagic.common.compat;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class SOMConfig {

   public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
   public static final ForgeConfigSpec SPEC;

   public static ForgeConfigSpec.BooleanValue enable_magic_system;
   public static ForgeConfigSpec.BooleanValue enable_dungeons;
   public static ForgeConfigSpec.BooleanValue enable_world_gen;
   public static ForgeConfigSpec.BooleanValue enable_mysterious_application_recipe;
   public static ForgeConfigSpec.IntValue magic_regen_rate;
   public static boolean dry_heads_into_skeleton_skulls = true;
   public static boolean dry_skulls_into_wither_skulls = true;

   public static ForgeConfigSpec.BooleanValue enable_offensive_spells;
   public static ForgeConfigSpec.BooleanValue enable_defensive_spells;
   public static ForgeConfigSpec.IntValue spell_cooldown_ticks;
   public static ForgeConfigSpec.BooleanValue enable_custom_spell_making;
   public static ForgeConfigSpec.IntValue default_ring_spell_slot;
   public static ForgeConfigSpec.BooleanValue ring_override_blocks;
   public static ForgeConfigSpec.BooleanValue ring_override_entities;
   public static ForgeConfigSpec.BooleanValue ring_override_items;

   public static ForgeConfigSpec.ConfigValue<java.util.List<? extends String>> dry_transforms;
   public static ForgeConfigSpec.ConfigValue<java.util.List<? extends String>> fast_forward_transforms;

   public static ForgeConfigSpec.BooleanValue enable_pyromancy;
   public static ForgeConfigSpec.BooleanValue enable_heliomancy;
   public static ForgeConfigSpec.BooleanValue enable_aeromancy;
   public static ForgeConfigSpec.BooleanValue enable_geomancy;
   public static ForgeConfigSpec.BooleanValue enable_animancy;
   public static ForgeConfigSpec.BooleanValue enable_electromancy;
   public static ForgeConfigSpec.BooleanValue enable_hydromancy;
   public static ForgeConfigSpec.BooleanValue enable_cryomancy;
   public static ForgeConfigSpec.BooleanValue enable_hieromancy;
   public static ForgeConfigSpec.BooleanValue enable_chaotics;
   public static ForgeConfigSpec.BooleanValue enable_auramancy;
   public static ForgeConfigSpec.BooleanValue enable_astromancy;
   public static ForgeConfigSpec.BooleanValue enable_infernality;
   public static ForgeConfigSpec.BooleanValue enable_spectromancy;
   public static ForgeConfigSpec.BooleanValue enable_umbramancy;
   public static ForgeConfigSpec.BooleanValue enable_necromancy;

   public static ForgeConfigSpec.IntValue biome_acolyte_woods_weight;
   public static ForgeConfigSpec.IntValue biome_autumn_woods_weight;
   public static ForgeConfigSpec.IntValue biome_vermilion_grove_weight;
   public static ForgeConfigSpec.IntValue biome_sinister_swamp_weight;

   public static ForgeConfigSpec.IntValue ore_copper_rarity;
   public static ForgeConfigSpec.IntValue ore_silver_rarity;
   public static ForgeConfigSpec.IntValue ore_gem_min_vein;
   public static ForgeConfigSpec.IntValue ore_gem_max_vein;

   public static boolean passiveDeadManaHealing = true;
   public static boolean sleepDeadManaHealing = true;
   public static float alarm_cost = 20.0F;
   public static float banish_cost = 18.0F;
   public static float banish_rain_cost = 60.0F;
   public static float banish_thunderstorm_cost = 60.0F;
   public static float barren_tree_cost = 0.5F;
   public static float beacon_bolt_cost = 10.0F;
   public static float become_undead_cost = 25.0F;
   public static float blaze_cost = 8.0F;
   public static float breath_cost = 2.0F;
   public static float bug_repellent_cost = 20.0F;
   public static float command_undead_cost = 90.0F;
   public static float cure_poison_cost = 10.0F;
   public static float deadManaPassiveHealingRate = 18000.0F;
   public static float deadManaRate = 4500.0F;
   public static float deadManaSleepHealingRate = 0.125F;
   public static float dimensional_banish_cost = 40.0F;
   public static float dislocation_cost = 25.0F;
   public static float dry_cost = 6.0F;
   public static float earth_blessing_cost = 10.0F;
   public static float earthquake_cost = 200.0F;
   public static float electrocute_cost = 0.5F;
   public static float empower_undead_cost = 30.0F;
   public static float energize_cost = 20.0F;
   public static float fiery_blessing_cost = 20.0F;
   public static float fire_ring_cost = 12.0F;
   public static float flamability_cost = 9.0F;
   public static float gale_cost = 4.0F;
   public static float gills_cost = 20.0F;
   public static float glowing_cost = 18.0F;
   public static float grow_apple_cost = 0F;
   public static float grow_cactus_cost = 8.0F;
   public static float grow_nightshade_cost = 8.0F;
   public static float growth_cost = 5.0F;
   public static float ice_shell_cost = 12.0F;
   public static float ignite_cost = 6.0F;
   public static float incinerate_cost = 10.0F;
   public static float infatuation_cost = 30.0F;
   public static float invisibility_cost = 30.0F;
   public static float ironhide_cost = 20.0F;
   public static float launch_stone_cost = 10.0F;
   public static float levitate_cost = 0.5F;
   public static float light_axe_cost = 20.0F;
   public static float light_hoe_cost = 12.0F;
   public static float light_pickaxe_cost = 20.0F;
   public static float light_shovel_cost = 12.0F;
   public static float light_sword_cost = 20.0F;
   public static float locate_lava_cost = 0.15F;
   public static float locate_ore_coal_cost = 0.2F;
   public static float locate_ore_copper_cost = 0.2F;
   public static float locate_ore_cost = 0.2F;
   public static float locate_ore_gem_cost = 0.25F;
   public static float locate_ore_gold_cost = 0.2F;
   public static float locate_ore_iron_cost = 0.2F;
   public static float locate_ore_lapis_cost = 0.2F;
   public static float locate_ore_lead_cost = 0.2F;
   public static float locate_ore_redstone_cost = 0.2F;
   public static float locate_ore_silver_cost = 0.2F;
   public static float manaExhaustionThreshold = 0.08F;
   public static float manaXPRate = 0.15F;
   public static float meteor_strike_cost = 50.0F;
   public static float mow_grass_cost = 0.3F;
   public static float mutate_skeleton_cost = 90.0F;
   public static float mutate_zombie_cost = 90.0F;
   public static float part_lava_cost = 30.0F;
   public static float part_water_cost = 10.0F;
   public static float phantom_fire_cost = 2.0F;
   public static float pollen_cloud_cost = 6.0F;
   public static float practice_cost = 8.0F;
   public static float prickly_hide_cost = 20.0F;
   public static float protect_undead_cost = 50.0F;
   public static float quick_sand_cost = 0.5F;
   public static float rabbit_jump_cost = 20.0F;
   public static float raise_skeleton_cost = 80.0F;
   public static float raise_zombie_cost = 80.0F;
   public static float shadow_spy_cost = 50.0F;
   public static float shriek_cost = 12.0F;
   public static float shulker_bullet_cost = 25.0F;
   public static float spectral_hand_cost = 40.0F;
   public static float squid_eyes_cost = 20.0F;
   public static float starterMana = 120.0F;
   public static float suffocate_cost = 0.6F;
   public static float summon_rain_cost = 60.0F;
   public static float summon_thunderstorm_cost = 60.0F;
   public static float switch_location_cost = 30.0F;
   public static float thorn_ring_cost = 10.0F;
   public static float time_warp_cost = 300.0F;
   public static float tremor_cost = 10.0F;
   public static float true_bolt_cost = 20.0F;
   public static float water_jet_cost = 3.0F;
   public static float whispers_cost = 18.0F;
   public static float winter_roar_cost = 9.0F;
   public static float wither_blight_cost = 15.0F;
   public static float wolf_strength_cost = 20.0F;
   public static float zephyr_cost = 4.0F;
   public static int advancedArcanaLevel = 20;
   public static int alarm_minLevel = 4;
   public static int banish_minLevel = 4;
   public static int banish_rain_minLevel = 4;
   public static int banish_thunderstorm_minLevel = 4;
   public static int barren_tree_minLevel = 4;
   public static int beacon_bolt_minLevel = 4;
   public static int become_undead_minLevel = 4;
   public static int blaze_minLevel = 0;
   public static int breath_minLevel = 0;
   public static int bug_repellent_minLevel = 4;
   public static int command_undead_minLevel = 19;
   public static int cure_poison_minLevel = 0;
   public static int dimensional_banish_minLevel = 9;
   public static int dislocation_minLevel = 9;
   public static int dry_minLevel = 0;
   public static int earth_blessing_minLevel = 4;
   public static int earthquake_minLevel = 29;
   public static int electrocute_minLevel = 0;
   public static int empower_undead_minLevel = 4;
   public static int energize_minLevel = 4;
   public static int fiery_blessing_minLevel = 4;
   public static int fire_ring_minLevel = 4;
   public static int flamability_minLevel = 4;
   public static int gale_minLevel = 0;
   public static int gills_minLevel = 0;
   public static int glowing_minLevel = 0;
   public static int grow_apple_minLevel = 0;
   public static int grow_cactus_minLevel = 4;
   public static int grow_nightshade_minLevel = 0;
   public static int growth_minLevel = 0;
   public static int ice_shell_minLevel = 4;
   public static int ignite_minLevel = 0;
   public static int incinerate_minLevel = 4;
   public static int infatuation_minLevel = 4;
   public static int intermediateArcanaLevel = 5;
   public static int invisibility_minLevel = 9;
   public static int ironhide_minLevel = 4;
   public static int launch_stone_minLevel = 0;
   public static int levitate_minLevel = 9;
   public static int light_axe_minLevel = 4;
   public static int light_hoe_minLevel = 4;
   public static int light_pickaxe_minLevel = 4;
   public static int light_shovel_minLevel = 4;
   public static int light_sword_minLevel = 4;
   public static int locate_lava_minLevel = 0;
   public static int locate_ore_coal_minLevel = 0;
   public static int locate_ore_copper_minLevel = 0;
   public static int locate_ore_gem_minLevel = 9;
   public static int locate_ore_gold_minLevel = 0;
   public static int locate_ore_iron_minLevel = 0;
   public static int locate_ore_lapis_minLevel = 0;
   public static int locate_ore_lead_minLevel = 0;
   public static int locate_ore_minLevel = 4;
   public static int locate_ore_redstone_minLevel = 0;
   public static int locate_ore_silver_minLevel = 0;
   public static int manaExhaustionChance = 100;
   public static int maxLevel = 300;
   public static int meteor_strike_minLevel = 9;
   public static int mow_grass_minLevel = 0;
   public static int mutate_skeleton_minLevel = 14;
   public static int mutate_zombie_minLevel = 14;
   public static int part_lava_minLevel = 4;
   public static int part_water_minLevel = 4;
   public static int phantom_fire_minLevel = 0;
   public static int pollen_cloud_minLevel = 0;
   public static int practice_minLevel = 0;
   public static int prickly_hide_minLevel = 4;
   public static int protect_undead_minLevel = 14;
   public static int quick_sand_minLevel = 0;
   public static int rabbit_jump_minLevel = 4;
   public static int raise_skeleton_minLevel = 9;
   public static int raise_zombie_minLevel = 9;
   public static int shadow_spy_minLevel = 14;
   public static int shriek_minLevel = 4;
   public static int shulker_bullet_minLevel = 4;
   public static int spectral_hand_minLevel = 11;
   public static int squid_eyes_minLevel = 4;
   public static int suffocate_minLevel = 0;
   public static int summon_rain_minLevel = 4;
   public static int summon_thunderstorm_minLevel = 4;
   public static int switch_location_minLevel = 14;
   public static int thorn_ring_minLevel = 9;
   public static int time_warp_minLevel = 0;
   public static int tremor_minLevel = 0;
   public static int true_bolt_minLevel = 9;
   public static int water_jet_minLevel = 0;
   public static int whispers_minLevel = 4;
   public static int winter_roar_minLevel = 0;
   public static int wither_blight_minLevel = 9;
   public static int wolf_strength_minLevel = 4;
   public static int zephyr_minLevel = 4;

   static {
      BUILDER.comment("School of Magic - General settings").push("general");
      enable_magic_system = BUILDER.comment("Master toggle for the magic system").define("enable_magic_system", true);
      enable_dungeons = BUILDER.comment("Enable SOM dungeon generation").define("enable_dungeons", true);
      enable_world_gen = BUILDER.comment("Enable SOM world generation").define("enable_world_gen", true);
      enable_mysterious_application_recipe = BUILDER.comment("Enable the Mysterious Application crafting recipe").define("enable_mysterious_application_recipe", true);
      magic_regen_rate = BUILDER.comment("Ticks between mana regeneration ticks").defineInRange("magic_regen_rate", 20, 1, 200);
      BUILDER.pop();

      BUILDER.comment("Spell tuning").push("spells");
      enable_offensive_spells = BUILDER.define("enable_offensive_spells", true);
      enable_defensive_spells = BUILDER.define("enable_defensive_spells", true);
      spell_cooldown_ticks = BUILDER.defineInRange("spell_cooldown_ticks", 40, 0, 1200);
      enable_custom_spell_making = BUILDER.comment("Allow players to create custom spells").define("enable_custom_spell_making", true);
      default_ring_spell_slot = BUILDER.comment("Default hotbar slot (1-9) assigned as the Apprentice Ring spell slot").defineInRange("default_ring_spell_slot", 1, 1, 9);
      ring_override_blocks = BUILDER.comment("Apprentice Ring casting overrides right-clicking blocks").define("ring_override_blocks", true);
      ring_override_entities = BUILDER.comment("Apprentice Ring casting overrides right-clicking entities").define("ring_override_entities", true);
      ring_override_items = BUILDER.comment("Apprentice Ring casting overrides right-clicking items and empty air").define("ring_override_items", true);
      BUILDER.pop();

      BUILDER.comment("Per-school enable toggles").push("schools");
      enable_pyromancy = BUILDER.define("enable_pyromancy", true);
      enable_heliomancy = BUILDER.define("enable_heliomancy", true);
      enable_aeromancy = BUILDER.define("enable_aeromancy", true);
      enable_geomancy = BUILDER.define("enable_geomancy", true);
      enable_animancy = BUILDER.define("enable_animancy", true);
      enable_electromancy = BUILDER.define("enable_electromancy", true);
      enable_hydromancy = BUILDER.define("enable_hydromancy", true);
      enable_cryomancy = BUILDER.define("enable_cryomancy", true);
      enable_hieromancy = BUILDER.define("enable_hieromancy", true);
      enable_chaotics = BUILDER.define("enable_chaotics", true);
      enable_auramancy = BUILDER.define("enable_auramancy", true);
      enable_astromancy = BUILDER.define("enable_astromancy", true);
      enable_infernality = BUILDER.define("enable_infernality", true);
      enable_spectromancy = BUILDER.define("enable_spectromancy", true);
      enable_umbramancy = BUILDER.define("enable_umbramancy", true);
      enable_necromancy = BUILDER.define("enable_necromancy", true);
      BUILDER.pop();

      BUILDER.comment("Biome generation weights").push("biomes");
      biome_acolyte_woods_weight = BUILDER.defineInRange("acolyte_woods_weight", 6, 0, 30);
      biome_autumn_woods_weight = BUILDER.defineInRange("autumn_woods_weight", 6, 0, 30);
      biome_vermilion_grove_weight = BUILDER.defineInRange("vermilion_grove_weight", 5, 0, 30);
      biome_sinister_swamp_weight = BUILDER.defineInRange("sinister_swamp_weight", 4, 0, 30);
      BUILDER.pop();

      BUILDER.comment("Ore generation").push("ores");
      ore_copper_rarity = BUILDER.defineInRange("copper_rarity", 8, 0, 30);
      ore_silver_rarity = BUILDER.defineInRange("silver_rarity", 8, 0, 30);
      ore_gem_min_vein = BUILDER.defineInRange("gem_min_vein", 1, 0, 5);
      ore_gem_max_vein = BUILDER.defineInRange("gem_max_vein", 8, 0, 20);
      BUILDER.pop();

      BUILDER.comment("Spell block transforms. Each entry is \"source_block>result_block\" using namespaced block ids.").push("spell_transforms");
      dry_transforms = BUILDER
         .comment("Blocks the Sun Dry spell converts when cast on them, and what they become.")
         .defineListAllowEmpty(java.util.List.of("dry_transforms"),
            () -> java.util.Arrays.asList(
               "minecraft:mycelium>minecraft:dirt",
               "minecraft:podzol>minecraft:dirt",
               "minecraft:dirt_path>minecraft:dirt"),
            o -> o instanceof String && ((String) o).contains(">"));
      fast_forward_transforms = BUILDER
         .comment("Extra block conversions the Fast Forward spell applies (in addition to copper weathering, ice/snow melting and plank rotting).")
         .defineListAllowEmpty(java.util.List.of("fast_forward_transforms"),
            () -> java.util.Arrays.asList(
               "minecraft:cobblestone>minecraft:mossy_cobblestone",
               "minecraft:stone_bricks>minecraft:mossy_stone_bricks"),
            o -> o instanceof String && ((String) o).contains(">"));
      BUILDER.pop();

      SPEC = BUILDER.build();
   }

   private static java.util.Map<net.minecraft.world.level.block.Block, net.minecraft.world.level.block.Block> dryMap;
   private static java.util.Map<net.minecraft.world.level.block.Block, net.minecraft.world.level.block.Block> fastForwardMap;

   public static net.minecraft.world.level.block.Block getDryResult(net.minecraft.world.level.block.Block source) {
      if (!SPEC.isLoaded()) {
         return null;
      }
      if (dryMap == null) {
         dryMap = parseTransforms(dry_transforms.get());
      }
      return dryMap.get(source);
   }

   public static boolean dryHeadsToSkulls() {
      return dry_heads_into_skeleton_skulls;
   }

   public static boolean drySkullsToWither() {
      return dry_skulls_into_wither_skulls;
   }

   public static net.minecraft.world.level.block.Block getFastForwardResult(net.minecraft.world.level.block.Block source) {
      if (!SPEC.isLoaded()) {
         return null;
      }
      if (fastForwardMap == null) {
         fastForwardMap = parseTransforms(fast_forward_transforms.get());
      }
      return fastForwardMap.get(source);
   }

   private static java.util.Map<net.minecraft.world.level.block.Block, net.minecraft.world.level.block.Block> parseTransforms(java.util.List<? extends String> entries) {
      java.util.Map<net.minecraft.world.level.block.Block, net.minecraft.world.level.block.Block> map = new java.util.HashMap<>();
      for (String entry : entries) {
         String[] parts = entry.split(">");
         if (parts.length != 2) {
            continue;
         }
         net.minecraft.resources.ResourceLocation srcId = net.minecraft.resources.ResourceLocation.tryParse(parts[0].trim());
         net.minecraft.resources.ResourceLocation resId = net.minecraft.resources.ResourceLocation.tryParse(parts[1].trim());
         if (srcId == null || resId == null
               || !net.minecraftforge.registries.ForgeRegistries.BLOCKS.containsKey(srcId)
               || !net.minecraftforge.registries.ForgeRegistries.BLOCKS.containsKey(resId)) {
            continue;
         }
         map.put(net.minecraftforge.registries.ForgeRegistries.BLOCKS.getValue(srcId),
            net.minecraftforge.registries.ForgeRegistries.BLOCKS.getValue(resId));
      }
      return map;
   }

   public static void register(ModLoadingContext ctx) {
      ctx.registerConfig(ModConfig.Type.COMMON, SPEC, "som-common.toml");
   }

   public static void clientPreInit() {
   }
}
