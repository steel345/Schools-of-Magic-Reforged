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
import net.minecraft.world.InteractionHand;
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

   // the flash decoy keeps the old fixed span, the illusion one sets its own
   // chaotimancy, the colour the copies come apart in
   private static final double CHAOS_R = 188.0D / 255.0D;
   private static final double CHAOS_G = 54.0D / 255.0D;
   private static final double CHAOS_B = 177.0D / 255.0D;

   private int life = LIFETIME;

   // a copy that walks. it holds its place beside the caster and does whatever they do
   private boolean mirror;
   private static final float FOOLED = 0.75F;

   private int lastSwing = -99;
   private boolean using;
   private double offX;
   private double offZ;

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

      // vanilla drives the legs off the end of travel, and this thing never travels. worked out
      // here instead, off how far it actually shifted since last tick, on both sides
      this.calculateEntityAnimation(false);
      if (!this.level().isClientSide) {
         if (this.pending != null) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
               this.setItemSlot(slot, this.pending[slot.ordinal()]);
            }
            this.pending = null;
         }
         if (this.mirror) this.followOwner();
         if (this.tickCount == 1) this.drawAttention();
         else if (this.mirror && this.tickCount % 20 == 0) this.keepAttention();
         if (this.tickCount > this.life) {
            this.burst();
         }
      }
   }

   // it keeps its offset and copies the aim and the crouch, so the whole set moves as one
   private void followOwner() {
      UUID owner = this.getOwnerId();
      if (owner == null) return;
      Player caster = this.level().getPlayerByUUID(owner);
      if (caster == null || !caster.isAlive()) return;

      this.moveTo(caster.getX() + this.offX, caster.getY(), caster.getZ() + this.offZ,
         caster.getYRot(), caster.getXRot());
      this.yHeadRot = caster.yHeadRot;
      this.yBodyRot = caster.yBodyRot;
      this.entityData.set(POSE_FLAGS, (byte) caster.getPose().ordinal());
      this.setShiftKeyDown(caster.isShiftKeyDown());

      // whatever they are holding now, not whatever they held when it was cast
      for (EquipmentSlot slot : EquipmentSlot.values()) {
         ItemStack worn = caster.getItemBySlot(slot);
         if (!ItemStack.matches(this.getItemBySlot(slot), worn)) {
            this.setItemSlot(slot, worn.copy());
         }
      }

      // the arm has to be swung properly rather than have its fields copied. swing sends the
      // packet that makes every client play it, copying the numbers reaches nobody. the swing
      // clock restarting is what marks a new one, so held down attacks come through as well
      int swing = caster.swinging ? caster.swingTime : -99;
      if (caster.swinging && swing <= this.lastSwing) {
         this.swing(caster.swingingArm == null ? InteractionHand.MAIN_HAND : caster.swingingArm, true);
      }
      this.lastSwing = swing;

      // holding right click is not a swing at all, it is the use pose. drawing a bow, eating,
      // raising a shield and holding a wand out all come through here
      if (caster.isUsingItem() && !this.using) {
         this.startUsingItem(caster.getUsedItemHand());
         this.using = true;
      } else if (!caster.isUsingItem() && this.using) {
         this.stopUsingItem();
         this.using = false;
      }
   }

   private java.util.List<EntityFlashDecoy> siblings() {
      UUID owner = this.getOwnerId();
      java.util.List<EntityFlashDecoy> rest = new java.util.ArrayList<>();
      if (owner == null) return rest;

      for (EntityFlashDecoy other : this.level().getEntitiesOfClass(EntityFlashDecoy.class,
            this.getBoundingBox().inflate(48.0D))) {
         if (other == this || other.isRemoved() || !other.mirror) continue;
         if (owner.equals(other.getOwnerId())) rest.add(other);
      }
      return rest;
   }

   // whoever was swinging at this one is handed on to another copy, most of the time
   private void handOver() {
      java.util.List<EntityFlashDecoy> rest = this.siblings();
      if (rest.isEmpty()) return;

      for (Mob mob : this.level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(24.0D))) {
         if (mob.getTarget() != this) continue;
         if (this.random.nextFloat() > FOOLED) continue;
         mob.setTarget(rest.get(this.random.nextInt(rest.size())));
      }
   }

   // and anything that works out where the real one is gets turned back round, most of the time
   private void keepAttention() {
      UUID owner = this.getOwnerId();
      if (owner == null) return;
      Player caster = this.level().getPlayerByUUID(owner);
      if (caster == null) return;

      for (Mob mob : this.level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(24.0D))) {
         if (mob.getTarget() != caster) continue;
         if (this.random.nextFloat() > FOOLED) continue;
         mob.setTarget(this);
      }
   }

   // the bar is counting down the copies, so it stops when the last of them does
   private void lastOneOut() {
      UUID owner = this.getOwnerId();
      if (owner == null) return;
      Player caster = this.level().getPlayerByUUID(owner);
      if (caster == null) return;

      AABB around = caster.getBoundingBox().inflate(64.0D);
      for (EntityFlashDecoy other : this.level().getEntitiesOfClass(EntityFlashDecoy.class, around)) {
         if (other == this || other.isRemoved() || !other.mirror) continue;
         if (owner.equals(other.getOwnerId())) return;
      }
      com.paleimitations.schoolsofmagic.common.spells.spells.DecoyBar.set(caster, 0);
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

   public void setMirror(double offX, double offZ) {
      this.mirror = true;
      this.offX = offX;
      this.offZ = offZ;
   }

   public void setLife(int ticks) {
      this.life = ticks;
   }

   private void burst() {
      if (this.bursting) return;
      this.bursting = true;
      this.discard();

      float blast = this.entityData.get(BLAST);

      // nothing to go off means it was only ever a picture of somebody. it comes apart quietly
      if (blast <= 0.0F) {
         this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
            SoundEvents.ILLUSIONER_MIRROR_MOVE, SoundSource.PLAYERS, 0.9F, 1.1F);
         this.handOver();
         this.lastOneOut();
         if (this.level() instanceof ServerLevel quiet) {
            // the mods spell cloud, tinted the colour of the school it came from. the seed carries
            // the colour in the argument slots the puff has no room for
            for (int i = 0; i < 3; i++) {
               double sx = this.getX() + (this.random.nextDouble() - 0.5D) * 0.7D;
               double sy = this.getY() + 0.4D + this.random.nextDouble() * 1.4D;
               double sz = this.getZ() + (this.random.nextDouble() - 0.5D) * 0.7D;
               quiet.sendParticles(
                  com.paleimitations.schoolsofmagic.common.registries.ParticleTypeRegistry.SPORE_SEED.get(),
                  sx, sy, sz, 0, CHAOS_R, CHAOS_G, CHAOS_B, 1.0D);
            }
         }
         return;
      }
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
      nbt.putInt("Life", this.life);
      nbt.putBoolean("Mirror", this.mirror);
      nbt.putDouble("OffX", this.offX);
      nbt.putDouble("OffZ", this.offZ);
   }

   @Override
   public void readAdditionalSaveData(CompoundTag nbt) {
      if (nbt.hasUUID("Owner")) this.entityData.set(OWNER, java.util.Optional.of(nbt.getUUID("Owner")));
      if (nbt.contains("Blast")) this.entityData.set(BLAST, nbt.getFloat("Blast"));
   }
}
