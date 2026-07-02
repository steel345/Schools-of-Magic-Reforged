package com.paleimitations.schoolsofmagic.common.entity;

import com.paleimitations.schoolsofmagic.common.blocks.BlockToadSpawn;
import com.paleimitations.schoolsofmagic.common.entity.ai.EntityToadAIMate;
import com.paleimitations.schoolsofmagic.common.handlers.LootTableHandlers;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.registries.BiomeRegistry;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityToad extends Animal {
   private static final EntityDataAccessor<Integer> TOAD_TYPE = SynchedEntityData.defineId(EntityToad.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Boolean> PREGNANT = SynchedEntityData.defineId(EntityToad.class, EntityDataSerializers.BOOLEAN);
   private int jumpTicks;
   private int jumpDuration;
   private boolean wasOnGround;
   private int currentMoveTypeDuration;
   public int mouthCounter;
   private int f = 0;

   public EntityToad(EntityType<? extends Animal> type, Level level) {
      super(type, level);
      this.jumpControl = new ToadJumpHelper(this);
      this.moveControl = new ToadMoveHelper(this);
      this.setMovementSpeed(0.0);
   }

   public static AttributeSupplier.Builder createAttributes() {
      return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.3D);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(1, new FloatGoal(this));
      this.goalSelector.addGoal(1, new AIPanic(this, 2.2D));
      this.goalSelector.addGoal(2, new ToadBreedGoal(this, 1.0D));
      this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, Ingredient.of(Items.SPIDER_EYE), false));
      this.goalSelector.addGoal(4, new AIAvoidEntity<>(this, Player.class, 8.0F, 2.2D, 2.2D));
      this.goalSelector.addGoal(4, new AIAvoidEntity<>(this, Monster.class, 4.0F, 2.2D, 2.2D));
      this.goalSelector.addGoal(5, new MoveToBlockGoal(this, 0.5, 24) {
         @Override
         protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
            return EntityToad.this.isPregnant()
               && worldIn.getBlockState(pos).getBlock() == Blocks.WATER
               && worldIn.getBlockState(pos.above()).getBlock() == Blocks.AIR;
         }
      });
      this.goalSelector.addGoal(5, new MoveToBlockGoal(this, 0.5, 20) {
         @Override
         protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
            boolean flag = false;
            if (worldIn.getBlockState(pos).getBlock() == Blocks.LILY_PAD && worldIn.getBlockState(pos.above()).getBlock() == Blocks.AIR) {
               flag = true;
            }
            return flag;
         }
      });
   }

   public void setPregnant(boolean pregnant) {
      this.entityData.set(PREGNANT, pregnant);
   }

   public boolean isPregnant() {
      return this.entityData.get(PREGNANT);
   }

   @Override
   public boolean canBeCollidedWith() {
      return false;
   }

   @Override
   public boolean canBreatheUnderwater() {
      return true;
   }

   @Override
   protected float getJumpPower() {
      float addition = 0.2F;
      if (!(this.horizontalCollision || this.moveControl.hasWanted() && !(this.moveControl.getWantedY() <= this.getY() + 0.5))) {
         Path path = this.getNavigation().getPath();
         if (path != null && !path.isDone()) {
            Vec3 vec3d = path.getNextEntityPos(this);
            if (vec3d.y > this.getY() + 0.5) {
               return 0.5F + addition;
            }
         }
         return this.moveControl.getSpeedModifier() <= 0.6D + (double)addition ? 0.2F + addition : 0.3F + addition;
      }
      return 0.5F + addition;
   }

   @Override
   protected void jumpFromGround() {
      double d1;
      super.jumpFromGround();
      double d0 = this.moveControl.getSpeedModifier();
      if (d0 > 0.0 && (d1 = this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z) < 0.010000000000000002) {
         this.moveRelative(0.0F, new Vec3(0.0F, 0.0F, 1.0F));
      }
      if (!this.level().isClientSide) {
         this.level().broadcastEntityEvent(this, (byte)1);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public float setJumpCompletion(float p_175521_1_) {
      return this.jumpDuration == 0 ? 0.0F : ((float)this.jumpTicks + p_175521_1_) / (float)this.jumpDuration;
   }

   public void setMovementSpeed(double newSpeed) {
      this.getNavigation().setSpeedModifier(newSpeed);
      this.moveControl.setWantedPosition(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ(), newSpeed);
   }

   @Override
   public void setJumping(boolean jumping) {
      super.setJumping(jumping);
   }

   public void startJumping() {
      this.setJumping(true);
      this.jumpDuration = 10;
      this.jumpTicks = 0;
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(TOAD_TYPE, 0);
      this.entityData.define(PREGNANT, false);
   }

   @Override
   public void customServerAiStep() {
      if (this.currentMoveTypeDuration > 0) {
         --this.currentMoveTypeDuration;
      }
      if (this.onGround()) {
         ToadJumpHelper entitytoad$toadjumphelper;
         LivingEntity entitylivingbase;
         if (!this.wasOnGround) {
            this.setJumping(false);
            this.checkLandingDelay();
         }
         if (this.getToadType() == 7 && this.currentMoveTypeDuration == 0 && (entitylivingbase = this.getTarget()) != null && this.distanceToSqr(entitylivingbase) < 16.0) {
            this.calculateRotationYaw(entitylivingbase.getX(), entitylivingbase.getZ());
            this.moveControl.setWantedPosition(entitylivingbase.getX(), entitylivingbase.getY(), entitylivingbase.getZ(), this.moveControl.getSpeedModifier());
            this.startJumping();
            this.wasOnGround = true;
         }
         if (!(entitytoad$toadjumphelper = (ToadJumpHelper)this.jumpControl).getIsJumping()) {
            if (this.moveControl.hasWanted() && this.currentMoveTypeDuration == 0) {
               Path path = this.getNavigation().getPath();
               Vec3 vec3d = new Vec3(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ());
               if (path != null && !path.isDone()) {
                  vec3d = path.getNextEntityPos(this);
               }
               this.calculateRotationYaw(vec3d.x, vec3d.z);
               this.startJumping();
            }
         } else if (!entitytoad$toadjumphelper.canJump()) {
            this.enableJumpControl();
         }
      }
      this.wasOnGround = this.onGround();
   }

   @Override
   public void updateSwimming() {
   }

   private void calculateRotationYaw(double x, double z) {
      this.setYRot((float)(Mth.atan2(z - this.getZ(), x - this.getX()) * 57.29577951308232) - 90.0F);
   }

   private void enableJumpControl() {
      ((ToadJumpHelper)this.jumpControl).setCanJump(true);
   }

   private void disableJumpControl() {
      ((ToadJumpHelper)this.jumpControl).setCanJump(false);
   }

   private void updateMoveTypeDuration() {
      this.currentMoveTypeDuration = this.moveControl.getSpeedModifier() < 2.2 ? 10 : 1;
   }

   private void checkLandingDelay() {
      this.updateMoveTypeDuration();
      this.disableJumpControl();
   }

   @Override
   public void baseTick() {
      super.baseTick();
      if (this.isPregnant() && this.isInWater() && this.level().getBlockState(this.blockPosition()).getBlock() == Blocks.WATER && this.level().isEmptyBlock(this.blockPosition().above())) {
         this.level().setBlock(this.blockPosition().above(), BlockRegistry.spawn.get().defaultBlockState().setValue(BlockToadSpawn.TOAD_TYPE, Integer.valueOf(this.getToadType())), 3);
         this.setPregnant(false);
      }
   }

   @Override
   public void aiStep() {
      super.aiStep();
      Random rand = new Random();
      if (rand.nextInt(200) == 1) {
         this.croak();
      }
      if (this.mouthCounter == 1 && this.tickCount >= this.f) {
         this.mouthCounter = 0;
      }
      if (this.jumpTicks != this.jumpDuration) {
         ++this.jumpTicks;
      } else if (this.jumpDuration != 0) {
         this.jumpTicks = 0;
         this.jumpDuration = 0;
         this.setJumping(false);
      }
   }

   private void croak() {
      this.playSound(SOMSoundHandler.TOAD_CROAK.get(), 0.75F, 1.0F);
      this.mouthCounter = 1;
      this.f = this.tickCount + 12;
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("ToadType", this.getToadType());
      compound.putBoolean("Pregnant", this.isPregnant());
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setToadType(compound.getInt("ToadType"));
      this.setPregnant(compound.getBoolean("Pregnant"));
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SOMSoundHandler.TOAD_AMBIENT.get();
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return SOMSoundHandler.TOAD_INJURED.get();
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SOMSoundHandler.TOAD_DEATH.get();
   }

   @Override
   public boolean doHurtTarget(Entity entityIn) {
      if (this.getToadType() == 7) {
         this.croak();
         return entityIn.hurt(this.level().damageSources().mobAttack(this), 8.0F);
      }
      return entityIn.hurt(this.level().damageSources().mobAttack(this), 3.0F);
   }

   @Override
   public SoundSource getSoundSource() {
      return this.getToadType() == 7 ? SoundSource.HOSTILE : SoundSource.NEUTRAL;
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      return this.isInvulnerableTo(source) ? false : super.hurt(source, amount);
   }

   @Nullable
   @Override
   public ResourceLocation getDefaultLootTable() {
      return LootTableHandlers.TOAD;
   }

   private boolean isToadBreedingItem(Item itemIn) {
      return itemIn == Items.SPIDER_EYE;
   }

   @Nullable
   @Override
   public AgeableMob getBreedOffspring(net.minecraft.server.level.ServerLevel level, AgeableMob otherParent) {
      return null;
   }

   @Override
   public boolean isFood(ItemStack stack) {
      return this.isToadBreedingItem(stack.getItem());
   }

   static class ToadBreedGoal extends net.minecraft.world.entity.ai.goal.BreedGoal {
      public ToadBreedGoal(net.minecraft.world.entity.animal.Animal animal, double speed) {
         super(animal, speed);
      }

      @Override
      protected void breed() {
         net.minecraft.server.level.ServerPlayer cause = this.animal.getLoveCause();
         if (cause == null && this.partner != null && this.partner.getLoveCause() != null) {
            cause = this.partner.getLoveCause();
         }
         if (cause != null) {
            cause.awardStat(net.minecraft.stats.Stats.ANIMALS_BRED);
            net.minecraft.advancements.CriteriaTriggers.BRED_ANIMALS.trigger(cause, this.animal, this.partner, null);
         }
         if (this.animal instanceof EntityToad toad) {
            toad.setPregnant(true);
         }
         this.animal.setAge(6000);
         this.partner.setAge(6000);
         this.animal.resetLove();
         this.partner.resetLove();
         net.minecraft.world.level.Level lvl = this.animal.level();
         if (lvl instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            serverLevel.broadcastEntityEvent(this.animal, (byte) 18);
            if (lvl.getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_DOMOBLOOT)) {
               lvl.addFreshEntity(new net.minecraft.world.entity.ExperienceOrb(lvl,
                  this.animal.getX(), this.animal.getY(), this.animal.getZ(), this.animal.getRandom().nextInt(7) + 1));
            }
         }
      }
   }

   public int getToadType() {
      return this.entityData.get(TOAD_TYPE);
   }

   public void setToadType(int toadTypeId) {
      if (toadTypeId == 7) {
         this.getAttribute(Attributes.ARMOR).setBaseValue(8.0D);
         this.goalSelector.addGoal(4, new AIEvilAttack(this));
         this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
         this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
      }
      this.entityData.set(TOAD_TYPE, toadTypeId);
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {

      spawnData = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
      int i = this.getRandomToadType();
      boolean flag = false;
      if (spawnData instanceof ToadTypeData) {
         i = ((ToadTypeData)spawnData).typeData;
         flag = true;
      } else {
         spawnData = new ToadTypeData(i);
      }
      this.setToadType(i);
      if (flag) {
         this.setAge(-24000);
      }
      return spawnData;
   }

   public static boolean checkToadSpawnRules(EntityType<? extends Animal> type, ServerLevelAccessor level,
         MobSpawnType reason, BlockPos pos, net.minecraft.util.RandomSource rand) {
      BlockState below = level.getBlockState(pos.below());
      boolean groundOk = below.isFaceSturdy(level, pos.below(), net.minecraft.core.Direction.UP)
         || below.is(net.minecraft.tags.BlockTags.ANIMALS_SPAWNABLE_ON);
      return groundOk && hasWaterWithin(level, pos, 15);
   }

   private static boolean hasWaterWithin(LevelReader level, BlockPos center, int radius) {
      BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos();
      for (int dx = -radius; dx <= radius; dx += 2) {
         for (int dz = -radius; dz <= radius; dz += 2) {
            for (int dy = -5; dy <= 3; dy += 2) {
               m.set(center.getX() + dx, center.getY() + dy, center.getZ() + dz);
               if (level.getFluidState(m).is(net.minecraft.tags.FluidTags.WATER)) {
                  return true;
               }
            }
         }
      }
      return false;
   }

   public int getRandomToadType() {
      int j = this.getRandom().nextInt(99);
      int i = 0 <= j && j <= 11 ? 0 : (12 <= j && j <= 23 ? 1 : (24 <= j && j <= 35 ? 2 : (36 <= j && j <= 47 ? 3 : (48 <= j && j <= 59 ? 4 : (60 <= j && j <= 71 ? 5 : (72 <= j && j <= 77 ? 8 : (78 <= j && j <= 83 ? 9 : (84 <= j && j <= 89 ? 10 : (90 <= j && j <= 95 ? 11 : (96 <= j && j <= 98 ? 6 : 7))))))))));
      return i;
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void handleEntityEvent(byte id) {
      if (id == 1) {
         this.spawnSprintParticle();
         this.jumpDuration = 15;
         this.jumpTicks = 0;
      } else {
         super.handleEntityEvent(id);
      }
   }

   static class AISitOnLily extends MoveToBlockGoal {
      private final EntityToad toad;
      private boolean canSit;

      public AISitOnLily(EntityToad toadIn, double speedIn) {
         super(toadIn, speedIn, 16);
         this.toad = toadIn;
      }

      @Override
      public boolean canUse() {
         return super.canUse();
      }

      @Override
      public boolean canContinueToUse() {
         return super.canContinueToUse();
      }

      @Override
      public void tick() {
         super.tick();
         this.toad.getLookControl().setLookAt(this.blockPos.getX() + 0.5, this.blockPos.getY() + 1, this.blockPos.getZ() + 0.5, 10.0F, this.toad.getMaxHeadXRot());
         if (this.isReachedTarget()) {
            Level world = this.toad.level();
            BlockPos blockpos = this.blockPos.above();
            BlockState iblockstate = world.getBlockState(blockpos);
            if (this.canSit && iblockstate.isAir()) {
               world.levelEvent(2001, blockpos, net.minecraft.world.level.block.Block.getId(iblockstate));
            }
         }
      }

      @Override
      protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
         BlockState st = worldIn.getBlockState(pos);
         if (st.getBlock() == Blocks.LILY_PAD && !this.canSit && worldIn.getBlockState(pos.above()).isAir()) {
            this.canSit = true;
            return true;
         }
         return false;
      }
   }

   public static class ToadTypeData extends net.minecraft.world.entity.AgeableMob.AgeableMobGroupData {
      public int typeData;

      public ToadTypeData(int type) {
         super(false);
         this.typeData = type;
      }
   }

   static class ToadMoveHelper extends MoveControl {
      private final EntityToad toad;
      private double nextJumpSpeed;

      public ToadMoveHelper(EntityToad toad) {
         super(toad);
         this.toad = toad;
      }

      @Override
      public void tick() {
         if (this.toad.onGround() && !this.toad.jumping && !((ToadJumpHelper)this.toad.jumpControl).getIsJumping()) {
            this.toad.setMovementSpeed(0.0);
         } else if (this.hasWanted()) {
            this.toad.setMovementSpeed(this.nextJumpSpeed);
         }
         super.tick();
      }

      @Override
      public void setWantedPosition(double x, double y, double z, double speedIn) {
         if (this.toad.isInWater()) {
            speedIn = 1.5;
         }
         super.setWantedPosition(x, y, z, speedIn);
         if (speedIn > 0.0) {
            this.nextJumpSpeed = speedIn;
         }
      }
   }

   public class ToadJumpHelper extends JumpControl {
      private final EntityToad toad;
      private boolean canJump;

      public ToadJumpHelper(EntityToad toad) {
         super(toad);
         this.toad = toad;
      }

      public boolean getIsJumping() {
         return this.jump;
      }

      public boolean canJump() {
         return this.canJump;
      }

      public void setCanJump(boolean canJumpIn) {
         this.canJump = canJumpIn;
      }

      @Override
      public void tick() {
         if (this.jump) {
            this.toad.startJumping();
            this.jump = false;
         }
      }
   }

   static class AIPanic extends PanicGoal {
      private final EntityToad toad;

      public AIPanic(EntityToad toad, double speedIn) {
         super(toad, speedIn);
         this.toad = toad;
      }

      @Override
      public void tick() {
         super.tick();
         this.toad.setMovementSpeed(this.speedModifier);
      }
   }

   static class AIEvilAttack extends MeleeAttackGoal {
      public AIEvilAttack(EntityToad toad) {
         super(toad, 1.4, true);
      }

      @Override
      protected double getAttackReachSqr(LivingEntity attackTarget) {
         return 4.0F + attackTarget.getBbWidth();
      }
   }

   static class AIAvoidEntity<T extends LivingEntity> extends AvoidEntityGoal<T> {
      private final EntityToad toad;

      public AIAvoidEntity(EntityToad toad, Class<T> p_i46403_2_, float p_i46403_3_, double p_i46403_4_, double p_i46403_6_) {
         super(toad, p_i46403_2_, p_i46403_3_, p_i46403_4_, p_i46403_6_);
         this.toad = toad;
      }

      @Override
      public boolean canUse() {
         return this.toad.getToadType() != 7 && super.canUse();
      }
   }
}
