package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;

public abstract class AbstractMagicCircle extends Mob {
   private LivingEntity ownerBackup = null;
   private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(AbstractMagicCircle.class, EntityDataSerializers.OPTIONAL_UUID);
   private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(AbstractMagicCircle.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> RADIUS = SynchedEntityData.defineId(AbstractMagicCircle.class, EntityDataSerializers.INT);

   public AbstractMagicCircle(EntityType<? extends AbstractMagicCircle> type, Level worldIn) {
      super(type, worldIn);
   }

   public abstract void performSpell();

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(OWNER, Optional.empty());
      this.entityData.define(DURATION, 1200);
      this.entityData.define(RADIUS, 20);
   }

   public void setOwnerID(@Nullable UUID id) {
      this.entityData.set(OWNER, Optional.ofNullable(id));
   }

   @Nullable
   public UUID getOwnerID() {
      return this.entityData.get(OWNER).orElse(null);
   }

   public void setOwner(LivingEntity owner) {
      this.ownerBackup = owner;
      this.setOwnerID(owner.getUUID());
   }

   @Override
   public boolean isInvulnerableTo(DamageSource source) {
      return true;
   }

   @Override
   public void move(MoverType type, net.minecraft.world.phys.Vec3 vec) {
   }

   @Override
   public boolean isAttackable() {
      return false;
   }

   @Override
   protected boolean shouldDespawnInPeaceful() {
      return false;
   }

   @Override
   public void push(Entity entityIn) {
   }

   @Override
   protected void doPush(Entity entityIn) {
   }

   public LivingEntity getOwner() {
      if (this.ownerBackup != null && this.ownerBackup.getUUID().equals(this.getOwnerID())) {
         return this.ownerBackup;
      }
      if (this.getOwnerID() != null && Utils.getEntity(this.level(), this.getOwnerID()) != null && Utils.getEntity(this.level(), this.getOwnerID()) instanceof LivingEntity) {
         return (LivingEntity) Utils.getEntity(this.level(), this.getOwnerID());
      }
      return this.ownerBackup;
   }

   public void setRadius(int radius) {
      this.entityData.set(RADIUS, radius);
   }

   public int getRadius() {
      return this.entityData.get(RADIUS);
   }

   public void setDuration(int duration) {
      this.entityData.set(DURATION, duration);
   }

   public int getDuration() {
      return this.entityData.get(DURATION);
   }

   @Override
   public void tick() {
      if (this.tickCount >= this.getDuration() && this.getDuration() > 0) {
         this.discard();
         return;
      }
      super.tick();
   }

   @Override
   public void readAdditionalSaveData(CompoundTag nbt) {
      super.readAdditionalSaveData(nbt);
      this.setDuration(nbt.getInt("duration"));
      this.setRadius(nbt.getInt("radius"));
      if (nbt.contains("ownerBackup") && this.level().getEntity(nbt.getInt("ownerBackup")) instanceof LivingEntity) {
         this.ownerBackup = (LivingEntity) this.level().getEntity(nbt.getInt("ownerBackup"));
      }
      if (nbt.contains("owner")) {
         this.setOwnerID(UUID.fromString(nbt.getString("owner")));
      }
   }

   @Override
   public void addAdditionalSaveData(CompoundTag nbt) {
      super.addAdditionalSaveData(nbt);
      nbt.putInt("duration", this.getDuration());
      nbt.putInt("radius", this.getRadius());
      if (this.ownerBackup != null) {
         nbt.putInt("ownerBackup", this.ownerBackup.getId());
      }
      if (this.getOwnerID() != null) {
         nbt.putString("owner", this.getOwnerID().toString());
      }
   }
}
