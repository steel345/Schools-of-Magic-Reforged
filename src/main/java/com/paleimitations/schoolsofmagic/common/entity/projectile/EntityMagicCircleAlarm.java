package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class EntityMagicCircleAlarm extends AbstractMagicCircle {
   private List<LivingEntity> list = Lists.newArrayList();
   private boolean onlyNew = false;

   public EntityMagicCircleAlarm(EntityType<? extends EntityMagicCircleAlarm> type, Level worldIn) {
      super(type, worldIn);
   }

   public EntityMagicCircleAlarm(Level worldIn) {
      super(EntityRegistry.CIRCLE_ALARM.get(), worldIn);
   }

   public void toggleSetting() {
      this.onlyNew = !this.onlyNew;
   }

   @Override
   public void tick() {
      int radius = this.getRadius();
      AABB box = new AABB(this.getX() + (double)radius, this.getY() + (double)radius, this.getZ() + (double)radius, this.getX() - (double)radius, this.getY() - (double)radius, this.getZ() - (double)radius);
      List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, box);
      List<LivingEntity> listIn = Utils.getDifference(entities, this.list);
      if (this.onlyNew) {
         if (!(listIn.isEmpty() || listIn.size() == 1 && listIn.get(0) == this.getOwner() || this.tickCount % 5 != 0)) {
            this.performSpell();
         }
         if (this.tickCount % 20 == 0) {
            this.list = entities;
         }
      } else if (!(entities.isEmpty() || entities.size() == 1 && entities.get(0) == this.getOwner() || this.random.nextInt(20) != 0)) {
         this.performSpell();
      }
      super.tick();
   }

   @Override
   public void performSpell() {
      if (this.getOwner() instanceof Player) {
         this.level().playSound((Player)this.getOwner(), this.getOwner().getX(), this.getOwner().getY(), this.getOwner().getZ(), SoundEvents.NOTE_BLOCK_BELL.value(), SoundSource.PLAYERS, 0.3F, 1.0F);
      } else {
         this.playSound(SoundEvents.NOTE_BLOCK_BELL.value(), 0.3F, 1.0F);
      }
   }

   @Override
   public void readAdditionalSaveData(CompoundTag nbt) {
      super.readAdditionalSaveData(nbt);
      this.onlyNew = nbt.getBoolean("onlyNew");
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
      nbt.putBoolean("onlyNew", this.onlyNew);
      nbt.putInt("list_size", this.list.size());
      for (int i = 0; i < this.list.size(); ++i) {
         if (this.list.get(i) == null || this.list.get(i).isRemoved()) continue;
         nbt.putInt("entity_" + String.valueOf(i), this.list.get(i).getId());
      }
   }
}
