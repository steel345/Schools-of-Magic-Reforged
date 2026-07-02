package com.paleimitations.schoolsofmagic.common.potions.potions;

import java.util.Random;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class PotionFatalPoison extends MobEffect {
   public PotionFatalPoison(MobEffectCategory category, int color) {
      super(category, color);
   }

   public boolean isInstantenous() {
      return false;
   }

   public boolean isDurationEffectTick(int duration, int amplifier) {
      int j = 25 >> amplifier;
      if (duration < 2) {
         return true;
      } else {
         return j > 0 ? duration % j == 0 : true;
      }
   }

   public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
      super.applyEffectTick(entityLivingBaseIn, amplifier);
      new Random();
      Level world = entityLivingBaseIn.level();
      if (entityLivingBaseIn.getEffect(this).getDuration() < 2) {
         entityLivingBaseIn.hurt(world.damageSources().magic(), 20.0F);
      }

      if (entityLivingBaseIn.getHealth() > 1.0F) {
         entityLivingBaseIn.hurt(world.damageSources().magic(), 1.0F);
      }
   }
}
