package com.paleimitations.schoolsofmagic.common.entity.ai;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.CapabilityCreatureBehavior;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.ICreatureBehavior;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class EntityAIAngryFindTarget extends NearestAttackableTargetGoal<LivingEntity> {
   public EntityAIAngryFindTarget(PathfinderMob creature) {
      super(creature, LivingEntity.class, 0, true, true, null);
   }

   protected boolean canAttack(LivingEntity target, TargetingConditions conditions) {
      ICreatureBehavior behavior = this.mob.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
      if (behavior == null || target != behavior.getAngerTarget(this.mob.level()) || !target.isAlliedTo(behavior.getAngerTarget(this.mob.level()))) {
         return false;
      }
      return true;
   }

   @Override
   public boolean canUse() {
      ICreatureBehavior behavior = this.mob.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
      if (behavior == null || !behavior.isAngry() || behavior.getAngerTarget(this.mob.level()) == null) {
         return false;
      }
      return super.canUse();
   }

   @Override
   public boolean canContinueToUse() {
      ICreatureBehavior behavior = this.mob.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
      if (behavior == null || !behavior.isAngry() || behavior.getAngerTarget(this.mob.level()) == null) {
         return false;
      }
      return super.canContinueToUse();
   }
}
