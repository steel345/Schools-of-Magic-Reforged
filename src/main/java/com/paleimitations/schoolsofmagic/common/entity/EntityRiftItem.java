package com.paleimitations.schoolsofmagic.common.entity;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.rift_storage.CapabilityRiftStorage;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.rift_storage.IRiftStorage;
import com.paleimitations.schoolsofmagic.common.world.dimensions.AstralCorridorGenerator;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

// one thing out of the rift, lying on the floor of the corridor. it does not fall, it does not
// bob and it does not turn, it is just where it was put. the stack it shows is a copy, the real
// one never leaves the owners storage until somebody reaches down for it
public class EntityRiftItem extends Entity {
   private static final EntityDataAccessor<ItemStack> SHOWN =
      SynchedEntityData.defineId(EntityRiftItem.class, EntityDataSerializers.ITEM_STACK);
   private static final EntityDataAccessor<Float> LIE =
      SynchedEntityData.defineId(EntityRiftItem.class, EntityDataSerializers.FLOAT);

   private static final double NUDGE = 0.85D;
   private static final float EDGE = 0.35F;

   private UUID owner;
   private int slot = -1;

   public EntityRiftItem(EntityType<? extends EntityRiftItem> type, Level level) {
      super(type, level);
      this.noPhysics = true;
      this.setNoGravity(true);
   }

   @Override
   protected void defineSynchedData() {
      this.getEntityData().define(SHOWN, ItemStack.EMPTY);
      this.getEntityData().define(LIE, 0.0F);
   }

   public ItemStack shown() {
      return this.getEntityData().get(SHOWN);
   }

   public float lie() {
      return this.getEntityData().get(LIE);
   }

   public void setup(Player owner, int slot, ItemStack stack, float lie) {
      this.owner = owner.getUUID();
      this.slot = slot;
      this.getEntityData().set(SHOWN, stack.copy());
      this.getEntityData().set(LIE, lie);
   }

   @Override
   public void tick() {
      super.tick();
      if (this.level().isClientSide) return;

      if (this.shown().isEmpty()) {
         this.discard();
         return;
      }

      // walking through the pile shoves things out of the way rather than walking over the top
      for (Player near : this.level().getEntitiesOfClass(Player.class,
            this.getBoundingBox().inflate(NUDGE, 1.2D, NUDGE))) {
         double dx = this.getX() - near.getX();
         double dz = this.getZ() - near.getZ();
         double away = Math.sqrt(dx * dx + dz * dz);
         if (away > NUDGE || away < 1.0E-4D) continue;

         double push = (NUDGE - away) * 0.22D;
         double x = this.getX() + dx / away * push;
         double z = this.getZ() + dz / away * push;

         // but never out through the walls
         x = Math.min(Math.max(x, AstralCorridorGenerator.WEST_WALL + 1 + EDGE),
                      AstralCorridorGenerator.EAST_WALL - EDGE);
         this.setPos(x, this.getY(), z);
      }
   }

   @Override
   public InteractionResult interact(Player player, InteractionHand hand) {
      if (this.level().isClientSide) {
         return player.isShiftKeyDown() ? InteractionResult.SUCCESS : InteractionResult.PASS;
      }
      if (!player.isShiftKeyDown()) return InteractionResult.PASS;
      if (this.owner != null && !this.owner.equals(player.getUUID())) return InteractionResult.PASS;

      IRiftStorage storage = CapabilityRiftStorage.get(player);
      if (storage == null || this.slot < 0 || this.slot >= storage.size()) {
         this.discard();
         return InteractionResult.CONSUME;
      }

      // the storage is the only copy there has ever been. if it is already gone the entity was
      // stale and nothing is handed over
      ItemStack real = storage.get(this.slot);
      if (real.isEmpty()) {
         this.discard();
         return InteractionResult.CONSUME;
      }

      storage.set(this.slot, ItemStack.EMPTY);
      if (!player.getInventory().add(real)) player.drop(real, false);

      this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
         SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.3F, 1.4F);
      this.discard();
      return InteractionResult.CONSUME;
   }

   @Override
   public InteractionResult interactAt(Player player, net.minecraft.world.phys.Vec3 where, InteractionHand hand) {
      return this.interact(player, hand);
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
   protected void readAdditionalSaveData(CompoundTag tag) {
      this.slot = tag.getInt("Slot");
      if (tag.hasUUID("Owner")) this.owner = tag.getUUID("Owner");
      this.getEntityData().set(SHOWN, ItemStack.of(tag.getCompound("Shown")));
      this.getEntityData().set(LIE, tag.getFloat("Lie"));
   }

   @Override
   protected void addAdditionalSaveData(CompoundTag tag) {
      tag.putInt("Slot", this.slot);
      if (this.owner != null) tag.putUUID("Owner", this.owner);
      CompoundTag shown = new CompoundTag();
      this.shown().save(shown);
      tag.put("Shown", shown);
      tag.putFloat("Lie", this.lie());
   }

   @Nullable
   public UUID owner() {
      return this.owner;
   }

   @Override
   public Packet<ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }
}
