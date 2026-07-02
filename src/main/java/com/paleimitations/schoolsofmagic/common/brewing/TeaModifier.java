package com.paleimitations.schoolsofmagic.common.brewing;

/** Immutable data describing one brewing modifier (added before the herbs). */
public final class TeaModifier {

   public final String descriptor;          // name fragment, e.g. "Sweet"
   public final int waterTintColor;         // 0xRRGGBB, kept in the blue/cyan/purple-blue range
   public final int potencyBonus;
   public final float durationMultiplier;
   public final int stabilityBonus;
   public final int toxicityBonus;
   public final float secondaryEffectChanceBonus;
   public final float corruptionChance;

   public TeaModifier(String descriptor, int waterTintColor, int potencyBonus, float durationMultiplier,
                      int stabilityBonus, int toxicityBonus, float secondaryEffectChanceBonus, float corruptionChance) {
      this.descriptor = descriptor;
      this.waterTintColor = waterTintColor;
      this.potencyBonus = potencyBonus;
      this.durationMultiplier = durationMultiplier;
      this.stabilityBonus = stabilityBonus;
      this.toxicityBonus = toxicityBonus;
      this.secondaryEffectChanceBonus = secondaryEffectChanceBonus;
      this.corruptionChance = corruptionChance;
   }

   /** Neutral fallback used when no recognised modifier was infused. */
   public static final TeaModifier NEUTRAL =
      new TeaModifier("", 0x3F76E4, 0, 1.0F, 0, 0, 0.0F, 0.0F);
}
