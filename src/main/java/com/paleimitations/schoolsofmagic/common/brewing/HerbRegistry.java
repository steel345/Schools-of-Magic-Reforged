package com.paleimitations.schoolsofmagic.common.brewing;

import static com.paleimitations.schoolsofmagic.common.brewing.HerbProfile.fx;
import static com.paleimitations.schoolsofmagic.common.brewing.HerbProfile.of;

import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import java.util.EnumMap;
import java.util.Map;

/**
 * Data-driven roster of every crushed herb's brewing profile, keyed by {@link EnumPlantType}.
 * The 16 "school" plants map to their in-game herb names (Pyracantha, Yarrow, ...).
 */
public final class HerbRegistry {

   private static final Map<EnumPlantType, HerbProfile> HERBS = new EnumMap<>(EnumPlantType.class);

   static {
      // --- school herbs ---
      put(EnumPlantType.PYROMANCY,    of(fx("fire_resistance"), fx("strength"), fx("flammability"), 1, 0, 2, 0xD65A2E)); // Pyracantha
      put(EnumPlantType.HELIOMANCY,   of(fx("regeneration"), fx("absorption"), fx("mana_regeneration"), 0, 2, 1, 0xF2A51A)); // Marigold
      put(EnumPlantType.AEROMANCY,    of(fx("instant_health"), fx("regeneration", "earth_protection"), fx(), 0, 3, 1, 0xE8DFA3)); // Yarrow
      put(EnumPlantType.GEOMANCY,     of(fx("luck"), fx("hero_of_the_village"), fx("mana_regeneration"), 0, 2, 1, 0x4CAF50)); // Clover
      put(EnumPlantType.ANIMANCY,     of(fx("mana_regeneration"), fx("strength"), fx("bewilderment"), 1, -1, 3, 0x7B5A3A)); // Mandrake
      put(EnumPlantType.ELECTROMANCY, of(fx("speed"), fx("haste"), fx("stunned"), 1, -2, 2, 0x6DA8FF)); // Storm Thistle
      put(EnumPlantType.HYDROMANCY,   of(fx("water_breathing"), fx("dolphins_grace"), fx("mana_regeneration"), 0, 2, 1, 0x7AD7D1)); // Lotus
      put(EnumPlantType.CRYOMANCY,    of(fx("frostbite"), fx("resistance"), fx("slow_falling"), 0, 1, 2, 0xCDEEFF)); // Snowbell
      put(EnumPlantType.HIEROMANCY,   of(fx("sleep"), fx("slow_falling"), fx("haze"), 0, 3, 1, 0xA98AD6)); // Lavender
      put(EnumPlantType.CHAOTICS,     of(fx("earth_protection"), fx("resistance"), fx("health_boost"), 0, 3, 1, 0x9A8F5A)); // Lithop
      put(EnumPlantType.AURAMANCY,    of(fx("regeneration"), fx("saturation"), fx("mana_regeneration"), 0, 2, 1, 0xD88FAF)); // Mallow
      put(EnumPlantType.ASTROMANCY,   of(fx("hallucination"), fx("confusion"), fx("bewilderment", "colorblind"), 2, -3, 2, 0x8E5BB5)); // Jimsonweed
      put(EnumPlantType.INFERNALITY,  of(fx("poison"), fx("wither", "weakling"), fx("demonic_possession"), 4, -3, 3, 0x2F4A24)); // Hemlock
      put(EnumPlantType.SPECTROMANCY, of(fx("obscuration"), fx("night_vision"), fx("fear"), 1, -1, 2, 0x7D8A3A)); // Wormwood
      put(EnumPlantType.UMBRAMANCY,   of(fx("snake_poison"), fx("poison", "obscuration"), fx(), 3, -2, 2, 0x2A163D)); // Nightshade
      put(EnumPlantType.NECROMANCY,   of(fx("undead"), fx("fear", "obscuration"), fx(), 2, -2, 2, 0x3B4A35)); // Gravegrass

      // --- named herbs ---
      put(EnumPlantType.MAYBELL,      of(fx("luck"), fx("regeneration"), fx("mana_regeneration"), 0, 2, 1, 0xFFE680));
      put(EnumPlantType.BRAMBLE,      of(fx("poison_thorn"), fx("resistance", "strength"), fx(), 1, 0, 2, 0x5F7A35));
      put(EnumPlantType.NIGHTBERRY,   of(fx("night_vision"), fx("invisibility", "obscuration"), fx(), 1, -1, 2, 0x24143F)); // Nightberries
      put(EnumPlantType.ROSE,         of(fx("infatuation"), fx("regeneration", "absorption"), fx(), 0, 2, 1, 0xD63A55));
      put(EnumPlantType.BRITTLEBUSH,  of(fx("jump_boost"), fx("slow_falling", "speed"), fx(), 0, -1, 1, 0xB7D978)); // Brittleleaf
      put(EnumPlantType.WHEAT,        of(fx("saturation"), fx("regeneration"), fx("greasy_skin"), 0, 2, 1, 0xD9B45F));
      put(EnumPlantType.MANDRAKE,     of(fx("strength"), fx("health_boost"), fx("mana_regeneration"), 1, 0, 3, 0x6B4A2E)); // Mandrake Root
      put(EnumPlantType.SUNFLOWER,    of(fx("glowing"), fx("fire_resistance"), fx("mana_regeneration"), 0, 2, 1, 0xFFD23A));
      put(EnumPlantType.CATTAIL,      of(fx("water_breathing"), fx("dolphins_grace"), fx("repellent"), 0, 1, 1, 0x8A6F3D));
      put(EnumPlantType.LILAC,        of(fx("infatuation"), fx("luck", "regeneration"), fx(), 0, 2, 1, 0xB88BE0));
      put(EnumPlantType.HYDRANGEA,    of(fx("haze"), fx("water_breathing", "regeneration"), fx(), 0, 1, 1, 0x6A9BE8));
      put(EnumPlantType.CREOSOTE,     of(fx("stench"), fx("fire_resistance", "strength"), fx(), 1, 0, 2, 0x536B2F)); // Creosote Leaves
      put(EnumPlantType.SAGE,         of(fx("mana_regeneration"), fx("night_vision", "luck"), fx(), 0, 3, 1, 0x8BAE6A)); // Sage Leaves
      put(EnumPlantType.FLYTRAP,      of(fx("paralysis"), fx("poison", "hunger"), fx(), 2, -1, 3, 0x6C9A3F)); // Venus Flytrap
      put(EnumPlantType.FIREBERRY,    of(fx("strength"), fx("fire_resistance"), fx("flammability"), 1, -1, 3, 0xE23A22)); // Fireberries
      put(EnumPlantType.CARROT,       of(fx("saturation"), fx("health_boost"), fx("mana_regeneration"), 0, 2, 1, 0xE8872E));
      put(EnumPlantType.PALM,         of(fx("resistance"), fx("water_breathing"), fx("sweaty_hands"), 0, 1, 1, 0x4FA35A)); // Palm Leaves
      put(EnumPlantType.SUGARCANE,    of(fx("speed"), fx("haste"), fx("mana_exhaustion"), 0, 0, 1, 0xB7E37A));
      put(EnumPlantType.BEANSTALK,    of(fx("jump_boost"), fx("health_boost"), fx("slow_falling"), 0, 1, 2, 0x5EBA4D));
      put(EnumPlantType.PEONY,        of(fx("infatuation"), fx("regeneration", "absorption"), fx(), 0, 2, 1, 0xE890C2));
      put(EnumPlantType.OLEANDER,     of(fx("poison"), fx("weakling", "snake_poison"), fx(), 3, -2, 2, 0xC45A8C));
      put(EnumPlantType.BLADDERWORT,  of(fx("puffer_toxin"), fx("water_breathing", "slowness"), fx(), 2, -1, 2, 0xD8C548));
      put(EnumPlantType.GRAVEROOT,    of(fx("undead"), fx("wither", "vulnerability"), fx("fear"), 3, -3, 3, 0x2B221F)); // Grave Root
      put(EnumPlantType.MISTLETOE,    of(fx("luck"), fx("regeneration"), fx("mana_regeneration"), 0, 2, 1, 0xDDE8C0));
   }

   private static void put(EnumPlantType type, HerbProfile profile) {
      HERBS.put(type, profile);
   }

   public static HerbProfile get(EnumPlantType type) {
      return HERBS.get(type);
   }

   public static boolean isHerb(EnumPlantType type) {
      return HERBS.containsKey(type);
   }

   private HerbRegistry() {}
}
