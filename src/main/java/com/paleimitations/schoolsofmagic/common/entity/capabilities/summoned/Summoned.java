package com.paleimitations.schoolsofmagic.common.entity.capabilities.summoned;

import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketUpdateSummoned;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.PacketDistributor;

public class Summoned implements INBTSerializable<CompoundTag>, ISummoned {
   private int despawnCountdown;
   private boolean isSummoned;

   public Summoned() {
   }

   @Override
   public void update(LivingEntity living) {
      PacketUpdateSummoned message = new PacketUpdateSummoned(living.getId(), this.serializeNBT());
      PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> living), message);
   }

   @Override
   public void setDespawnCountdown(LivingEntity living, int despawnCountdown) {
      this.despawnCountdown = despawnCountdown;
   }

   @Override
   public int getDespawnCountdown() {
      return this.despawnCountdown;
   }

   @Override
   public boolean isSummoned() {
      return this.isSummoned;
   }

   @Override
   public void setSummoned(LivingEntity living, boolean isSummoned) {
      this.isSummoned = isSummoned;
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      nbt.putInt("despawnCountdown", this.despawnCountdown);
      nbt.putBoolean("isSummoned", this.isSummoned);
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      this.despawnCountdown = nbt.getInt("despawnCountdown");
      this.isSummoned = nbt.getBoolean("isSummoned");
   }
}
