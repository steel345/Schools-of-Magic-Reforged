package com.paleimitations.schoolsofmagic.common.potions.potions;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PotionCleansing extends MobEffect {

   public PotionCleansing(MobEffectCategory category, int color) {
      super(category, color);
   }

   @Override
   public boolean isInstantenous() {
      return true;
   }

   @Override
   public boolean isDurationEffectTick(int duration, int amplifier) {
      return true;
   }

   @Override
   public void applyEffectTick(LivingEntity target, int amplifier) {
      cleanse(target);
   }

   @Override
   public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity target, int amplifier, double health) {
      cleanse(target);
   }

   private static void cleanse(LivingEntity target) {
      if (target.level().isClientSide) {
         return;
      }
      List<MobEffect> harmful = new ArrayList<>();
      for (MobEffectInstance inst : target.getActiveEffects()) {
         if (inst.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
            harmful.add(inst.getEffect());
         }
      }
      for (MobEffect effect : harmful) {
         target.removeEffect(effect);
      }
   }
}
