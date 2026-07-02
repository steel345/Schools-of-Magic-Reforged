package com.paleimitations.schoolsofmagic.common.potions.potions;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import java.util.Random;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class PotionManaRegen extends MobEffect {
   public PotionManaRegen(MobEffectCategory category, int color) {
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
      IManaData mana = entityLivingBaseIn.getCapability(CapabilityManaData.CAP).orElse(null);
      if (mana != null) {
         int amp = 60 - 20 * amplifier;
         int counter = entityLivingBaseIn.tickCount %= amp;
         if (counter == 0 || amp <= 0) {
            mana.addMana(1.0F);
         }
      }
   }
}
