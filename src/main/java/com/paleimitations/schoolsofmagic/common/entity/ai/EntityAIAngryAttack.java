package com.paleimitations.schoolsofmagic.common.entity.ai;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.CapabilityCreatureBehavior;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.ICreatureBehavior;
import java.util.EnumSet;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;

public class EntityAIAngryAttack extends Goal {
   Level world;
   protected PathfinderMob attacker;
   protected int attackTick;
   double speedTowardsTarget;
   boolean longMemory;
   Path path;
   private int delayCounter;
   private double targetX;
   private double targetY;
   private double targetZ;
   protected final int attackInterval = 20;
   private int failedPathFindingPenalty = 0;
   private boolean canPenalize = false;

   public EntityAIAngryAttack(PathfinderMob creature, double speedIn, boolean useLongMemory) {
      this.attacker = creature;
      this.world = creature.level();
      this.speedTowardsTarget = speedIn;
      this.longMemory = useLongMemory;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
   }

   @Override
   public boolean canUse() {
      LivingEntity entitylivingbase = this.attacker.getTarget();
      ICreatureBehavior behavior = this.attacker.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
      if (behavior == null || !behavior.isAngry()) {
         return false;
      }
      if (entitylivingbase == null) {
         return false;
      }
      if (!entitylivingbase.isAlive()) {
         return false;
      }
      if (this.canPenalize) {
         if (--this.delayCounter <= 0) {
            this.path = this.attacker.getNavigation().createPath(entitylivingbase, 0);
            this.delayCounter = 4 + this.attacker.getRandom().nextInt(7);
            return this.path != null;
         }
         return true;
      }
      this.path = this.attacker.getNavigation().createPath(entitylivingbase, 0);
      if (this.path != null) {
         return true;
      }
      return this.getAttackReachSqr(entitylivingbase) >= this.attacker.distanceToSqr(entitylivingbase.getX(), entitylivingbase.getBoundingBox().minY, entitylivingbase.getZ());
   }

   @Override
   public boolean canContinueToUse() {
      LivingEntity entitylivingbase = this.attacker.getTarget();
      ICreatureBehavior behavior = this.attacker.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
      if (behavior == null || !behavior.isAngry()) {
         return false;
      }
      if (entitylivingbase == null) {
         return false;
      }
      if (!entitylivingbase.isAlive()) {
         return false;
      }
      if (!this.longMemory) {
         return !this.attacker.getNavigation().isDone();
      }
      if (!this.attacker.isWithinRestriction(entitylivingbase.blockPosition())) {
         return false;
      }
      return !(entitylivingbase instanceof Player) || !((Player)entitylivingbase).isSpectator() && !((Player)entitylivingbase).isCreative();
   }

   @Override
   public void start() {
      this.attacker.getNavigation().moveTo(this.path, this.speedTowardsTarget);
      this.delayCounter = 0;
   }

   @Override
   public void stop() {
      LivingEntity entitylivingbase = this.attacker.getTarget();
      if (entitylivingbase instanceof Player && (((Player)entitylivingbase).isSpectator() || ((Player)entitylivingbase).isCreative())) {
         this.attacker.setTarget(null);
      }
      this.attacker.getNavigation().stop();
   }

   @Override
   public void tick() {
      LivingEntity entitylivingbase = this.attacker.getTarget();
      if (entitylivingbase == null) return;
      this.attacker.getLookControl().setLookAt(entitylivingbase, 30.0F, 30.0F);
      double d0 = this.attacker.distanceToSqr(entitylivingbase.getX(), entitylivingbase.getBoundingBox().minY, entitylivingbase.getZ());
      --this.delayCounter;
      if ((this.longMemory || this.attacker.getSensing().hasLineOfSight(entitylivingbase)) && this.delayCounter <= 0 && (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0 || entitylivingbase.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0 || this.attacker.getRandom().nextFloat() < 0.05F)) {
         this.targetX = entitylivingbase.getX();
         this.targetY = entitylivingbase.getBoundingBox().minY;
         this.targetZ = entitylivingbase.getZ();
         this.delayCounter = 4 + this.attacker.getRandom().nextInt(7);
         if (this.canPenalize) {
            this.delayCounter += this.failedPathFindingPenalty;
            if (this.attacker.getNavigation().getPath() != null) {
               Node finalPathPoint = this.attacker.getNavigation().getPath().getEndNode();
               if (finalPathPoint != null && entitylivingbase.distanceToSqr((double)finalPathPoint.x, (double)finalPathPoint.y, (double)finalPathPoint.z) < 1.0) {
                  this.failedPathFindingPenalty = 0;
               } else {
                  this.failedPathFindingPenalty += 10;
               }
            } else {
               this.failedPathFindingPenalty += 10;
            }
         }
         if (d0 > 1024.0) {
            this.delayCounter += 10;
         } else if (d0 > 256.0) {
            this.delayCounter += 5;
         }
         if (!this.attacker.getNavigation().moveTo(entitylivingbase, this.speedTowardsTarget)) {
            this.delayCounter += 15;
         }
      }
      this.attackTick = Math.max(this.attackTick - 1, 0);
      this.checkAndPerformAttack(entitylivingbase, d0);
   }

   protected void checkAndPerformAttack(LivingEntity p_190102_1_, double p_190102_2_) {
      double d0 = this.getAttackReachSqr(p_190102_1_);
      if (p_190102_2_ <= d0 && this.attackTick <= 0) {
         this.attackTick = 20;
         this.attacker.swing(InteractionHand.MAIN_HAND);
         this.attackEntityAsMob(p_190102_1_);
      }
   }

   public boolean attackEntityAsMob(Entity entityIn) {
      float f = this.attacker.getAttribute(Attributes.ATTACK_DAMAGE) != null ? (float)this.attacker.getAttribute(Attributes.ATTACK_DAMAGE).getValue() : 4.0F;
      int i = 0;
      if (entityIn instanceof LivingEntity) {
         f += EnchantmentHelper.getDamageBonus(this.attacker.getMainHandItem(), ((LivingEntity)entityIn).getMobType());
         i += EnchantmentHelper.getKnockbackBonus(this.attacker);
      }
      DamageSource source = this.attacker.level().damageSources().mobAttack(this.attacker);
      boolean flag = entityIn.hurt(source, f);
      if (flag) {
         if (i > 0 && entityIn instanceof LivingEntity) {
            ((LivingEntity)entityIn).knockback((float)i * 0.5F, (double)Mth.sin(this.attacker.getYRot() * ((float)Math.PI / 180F)), (double)(-Mth.cos(this.attacker.getYRot() * ((float)Math.PI / 180F))));
            this.attacker.setDeltaMovement(this.attacker.getDeltaMovement().multiply(0.6, 1.0, 0.6));
         }
         int j = EnchantmentHelper.getFireAspect(this.attacker);
         if (j > 0) {
            entityIn.setSecondsOnFire(j * 4);
         }
         if (entityIn instanceof Player) {
            Player entityplayer = (Player)entityIn;
            ItemStack itemstack = this.attacker.getMainHandItem();
            ItemStack itemstack1 = entityplayer.isUsingItem() ? entityplayer.getUseItem() : ItemStack.EMPTY;
            if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem().canDisableShield(itemstack, itemstack1, entityplayer, this.attacker) && itemstack1.canPerformAction(net.minecraftforge.common.ToolActions.SHIELD_BLOCK)) {
               float f1 = 0.25F + (float)EnchantmentHelper.getBlockEfficiency(this.attacker) * 0.05F;
               if (this.attacker.getRandom().nextFloat() < f1) {
                  entityplayer.getCooldowns().addCooldown(itemstack1.getItem(), 100);
                  this.world.broadcastEntityEvent(entityplayer, (byte)30);
               }
            }
         }
      }
      return flag;
   }

   protected double getAttackReachSqr(LivingEntity attackTarget) {
      return (double)(this.attacker.getBbWidth() * 2.0F + attackTarget.getBbWidth());
   }
}
