package com.paleimitations.schoolsofmagic.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityWebbedCocoon extends Mob {
   private static final EntityDataAccessor<Float> WIDTH = SynchedEntityData.defineId(EntityWebbedCocoon.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Float> HEIGHT = SynchedEntityData.defineId(EntityWebbedCocoon.class, EntityDataSerializers.FLOAT);

   public EntityWebbedCocoon(EntityType<EntityWebbedCocoon> type, Level level) {
      super(type, level);
   }

   public EntityWebbedCocoon(Level level, LivingEntity living) {
      super(com.paleimitations.schoolsofmagic.common.registries.EntityRegistry.COCOON.get(), level);
      living.startRiding(this, true);
   }

   public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
      return net.minecraft.world.entity.Mob.createMobAttributes()
            .add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 20.0D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED, 0.0D);
   }

   @Override
   protected void positionRider(Entity passenger, Entity.MoveFunction callback) {
      if (this.hasPassenger(passenger)) {
         callback.accept(passenger, this.getX(), this.getY(), this.getZ());
      }
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(WIDTH, 1.0F);
      this.entityData.define(HEIGHT, 1.0F);
   }

   public float getCocoonWidth() {
      return this.entityData.get(WIDTH);
   }

   public void setCocoonWidth(float width) {
      this.entityData.set(WIDTH, width);
   }

   public float getCocoonHeight() {
      return this.entityData.get(HEIGHT);
   }

   public void setCocoonHeight(float height) {
      this.entityData.set(HEIGHT, height);
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putFloat("width", this.getCocoonWidth());
      compound.putFloat("height", this.getCocoonHeight());
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setCocoonWidth(compound.getFloat("width"));
      this.setCocoonHeight(compound.getFloat("height"));
   }

   @Override
   public boolean removeWhenFarAway(double distance) {
      return false;
   }

   public boolean shouldRiderSit() {
      return false;
   }

   @Override
   protected boolean canRide(Entity entityIn) {
      return entityIn instanceof LivingEntity;
   }

   @Override
   public void stopRiding() {
   }

   @Override
   public boolean isPushable() {
      return false;
   }

   @Override
   public boolean isNoGravity() {
      return true;
   }

   @Override
   public void move(MoverType type, Vec3 movement) {
   }
}
