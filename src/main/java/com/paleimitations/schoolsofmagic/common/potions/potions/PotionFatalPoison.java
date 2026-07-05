package com.paleimitations.schoolsofmagic.common.potions.potions;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class PotionFatalPoison extends MobEffect {
   private final boolean lethal;

   public PotionFatalPoison(MobEffectCategory category, int color) {
      this(category, color, false);
   }

   public PotionFatalPoison(MobEffectCategory category, int color, boolean lethal) {
      super(category, color);
      this.lethal = lethal;
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
      Level world = entityLivingBaseIn.level();
      if (this.lethal && entityLivingBaseIn.getEffect(this).getDuration() < 2) {
         entityLivingBaseIn.hurt(world.damageSources().magic(), 20.0F);
      }

      if (entityLivingBaseIn.getHealth() > 1.0F) {
         entityLivingBaseIn.hurt(world.damageSources().magic(), 1.0F);
      }
   }
}
