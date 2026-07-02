package com.paleimitations.schoolsofmagic.common.entity.ai;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.CapabilityCreatureBehavior;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.ICreatureBehavior;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public class EntityAIInfatuationFollow extends Goal {
   private final PathfinderMob creature;
   private final Predicate<LivingEntity> followPredicate;
   private LivingEntity followingEntity;
   private final double speedModifier;
   private final PathNavigation navigation;
   private int timeToRecalcPath;
   private final float stopDistance;
   private float oldWaterCost;
   private final float areaSize;

   public EntityAIInfatuationFollow(final PathfinderMob creature, double speedModifier, float stopDistance, float areaSize) {
      this.creature = creature;
      this.followPredicate = living -> {
         ICreatureBehavior behavior = creature.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
         return behavior != null && behavior.isInfatuated() && (behavior.getInfatuationTarget(creature.level()) == null || behavior.getInfatuationTarget(creature.level()) == living) && !living.isRemoved();
      };
      this.speedModifier = speedModifier;
      this.navigation = creature.getNavigation();
      this.stopDistance = stopDistance;
      this.areaSize = areaSize;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
      if (!(creature.getNavigation() instanceof GroundPathNavigation) && !(creature.getNavigation() instanceof FlyingPathNavigation)) {
         throw new IllegalArgumentException("Unsupported mob type for FollowMobGoal");
      }
   }

   @Override
   public boolean canUse() {
      ICreatureBehavior behavior = this.creature.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
      if (behavior == null || !behavior.isInfatuated()) {
         return false;
      }
      List<LivingEntity> list = this.creature.level().getEntitiesOfClass(LivingEntity.class, this.creature.getBoundingBox().inflate((double)this.areaSize), this.followPredicate);
      if (!list.isEmpty()) {
         for (LivingEntity entityliving : list) {
            if (entityliving.isInvisible()) continue;
            this.followingEntity = entityliving;
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean canContinueToUse() {
      ICreatureBehavior behavior = this.creature.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
      return behavior != null && behavior.isInfatuated() && this.followingEntity != null && !this.navigation.isDone() && this.creature.distanceToSqr(this.followingEntity) > (double)(this.stopDistance * this.stopDistance);
   }

   @Override
   public void start() {
      this.timeToRecalcPath = 0;
      this.oldWaterCost = this.creature.getPathfindingMalus(BlockPathTypes.WATER);
      this.creature.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
   }

   @Override
   public void stop() {
      this.followingEntity = null;
      this.navigation.stop();
      this.creature.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
   }

   @Override
   public void tick() {
      if (this.followingEntity != null && !this.creature.isLeashed()) {
         this.creature.getLookControl().setLookAt(this.followingEntity, 10.0F, (float)this.creature.getMaxHeadXRot());
         if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            double d0 = this.creature.getX() - this.followingEntity.getX();
            double d1 = this.creature.getY() - this.followingEntity.getY();
            double d2 = this.creature.getZ() - this.followingEntity.getZ();
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            if (d3 > (double)(this.stopDistance * this.stopDistance)) {
               this.navigation.moveTo(this.followingEntity, this.speedModifier);
            } else {
               this.navigation.stop();
               if (d3 <= (double)this.stopDistance) {
                  double d4 = this.followingEntity.getX() - this.creature.getX();
                  double d5 = this.followingEntity.getZ() - this.creature.getZ();
                  this.navigation.moveTo(this.creature.getX() - d4, this.creature.getY(), this.creature.getZ() - d5, this.speedModifier);
               }
            }
         }
      }
   }
}
