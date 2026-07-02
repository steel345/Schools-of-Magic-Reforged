package com.paleimitations.schoolsofmagic.common.entity.ai;

import java.util.EnumSet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class GoalRumorAttack extends Goal {
   private final Mob mob;
   private final LivingEntity target;
   private int cooldown;

   public GoalRumorAttack(Mob mob, LivingEntity target) {
      this.mob = mob;
      this.target = target;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
   }

   private boolean valid() {
      return this.target != null && this.target.isAlive() && this.mob.isAlive() && this.target != this.mob;
   }

   @Override
   public boolean canUse() {
      return this.valid();
   }

   @Override
   public boolean canContinueToUse() {
      return this.valid();
   }

   @Override
   public void stop() {
      this.mob.getNavigation().stop();
   }

   @Override
   public void tick() {
      if (!this.valid()) {
         return;
      }
      this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
      double reach = (double) (this.mob.getBbWidth() + this.target.getBbWidth()) + 0.6D;
      double dist = this.mob.distanceTo(this.target);
      if (dist > reach) {
         this.mob.getNavigation().moveTo(this.target, 1.2D);
      } else {
         this.mob.getNavigation().stop();
         if (this.cooldown <= 0) {
            this.cooldown = 20;
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.target.hurt(this.mob.damageSources().mobAttack(this.mob), 1.0F);
         }
      }
      if (this.cooldown > 0) {
         --this.cooldown;
      }
   }
}
