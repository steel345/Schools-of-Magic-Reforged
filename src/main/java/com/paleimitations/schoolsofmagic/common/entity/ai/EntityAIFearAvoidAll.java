package com.paleimitations.schoolsofmagic.common.entity.ai;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.CapabilityCreatureBehavior;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.ICreatureBehavior;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

public class EntityAIFearAvoidAll extends AvoidEntityGoal<LivingEntity> {
   @SuppressWarnings({"unchecked", "rawtypes"})
   public EntityAIFearAvoidAll(PathfinderMob entityIn, Class classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
      super(entityIn, classToAvoidIn, avoidDistanceIn, farSpeedIn, nearSpeedIn);
   }

   @Override
   public boolean canUse() {
      ICreatureBehavior behavior = this.mob.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
      return behavior != null && behavior.isAfraid() && behavior.getFearTarget(this.mob.level()) == null && super.canUse();
   }

   @Override
   public boolean canContinueToUse() {
      ICreatureBehavior behavior = this.mob.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
      return behavior != null && behavior.isAfraid() && behavior.getFearTarget(this.mob.level()) == null && super.canContinueToUse();
   }
}
