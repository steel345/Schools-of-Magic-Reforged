package com.paleimitations.schoolsofmagic.common.entity.ai;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.CapabilityCreatureBehavior;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.ICreatureBehavior;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

public class EntityAIAngryRevenge extends HurtByTargetGoal {
   public EntityAIAngryRevenge(PathfinderMob creatureIn, boolean entityCallsForHelpIn) {
      super(creatureIn);
      if (entityCallsForHelpIn) {
         this.setAlertOthers();
      }
   }

   @Override
   public boolean canUse() {
      ICreatureBehavior behavior = this.mob.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
      if (behavior == null || !behavior.isAngry()) {
         return false;
      }
      return super.canUse();
   }

   @Override
   public boolean canContinueToUse() {
      ICreatureBehavior behavior = this.mob.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
      if (behavior == null || !behavior.isAngry()) {
         return false;
      }
      return super.canContinueToUse();
   }
}
