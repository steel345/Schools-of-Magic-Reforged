package com.paleimitations.schoolsofmagic.common.potions.potions;

import java.util.Random;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class PotionDemonicPossession extends MobEffect {
   public PotionDemonicPossession(MobEffectCategory category, int color) {
      super(category, color);
   }

   public boolean isInstantenous() {
      return false;
   }

   public boolean isDurationEffectTick(int duration, int amplifier) {
      int j = 15 >> amplifier;
      return j > 0 ? duration % j == 0 : true;
   }

   public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
      super.applyEffectTick(entityLivingBaseIn, amplifier);
      Random rand = new Random();
      Level world = entityLivingBaseIn.level();
      if (rand.nextInt(4) == 3) {
         entityLivingBaseIn.push(
            (double)(1 * (amplifier + 1)) - 2.0 * rand.nextDouble() * (double)(amplifier + 1),
            (double)(1 * (amplifier + 1)) - 2.0 * rand.nextDouble() * (double)(amplifier + 1),
            (double)(1 * (amplifier + 1)) - 2.0 * rand.nextDouble() * (double)(amplifier + 1)
         );
      }
   }
}
