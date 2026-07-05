package com.paleimitations.schoolsofmagic.common.brewing;

public final class TeaModifier {

   public final String descriptor;
   public final int waterTintColor;
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

   public static final TeaModifier NEUTRAL =
      new TeaModifier("", 0x3F76E4, 0, 1.0F, 0, 0, 0.0F, 0.0F);
}
