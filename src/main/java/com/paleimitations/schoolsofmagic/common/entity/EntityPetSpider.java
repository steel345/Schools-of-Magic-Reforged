package com.paleimitations.schoolsofmagic.common.entity;

import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;

public class EntityPetSpider extends Spider {
   private static final EntityDataAccessor<Integer> SPIDER_TYPE = SynchedEntityData.defineId(EntityPetSpider.class, EntityDataSerializers.INT);

   public EntityPetSpider(EntityType<? extends Spider> type, Level level) {
      super(type, level);
      this.setMaxUpStep(1.0F);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(SPIDER_TYPE, 0);
   }

   public int getPetSpiderType() {
      return this.entityData.get(SPIDER_TYPE);
   }

   public void setPetSpiderType(int toadTypeId) {
      this.entityData.set(SPIDER_TYPE, toadTypeId);
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
      spawnData = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
      int i = this.getRandom().nextInt(5);
      boolean flag = false;
      if (spawnData instanceof SpiderTypeData) {
         i = ((SpiderTypeData)spawnData).typeData;
         flag = true;
      } else {
         spawnData = new SpiderTypeData(i);
      }
      this.setPetSpiderType(i);
      return spawnData;
   }

   public static class SpiderTypeData implements SpawnGroupData {
      public int typeData;

      public SpiderTypeData(int type) {
         this.typeData = type;
      }
   }
}
