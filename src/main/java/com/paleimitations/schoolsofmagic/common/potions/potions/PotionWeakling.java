package com.paleimitations.schoolsofmagic.common.potions.potions;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class PotionWeakling extends MobEffect {
   public PotionWeakling(MobEffectCategory category, int color) {
      super(category, color);
      this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "8CF2D30E-63B4-11E9-A923-1681BE663D3E", -0.2, AttributeModifier.Operation.MULTIPLY_TOTAL);
      this.addAttributeModifier(Attributes.FLYING_SPEED, "8CF2D5DE-63B4-11E9-A923-1681BE663D3E", -0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
      this.addAttributeModifier(Attributes.MAX_HEALTH, "8CF2D9C6-63B4-11E9-A923-1681BE663D3E", -0.15, AttributeModifier.Operation.MULTIPLY_TOTAL);
   }

   public boolean isInstantenous() {
      return false;
   }

   public boolean isDurationEffectTick(int duration, int amplifier) {
      return true;
   }
}
