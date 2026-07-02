package com.paleimitations.schoolsofmagic.common.entity.boat;

import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class SOMBoat extends Boat {

   private static final EntityDataAccessor<String> WOOD =
      SynchedEntityData.defineId(SOMBoat.class, EntityDataSerializers.STRING);

   public SOMBoat(EntityType<? extends Boat> type, Level level) {
      super(type, level);
   }

   public SOMBoat(Level level, double x, double y, double z) {
      this(EntityRegistry.SOM_BOAT.get(), level);
      this.setPos(x, y, z);
      this.xo = x;
      this.yo = y;
      this.zo = z;
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(WOOD, "ash");
   }

   public void setWood(String wood) {
      this.entityData.set(WOOD, wood);
   }

   public String getWood() {
      return this.entityData.get(WOOD);
   }

   @Override
   protected void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
      tag.putString("Wood", this.getWood());
   }

   @Override
   protected void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      if (tag.contains("Wood")) this.setWood(tag.getString("Wood"));
   }

   @Override
   public Item getDropItem() {
      return SOMBoats.boatItem(this.getWood(), false);
   }
}
