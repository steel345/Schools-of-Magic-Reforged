package com.paleimitations.schoolsofmagic.common.potions.potions;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class PotionSpellCharge extends MobEffect {
   public PotionSpellCharge(MobEffectCategory category, int color) {
      super(category, color);
   }

   public boolean isInstantenous() {
      return true;
   }

   public void applyInstantenousEffect(Entity source, Entity indirectSource, LivingEntity entityLivingBaseIn, int amplifier, double health) {
      super.applyInstantenousEffect(source, indirectSource, entityLivingBaseIn, amplifier, health);
      if (entityLivingBaseIn.level().isClientSide) {
         return;
      }
      IManaData mana = entityLivingBaseIn.getCapability(CapabilityManaData.CAP).orElse(null);
      if (mana == null) {
         return;
      }
      Spell current = mana.getCurrentSpell();
      int chargeLevel = current != null ? current.currentSpellChargeLevel : mana.getLargestChargeLevel();
      if (chargeLevel < 0) {
         return;
      }
      for (int i = 0; i <= amplifier; i++) {
         if (mana.canAddCharge(chargeLevel)) {
            mana.addCharge(chargeLevel);
         }
      }
   }
}
