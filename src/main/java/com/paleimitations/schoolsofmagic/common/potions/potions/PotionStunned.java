package com.paleimitations.schoolsofmagic.common.potions.potions;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class PotionStunned extends MobEffect {
   public PotionStunned(MobEffectCategory category, int color) {
      super(category, color);
      this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "5A587124-63B4-11E9-A923-1681BE663D3E", -0.4, AttributeModifier.Operation.MULTIPLY_TOTAL);
      this.addAttributeModifier(Attributes.FLYING_SPEED, "759AD38C-63B4-11E9-A923-1681BE663D3E", -0.3, AttributeModifier.Operation.MULTIPLY_TOTAL);
   }

   public boolean isInstantenous() {
      return false;
   }

   public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
      super.applyEffectTick(entityLivingBaseIn, amplifier);
   }

   public boolean isDurationEffectTick(int duration, int amplifier) {
      return true;
   }
}
