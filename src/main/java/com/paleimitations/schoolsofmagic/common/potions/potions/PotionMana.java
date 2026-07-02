package com.paleimitations.schoolsofmagic.common.potions.potions;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import java.util.Random;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class PotionMana extends MobEffect {
   public PotionMana(MobEffectCategory category, int color) {
      super(category, color);
   }

   public boolean isInstantenous() {
      return true;
   }

   public void applyInstantenousEffect(Entity source, Entity indirectSource, LivingEntity entityLivingBaseIn, int amplifier, double health) {
      super.applyInstantenousEffect(source, indirectSource, entityLivingBaseIn, amplifier, health);
      new Random();
      Level world = entityLivingBaseIn.level();
      IManaData mana = entityLivingBaseIn.getCapability(CapabilityManaData.CAP).orElse(null);
      if (mana != null) {
         mana.addMana((float)(120 * (amplifier + 1)));
      }
   }
}
