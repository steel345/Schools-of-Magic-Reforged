package com.paleimitations.schoolsofmagic.common.entity;

import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EntityGaianWarrior extends net.minecraft.world.entity.animal.AbstractGolem {
   public static final int CLAY = 0;
   public static final int DIRT = 1;
   public static final int COBBLESTONE = 2;
   public static final int STONE = 3;
   public static final int DEEPSLATE = 4;

   private static final float[] HEALTH = {20.0F, 25.0F, 35.0F, 40.0F, 50.0F};
   private static final double[] DAMAGE = {4.0D, 6.0D, 7.0D, 8.0D, 12.0D};
   private static final double[] KNOCKBACK = {0.4D, 0.7D, 1.0D, 1.4D, 1.8D};

   private static final EntityDataAccessor<Integer> VARIANT =
      SynchedEntityData.defineId(EntityGaianWarrior.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> LIFE =
      SynchedEntityData.defineId(EntityGaianWarrior.class, EntityDataSerializers.INT);

   private static final int SPAWN_TICKS = 20;
   private static final int DESPAWN_TICKS = 20;
   private static final int DEATH_TICKS = 30;
   private static final int SWING_DELAY = 10;

   public final AnimationState idleState = new AnimationState();
   public final AnimationState attackState = new AnimationState();
   public final AnimationState spawnState = new AnimationState();
   public final AnimationState deathState = new AnimationState();
   public final AnimationState despawnState = new AnimationState();

   private UUID owner;
   private int attackAnimTick;
   private LivingEntity pendingTarget;
   private int windUp;

   public EntityGaianWarrior(EntityType<? extends EntityGaianWarrior> type, Level level) {
      super(type, level);
      this.setMaxUpStep(1.0F);
   }

   public static AttributeSupplier.Builder createAttributes() {
      return Mob.createMobAttributes()
         .add(Attributes.MAX_HEALTH, 20.0D)
         .add(Attributes.MOVEMENT_SPEED, 0.25D)
         .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
         .add(Attributes.ATTACK_DAMAGE, 4.0D)
         .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
         .add(Attributes.FOLLOW_RANGE, 32.0D);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
      this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
      this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.6D));
      this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
      this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
      this.goalSelector.addGoal(0, new FloatGoal(this));

      this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
      this.targetSelector.addGoal(2, new OwnerHurtByTargetGoal(this));
      this.targetSelector.addGoal(3, new OwnerHurtTargetGoal(this));
      this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Mob.class, 5, false, false,
         living -> living instanceof Enemy && !(living instanceof net.minecraft.world.entity.monster.Creeper)));
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.getEntityData().define(VARIANT, 0);
      this.getEntityData().define(LIFE, 300);
   }

   public int getVariant() {
      return net.minecraft.util.Mth.clamp(this.getEntityData().get(VARIANT), 0, HEALTH.length - 1);
   }

   public void setVariant(int variant) {
      int clamped = net.minecraft.util.Mth.clamp(variant, 0, HEALTH.length - 1);
      this.getEntityData().set(VARIANT, clamped);
      this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(HEALTH[clamped]);
      this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(DAMAGE[clamped]);
      this.getAttribute(Attributes.ATTACK_KNOCKBACK).setBaseValue(KNOCKBACK[clamped]);
      this.setHealth(HEALTH[clamped]);
   }

   public int getLife() {
      return this.getEntityData().get(LIFE);
   }

   public void setLife(int ticks) {
      this.getEntityData().set(LIFE, ticks);
   }

   public void setOwner(@Nullable LivingEntity living) {
      this.owner = living == null ? null : living.getUUID();
   }

   @Nullable
   public LivingEntity getOwner() {
      if (this.owner == null || !(this.level() instanceof ServerLevel server)) return null;
      Entity entity = server.getEntity(this.owner);
      return entity instanceof LivingEntity living ? living : null;
   }

   public boolean isOwner(Entity entity) {
      return entity != null && this.owner != null && this.owner.equals(entity.getUUID());
   }

   @Override
   public void tick() {
      super.tick();

      if (this.level().isClientSide) {
         this.animations();
         return;
      }

      this.swing();

      int life = this.getLife() - 1;
      this.setLife(life);
      if (life <= 0) {
         this.crumble();
      }
   }

   private void animations() {
      if (this.tickCount <= SPAWN_TICKS) {
         this.spawnState.startIfStopped(this.tickCount);
      }
      if (this.getLife() <= DESPAWN_TICKS) {
         this.despawnState.startIfStopped(this.tickCount);
      }
      if (this.attackAnimTick > 0) {
         this.attackAnimTick--;
      }
      this.idleState.animateWhen(this.attackAnimTick <= 0, this.tickCount);
   }

   @Override
   public boolean doHurtTarget(Entity target) {
      this.attackAnimTick = SWING_DELAY;
      this.level().broadcastEntityEvent(this, (byte) 4);
      if (target instanceof LivingEntity living) {
         this.pendingTarget = living;
         this.windUp = SWING_DELAY;
      }
      return true;
   }

   private void swing() {
      if (this.windUp <= 0 || --this.windUp > 0) return;

      LivingEntity target = this.pendingTarget;
      this.pendingTarget = null;
      if (target == null || !target.isAlive()) return;
      if (this.distanceToSqr(target) > this.getMeleeAttackRangeSqr(target) * 1.5D) return;

      if (super.doHurtTarget(target)) {
         this.playSound(this.stepSound(), 1.0F, 0.8F);
      }
   }

   // two golems raised by the same caster stand together, ones from different casters do not
   private boolean sameCaster(EntityGaianWarrior other) {
      return this.owner == null ? other.owner == null : this.owner.equals(other.owner);
   }

   @Override
   public boolean canAttack(LivingEntity target) {
      if (target instanceof EntityGaianWarrior other && this.sameCaster(other)) return false;
      return super.canAttack(target);
   }

   @Override
   public void setTarget(@Nullable LivingEntity target) {
      if (target instanceof EntityGaianWarrior other && this.sameCaster(other)) return;
      super.setTarget(target);
   }

   @Override
   public void handleEntityEvent(byte id) {
      if (id == 4) {
         this.attackAnimTick = 10;
         this.attackState.start(this.tickCount);
      } else if (id == 3) {
         this.deathState.start(this.tickCount);
         super.handleEntityEvent(id);
      } else {
         super.handleEntityEvent(id);
      }
   }

   private void crumble() {
      if (!this.level().isClientSide) {
         this.playSound(this.getDeathSound(), 1.0F, 0.9F);
      }
      this.discard();
   }

   private SoundEvent stepSound() {
      return switch (this.getVariant()) {
         case CLAY -> SoundEvents.GRAVEL_STEP;
         case DIRT -> SoundEvents.GRASS_STEP;
         case COBBLESTONE -> SoundEvents.STONE_STEP;
         case STONE -> SoundEvents.STONE_STEP;
         default -> SoundEvents.DEEPSLATE_STEP;
      };
   }

   @Override
   protected void playStepSound(net.minecraft.core.BlockPos pos, net.minecraft.world.level.block.state.BlockState state) {
      this.playSound(this.stepSound(), 1.0F, 0.7F);
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource source) {
      return switch (this.getVariant()) {
         case CLAY -> SoundEvents.GRAVEL_HIT;
         case DIRT -> SoundEvents.GRASS_HIT;
         case COBBLESTONE, STONE -> SoundEvents.STONE_HIT;
         default -> SoundEvents.DEEPSLATE_HIT;
      };
   }

   @Override
   protected SoundEvent getDeathSound() {
      return switch (this.getVariant()) {
         case CLAY -> SoundEvents.GRAVEL_BREAK;
         case DIRT -> SoundEvents.GRASS_BREAK;
         case COBBLESTONE, STONE -> SoundEvents.STONE_BREAK;
         default -> SoundEvents.DEEPSLATE_BREAK;
      };
   }

   @Override
   public boolean canAttackType(EntityType<?> type) {
      return type != EntityType.PLAYER && super.canAttackType(type);
   }

   @Override
   public void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
      tag.putInt("Variant", this.getVariant());
      tag.putInt("Life", this.getLife());
      if (this.owner != null) tag.putUUID("Owner", this.owner);
   }

   @Override
   public void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      this.setVariant(tag.getInt("Variant"));
      this.setLife(tag.getInt("Life"));
      if (tag.hasUUID("Owner")) this.owner = tag.getUUID("Owner");
   }

   // the golem answers for whoever raised it, the same way a tamed wolf does
   private static class OwnerHurtByTargetGoal extends net.minecraft.world.entity.ai.goal.target.TargetGoal {
      private final EntityGaianWarrior golem;
      private LivingEntity attacker;
      private int timestamp;

      OwnerHurtByTargetGoal(EntityGaianWarrior golem) {
         super(golem, false);
         this.golem = golem;
         this.setFlags(java.util.EnumSet.of(Goal.Flag.TARGET));
      }

      @Override
      public boolean canUse() {
         LivingEntity owner = this.golem.getOwner();
         if (owner == null) return false;
         this.attacker = owner.getLastHurtByMob();
         return owner.getLastHurtByMobTimestamp() != this.timestamp && this.canAttack(this.attacker, TargetingConditions.DEFAULT);
      }

      @Override
      public void start() {
         this.mob.setTarget(this.attacker);
         LivingEntity owner = this.golem.getOwner();
         if (owner != null) this.timestamp = owner.getLastHurtByMobTimestamp();
         super.start();
      }
   }

   private static class OwnerHurtTargetGoal extends net.minecraft.world.entity.ai.goal.target.TargetGoal {
      private final EntityGaianWarrior golem;
      private LivingEntity victim;
      private int timestamp;

      OwnerHurtTargetGoal(EntityGaianWarrior golem) {
         super(golem, false);
         this.golem = golem;
         this.setFlags(java.util.EnumSet.of(Goal.Flag.TARGET));
      }

      @Override
      public boolean canUse() {
         LivingEntity owner = this.golem.getOwner();
         if (owner == null) return false;
         this.victim = owner.getLastHurtMob();
         return owner.getLastHurtMobTimestamp() != this.timestamp && this.canAttack(this.victim, TargetingConditions.DEFAULT);
      }

      @Override
      public void start() {
         this.mob.setTarget(this.victim);
         LivingEntity owner = this.golem.getOwner();
         if (owner != null) this.timestamp = owner.getLastHurtMobTimestamp();
         super.start();
      }
   }

   // stays up long enough to finish falling apart, then goes with the usual puff of white
   @Override
   protected void tickDeath() {
      ++this.deathTime;
      if (this.deathTime >= DEATH_TICKS && !this.level().isClientSide() && !this.isRemoved()) {
         this.level().broadcastEntityEvent(this, (byte) 60);
         this.remove(RemovalReason.KILLED);
      }
   }
}
