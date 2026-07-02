package com.paleimitations.schoolsofmagic.common.entity;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class EntityAcolyteWisp extends PathfinderMob implements FlyingAnimal {

   private boolean resting;

   public EntityAcolyteWisp(EntityType<? extends PathfinderMob> type, Level level) {
      super(type, level);
      this.moveControl = new FlyingMoveControl(this, 20, true);
      this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
      this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16.0F);
      this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
      this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
   }

   public static AttributeSupplier.Builder createAttributes() {
      return Mob.createMobAttributes()
         .add(Attributes.MAX_HEALTH, 1.0D)
         .add(Attributes.FLYING_SPEED, 0.6D)
         .add(Attributes.MOVEMENT_SPEED, 0.3D)
         .add(Attributes.FOLLOW_RANGE, 16.0D);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(1, new WispRestGoal(this));
      this.goalSelector.addGoal(2, new WispFlyGoal(this, 1.0D));
      this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
      this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
   }

   @Override
   protected PathNavigation createNavigation(Level level) {
      FlyingPathNavigation nav = new FlyingPathNavigation(this, level);
      nav.setCanFloat(true);
      nav.setCanOpenDoors(false);
      nav.setCanPassDoors(true);
      return nav;
   }

   @Override
   public boolean isFlying() {
      return !this.onGround();
   }

   public boolean isResting() {
      return this.resting;
   }

   public void setResting(boolean resting) {
      this.resting = resting;
   }

   public static boolean checkSpawnRules(EntityType<EntityAcolyteWisp> type, LevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
      return true;
   }

   @Nullable
   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.BEE_LOOP;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource source) {
      return SoundEvents.BEE_LOOP_AGGRESSIVE;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.BEE_DEATH;
   }

   @Override
   public float getVoicePitch() {
      return 1.7F + this.getRandom().nextFloat() * 0.3F;
   }

   @Override
   protected float getSoundVolume() {
      return 0.4F;
   }

   @Override
   public SoundSource getSoundSource() {
      return SoundSource.NEUTRAL;
   }

   @Override
   protected void playStepSound(BlockPos pos, BlockState state) {
   }

   @Override
   public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {
      return false;
   }

   @Override
   protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
   }

   static class WispFlyGoal extends Goal {

      private final EntityAcolyteWisp wisp;
      private final double speed;
      private int stuckTicks;
      private int retargetTimer;

      WispFlyGoal(EntityAcolyteWisp wisp, double speed) {
         this.wisp = wisp;
         this.speed = speed;
         this.setFlags(EnumSet.of(Goal.Flag.MOVE));
      }

      @Override
      public boolean canUse() {
         return !this.wisp.isResting();
      }

      @Override
      public boolean canContinueToUse() {
         return !this.wisp.isResting();
      }

      @Override
      public void start() {
         this.pickTarget();
      }

      @Override
      public void tick() {
         net.minecraft.world.entity.ai.navigation.PathNavigation nav = this.wisp.getNavigation();

         if (this.wisp.getDeltaMovement().lengthSqr() < 0.0009D) {
            this.stuckTicks++;
         } else {
            this.stuckTicks = 0;
         }

         if (nav.isDone() || this.stuckTicks > 30 || --this.retargetTimer <= 0) {
            this.pickTarget();
            this.stuckTicks = 0;
            this.retargetTimer = 100 + this.wisp.getRandom().nextInt(100);
         }
      }

      private void pickTarget() {
         RandomSource r = this.wisp.getRandom();
         net.minecraft.world.level.Level lvl = this.wisp.level();
         for (int i = 0; i < 16; i++) {
            int tx = net.minecraft.util.Mth.floor(this.wisp.getX() + (r.nextDouble() * 2.0D - 1.0D) * 10.0D);
            int tz = net.minecraft.util.Mth.floor(this.wisp.getZ() + (r.nextDouble() * 2.0D - 1.0D) * 10.0D);
            int gy = lvl.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, tx, tz);
            int ty = gy + 1 + r.nextInt(5);
            BlockPos bp = new BlockPos(tx, ty, tz);
            if (lvl.getBlockState(bp).isAir() && this.wisp.getNavigation().moveTo(tx + 0.5D, ty + 0.5D, tz + 0.5D, this.speed)) {
               return;
            }
         }
         int gy = lvl.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            net.minecraft.util.Mth.floor(this.wisp.getX()), net.minecraft.util.Mth.floor(this.wisp.getZ()));
         this.wisp.getNavigation().moveTo(this.wisp.getX(), gy + 2.0D, this.wisp.getZ(), this.speed);
      }
   }

   static class WispRestGoal extends Goal {

      private final EntityAcolyteWisp wisp;
      private BlockPos leafPos;

      WispRestGoal(EntityAcolyteWisp wisp) {
         this.wisp = wisp;
         this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
      }

      @Override
      public boolean canUse() {
         if (!this.wisp.level().isNight()) {
            return false;
         }
         BlockPos found = this.findLeaf();
         if (found == null) {
            return false;
         }
         this.leafPos = found;
         return true;
      }

      @Override
      public boolean canContinueToUse() {
         return this.wisp.level().isNight() && this.leafPos != null
            && this.wisp.level().getBlockState(this.leafPos).is(BlockRegistry.leaves_ash.get());
      }

      @Override
      public void start() {
         if (this.leafPos != null) {
            this.wisp.getNavigation().moveTo(this.leafPos.getX() + 0.5D, this.leafPos.getY() + 0.5D, this.leafPos.getZ() + 0.5D, 1.0D);
         }
      }

      @Override
      public void stop() {
         this.wisp.setResting(false);
         this.wisp.getNavigation().stop();
         this.leafPos = null;
      }

      @Override
      public void tick() {
         if (this.leafPos == null) {
            return;
         }
         double dist = this.wisp.distanceToSqr(this.leafPos.getX() + 0.5D, this.leafPos.getY() + 0.5D, this.leafPos.getZ() + 0.5D);
         if (dist <= 2.25D) {
            this.wisp.getNavigation().stop();
            this.wisp.setResting(true);
            this.wisp.getLookControl().setLookAt(this.leafPos.getX() + 0.5D, this.leafPos.getY() + 0.5D, this.leafPos.getZ() + 0.5D);
         } else {
            this.wisp.setResting(false);
            if (this.wisp.getNavigation().isDone()) {
               this.wisp.getNavigation().moveTo(this.leafPos.getX() + 0.5D, this.leafPos.getY() + 0.5D, this.leafPos.getZ() + 0.5D, 1.0D);
            }
         }
      }

      @Nullable
      private BlockPos findLeaf() {
         BlockPos origin = this.wisp.blockPosition();
         BlockPos best = null;
         double bestDist = Double.MAX_VALUE;
         for (BlockPos p : BlockPos.betweenClosed(origin.offset(-12, -6, -12), origin.offset(12, 6, 12))) {
            BlockState state = this.wisp.level().getBlockState(p);
            if (state.is(BlockRegistry.leaves_ash.get())) {
               double d = origin.distSqr(p);
               if (d < bestDist) {
                  bestDist = d;
                  best = p.immutable();
               }
            }
         }
         return best;
      }
   }
}
