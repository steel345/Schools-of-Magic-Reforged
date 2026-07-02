package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import java.awt.Color;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class EntityPotionShot extends AbstractSpellShot {
   private final List<MobEffectInstance> effects = Lists.newArrayList();
   private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(EntityPotionShot.class, EntityDataSerializers.INT);
   private boolean colorSet;
   private float impactDamage = 0.0F;

   public void setImpactDamage(float damage) {
      this.impactDamage = damage;
   }

   public EntityPotionShot(EntityType<? extends EntityPotionShot> type, Level worldIn) {
      super(type, worldIn);
   }

   public EntityPotionShot(Level worldIn, LivingEntity throwerIn) {
      super(EntityRegistry.POTION_SHOT.get(), worldIn, throwerIn);
   }

   public EntityPotionShot(Level worldIn, double x, double y, double z) {
      super(EntityRegistry.POTION_SHOT.get(), worldIn, x, y, z);
   }

   @Override
   protected void defineSynchedData() {
      this.getEntityData().define(COLOR, 0);
   }

   private void updateFixedColor() {
      this.getEntityData().set(COLOR, PotionUtils.getColor(this.effects));
   }

   public void addEffect(MobEffectInstance effect) {
      this.effects.add(effect);
      if (!this.colorSet) {
         this.updateFixedColor();
      }
   }

   @Override
   public int getColor() {
      return this.getEntityData().get(COLOR);
   }

   @Override
   public Color getColorColor() {
      return new Color(this.getColor());
   }

   public void setColor(int colorIn) {
      this.colorSet = true;
      this.getEntityData().set(COLOR, colorIn);
   }

   @Override
   protected float getGravityVelocity() {
      return 0.0F;
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      if (compound.contains("Color", 99)) {
         this.setColor(compound.getInt("Color"));
      }
      if (compound.contains("Effects", 9)) {
         ListTag nbttaglist = compound.getList("Effects", 10);
         this.effects.clear();
         for (int i = 0; i < nbttaglist.size(); ++i) {
            MobEffectInstance potioneffect = MobEffectInstance.load(nbttaglist.getCompound(i));
            if (potioneffect == null) continue;
            this.addEffect(potioneffect);
         }
      }
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      if (this.colorSet) {
         compound.putInt("Color", this.getColor());
      }
      if (!this.effects.isEmpty()) {
         ListTag nbttaglist = new ListTag();
         for (MobEffectInstance potioneffect : this.effects) {
            nbttaglist.add((Tag)potioneffect.save(new CompoundTag()));
         }
         compound.put("Effects", nbttaglist);
      }
      super.addAdditionalSaveData(compound);
   }

   @Override
   public void performSpell(HitResult result) {
      List<MobEffectInstance> list = this.effects;
      if (result instanceof EntityHitResult) {
         Entity entity = ((EntityHitResult)result).getEntity();
         if (entity != null && entity instanceof LivingEntity) {
            if (entity != this.getOwner()) {
               if (this.impactDamage > 0.0F) {
                  entity.hurt(this.level().damageSources().indirectMagic(this, this.getOwner()), this.impactDamage);
               }
               for (MobEffectInstance effect : list) {
                  ((LivingEntity)entity).addEffect(effect);
               }
            } else {
               return;
            }
         }
      }
      this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.AMBIENT, 1.0F, 1.0F, false);
   }
}
