package com.paleimitations.schoolsofmagic.common.entity;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

public class EntityTadpole extends Animal {
   private static final EntityDataAccessor<Integer> TOAD_TYPE = SynchedEntityData.defineId(EntityTadpole.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Boolean> MOVING = SynchedEntityData.defineId(EntityTadpole.class, EntityDataSerializers.BOOLEAN);
   protected RandomStrollGoal wander;

   public EntityTadpole(EntityType<? extends Animal> type, Level worldIn) {
      super(type, worldIn);
      this.moveControl = new TadMoveHelper(this);
   }

   @Override
   protected PathNavigation createNavigation(Level worldIn) {
      return new WaterBoundPathNavigation(this, worldIn);
   }

   @Override
   public void travel(Vec3 travelVector) {
      if (this.isEffectiveAi() && this.isInWater()) {
         this.moveRelative(0.1f, travelVector);
         this.move(MoverType.SELF, this.getDeltaMovement());
         this.setDeltaMovement(this.getDeltaMovement().scale(0.9f));
         if (!this.isMoving() && this.getTarget() == null) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
         }
      } else {
         super.travel(travelVector);
      }
   }

   protected SoundEvent getFlopSound() {
      return SoundEvents.GUARDIAN_FLOP;
   }

   @Override
   public void aiStep() {
      if (this.level().isClientSide && !this.isInWater() && this.getDeltaMovement().y > 0.0 && !this.isSilent()) {
         this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), this.getFlopSound(), this.getSoundSource(), 1.0f, 1.0f, false);
      }
      if (this.isInWater()) {
         this.setAirSupply(300);
      } else if (this.onGround()) {
         this.setDeltaMovement(this.getDeltaMovement().add((this.random.nextFloat() * 2.0f - 1.0f) * 0.4f, 0.5, (this.random.nextFloat() * 2.0f - 1.0f) * 0.4f));
         this.setYRot(this.random.nextFloat() * 360.0f);
         this.setOnGround(false);
         this.hasImpulse = true;
      }
      super.aiStep();
   }

   public boolean isMovementNoisy() {
      return false;
   }

   public int getToadType() {
      return this.entityData.get(TOAD_TYPE);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(TOAD_TYPE, 0);
      this.entityData.define(MOVING, false);
   }

   public boolean isMoving() {
      return this.entityData.get(MOVING);
   }

   private void setMoving(boolean moving) {
      this.entityData.set(MOVING, moving);
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("ToadType", this.getToadType());
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setToadType(compound.getInt("ToadType"));
   }

   public void setToadType(int toadTypeId) {
      if (toadTypeId == 7) {
         net.minecraft.world.entity.ai.attributes.AttributeInstance attack = this.getAttribute(Attributes.ATTACK_DAMAGE);
         if (attack != null) {
            attack.setBaseValue(8.0);
         }
         this.goalSelector.addGoal(4, new AIEvilAttack(this));
         this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
         this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
      }
      this.entityData.set(TOAD_TYPE, toadTypeId);
   }

   @Override
   protected void registerGoals() {
      MoveTowardsRestrictionGoal movetowardsrestriction = new MoveTowardsRestrictionGoal(this, 1.0);
      this.wander = new RandomStrollGoal(this, 1.0, 80);
      this.goalSelector.addGoal(1, new AIPanic(this, 2.2));
      this.goalSelector.addGoal(5, movetowardsrestriction);
      this.goalSelector.addGoal(7, this.wander);
      this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0f));
      this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Guardian.class, 12.0f, 0.01f));
      this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
      this.wander.setFlags(java.util.EnumSet.of(net.minecraft.world.entity.ai.goal.Goal.Flag.MOVE));
      movetowardsrestriction.setFlags(java.util.EnumSet.of(net.minecraft.world.entity.ai.goal.Goal.Flag.MOVE));
   }

   @Nullable
   @Override
   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob ageable) {
      return null;
   }

   @Override
   public boolean isPushedByFluid() {
      return false;
   }

   @Override
   public boolean canBreatheUnderwater() {
      return true;
   }

   public static AttributeSupplier.Builder createAttributes() {
      return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 3.0).add(Attributes.MOVEMENT_SPEED, 0.3f).add(Attributes.ATTACK_DAMAGE, 1.0);
   }

   public void setMovementSpeed(double newSpeed) {
      this.getNavigation().setSpeedModifier(newSpeed);
      this.moveControl.setWantedPosition(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ(), newSpeed);
   }

   @Override
   public void baseTick() {
      int i = this.getAirSupply();
      super.baseTick();
      if (this.isAlive() && !this.isInWater()) {
         this.setAirSupply(--i);
         if (this.getAirSupply() == -20) {
            this.setAirSupply(0);
            this.hurt(this.level().damageSources().drown(), 2.0f);
         }
      } else {
         this.setAirSupply(300);
      }
      if (this.isAlive() && this.tickCount >= 9000 && !this.level().isClientSide) {
         this.discard();
         EntityToad toad = new EntityToad(com.paleimitations.schoolsofmagic.common.registries.EntityRegistry.TOAD.get(), this.level());
         toad.setToadType(this.getToadType());
         toad.moveTo(this.getX(), this.getY(), this.getZ(), 0.0f, 0.0f);
         this.level().addFreshEntity(toad);
      }
   }

   static class TadMoveHelper extends MoveControl {
      private final EntityTadpole entityGuardian;

      public TadMoveHelper(EntityTadpole guardian) {
         super(guardian);
         this.entityGuardian = guardian;
      }

      @Override
      public void tick() {
         if (this.operation == MoveControl.Operation.MOVE_TO && !this.entityGuardian.getNavigation().isDone()) {
            double d0 = this.wantedX - this.entityGuardian.getX();
            double d1 = this.wantedY - this.entityGuardian.getY();
            double d2 = this.wantedZ - this.entityGuardian.getZ();
            double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
            d1 /= d3;
            float f = (float)(Mth.atan2(d2, d0) * 57.29577951308232) - 90.0f;
            this.entityGuardian.setYRot(this.rotlerp(this.entityGuardian.getYRot(), f, 90.0f));
            this.entityGuardian.yBodyRot = this.entityGuardian.getYRot();
            float f1 = (float)(this.speedModifier * this.entityGuardian.getAttributeValue(Attributes.MOVEMENT_SPEED));
            this.entityGuardian.setSpeed(this.entityGuardian.getSpeed() + (f1 - this.entityGuardian.getSpeed()) * 0.125f);
            double d4 = Math.sin((this.entityGuardian.tickCount + this.entityGuardian.getId()) * 0.5) * 0.05;
            double d5 = Math.cos(this.entityGuardian.getYRot() * ((float)Math.PI / 180));
            double d6 = Math.sin(this.entityGuardian.getYRot() * ((float)Math.PI / 180));
            Vec3 motion = this.entityGuardian.getDeltaMovement();
            motion = motion.add(d4 * d5, 0.0, d4 * d6);
            d4 = Math.sin((this.entityGuardian.tickCount + this.entityGuardian.getId()) * 0.75) * 0.05;
            motion = motion.add(0.0, d4 * (d6 + d5) * 0.25 + this.entityGuardian.getSpeed() * d1 * 0.1, 0.0);
            this.entityGuardian.setDeltaMovement(motion);
            net.minecraft.world.entity.ai.control.LookControl lookControl = this.entityGuardian.getLookControl();
            double d7 = this.entityGuardian.getX() + d0 / d3 * 2.0;
            double d8 = this.entityGuardian.getEyeY() + d1 / d3;
            double d9 = this.entityGuardian.getZ() + d2 / d3 * 2.0;
            double d10 = lookControl.getWantedX();
            double d11 = lookControl.getWantedY();
            double d12 = lookControl.getWantedZ();
            if (!lookControl.isLookingAtTarget()) {
               d10 = d7;
               d11 = d8;
               d12 = d9;
            }
            this.entityGuardian.getLookControl().setLookAt(d10 + (d7 - d10) * 0.125, d11 + (d8 - d11) * 0.125, d12 + (d9 - d12) * 0.125, 10.0f, 40.0f);
            this.entityGuardian.setMoving(true);
         } else {
            this.entityGuardian.setSpeed(0.0f);
            this.entityGuardian.setMoving(false);
         }
      }
   }

   static class AIEvilAttack extends MeleeAttackGoal {
      public AIEvilAttack(EntityTadpole toad) {
         super(toad, 1.4, true);
      }

      @Override
      protected double getAttackReachSqr(LivingEntity attackTarget) {
         return 4.0f + attackTarget.getBbWidth();
      }
   }

   static class AIPanic extends PanicGoal {
      private final EntityTadpole toad;

      public AIPanic(EntityTadpole toad, double speedIn) {
         super(toad, speedIn);
         this.toad = toad;
      }

      @Override
      public void tick() {
         super.tick();
         this.toad.setMovementSpeed(this.speedModifier);
      }
   }

   static class AIAvoidEntity<T extends LivingEntity> extends net.minecraft.world.entity.ai.goal.AvoidEntityGoal<T> {
      private final EntityTadpole toad;

      public AIAvoidEntity(EntityTadpole toad, Class<T> p_i46403_2_, float p_i46403_3_, double p_i46403_4_, double p_i46403_6_) {
         super(toad, p_i46403_2_, p_i46403_3_, p_i46403_4_, p_i46403_6_);
         this.toad = toad;
      }

      @Override
      public boolean canUse() {
         return super.canUse();
      }
   }
}
