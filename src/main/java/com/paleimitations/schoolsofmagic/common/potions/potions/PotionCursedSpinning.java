package com.paleimitations.schoolsofmagic.common.potions.potions;

import java.util.Random;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PotionCursedSpinning extends MobEffect {
   public PotionCursedSpinning(MobEffectCategory category, int color) {
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
      entityLivingBaseIn.xRotO += (float)((amplifier + 1) * 3);
      entityLivingBaseIn.yBodyRotO += (float)((amplifier + 1) * 3);
      entityLivingBaseIn.yHeadRotO += (float)((amplifier + 1) * 3);
      entityLivingBaseIn.setYRot(entityLivingBaseIn.getYRot() + (float)((amplifier + 1) * 3));
      entityLivingBaseIn.yBodyRot += (float)((amplifier + 1) * 3);
      entityLivingBaseIn.yHeadRot += (float)((amplifier + 1) * 3);
      if (entityLivingBaseIn instanceof Mob && !(entityLivingBaseIn instanceof Player)) {
         Mob living = (Mob)entityLivingBaseIn;
         living.zza += (float)((amplifier + 1) * 3);
         RandomLookAroundGoal task = new RandomLookAroundGoal(living);
         boolean flag = false;

         for (WrappedGoal entry : living.goalSelector.getAvailableGoals()) {
            if (entry.getGoal() == task) {
               flag = true;
            }
         }

         if (!flag) {
            living.goalSelector.addGoal(1, task);
         }

         if (living.getEffect(this).getDuration() < 2) {
            living.goalSelector.removeGoal(task);
         }
      }
   }
}
