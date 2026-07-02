package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import java.awt.Color;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;

public class EntityMagicBolt extends AbstractSpellShot {
   private static final EntityDataAccessor<Integer> COLOR =
      SynchedEntityData.defineId(EntityMagicBolt.class, EntityDataSerializers.INT);
   private MobEffectInstance effect;

   public EntityMagicBolt(EntityType<? extends EntityMagicBolt> type, Level level) {
      super(type, level);
   }

   public EntityMagicBolt(Level level, LivingEntity thrower) {
      super(EntityRegistry.MAGIC_BOLT.get(), level, thrower);
   }

   @Override
   protected void defineSynchedData() {
      this.getEntityData().define(COLOR, 0xFFFFFF);
   }

   public void setColor(int color) { this.getEntityData().set(COLOR, color); }

   @Override
   public int getColor() { return this.getEntityData().get(COLOR); }

   @Override
   public Color getColorColor() { return new Color(this.getColor()); }

   public void setEffect(MobEffectInstance effect) { this.effect = effect; }

   @Override
   protected float getGravityVelocity() { return 0.0F; }

   @Override
   public void performSpell(HitResult result) {
      if (result instanceof EntityHitResult ehr && ehr.getEntity() instanceof LivingEntity le && le != this.getOwner()) {
         le.hurt(this.level().damageSources().indirectMagic(this, this.getOwner()), 6.0F);
         if (this.effect != null) le.addEffect(new MobEffectInstance(this.effect));
      }
      this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.GLASS_BREAK, SoundSource.AMBIENT, 0.7F, 1.4F, false);
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setColor(compound.getInt("Color"));
      this.effect = compound.contains("Effect") ? MobEffectInstance.load(compound.getCompound("Effect")) : null;
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("Color", this.getColor());
      if (this.effect != null) compound.put("Effect", this.effect.save(new CompoundTag()));
   }
}
