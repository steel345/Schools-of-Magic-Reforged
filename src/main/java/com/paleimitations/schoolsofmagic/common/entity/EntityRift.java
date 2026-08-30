package com.paleimitations.schoolsofmagic.common.entity;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkHooks;

public class EntityRift extends Entity {
   public static final int OPEN_TICKS = 20;
   public static final int CLOSE_TICKS = 17;

   private static final EntityDataAccessor<Integer> LIFE =
      SynchedEntityData.defineId(EntityRift.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> MAX_LIFE =
      SynchedEntityData.defineId(EntityRift.class, EntityDataSerializers.INT);

   private UUID owner;
   private boolean homeward;

   // when it is due to close, in world time. counting ticks down only works while something is
   // ticking it, and walking off to another dimension leaves its chunk unloaded and the count
   // stopped where it stood
   private long expiry = -1L;

   public EntityRift(EntityType<? extends EntityRift> type, Level level) {
      super(type, level);
      this.noPhysics = true;
      this.setNoGravity(true);
   }

   @Override
   protected void defineSynchedData() {
      this.getEntityData().define(LIFE, 400);
      this.getEntityData().define(MAX_LIFE, 400);
   }

   public int getLife() {
      return this.getEntityData().get(LIFE);
   }

   public void setLife(int ticks) {
      this.getEntityData().set(LIFE, ticks);
      this.getEntityData().set(MAX_LIFE, ticks);
      this.expiry = ticks >= Integer.MAX_VALUE / 2 ? -1L : this.level().getGameTime() + ticks;
   }

   public int getMaxLife() {
      return this.getEntityData().get(MAX_LIFE);
   }

   public void setOwner(@Nullable Player player) {
      this.owner = player == null ? null : player.getUUID();
   }

   // the one waiting on the far side. it does not run down and it takes you home instead of
   // opening the store
   public boolean isHomeward() {
      return this.homeward;
   }

   public void setHomeward(boolean homeward) {
      this.homeward = homeward;
   }

   public boolean isOwner(Player player) {
      return this.owner != null && this.owner.equals(player.getUUID());
   }

   @Override
   public void tick() {
      super.tick();
      if (this.level().isClientSide) return;

      if (!this.homeward) {
         int life = this.getEntityData().get(LIFE) - 1;
         this.getEntityData().set(LIFE, life);

         // the clock is what closes it, the count is only there for the bar and the last frames
         boolean over = this.expiry >= 0L && this.level().getGameTime() >= this.expiry;
         if (life <= 0 || over) {
            this.discard();
            return;
         }
      }

      net.minecraft.world.phys.AABB reach = this.getBoundingBox().inflate(1.4D, 2.0D, 1.4D);
      for (Player near : this.level().getEntitiesOfClass(Player.class, reach)) {
         if (near instanceof ServerPlayer server) {
            com.paleimitations.schoolsofmagic.common.handlers.AstralPlaneHandler.standingOn(this, server);
         }
      }
   }

   @Override
   public InteractionResult interact(Player player, InteractionHand hand) {
      if (this.level().isClientSide) return InteractionResult.SUCCESS;
      if (!(player instanceof ServerPlayer server)) return InteractionResult.PASS;
      if (this.owner != null && !this.isOwner(player)) {
         return InteractionResult.PASS;
      }
      if (this.homeward) return InteractionResult.PASS;

      MenuProvider menu = new SimpleMenuProvider(
         (id, inventory, p) -> new com.paleimitations.schoolsofmagic.common.containers.ContainerRift(id, inventory),
         Component.translatable("container.som.rift"));
      NetworkHooks.openScreen(server, menu);
      this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
         SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 0.6F, 1.6F);
      return InteractionResult.CONSUME;
   }

   @Override
   public boolean isPickable() {
      return true;
   }

   @Override
   protected void readAdditionalSaveData(CompoundTag tag) {
      this.getEntityData().set(LIFE, tag.getInt("Life"));
      this.getEntityData().set(MAX_LIFE, tag.getInt("MaxLife"));
      this.homeward = tag.getBoolean("Homeward");
      this.expiry = tag.contains("Expiry") ? tag.getLong("Expiry") : -1L;
      if (tag.hasUUID("Owner")) this.owner = tag.getUUID("Owner");
   }

   @Override
   protected void addAdditionalSaveData(CompoundTag tag) {
      tag.putBoolean("Homeward", this.homeward);
      tag.putLong("Expiry", this.expiry);
      tag.putInt("Life", this.getLife());
      tag.putInt("MaxLife", this.getMaxLife());
      if (this.owner != null) tag.putUUID("Owner", this.owner);
   }

   @Override
   public Packet<ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }
}
