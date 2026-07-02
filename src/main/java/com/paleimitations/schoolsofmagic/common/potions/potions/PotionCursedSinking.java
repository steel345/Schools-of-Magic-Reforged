package com.paleimitations.schoolsofmagic.common.potions.potions;

import java.util.Random;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class PotionCursedSinking extends MobEffect {
   public PotionCursedSinking(MobEffectCategory category, int color) {
      super(category, color);
   }

   public boolean isInstantenous() {
      return false;
   }

   public boolean isDurationEffectTick(int duration, int amplifier) {
      return true;
   }

   public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
      super.applyEffectTick(entityLivingBaseIn, amplifier);
      new Random();
      Level world = entityLivingBaseIn.level();
      entityLivingBaseIn.push(0.0, -0.05 - 0.00625 * (double)amplifier, 0.0);
   }
}
