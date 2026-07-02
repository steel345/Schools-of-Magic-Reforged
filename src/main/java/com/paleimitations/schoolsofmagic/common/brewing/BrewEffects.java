package com.paleimitations.schoolsofmagic.common.brewing;

import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

/**
 * Central data-driven mapping of brewing effect keys to actual {@link MobEffect}s, plus
 * the supporting tables (powerful effects, negative side effects, corruption). All keys are
 * lower_snake_case and referenced from {@link HerbRegistry} / {@link ModifierRegistry}.
 */
public final class BrewEffects {

   public static final Map<String, Supplier<MobEffect>> EFFECTS = new HashMap<>();

   /** Powerful/rare effects whose amplifier is capped low regardless of potency. */
   public static final Set<String> POWERFUL =
      new HashSet<>(Arrays.asList("indestructibility", "demonic_possession", "undead", "paralysis"));

   /** Negative effects that may be inflicted as a toxic/unstable side effect. */
   public static final List<String> NEGATIVE_SIDE_EFFECTS = Arrays.asList(
      "nausea", "weakness", "poison", "mana_exhaustion", "vulnerability",
      "fear", "daze", "blurry_vision", "sweaty_hands", "stench");

   /** Corruption table: a clean effect twists into one of the listed darker effects. */
   public static final Map<String, String[]> CORRUPTION = new HashMap<>();

   static {
      // --- vanilla effects ---
      v("fire_resistance", MobEffects.FIRE_RESISTANCE);
      v("strength", MobEffects.DAMAGE_BOOST);
      v("regeneration", MobEffects.REGENERATION);
      v("absorption", MobEffects.ABSORPTION);
      v("instant_health", MobEffects.HEAL);
      v("luck", MobEffects.LUCK);
      v("hero_of_the_village", MobEffects.HERO_OF_THE_VILLAGE);
      v("speed", MobEffects.MOVEMENT_SPEED);
      v("haste", MobEffects.DIG_SPEED);
      v("water_breathing", MobEffects.WATER_BREATHING);
      v("dolphins_grace", MobEffects.DOLPHINS_GRACE);
      v("resistance", MobEffects.DAMAGE_RESISTANCE);
      v("slow_falling", MobEffects.SLOW_FALLING);
      v("health_boost", MobEffects.HEALTH_BOOST);
      v("saturation", MobEffects.SATURATION);
      v("night_vision", MobEffects.NIGHT_VISION);
      v("invisibility", MobEffects.INVISIBILITY);
      v("jump_boost", MobEffects.JUMP);
      v("glowing", MobEffects.GLOWING);
      v("poison", MobEffects.POISON);
      v("wither", MobEffects.WITHER);
      v("hunger", MobEffects.HUNGER);
      v("slowness", MobEffects.MOVEMENT_SLOWDOWN);
      v("weakness", MobEffects.WEAKNESS);
      v("confusion", MobEffects.CONFUSION);
      v("nausea", MobEffects.CONFUSION);

      // --- mod effects (PotionRegistry) ---
      m("flammability", PotionRegistry.flamability);
      m("mana_regeneration", PotionRegistry.mana_regen);
      m("mana_exhaustion", PotionRegistry.mana_exhaustion);
      m("earth_protection", PotionRegistry.earth_protection);
      m("stunned", PotionRegistry.stunned);
      m("frostbite", PotionRegistry.frostbite);
      m("sleep", PotionRegistry.sleep);
      m("haze", PotionRegistry.haze);
      m("hallucination", PotionRegistry.hallucination);
      m("bewilderment", PotionRegistry.bewilderment);
      m("colorblind", PotionRegistry.color_blind);
      m("weakling", PotionRegistry.weakling);
      m("demonic_possession", PotionRegistry.demonic_possession);
      m("obscuration", PotionRegistry.obscuration);
      m("fear", PotionRegistry.fear);
      m("snake_poison", PotionRegistry.snake_poison);
      m("undead", PotionRegistry.undead);
      m("infatuation", PotionRegistry.infatuation);
      m("poison_thorn", PotionRegistry.poison_thorned);
      m("greasy_skin", PotionRegistry.greasy_skin);
      m("repellent", PotionRegistry.repellant);
      m("paralysis", PotionRegistry.paralysis);
      m("stench", PotionRegistry.stench);
      m("puffer_toxin", PotionRegistry.puffer_toxin);
      m("vulnerability", PotionRegistry.vulnerability);
      m("indestructibility", PotionRegistry.indestructibility);
      m("daze", PotionRegistry.daze);
      m("blurry_vision", PotionRegistry.blurry_vision);
      m("sweaty_hands", PotionRegistry.sweaty_hands);

      // --- corruption table ---
      CORRUPTION.put("regeneration", new String[]{"wither", "weakness"});
      CORRUPTION.put("night_vision", new String[]{"blurry_vision", "hallucination"});
      CORRUPTION.put("speed", new String[]{"daze", "sweaty_hands"});
      CORRUPTION.put("strength", new String[]{"vulnerability"});
      CORRUPTION.put("mana_regeneration", new String[]{"mana_exhaustion"});
      CORRUPTION.put("water_breathing", new String[]{"puffer_toxin"});
      CORRUPTION.put("invisibility", new String[]{"obscuration"});
      CORRUPTION.put("luck", new String[]{"fear", "bewilderment"});
   }

   private static void v(String key, MobEffect effect) {
      EFFECTS.put(key, () -> effect);
   }

   private static void m(String key, Supplier<MobEffect> supplier) {
      EFFECTS.put(key, supplier);
   }

   public static MobEffect get(String key) {
      Supplier<MobEffect> s = EFFECTS.get(key);
      return s == null ? null : s.get();
   }

   public static boolean isPowerful(String key) {
      return POWERFUL.contains(key);
   }

   private BrewEffects() {}
}
