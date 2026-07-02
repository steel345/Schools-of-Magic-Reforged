package com.paleimitations.schoolsofmagic.common.potions.potions;

import java.util.Random;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class PotionFrostbite extends MobEffect {
   public PotionFrostbite(MobEffectCategory category, int color) {
      super(category, color);
      this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "5A587124-63B4-11E9-A923-1681BE663D3E", -0.2, AttributeModifier.Operation.MULTIPLY_TOTAL);
      this.addAttributeModifier(Attributes.FLYING_SPEED, "759AD38C-63B4-11E9-A923-1681BE663D3E", -0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
   }

   public boolean isInstantenous() {
      return false;
   }

   public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
      super.applyEffectTick(entityLivingBaseIn, amplifier);
      Random rand = new Random();
      Level world = entityLivingBaseIn.level();
      if (entityLivingBaseIn.canFreeze()) {
         entityLivingBaseIn.setTicksFrozen(entityLivingBaseIn.getTicksRequiredToFreeze() + 5);
      }
      if (rand.nextInt(10) == 0) {
         entityLivingBaseIn.hurt(world.damageSources().magic(), 0.5F * (float)(1 + amplifier));
      }
   }

   public boolean isDurationEffectTick(int duration, int amplifier) {
      return true;
   }
}
