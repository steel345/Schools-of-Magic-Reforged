package com.paleimitations.schoolsofmagic.common.entity.ai;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.CapabilityCreatureBehavior;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.ICreatureBehavior;
import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;

public class EntityAILoyaltyOwnerTarget extends TargetGoal {
   PathfinderMob tameable;
   LivingEntity attacker;
   private int timestamp;

   public EntityAILoyaltyOwnerTarget(PathfinderMob theEntityTameableIn) {
      super(theEntityTameableIn, false);
      this.tameable = theEntityTameableIn;
      this.setFlags(EnumSet.of(Goal.Flag.TARGET));
   }

   @Override
   public boolean canUse() {
      ICreatureBehavior behavior = this.tameable.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
      if (behavior == null) {
         return false;
      }
      if (!behavior.isLoyal()) {
         return false;
      }
      LivingEntity entitylivingbase = behavior.getLoyaltyTarget(this.tameable.level());
      if (entitylivingbase == null) {
         return false;
      }
      this.attacker = entitylivingbase.getLastHurtMob();
      int i = entitylivingbase.getLastHurtMobTimestamp();
      return i != this.timestamp && this.canAttack(this.attacker, net.minecraft.world.entity.ai.targeting.TargetingConditions.DEFAULT);
   }

   protected boolean canAttack(LivingEntity target, net.minecraft.world.entity.ai.targeting.TargetingConditions conditions) {
      ICreatureBehavior behavior = this.tameable.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
      if (behavior == null || target == null) return false;
      if (target == behavior.getLoyaltyTarget(this.tameable.level()) || behavior.isOnTargetTeam(target, behavior.getLoyaltyTargetUUID())) {
         return false;
      }
      return super.canAttack(target, conditions);
   }

   @Override
   public void start() {
      ICreatureBehavior behavior = this.tameable.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
      this.mob.setTarget(this.attacker);
      if (behavior != null) {
         LivingEntity entitylivingbase = behavior.getLoyaltyTarget(this.tameable.level());
         if (entitylivingbase != null) {
            this.timestamp = entitylivingbase.getLastHurtMobTimestamp();
         }
      }
      super.start();
   }
}
