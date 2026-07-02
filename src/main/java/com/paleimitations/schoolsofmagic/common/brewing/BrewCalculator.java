package com.paleimitations.schoolsofmagic.common.brewing;

import com.mojang.logging.LogUtils;
import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;

public final class BrewCalculator {

   private static final Logger LOGGER = LogUtils.getLogger();
   private static final int WATER_BASE = 0x3F76E4; // vanilla water tint

   public static BrewResult resolve(java.util.List<Item> modifierItems, List<EnumPlantType> herbsIn) {
      List<EnumPlantType> herbs = new ArrayList<>();
      List<HerbProfile> profiles = new ArrayList<>();
      for (EnumPlantType t : herbsIn) {
         HerbProfile p = HerbRegistry.get(t);
         if (p != null) { herbs.add(t); profiles.add(p); }
      }

      TeaModifier mod = ModifierRegistry.combine(modifierItems);
      boolean hasMod = mod != null;
      if (!hasMod) mod = TeaModifier.NEUTRAL;

      BrewResult result = new BrewResult();
      result.modifierDescriptor = hasMod ? mod.descriptor : "";
      for (EnumPlantType t : herbs) result.herbsUsed.add(t.getSerializedName());

      long seed = seedFor(modifierItems, herbs);
      Random rng = new Random(seed);

      // --- weighted effect points ---
      Map<String, Integer> points = new HashMap<>();
      Map<String, Integer> support = new HashMap<>();   // sum(stability - toxicity) of contributors, for tie-breaks
      Set<String> nonRare = new HashSet<>();            // appeared as primary/secondary somewhere
      for (HerbProfile p : profiles) {
         int s = p.stability - p.toxicity;
         for (String e : p.primaryEffects)   { add(points, e, 3); add(support, e, s); nonRare.add(e); }
         for (String e : p.secondaryEffects) { add(points, e, 2); add(support, e, s); nonRare.add(e); }
         for (String e : p.rareEffects)      { add(points, e, 1); add(support, e, s); }
      }

      // --- totals ---
      int totalPotency = mod.potencyBonus;
      int totalStability = mod.stabilityBonus;
      int rawToxicity = mod.toxicityBonus;
      for (HerbProfile p : profiles) {
         totalPotency += p.potency;
         totalStability += p.stability;
         rawToxicity += p.toxicity;
      }
      int totalToxicity = Math.max(0, rawToxicity);
      result.totalStability = totalStability;
      result.totalToxicity = totalToxicity;

      // --- rank & pick primary / secondary ---
      List<String> ranked = rank(points, support, seed);
      String primary = null;
      for (String e : ranked) {
         if (nonRare.contains(e) || points.getOrDefault(e, 0) >= 4) { primary = e; break; }
      }
      if (primary == null && !ranked.isEmpty()) primary = ranked.get(0);
      String secondaryCandidate = null;
      for (String e : ranked) {
         if (!e.equals(primary)) { secondaryCandidate = e; break; }
      }
      result.primaryEffect = primary;

      // --- amplifier from potency (powerful effects capped low) ---
      int amp = amplifierFor(totalPotency);
      if (primary != null && BrewEffects.isPowerful(primary)) amp = Math.min(amp, 0);
      result.amplifier = amp;

      // --- duration (custom teas cap at 2 minutes) ---
      double duration = (120.0 + totalStability * 15.0) * mod.durationMultiplier;
      duration = clamp(duration, 30, 120);
      result.durationSeconds = (int) Math.round(duration);

      // --- secondary effect chance ---
      double secChance = clamp(0.15 + mod.secondaryEffectChanceBonus + totalStability * 0.03, 0.0, 0.6);
      if (secondaryCandidate != null && rng.nextDouble() < secChance) {
         result.secondaryEffect = secondaryCandidate;
      }

      // --- corruption: the primary may twist into a darker effect ---
      boolean corrupted = false;
      if (primary != null && mod.corruptionChance > 0 && rng.nextDouble() < mod.corruptionChance) {
         String[] dark = BrewEffects.CORRUPTION.get(primary);
         if (dark != null && dark.length > 0) {
            result.primaryEffect = dark[rng.nextInt(dark.length)];
            corrupted = true;
         }
      }

      // --- side effects from toxicity / instability ---
      double sideChance = clamp(totalToxicity * 0.08 - totalStability * 0.05 + mod.corruptionChance, 0.0, 0.9);
      if (rng.nextDouble() < sideChance) {
         String side = pickSideEffect(profiles, rng);
         if (side != null && !side.equals(result.primaryEffect)) result.sideEffects.add(side);
      }

      // --- colour blending ---
      int water = blend(WATER_BASE, mod.waterTintColor, 0.15F);
      if (!profiles.isEmpty()) {
         int herbColor = averageColor(profiles);
         result.finalTeaColor = blend(water, herbColor, 0.65F);
      } else {
         result.finalTeaColor = water;
      }

      // --- display name ---
      result.displayName = nameFor(corrupted, hasMod ? mod : null, totalStability, result.primaryEffect);

      debug(result, points, secChance, sideChance);
      return result;
   }

   // ---- ranking ----

   private static List<String> rank(Map<String, Integer> points, Map<String, Integer> support, long seed) {
      List<String> keys = new ArrayList<>(points.keySet());
      keys.sort((a, b) -> {
         int pa = points.getOrDefault(a, 0), pb = points.getOrDefault(b, 0);
         if (pa != pb) return Integer.compare(pb, pa);                       // more points first
         int sa = support.getOrDefault(a, 0), sb = support.getOrDefault(b, 0);
         if (sa != sb) return Integer.compare(sb, sa);                       // most stable / least toxic
         return Long.compare(tieHash(seed, a), tieHash(seed, b));            // deterministic
      });
      return keys;
   }

   private static long tieHash(long seed, String key) {
      long h = seed ^ (key.hashCode() * 0x9E3779B97F4A7C15L);
      h ^= (h >>> 32);
      return h;
   }

   // ---- numeric helpers ----

   private static int amplifierFor(int potency) {
      if (potency <= 3) return 0;
      if (potency <= 6) return 1;
      if (potency <= 9) return 2;
      return 3;
   }

   private static void add(Map<String, Integer> map, String key, int v) {
      map.merge(key, v, Integer::sum);
   }

   private static double clamp(double v, double lo, double hi) {
      return v < lo ? lo : (v > hi ? hi : v);
   }

   // ---- side effects ----

   private static String pickSideEffect(List<HerbProfile> profiles, Random rng) {
      List<String> pool = new ArrayList<>();
      for (HerbProfile p : profiles) {
         collectNegatives(p.primaryEffects, pool);
         collectNegatives(p.secondaryEffects, pool);
         collectNegatives(p.rareEffects, pool);
      }
      if (pool.isEmpty()) pool = BrewEffects.NEGATIVE_SIDE_EFFECTS;
      return pool.isEmpty() ? null : pool.get(rng.nextInt(pool.size()));
   }

   private static void collectNegatives(List<String> effects, List<String> out) {
      for (String e : effects) if (BrewEffects.NEGATIVE_SIDE_EFFECTS.contains(e)) out.add(e);
   }

   // ---- colour ----

   private static int averageColor(List<HerbProfile> profiles) {
      int r = 0, g = 0, b = 0, n = 0;
      for (HerbProfile p : profiles) {
         r += (p.brewColor >> 16) & 0xFF;
         g += (p.brewColor >> 8) & 0xFF;
         b += p.brewColor & 0xFF;
         n++;
      }
      if (n == 0) return WATER_BASE;
      return ((r / n) << 16) | ((g / n) << 8) | (b / n);
   }

   private static int blend(int a, int b, float t) {
      int ar = (a >> 16) & 0xFF, ag = (a >> 8) & 0xFF, ab = a & 0xFF;
      int br = (b >> 16) & 0xFF, bg = (b >> 8) & 0xFF, bb = b & 0xFF;
      int rr = Math.round(ar * (1 - t) + br * t);
      int rg = Math.round(ag * (1 - t) + bg * t);
      int rb = Math.round(ab * (1 - t) + bb * t);
      return (rr << 16) | (rg << 8) | rb;
   }

   // ---- naming ----

   private static String nameFor(boolean corrupted, TeaModifier mod, int stability, String primaryKey) {
      if (primaryKey == null) return "Plain Tea";
      String prefix;
      if (corrupted) {
         prefix = "Corrupted";
      } else if (mod != null && mod.descriptor != null && !mod.descriptor.isEmpty()) {
         prefix = mod.descriptor;
      } else {
         prefix = stabilityDescriptor(stability);
      }
      return prefix + " " + effectDisplay(primaryKey) + " Tea";
   }

   private static String stabilityDescriptor(int stability) {
      if (stability >= 5) return "Stable";
      if (stability >= 1) return "Balanced";
      if (stability >= -3) return "Unstable";
      return "Dangerous";
   }

   public static String effectDisplay(String key) {
      if (key == null) return "";
      String[] parts = key.split("_");
      StringBuilder sb = new StringBuilder();
      for (String part : parts) {
         if (part.isEmpty()) continue;
         if (sb.length() > 0) sb.append(' ');
         sb.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
      }
      return sb.toString();
   }

   // ---- seed ----

   private static long seedFor(java.util.List<Item> modifierItems, List<EnumPlantType> herbs) {
      long seed = 1125899906842597L;
      if (modifierItems != null) {
         for (Item it : modifierItems) seed = 31 * seed + (it == null ? 0 : it.toString().hashCode());
      }
      for (EnumPlantType t : herbs) seed = 31 * seed + t.ordinal();
      return seed;
   }

   // ---- debug ----

   private static void debug(BrewResult r, Map<String, Integer> points, double secChance, double sideChance) {
      if (!LOGGER.isDebugEnabled()) return;
      LOGGER.debug("[Tea] '{}' | primary={} (amp {}) secondary={} | {}s | stab={} tox={} | color=#{} | herbs={} | side={} | secChance={} sideChance={} | points={}",
         r.displayName, r.primaryEffect, r.amplifier, r.secondaryEffect, r.durationSeconds,
         r.totalStability, r.totalToxicity, String.format("%06X", r.finalTeaColor & 0xFFFFFF),
         r.herbsUsed, r.sideEffects, String.format("%.2f", secChance), String.format("%.2f", sideChance), points);
   }

   private BrewCalculator() {}
}
