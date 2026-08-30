package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class EntityShroom extends AbstractSpellShot {
   public static final int[] TINTS = {0x55A334, 0x184792, 0x92188F};

   private static final EntityDataAccessor<Integer> VARIANT =
      SynchedEntityData.defineId(EntityShroom.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> SPORE_TICKS =
      SynchedEntityData.defineId(EntityShroom.class, EntityDataSerializers.INT);

   private static final int LIFETIME = 120;
   private static final float DIRECT_DAMAGE = 1.0F;
   private static final double SHOVE_RANGE = 3.0D;
   private static final float SHOVE = 0.4F;

   public EntityShroom(EntityType<? extends EntityShroom> type, Level level) {
      super(type, level);
   }

   public EntityShroom(Level level, LivingEntity thrower) {
      super(EntityRegistry.SHROOM.get(), level, thrower);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.getEntityData().define(VARIANT, 0);
      this.getEntityData().define(SPORE_TICKS, 200);
   }

   public int getVariant() {
      return this.getEntityData().get(VARIANT);
   }

   public void setVariant(int variant) {
      this.getEntityData().set(VARIANT, Math.floorMod(variant, TINTS.length));
   }

   public void setSporeTicks(int ticks) {
      this.getEntityData().set(SPORE_TICKS, ticks);
   }

   public int getSporeTicks() {
      return this.getEntityData().get(SPORE_TICKS);
   }

   @Override
   public int getColor() {
      return TINTS[Math.floorMod(this.getVariant(), TINTS.length)];
   }

   @Override
   protected float getGravityVelocity() {
      return 0.03F;
   }

   @Override
   public void tick() {
      super.tick();
      if (this.tickCount > LIFETIME) this.discard();
   }

   public SoundEvent throwSound() {
      return switch (this.random.nextInt(3)) {
         case 0 -> SOMSoundHandler.SHROOM_USE1.get();
         case 1 -> SOMSoundHandler.SHROOM_USE2.get();
         default -> SOMSoundHandler.SHROOM_USE3.get();
      };
   }

   private SoundEvent burstSound() {
      return switch (this.random.nextInt(5)) {
         case 0 -> SOMSoundHandler.SHROOM_EXPLODE1.get();
         case 1 -> SOMSoundHandler.SHROOM_EXPLODE2.get();
         case 2 -> SOMSoundHandler.SHROOM_EXPLODE3.get();
         case 3 -> SOMSoundHandler.SHROOM_EXPLODE4.get();
         default -> SOMSoundHandler.SHROOM_EXPLODE5.get();
      };
   }

   @Override
   public void performSpell(HitResult result) {
      this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
         this.burstSound(), SoundSource.PLAYERS, 1.0F, 0.9F + this.random.nextFloat() * 0.3F);

      if (!(this.level() instanceof net.minecraft.server.level.ServerLevel server)) return;

      Entity owner = this.getOwner();
      if (result instanceof net.minecraft.world.phys.EntityHitResult hit
            && hit.getEntity() instanceof LivingEntity struck && struck != owner) {
         struck.hurt(this.level().damageSources().thrown(this, owner), DIRECT_DAMAGE);
      }
      shove(server, owner);
      com.paleimitations.schoolsofmagic.common.handlers.SporePuffHandler.burst(server,
         this.position(), 3.0D, this.getSporeTicks(), this.getColor(),
         owner == null ? null : owner.getUUID());
   }

   @Override
   public void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
      tag.putInt("Variant", this.getVariant());
      tag.putInt("SporeTicks", this.getSporeTicks());
   }

   @Override
   public void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      this.setVariant(tag.getInt("Variant"));
      this.setSporeTicks(tag.getInt("SporeTicks"));
   }

   // the burst pushes whatever is standing in it, the caster included
   private void shove(net.minecraft.server.level.ServerLevel server, Entity owner) {
      net.minecraft.world.phys.AABB reach = this.getBoundingBox().inflate(SHOVE_RANGE);
      for (LivingEntity living : server.getEntitiesOfClass(LivingEntity.class, reach)) {
         if (living == owner) continue;
         if (living.distanceToSqr(this.position()) > SHOVE_RANGE * SHOVE_RANGE) continue;
         living.knockback(SHOVE, this.getX() - living.getX(), this.getZ() - living.getZ());
      }
   }
}
