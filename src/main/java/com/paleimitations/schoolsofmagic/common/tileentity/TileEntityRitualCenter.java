package com.paleimitations.schoolsofmagic.common.tileentity;

import com.paleimitations.schoolsofmagic.common.containers.ItemStackHandlerSingleSlots;
import com.paleimitations.schoolsofmagic.common.registries.RitualRegistry;
import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import com.paleimitations.schoolsofmagic.common.rituals.Ritual;
import com.paleimitations.schoolsofmagic.common.rituals.RitualHelper;
import java.awt.Color;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityRitualCenter extends BlockEntity {
   private Random random = new Random();
   public ItemStackHandler handler = new ItemStackHandlerSingleSlots(9);
   private UUID ownerID;
   public String playerOwnerName = "";
   private boolean isActivated = false;
   private Ritual ritual;

   private int ritualCount = 0;
   private int extinguishIn = 0;

   private int dyeColor = -1;

   private int flare = 0;

   public TileEntityRitualCenter(BlockPos pos, BlockState state) {
      super(TileEntityRegistry.RITUAL_CENTER.get(), pos, state);
   }

   @Nullable
   public UUID getOwnerID() {
      return this.ownerID;
   }

   public void setOwnerID(UUID id) {
      this.ownerID = id;
   }

   public void setOwner(LivingEntity owner) {
      this.setOwnerID(owner.getUUID());
      if (owner instanceof Player) {
         this.playerOwnerName = owner.getName().getString();
      }
   }

   @Nullable
   public LivingEntity getOwner() {
      if (this.playerOwnerName != null && !this.playerOwnerName.isEmpty()) {

         for (Player p : this.level.players()) {
            if (p.getGameProfile().getName().equals(this.playerOwnerName)) {
               return p;
            }
         }
         return null;
      }
      try {
         UUID uuid = this.getOwnerID();
         return uuid == null ? null : this.level.getPlayerByUUID(uuid);
      }
      catch (IllegalArgumentException var2) {
         return null;
      }
   }

   public Color getColor() {
      return this.ritual != null ? this.ritual.getColor(this) : new Color(16760362);
   }

   public int getDyeColor() {
      return this.dyeColor;
   }

   public void setDyeColor(int rgb) {
      this.dyeColor = rgb;
   }

   public int getFireTint() {
      if (this.isActivated && this.ritual != null && this.ritual.tintsFire()) {
         return this.ritual.getColor(this).getRGB();
      }
      return this.dyeColor;
   }

   public void startFlare() {
      this.flare = 11;
   }

   private void setBrazierFlame(int level) {
      if (this.level == null || this.level.isClientSide) return;
      BlockState st = this.getBlockState();
      if (!st.hasProperty(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.FLAME)
            || st.getValue(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.FLAME) <= 0) {
         return;
      }
      boolean colored = this.getFireTint() != -1;
      boolean needFlame = st.getValue(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.FLAME) != level;
      boolean needColor = st.hasProperty(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.COLORED)
            && st.getValue(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.COLORED) != colored;
      if (needFlame || needColor) {
         BlockState ns = st.setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.FLAME, level);
         if (st.hasProperty(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.COLORED)) {
            ns = ns.setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.COLORED, colored);
         }
         this.level.setBlock(this.worldPosition, ns, 3);
      }
   }

   public Ritual getRitual() {

      if (this.ritual != null && this.isActivated) {
         return this.ritual;
      }

      boolean cachedIsCrystal = this.ritual instanceof
         com.paleimitations.schoolsofmagic.common.rituals.rituals.RitualPotionCrystal;
      if (this.ritual != null && this.ritual.isRitual(this) && (this.isActivated || !cachedIsCrystal)) {
         return this.ritual;
      }
      Ritual fallback = null;
      for (Ritual ritualIn : RitualRegistry.RITUALS) {
         if (!ritualIn.isRitual(this)) continue;
         if (ritualIn instanceof com.paleimitations.schoolsofmagic.common.rituals.rituals.RitualPotionCrystal) {
            if (fallback == null) fallback = ritualIn;
            continue;
         }
         this.ritual = RitualHelper.getNewRitualInstance(ritualIn.getResourceLocation(), ritualIn.serializeNBT());
         return this.ritual;
      }
      if (fallback != null) {

         if (!(this.ritual instanceof com.paleimitations.schoolsofmagic.common.rituals.rituals.RitualPotionCrystal)
               || !this.ritual.isRitual(this)) {
            this.ritual = RitualHelper.getNewRitualInstance(fallback.getResourceLocation(), fallback.serializeNBT());
         }
         return this.ritual;
      }
      return null;
   }

   @Override
   protected void saveAdditional(CompoundTag nbt) {
      super.saveAdditional(nbt);
      nbt.putBoolean("isActivated", this.isActivated);
      if (this.getOwnerID() != null) {
         nbt.putString("OwnerUUID", this.getOwnerID().toString());
      }
      nbt.putString("playerOwnerName", this.playerOwnerName);
      nbt.putInt("ritualCount", this.ritualCount);
      nbt.putInt("extinguishIn", this.extinguishIn);
      nbt.putInt("DyeColor", this.dyeColor);
      nbt.putInt("flare", this.flare);
      if (this.ritual != null) {
         nbt.putString("ritual_location", this.ritual.getResourceLocation().toString());
         nbt.put("ritual", this.ritual.serializeNBT());
      }
      nbt.put("inventory", this.handler.serializeNBT());
   }

   @Override
   public void load(CompoundTag nbt) {
      super.load(nbt);
      this.isActivated = nbt.getBoolean("isActivated");
      if (nbt.contains("ritual_location") && nbt.contains("ritual")) {
         this.ritual = RitualHelper.getNewRitualInstance(new ResourceLocation(nbt.getString("ritual_location")), nbt.getCompound("ritual"));
      }
      this.playerOwnerName = nbt.getString("playerOwnerName");
      this.ritualCount = nbt.getInt("ritualCount");
      this.extinguishIn = nbt.getInt("extinguishIn");
      this.dyeColor = nbt.contains("DyeColor") ? nbt.getInt("DyeColor") : -1;
      this.flare = nbt.getInt("flare");
      if (nbt.contains("OwnerUUID")) {
         this.setOwnerID(UUID.fromString(nbt.getString("OwnerUUID")));
      }
      this.handler.deserializeNBT(nbt.getCompound("inventory"));
   }

   public boolean isOwner(Player player) {
      return this.ownerID != null && this.ownerID.equals(player.getUUID());
   }

   public boolean hasOwner() {
      return this.ownerID != null;
   }

   public void startRitual(Player player) {
      if (this.isOwner(player)) {

         if (this.ritual != null && this.ritual.castRitual(player, this)) {
            this.isActivated = true;
         }
      } else if (!player.level().isClientSide) {
         player.sendSystemMessage(Component.literal("You aren't the owner of this Brazier."));
      }
   }

   public void setRitual(Ritual rit) {
      this.ritual = rit;
   }

   public void stopRitual(Player player) {
      if (this.isOwner(player)) {

         if (this.ritual != null && this.isActivated && this.level != null) {
            this.ritual.onRitualStop(this, this.level, this.worldPosition);
         }
         this.ritual = null;
         this.isActivated = false;
      }
   }

   public void setActivated(boolean activated) {
      this.isActivated = activated;
   }

   public boolean isActivated() {
      return this.isActivated;
   }

   public void incrementRitualCount() {
      this.ritualCount++;
   }

   public void scheduleBurnOutIfReached() {
      if (this.ritualCount >= 5) {
         this.ritualCount = 0;
         this.extinguishIn = 2;
      }
   }

   public void tick() {
      if (!this.level.getEntitiesOfClass(ItemEntity.class, new AABB(this.worldPosition)).isEmpty()) {
         for (ItemEntity entity : this.level.getEntitiesOfClass(ItemEntity.class, new AABB(this.worldPosition))) {
            if (entity.tickCount >= 10) continue;
            for (int i = 0; i < this.handler.getSlots(); ++i) {
               if (!this.handler.getStackInSlot(i).isEmpty()) continue;
               this.handler.setStackInSlot(i, entity.getItem().split(1));
            }
         }
      }

      if (this.level != null && !this.level.isClientSide) {
         BlockState cst = this.getBlockState();
         boolean tinted = cst.hasProperty(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.COLORED)
               && cst.getValue(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.COLORED)
               && cst.hasProperty(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.FLAME)
               && cst.getValue(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.FLAME) > 0;
         if (tinted) {
            for (int i = 0; i < this.handler.getSlots(); i++) {
               if (this.handler.getStackInSlot(i).getItem() == net.minecraft.world.item.Items.CHARCOAL) {
                  this.handler.getStackInSlot(i).shrink(1);
                  this.dyeColor = -1;
                  this.flare = 0;
                  if (this.ritual != null && this.isActivated) {
                     this.ritual.onRitualStop(this, this.level, this.worldPosition);
                  }
                  this.ritual = null;
                  this.isActivated = false;
                  this.level.setBlock(this.worldPosition,
                     cst.setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.FLAME, 0)
                        .setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.COLORED, false), 3);
                  this.level.playSound(null, this.worldPosition, net.minecraft.sounds.SoundEvents.FIRE_EXTINGUISH,
                     net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
                  this.setChanged();
                  break;
               }
            }
         }
      }

      if (this.flare > 0 && this.level != null && !this.level.isClientSide) {
         int t = 12 - this.flare;
         int flame;
         if (t <= 5) {
            flame = 1 + t;
         } else if (t == 6) {
            flame = 6;
         } else {
            flame = 6 - (t - 6);
         }
         this.setBrazierFlame(flame);
         this.flare--;
         if (this.flare <= 0) {
            this.setBrazierFlame(1);
         }
      }
      if (this.getRitual() != null && this.isActivated) {
         this.ritual.onRitualUpdate(this, this.level, this.worldPosition);
      }

      if (this.extinguishIn > 0 && !this.level.isClientSide) {
         this.extinguishIn--;
         if (this.extinguishIn == 0) {
            BlockState st = this.getBlockState();
            if (st.hasProperty(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.FLAME)
                  && st.getValue(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.FLAME) > 0) {
               this.level.setBlock(this.worldPosition,
                  st.setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.FLAME, 0), 3);
               this.level.playSound(null, this.worldPosition, net.minecraft.sounds.SoundEvents.FIRE_EXTINGUISH,
                  net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
            }
         }
      }
   }

   public void sendUpdates() {
      this.setChanged();
      this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
   }

   @Override
   public CompoundTag getUpdateTag() {
      CompoundTag t = new CompoundTag();
      this.saveAdditional(t);
      return t;
   }

   @Override
   public void handleUpdateTag(CompoundTag tag) {
      this.load(tag);
   }

   @Override
   public Packet<ClientGamePacketListener> getUpdatePacket() {
      return ClientboundBlockEntityDataPacket.create(this);
   }

   @Override
   public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
      if (pkt.getTag() != null) {
         this.load(pkt.getTag());
      }
   }
}
