package com.paleimitations.schoolsofmagic.common.entity;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

// a rune laid on the ground that watches. it tells the one who set it when something comes near,
// and it will not make a sound of its own until they say so
public class EntityAlarmRune extends Entity {
   private static final EntityDataAccessor<Boolean> WAILING =
      SynchedEntityData.defineId(EntityAlarmRune.class, EntityDataSerializers.BOOLEAN);

   private static final EntityDataAccessor<Float> WATCH =
      SynchedEntityData.defineId(EntityAlarmRune.class, EntityDataSerializers.FLOAT);

   public static final int WAIL_TICKS = 1200;
   private static final int QUIET = 100;
   private static final float BLAST = 3.0F;
   private static final double STAND_ON = 1.2D;

   private UUID owner;
   private int wail;
   private int hush;

   public EntityAlarmRune(EntityType<? extends EntityAlarmRune> type, Level level) {
      super(type, level);
      this.noPhysics = true;
      this.setNoGravity(true);
   }

   @Override
   protected void defineSynchedData() {
      this.getEntityData().define(WAILING, false);
      this.getEntityData().define(WATCH, 8.0F);
   }

   public boolean isWailing() {
      return this.getEntityData().get(WAILING);
   }

   public double watch() {
      return this.getEntityData().get(WATCH);
   }

   public void setWatch(double reach) {
      this.getEntityData().set(WATCH, (float) reach);
   }

   public void setOwner(@Nullable Player player) {
      this.owner = player == null ? null : player.getUUID();
   }

   private boolean isOwner(Entity entity) {
      return this.owner != null && this.owner.equals(entity.getUUID());
   }

   @Override
   public void tick() {
      super.tick();
      if (this.level().isClientSide) return;

      if (this.isWailing()) {
         this.wail--;

         // the one who set it can put a foot on it to shut it up
         Player caster = this.owner == null ? null : this.level().getPlayerByUUID(this.owner);
         if (caster != null && caster.distanceToSqr(this) <= STAND_ON * STAND_ON) {
            this.discard();
            return;
         }
         if (this.wail % 15 == 0) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
               SoundEvents.BELL_RESONATE, SoundSource.PLAYERS, 16.0F, 0.6F);
         }
         if (this.wail <= 0) this.discard();
         return;
      }

      // it waits as long as it takes. only sounding it ever spends it
      if (this.hush > 0) {
         this.hush--;
         return;
      }
      this.sweep();
   }

   private void sweep() {
      for (LivingEntity near : this.level().getEntitiesOfClass(LivingEntity.class,
            this.getBoundingBox().inflate(this.watch()))) {
         if (!near.isAlive() || this.isOwner(near)) continue;
         if (near.distanceToSqr(this) > this.watch() * this.watch()) continue;

         this.warn();
         this.hush = QUIET;
         return;
      }
   }

   private void warn() {
      if (this.owner == null) return;
      if (!(this.level().getPlayerByUUID(this.owner) instanceof ServerPlayer caster)) return;

      caster.displayClientMessage(Component.translatable("message.som.alarm.near"), true);
      caster.playNotifySound(SoundEvents.NOTE_BLOCK_BELL.value(), SoundSource.PLAYERS, 0.8F, 1.6F);
      com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.send(
         net.minecraftforge.network.PacketDistributor.PLAYER.with(() -> caster),
         new com.paleimitations.schoolsofmagic.common.network.PacketAlarmAlert(this.getId(), true));
   }

   // sounding it is the casters call, and it costs everything stood round it
   public boolean sound(Player player) {
      if (this.level().isClientSide) return false;
      if (!this.isOwner(player) || this.isWailing()) return false;
      this.wake();
      return true;
   }

   @Override
   public InteractionResult interact(Player player, InteractionHand hand) {
      if (this.level().isClientSide) return InteractionResult.SUCCESS;
      if (!this.sound(player)) return InteractionResult.PASS;
      return InteractionResult.CONSUME;
   }

   private void wake() {
      this.getEntityData().set(WAILING, true);
      this.wail = WAIL_TICKS;

      this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
         SoundEvents.BELL_RESONATE, SoundSource.PLAYERS, 16.0F, 0.6F);
      for (LivingEntity near : this.level().getEntitiesOfClass(LivingEntity.class,
            this.getBoundingBox().inflate(this.watch()))) {
         if (!near.isAlive() || this.isOwner(near)) continue;
         near.hurt(this.level().damageSources().magic(), BLAST);
      }

      if (this.owner != null && this.level().getPlayerByUUID(this.owner) instanceof ServerPlayer caster) {
         com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.send(
            net.minecraftforge.network.PacketDistributor.PLAYER.with(() -> caster),
            new com.paleimitations.schoolsofmagic.common.network.PacketAlarmAlert(this.getId(), false));
      }
   }

   @Override
   public boolean isPickable() {
      return true;
   }

   @Override
   protected void readAdditionalSaveData(CompoundTag tag) {
      if (tag.hasUUID("Owner")) this.owner = tag.getUUID("Owner");
      this.wail = tag.getInt("Wail");
      this.hush = tag.getInt("Hush");
      this.getEntityData().set(WAILING, tag.getBoolean("Wailing"));
      this.setWatch(tag.contains("Watch") ? tag.getDouble("Watch") : 8.0D);
   }

   @Override
   protected void addAdditionalSaveData(CompoundTag tag) {
      if (this.owner != null) tag.putUUID("Owner", this.owner);
      tag.putInt("Wail", this.wail);
      tag.putInt("Hush", this.hush);
      tag.putBoolean("Wailing", this.isWailing());
      tag.putDouble("Watch", this.watch());
   }

   @Override
   public Packet<ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }
}
