package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class EntityMagicCircleShriek extends AbstractMagicCircle {
   private List<LivingEntity> list = Lists.newArrayList();

   public EntityMagicCircleShriek(EntityType<? extends EntityMagicCircleShriek> type, Level worldIn) {
      super(type, worldIn);
   }

   public EntityMagicCircleShriek(Level worldIn) {
      super(EntityRegistry.CIRCLE_SHRIEK.get(), worldIn);
   }

   public SoundEvent getShriek() {
      switch (this.random.nextInt(4)) {
         case 0: {
            return SoundEvents.GHAST_SCREAM;
         }
         case 1: {
            return SoundEvents.ZOMBIE_DEATH;
         }
         case 2: {
            return SoundEvents.SKELETON_DEATH;
         }
         case 3: {
            return SoundEvents.WITHER_SPAWN;
         }
      }
      return SoundEvents.GHAST_SCREAM;
   }

   @Override
   public void tick() {
      int radius = this.getRadius();
      AABB box = new AABB(this.getX() + (double)radius, this.getY() + (double)radius, this.getZ() + (double)radius, this.getX() - (double)radius, this.getY() - (double)radius, this.getZ() - (double)radius);
      List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, box);
      List<LivingEntity> listIn = Utils.getDifference(entities, this.list);
      if (!(listIn.isEmpty() || listIn.size() == 1 && listIn.get(0) == this.getOwner())) {
         this.performSpell();
      }
      if (this.tickCount % 20 == 0) {
         this.list = entities;
      }
      super.tick();
   }

   @Override
   public void performSpell() {
      if (this.random.nextInt(10) == 0) {
         int radius = this.getRadius();
         AABB box = new AABB(this.getX() + (double)radius + 3.0, this.getY() + (double)radius + 3.0, this.getZ() + (double)radius + 3.0, this.getX() - (double)radius - 3.0, this.getY() - (double)radius - 3.0, this.getZ() - (double)radius - 3.0);
         List<PathfinderMob> list = this.level().getEntitiesOfClass(PathfinderMob.class, box);
         for (PathfinderMob creature : list) {
            if (!this.random.nextBoolean()) continue;
            for (WrappedGoal entry : creature.goalSelector.getAvailableGoals()) {
               if (!(entry.getGoal() instanceof AvoidEntityGoal)) continue;
               creature.setLastHurtByMob(this);
            }
         }
         this.playSound(this.getShriek(), 1.0F, 1.0F);
      }
   }

   @Override
   public void readAdditionalSaveData(CompoundTag nbt) {
      super.readAdditionalSaveData(nbt);
      ArrayList<LivingEntity> listIn = Lists.newArrayList();
      for (int i = 0; i < nbt.getInt("list_size"); ++i) {
         Entity entity = this.level().getEntity(nbt.getInt("entity_" + String.valueOf(i)));
         if (entity == null || !(entity instanceof LivingEntity) || entity.isRemoved()) continue;
         listIn.add((LivingEntity)entity);
      }
      this.list = listIn;
   }

   @Override
   public void addAdditionalSaveData(CompoundTag nbt) {
      super.addAdditionalSaveData(nbt);
      nbt.putInt("list_size", this.list.size());
      for (int i = 0; i < this.list.size(); ++i) {
         if (this.list.get(i) == null || this.list.get(i).isRemoved()) continue;
         nbt.putInt("entity_" + String.valueOf(i), this.list.get(i).getId());
      }
   }
}
