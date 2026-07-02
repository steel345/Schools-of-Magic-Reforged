package com.paleimitations.schoolsofmagic.common.entity.ai;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.CapabilityCreatureBehavior;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.ICreatureBehavior;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

public class EntityAIFearAvoidTarget extends AvoidEntityGoal<LivingEntity> {
   @SuppressWarnings({"unchecked", "rawtypes"})
   public EntityAIFearAvoidTarget(final PathfinderMob entityIn, Class classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
      super(entityIn, classToAvoidIn, avoidDistanceIn, farSpeedIn, nearSpeedIn, (Predicate<LivingEntity>) target -> {
         ICreatureBehavior behavior = entityIn.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
         if (behavior == null || !behavior.isAfraid() || behavior.getFearTarget(target.level()) == null) {
            return false;
         }
         return behavior.getFearTarget(target.level()).is(target) || behavior.isOnTargetTeam(target, behavior.getFearTarget(target.level()).getUUID());
      });
   }

   @Override
   public boolean canUse() {
      ICreatureBehavior behavior = this.mob.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
      if (behavior == null || !behavior.isAfraid() && behavior.getFearTarget(this.mob.level()) != null) {
         return false;
      }
      return super.canUse();
   }

   @Override
   public boolean canContinueToUse() {
      ICreatureBehavior behavior = this.mob.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
      if (behavior == null || !behavior.isAfraid() && behavior.getFearTarget(this.mob.level()) != null) {
         return false;
      }
      return super.canContinueToUse();
   }
}
