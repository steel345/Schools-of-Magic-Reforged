package com.paleimitations.schoolsofmagic.common.entity.capabilities.banishedentity;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

public class BanishedEntity implements INBTSerializable<CompoundTag>, IBanishedEntity {

   private Map<CompoundTag, Vec3i> banishedEntity = Maps.newHashMap();

   public BanishedEntity() {
   }

   @Override
   public Map<CompoundTag, Vec3i> getBanishedEntity() {
      return this.banishedEntity;
   }

   @Override
   public void removeEntity(CompoundTag entityData) {
      Map<CompoundTag, Vec3i> banishedEntityIn = Maps.newHashMap();

      for (Entry<CompoundTag, Vec3i> entry : this.banishedEntity.entrySet()) {
         if (entry.getKey() != entityData) {
            banishedEntityIn.put(entry.getKey(), entry.getValue());
         }
      }

      this.banishedEntity = banishedEntityIn;
   }

   @Override
   public void setBanishedEntity(Map<CompoundTag, Vec3i> banishedEntity) {
      this.banishedEntity = banishedEntity;
   }

   @Override
   public void setTimer(int entityId, CompoundTag entityData, int timer) {
      this.banishedEntity.replace(entityData, new Vec3i(entityId, timer, 0));
   }

   @Override
   public void clear() {
      this.banishedEntity.clear();
   }

   @Override
   public void banishEntity(int entityId, CompoundTag entityData, int timer) {
      this.banishedEntity.put(entityData, new Vec3i(entityId, timer, 0));
   }

   @Override
   public void banishEntity(net.minecraft.world.entity.EntityType<?> type, CompoundTag entityData, int timer) {

      net.minecraft.resources.ResourceLocation key =
            net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getKey(type);
      if (key != null) entityData.putString("som:typeKey", key.toString());
      this.banishedEntity.put(entityData, new Vec3i(0, timer, 0));
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      writeNBT(nbt);
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      readNBT(nbt);
   }

   public void writeNBT(CompoundTag nbt) {
      nbt.putInt("banishedEntity_size", this.banishedEntity.size());
      int i = 0;

      for (Entry<CompoundTag, Vec3i> entry : this.banishedEntity.entrySet()) {
         nbt.put("banishedEntity_tag" + i, entry.getKey());
         nbt.putInt("banishedEntity_id" + i, entry.getValue().getX());
         nbt.putInt("banishedEntity_timer" + i, entry.getValue().getY());
         i++;
      }
   }

   public void readNBT(CompoundTag nbt) {
      Map<CompoundTag, Vec3i> banishedEntity = Maps.newHashMap();

      for (int i = 0; i < nbt.getInt("banishedEntity_size"); i++) {
         Tag tag = nbt.get("banishedEntity_tag" + i);
         if (tag instanceof CompoundTag) {
            banishedEntity.put(
               (CompoundTag) tag,
               new Vec3i(nbt.getInt("banishedEntity_id" + i), nbt.getInt("banishedEntity_timer" + i), 0)
            );
         }
      }

      this.banishedEntity = banishedEntity;
   }
}
