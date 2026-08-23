package com.paleimitations.schoolsofmagic.common.entity;

import java.util.UUID;

import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class EntityFlashDecoy extends LivingEntity {
   private static final EntityDataAccessor<Float> BLAST =
      SynchedEntityData.defineId(EntityFlashDecoy.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Byte> POSE_FLAGS =
      SynchedEntityData.defineId(EntityFlashDecoy.class, EntityDataSerializers.BYTE);

   private static final EntityDataAccessor<java.util.Optional<UUID>> OWNER =
      SynchedEntityData.defineId(EntityFlashDecoy.class, EntityDataSerializers.OPTIONAL_UUID);

   public static final int LIFETIME = 200;
   public static final double FLASH_RANGE = 6.0D;
   public static final int BLINDNESS_TICKS = 200;

   private final net.minecraft.world.item.ItemStack[] gear = new ItemStack[6];
   private boolean bursting;
   private ItemStack[] pending;

   public EntityFlashDecoy(EntityType<? extends EntityFlashDecoy> type, Level level) {
      super(type, level);
      java.util.Arrays.fill(this.gear, ItemStack.EMPTY);
      this.setInvulnerable(false);
   }

   public EntityFlashDecoy(Level level) {
      this(EntityRegistry.FLASH_DECOY.get(), level);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(BLAST, 4.0F);
      this.entityData.define(POSE_FLAGS, (byte) 0);
      this.entityData.define(OWNER, java.util.Optional.empty());
   }

   public void copyFrom(Player player, float blast) {
      this.entityData.set(OWNER, java.util.Optional.of(player.getUUID()));
      this.entityData.set(BLAST, blast);
      this.entityData.set(POSE_FLAGS, (byte) player.getPose().ordinal());
      this.moveTo(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
      this.yHeadRot = player.yHeadRot;
      this.yHeadRotO = player.yHeadRot;
      this.yBodyRot = player.yBodyRot;
      this.yBodyRotO = player.yBodyRot;
      this.setYRot(player.getYRot());
      this.yRotO = player.getYRot();
      this.setXRot(player.getXRot());
      this.xRotO = player.getXRot();

      this.setPose(player.getPose());
      this.setShiftKeyDown(player.isShiftKeyDown());
      this.pending = new ItemStack[6];
      for (EquipmentSlot slot : EquipmentSlot.values()) {
         this.pending[slot.ordinal()] = player.getItemBySlot(slot).copy();
      }
   }

   public UUID getOwnerId() {
      return this.entityData.get(OWNER).orElse(null);
   }

   @Override
   public net.minecraft.network.chat.Component getName() {
      UUID owner = this.getOwnerId();
      if (owner != null && this.level() != null) {
         Player p = this.level().getPlayerByUUID(owner);
         if (p != null) return p.getName();
      }
      return super.getName();
   }

   @Override
   public boolean shouldShowName() {
      return true;
   }

   public net.minecraft.world.entity.Pose storedPose() {
      byte i = this.entityData.get(POSE_FLAGS);
      net.minecraft.world.entity.Pose[] all = net.minecraft.world.entity.Pose.values();
      return i >= 0 && i < all.length ? all[i] : net.minecraft.world.entity.Pose.STANDING;
   }

   public boolean isCrouchingPose() {
      return this.storedPose() == net.minecraft.world.entity.Pose.CROUCHING;
   }

   @Override
   public boolean isNoGravity() {
      return true;
   }

   @Override
   public void travel(net.minecraft.world.phys.Vec3 movement) {
   }

   @Override
   public boolean isAffectedByFluids() {
      return false;
   }

   @Override
   public boolean onClimbable() {
      return false;
   }

   @Override
   public void tick() {
      super.tick();
      this.setNoGravity(true);
      this.setDeltaMovement(net.minecraft.world.phys.Vec3.ZERO);

      this.setPose(this.storedPose());
      if (!this.level().isClientSide) {
         if (this.pending != null) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
               this.setItemSlot(slot, this.pending[slot.ordinal()]);
            }
            this.pending = null;
         }
         if (this.tickCount == 1) this.drawAttention();
         if (this.tickCount > LIFETIME) this.discard();
      }
   }

   private void drawAttention() {
      UUID owner = this.getOwnerId();
      if (owner == null) return;
      AABB around = this.getBoundingBox().inflate(24.0D);
      for (Mob mob : this.level().getEntitiesOfClass(Mob.class, around)) {
         LivingEntity target = mob.getTarget();
         if (target instanceof Player p && p.getUUID().equals(owner)) {
            mob.setTarget(this);
         }
      }
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      if (this.level().isClientSide) return false;

      if (this.bursting || this.isRemoved()) return false;
      this.burst();
      return true;
   }

   private void burst() {
      this.bursting = true;

      this.discard();
      float blast = this.entityData.get(BLAST);
      AABB reach = this.getBoundingBox().inflate(FLASH_RANGE);
      for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class, reach)) {
         if (living == this || !living.isAlive()) continue;
         if (living instanceof EntityFlashDecoy) continue;
         if (living instanceof Player p && p.getUUID().equals(this.getOwnerId())) continue;
         living.hurt(this.level().damageSources().magic(), blast);
         living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, BLINDNESS_TICKS, 0, false, true, true));
      }
      this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
         SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 1.2F, 1.6F);
      if (this.level() instanceof ServerLevel sl) {
         sl.sendParticles(net.minecraft.core.particles.ParticleTypes.FLASH,
            this.getX(), this.getY() + 1.0D, this.getZ(), 4, 0.0D, 0.0D, 0.0D, 0.0D);
         sl.sendParticles(net.minecraft.core.particles.ParticleTypes.END_ROD,
            this.getX(), this.getY() + 1.0D, this.getZ(), 60, 0.4D, 0.8D, 0.4D, 0.35D);
      }
   }

   @Override
   public Iterable<ItemStack> getArmorSlots() {
      return java.util.List.of(this.gear[2], this.gear[3], this.gear[4], this.gear[5]);
   }

   @Override
   public ItemStack getItemBySlot(EquipmentSlot slot) {
      return this.gear[slot.ordinal()];
   }

   @Override
   public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
      this.gear[slot.ordinal()] = stack;
   }

   @Override
   public HumanoidArm getMainArm() {
      return HumanoidArm.RIGHT;
   }

   @Override
   public boolean isPickable() {
      return true;
   }

   @Override
   public boolean isPushable() {
      return false;
   }

   @Override
   public void addAdditionalSaveData(CompoundTag nbt) {
      UUID owner = this.getOwnerId();
      if (owner != null) nbt.putUUID("Owner", owner);
      nbt.putFloat("Blast", this.entityData.get(BLAST));
   }

   @Override
   public void readAdditionalSaveData(CompoundTag nbt) {
      if (nbt.hasUUID("Owner")) this.entityData.set(OWNER, java.util.Optional.of(nbt.getUUID("Owner")));
      if (nbt.contains("Blast")) this.entityData.set(BLAST, nbt.getFloat("Blast"));
   }
}
