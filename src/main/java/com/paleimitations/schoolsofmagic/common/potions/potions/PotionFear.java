package com.paleimitations.schoolsofmagic.common.potions.potions;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.CapabilityCreatureBehavior;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.ICreatureBehavior;
import javax.annotation.Nullable;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class PotionFear extends PotionBasic {
   public PotionFear(MobEffectCategory category, int color) {
      super(category, color);
   }

   public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity entityLivingBaseIn, int amplifier, double health) {
      super.applyInstantenousEffect(source, indirectSource, entityLivingBaseIn, amplifier, health);
      ICreatureBehavior behavior = entityLivingBaseIn.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
      if (!entityLivingBaseIn.level().isClientSide && !behavior.isAfraid()) {
         behavior.setAfraid(true);
         if (indirectSource != null) {
            behavior.setFearTarget(indirectSource.getUUID());
         }

         behavior.setShouldUpdateClient(true);
      }
   }
}
