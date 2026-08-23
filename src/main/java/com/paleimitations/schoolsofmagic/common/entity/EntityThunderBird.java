package com.paleimitations.schoolsofmagic.common.entity;

import com.paleimitations.schoolsofmagic.common.handlers.LootTableHandlers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class EntityThunderBird extends EntityPhoenix {
   private int thunderCooldown;
   private boolean stormSpawned;
   private int departTimer;

   public EntityThunderBird(EntityType<? extends ShoulderRidingEntity> type, Level level) {
      super(type, level);
   }

   public boolean isStormSpawned() {
      return this.stormSpawned;
   }

   public void setStormSpawned(boolean value) {
      this.stormSpawned = value;
   }

   @Override
   public void aiStep() {
      super.aiStep();
      if (this.level().isClientSide || !this.stormSpawned || this.isTame() || this.isVehicle()) {
         return;
      }
      if (this.level().isThundering()) {
         this.departTimer = 0;
         return;
      }
      this.setTarget(null);
      this.getNavigation().stop();
      this.setNoGravity(true);
      this.setDeltaMovement(this.getDeltaMovement().x * 0.9D, 0.4D, this.getDeltaMovement().z * 0.9D);
      if (++this.departTimer > 70) {
         if (this.level() instanceof ServerLevel server) {
            server.sendParticles(net.minecraft.core.particles.ParticleTypes.CLOUD,
               this.getX(), this.getY() + 0.4D, this.getZ(), 12, 0.3D, 0.4D, 0.3D, 0.02D);
         }
         this.discard();
      }
   }

   @Override
   public void addAdditionalSaveData(net.minecraft.nbt.CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putBoolean("StormSpawned", this.stormSpawned);
   }

   @Override
   public void readAdditionalSaveData(net.minecraft.nbt.CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.stormSpawned = compound.getBoolean("StormSpawned");
   }

   @Override
   protected boolean rebirthEnabled() {
      return false;
   }

   @Override
   protected boolean coldEnabled() {
      return false;
   }

   @Override
   protected boolean courierEnabled() {
      return false;
   }

   @Override
   protected boolean fireEnabled() {
      return false;
   }

   @Override
   protected Item favoriteFood() {
      return Items.PORKCHOP;
   }

   @Override
   protected void tickSpecialAttack() {
      if (this.thunderCooldown > 0) {
         this.thunderCooldown--;
         return;
      }
      LivingEntity target = this.getTarget();
      if (target == null || !target.isAlive() || target == this.getOwner()) {
         return;
      }
      if (this.distanceToSqr(target) > 24.0D * 24.0D) {
         return;
      }
      if (this.random.nextInt(target instanceof Player ? 60 : 120) != 0) {
         return;
      }
      this.getLookControl().setLookAt(target);
      if (this.level() instanceof ServerLevel server) {
         LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(server);
         if (bolt != null) {
            bolt.moveTo(target.getX(), target.getY(), target.getZ());
            if (this.getOwner() instanceof ServerPlayer owner) {
               bolt.setCause(owner);
            }
            server.addFreshEntity(bolt);
         }
      }
      this.playCry();
      this.thunderCooldown = 140;
   }

   @Nullable
   @Override
   public ResourceLocation getDefaultLootTable() {
      return LootTableHandlers.THUNDER_BIRD;
   }
}
