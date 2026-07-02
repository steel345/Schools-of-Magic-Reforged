package com.paleimitations.schoolsofmagic.common.brewing;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Immutable data describing one crushed herb's brewing contribution. */
public final class HerbProfile {

   public final List<String> primaryEffects;
   public final List<String> secondaryEffects;
   public final List<String> rareEffects;
   public final int toxicity;
   public final int stability;
   public final int potency;
   public final int brewColor; // 0xRRGGBB

   public HerbProfile(List<String> primary, List<String> secondary, List<String> rare,
                      int toxicity, int stability, int potency, int brewColor) {
      this.primaryEffects = Collections.unmodifiableList(primary);
      this.secondaryEffects = Collections.unmodifiableList(secondary);
      this.rareEffects = Collections.unmodifiableList(rare);
      this.toxicity = toxicity;
      this.stability = stability;
      this.potency = potency;
      this.brewColor = brewColor;
   }

   public static HerbProfile of(List<String> primary, List<String> secondary, List<String> rare,
                                int toxicity, int stability, int potency, int brewColor) {
      return new HerbProfile(primary, secondary, rare, toxicity, stability, potency, brewColor);
   }

   public static List<String> fx(String... effects) {
      return effects.length == 0 ? Collections.emptyList() : Arrays.asList(effects);
   }
}
