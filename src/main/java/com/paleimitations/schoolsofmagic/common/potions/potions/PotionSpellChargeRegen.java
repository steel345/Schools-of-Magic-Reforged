package com.paleimitations.schoolsofmagic.common.potions.potions;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class PotionSpellChargeRegen extends MobEffect {
   public PotionSpellChargeRegen(MobEffectCategory category, int color) {
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
      if (entityLivingBaseIn.level().isClientSide) {
         return;
      }
      IManaData mana = entityLivingBaseIn.getCapability(CapabilityManaData.CAP).orElse(null);
      if (mana == null) {
         return;
      }
      int[] countdowns = mana.getCountdowns();
      int extra = 2 * (amplifier + 1);
      for (int i = 0; i < countdowns.length; i++) {
         if (countdowns[i] > 0) {
            countdowns[i] = Math.max(0, countdowns[i] - extra);
         }
      }
   }
}
