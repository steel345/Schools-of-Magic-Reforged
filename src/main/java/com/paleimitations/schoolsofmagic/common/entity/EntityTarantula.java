package com.paleimitations.schoolsofmagic.common.entity;

import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityWebProjectile;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EntityTarantula extends Spider {
   private static final EntityDataAccessor<Byte> ATTACK = SynchedEntityData.defineId(EntityTarantula.class, EntityDataSerializers.BYTE);
   private static final EntityDataAccessor<Integer> JUMP_TICK = SynchedEntityData.defineId(EntityTarantula.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> ATTACK_SWITCH_COUNTDOWN = SynchedEntityData.defineId(EntityTarantula.class, EntityDataSerializers.INT);
   private final Goal aiShoot = new EntityAIShootWeb(this, 1.0, 20, 15.0f);
   private final MeleeAttackGoal aiBite = new AISpiderBite(this);
   private final MeleeAttackGoal aiSwat = new AISpiderSwat(this);
   private final Goal aiPounce = new EntityAIPounceAttack(this, 1.2f);

   public EntityTarantula(EntityType<? extends Spider> type, Level worldIn) {
      super(type, worldIn);
      this.setMaxUpStep(1.5F);
   }

   @Override
   public boolean removeWhenFarAway(double distance) {
      return false;
   }

   public int getJumpTick() {
      return this.entityData.get(JUMP_TICK);
   }

   public void setJumpTick(int jumpTick) {
      if (jumpTick < 0) {
         jumpTick = 0;
      }
      this.entityData.set(JUMP_TICK, jumpTick);
   }

   public int getAttackSwitchTick() {
      return this.entityData.get(ATTACK_SWITCH_COUNTDOWN);
   }

   public void setAttackSwitchTick(int switchTick) {
      this.entityData.set(ATTACK_SWITCH_COUNTDOWN, switchTick);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(1, new FloatGoal(this));
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
      this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0f));
      this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this));

      this.targetSelector.addGoal(2, new net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal<>(this, Player.class, true));
      this.targetSelector.addGoal(3, new net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal<>(this, net.minecraft.world.entity.animal.IronGolem.class, true));
   }

   public void setCombatTask() {
      LivingEntity entity = this.getTarget();
      this.goalSelector.removeGoal(this.aiBite);
      this.goalSelector.removeGoal(this.aiPounce);
      this.goalSelector.removeGoal(this.aiShoot);
      this.goalSelector.removeGoal(this.aiSwat);
      this.setAttackType(AttackType.BITE);
      if (this.level() != null && !this.level().isClientSide && entity != null) {
         Boolean isPanicked = this.getHealth() <= this.getMaxHealth() / 2.0f;
         Boolean targetIsWeakened = entity.getHealth() <= entity.getMaxHealth() / 2.0f;
         double distance = Utils.getDistanceDouble(this.getX(), this.getY(), this.getZ(), entity.getX(), entity.getY(), entity.getZ());
         if (entity.getVehicle() instanceof EntityWebbedCocoon) {
            if (!targetIsWeakened.booleanValue()) {
               double melee;
               double d = melee = isPanicked != false ? 0.55 : 0.45;
               if (this.random.nextDouble() < melee) {
                  this.goalSelector.addGoal(4, this.aiSwat);
                  this.setAttackType(AttackType.MELEE);
                  this.setAttackSwitchTick(300);
               } else {
                  this.goalSelector.addGoal(4, this.aiBite);
                  this.setAttackType(AttackType.BITE);
                  this.setAttackSwitchTick(300);
               }
            }
         } else if (!this.isVehicle()) {
            if (distance > 6.0) {
               double ranged;
               double d = ranged = isPanicked != false ? 0.8 : 0.2;
               if (this.random.nextDouble() < ranged) {
                  this.goalSelector.addGoal(3, this.aiPounce);
                  this.setAttackType(AttackType.POUNCE);
                  this.setAttackSwitchTick(300);
               } else {
                  this.goalSelector.addGoal(4, this.aiShoot);
                  this.setAttackType(AttackType.RANGED);
                  this.setAttackSwitchTick(300);
               }
            } else {
               double melee;
               double d = melee = isPanicked != false ? 0.55 : 0.45;
               if (this.random.nextDouble() < melee) {
                  this.goalSelector.addGoal(4, this.aiSwat);
                  this.setAttackType(AttackType.MELEE);
                  this.setAttackSwitchTick(300);
               } else {
                  this.goalSelector.addGoal(4, this.aiBite);
                  this.setAttackType(AttackType.BITE);
                  this.setAttackSwitchTick(300);
               }
            }
         } else if (!targetIsWeakened.booleanValue() || isPanicked.booleanValue()) {
         }
      }
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(ATTACK, (byte)0);
      this.entityData.define(JUMP_TICK, 0);
      this.entityData.define(ATTACK_SWITCH_COUNTDOWN, 0);
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setJumpTick(compound.getInt("jumpTick"));
      this.setAttackSwitchTick(compound.getInt("attackSwitchTick"));
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("jumpTick", this.getJumpTick());
      compound.putInt("attackSwitchTick", this.getAttackSwitchTick());
   }

   public static AttributeSupplier.Builder createAttributes() {

      return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.45).add(Attributes.MAX_HEALTH, 80.0).add(Attributes.FOLLOW_RANGE, 24.0).add(Attributes.ATTACK_DAMAGE, 8.0);
   }

   public void setAttackType(AttackType attackType) {
      this.entityData.set(ATTACK, (byte)attackType.id);
   }

   public AttackType getAttackType() {
      return AttackType.getFromId(this.entityData.get(ATTACK).byteValue());
   }

   @Override
   protected void customServerAiStep() {
      super.customServerAiStep();
      if (this.getAttackSwitchTick() == 0) {
         this.setCombatTask();
      } else {
         this.setAttackSwitchTick(this.getAttackSwitchTick() - 1);
      }
   }

   @Override
   public void tick() {
      super.tick();
   }

   public class EntityAIShootWeb extends Goal {
      private final EntityTarantula attacker;
      private LivingEntity attackTarget;
      private int rangedAttackTime = -1;
      private int seeTime;
      private final int attackIntervalMin;
      private final int maxRangedAttackTime;
      private final float attackRadius;
      private final float maxAttackDistance;
      private final double entityMoveSpeed;

      public EntityAIShootWeb(EntityTarantula attacker, double movespeed, int maxAttackTime, float maxAttackDistanceIn) {
         this(attacker, movespeed, maxAttackTime, maxAttackTime, maxAttackDistanceIn);
      }

      public EntityAIShootWeb(EntityTarantula attacker, double movespeed, int p_i1650_4_, int maxAttackTime, float maxAttackDistanceIn) {
         this.attacker = attacker;
         this.attackIntervalMin = p_i1650_4_;
         this.maxRangedAttackTime = maxAttackTime;
         this.entityMoveSpeed = movespeed;
         this.attackRadius = maxAttackDistanceIn;
         this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
         this.setFlags(java.util.EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
      }

      @Override
      public boolean canUse() {
         LivingEntity entitylivingbase = this.attacker.getTarget();
         if (entitylivingbase == null || entitylivingbase instanceof Spider || entitylivingbase.getVehicle() instanceof EntityWebbedCocoon) {
            this.attacker.setAttackSwitchTick(20);
            return false;
         }
         this.attackTarget = entitylivingbase;
         return true;
      }

      @Override
      public boolean canContinueToUse() {
         return this.canUse();
      }

      @Override
      public void stop() {
         this.attackTarget = null;
         this.seeTime = 0;
         this.rangedAttackTime = -1;
      }

      @Override
      public void tick() {
         double d0 = Utils.getDistanceDouble(this.attacker.getX(), this.attacker.getY(), this.attacker.getZ(), this.attackTarget.getX(), this.attackTarget.getY(), this.attackTarget.getZ());
         boolean flag = this.attacker.getSensing().hasLineOfSight(this.attackTarget);
         this.seeTime = !flag ? ++this.seeTime : 0;
         if (d0 <= (double)this.maxAttackDistance && this.seeTime <= 20) {
            this.attacker.getNavigation().stop();
         } else {
            this.attacker.getNavigation().moveTo(this.attackTarget, this.entityMoveSpeed);
         }
         if (--this.rangedAttackTime == 0) {
            if (!flag) {
               return;
            }
            float f = (float)(d0 / (double)this.attackRadius);
            float lvt_5_1_ = Mth.clamp(f, 0.1f, 1.0f);
            if (!this.attacker.level().isClientSide) {
               EntityWebProjectile entityarrow = new EntityWebProjectile(this.attacker.level(), this.attacker);
               entityarrow.setPos(this.attacker.getX(), this.attacker.getY() + 3.75, this.attacker.getZ());
               double d1 = this.attackTarget.getX() - this.attacker.getX();
               double d2 = this.attackTarget.getY() + (double)(this.attackTarget.getBbHeight() / 1.5f) - entityarrow.getY();
               double d3 = this.attackTarget.getZ() - this.attacker.getZ();
               double d4 = Math.sqrt(d1 * d1 + d3 * d3);
               entityarrow.shoot(d1, d2 + d4 * 0.030000074505806, d3, 1.6f, 7 - this.attacker.level().getDifficulty().getId() * 2);
               this.attacker.playSound(SoundEvents.LLAMA_SPIT, 1.0f, 1.0f / (this.attacker.getRandom().nextFloat() * 0.4f + 0.8f));
               this.attacker.level().addFreshEntity(entityarrow);
            }
            this.rangedAttackTime = Mth.floor(f * (float)(this.maxRangedAttackTime - this.attackIntervalMin) + (float)this.attackIntervalMin);
         } else if (this.rangedAttackTime < 0) {
            float f2 = (float)(d0 / (double)this.attackRadius);
            this.rangedAttackTime = Mth.floor(f2 * (float)(this.maxRangedAttackTime - this.attackIntervalMin) + (float)this.attackIntervalMin);
         }
      }
   }

   public class EntityAIPounceAttack extends Goal {
      EntityTarantula leaper;
      LivingEntity leapTarget;
      float leapMotionY;
      private int delayCounter;
      protected int attackTick;
      private int jumpTick;

      public EntityAIPounceAttack(EntityTarantula leapingEntity, float leapMotionYIn) {
         this.leaper = leapingEntity;
         this.leapMotionY = leapMotionYIn;
         this.setFlags(java.util.EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
      }

      @Override
      public boolean canUse() {
         this.leapTarget = this.leaper.getTarget();
         if (this.leapTarget == null) {
            return false;
         }
         double d0 = Utils.getDistanceDouble(this.leaper.getX(), this.leaper.getY(), this.leaper.getZ(), this.leapTarget.getX(), this.leapTarget.getY(), this.leapTarget.getZ());
         if (d0 >= 1.0 && d0 <= 8.0) {
            if (!this.leaper.onGround()) {
               return false;
            }
            return this.leaper.getRandom().nextInt(5) == 0;
         }
         return false;
      }

      @Override
      public boolean canContinueToUse() {
         if (this.leaper.onGround() && this.jumpTick > 26) {
            this.jumpTick = 0;
            this.leaper.setJumpTick(0);
            return false;
         }
         return true;
      }

      @Override
      public void tick() {
         ++this.jumpTick;
         this.leaper.setJumpTick(this.jumpTick);
         if (this.jumpTick == 25) {
            double d1 = this.leapTarget.getX() - this.leaper.getX();
            double d2 = this.leapTarget.getZ() - this.leaper.getZ();
            float f = Mth.sqrt((float)(d1 * d1 + d2 * d2));
            if ((double)f >= 1.0E-4) {
               net.minecraft.world.phys.Vec3 motion = this.leaper.getDeltaMovement();
               this.leaper.setDeltaMovement(motion.x + (d1 / (double)f * 0.5 * (double)0.8f + motion.x * (double)0.2f), motion.y, motion.z + (d2 / (double)f * 0.5 * (double)0.8f + motion.z * (double)0.2f));
            }
            this.leaper.setDeltaMovement(this.leaper.getDeltaMovement().x, this.leapMotionY, this.leaper.getDeltaMovement().z);
         }
         double d0 = Utils.getDistanceDouble(this.leaper.getX(), this.leaper.getY(), this.leaper.getZ(), this.leapTarget.getX(), this.leapTarget.getY(), this.leapTarget.getZ());
         --this.delayCounter;
         this.delayCounter = 4 + this.leaper.getRandom().nextInt(7);
         this.attackTick = Math.max(this.attackTick - 1, 0);
         if (this.jumpTick > 25) {
            this.checkAndPerformAttack(d0);
         }
      }

      protected void checkAndPerformAttack(double distance) {
         double d0 = this.getAttackReach();
         if (distance <= d0 && this.attackTick <= 0) {
            this.attackTick = 20;
            this.leapTarget.hurt(this.leaper.level().damageSources().mobAttack(this.leaper), (float)(7 + this.leaper.getRandom().nextInt(15)));
         }
      }

      protected double getAttackReach() {
         return ((double)this.leaper.getBbWidth() * 1.25 + (double)this.leapTarget.getBbWidth()) / 2.0;
      }
   }

   static class AISpiderSwat extends MeleeAttackGoal {
      public AISpiderSwat(EntityTarantula spider) {
         super(spider, 1.2, true);
      }

      @Override
      public boolean canContinueToUse() {
         float f = this.mob.getLightLevelDependentMagicValue();
         if (f >= 0.5f && this.mob.getRandom().nextInt(100) == 0) {
            this.mob.setTarget(null);
            return false;
         }
         return super.canContinueToUse();
      }

      @Override
      protected double getAttackReachSqr(LivingEntity attackTarget) {
         return 5.0f + attackTarget.getBbWidth();
      }
   }

   static class AISpiderBite extends MeleeAttackGoal {
      public AISpiderBite(EntityTarantula spider) {
         super(spider, 1.2, true);
      }

      @Override
      public boolean canContinueToUse() {
         float f = this.mob.getLightLevelDependentMagicValue();
         if (f >= 0.5f && this.mob.getRandom().nextInt(100) == 0) {
            this.mob.setTarget(null);
            return false;
         }
         return super.canContinueToUse();
      }

      @Override
      protected double getAttackReachSqr(LivingEntity attackTarget) {
         return 5.0f + attackTarget.getBbWidth();
      }
   }

   public static enum AttackType {
      BITE(0),
      POUNCE(1),
      MELEE(2),
      RANGED(3),
      GRAPPLE(4);

      private final int id;

      private AttackType(int idIn) {
         this.id = idIn;
      }

      public static AttackType getFromId(int idIn) {
         for (AttackType type : AttackType.values()) {
            if (idIn != type.id) continue;
            return type;
         }
         return BITE;
      }
   }
}
