package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkHooks;

public class EntityStarfallCloud extends Entity {
   private static final EntityDataAccessor<Integer> COLOR =
      SynchedEntityData.defineId(EntityStarfallCloud.class, EntityDataSerializers.INT);
   private MobEffectInstance effect;
   private int meteorCount = 4;
   private int lifeTicks = 200;
   private UUID casterUuid;
   private int[] dropTicks = null;
   private int dropped = 0;

   public EntityStarfallCloud(EntityType<? extends EntityStarfallCloud> type, Level level) {
      super(type, level);
      this.noPhysics = true;
   }

   public EntityStarfallCloud(Level level) {
      this(EntityRegistry.STARFALL_CLOUD.get(), level);
   }

   @Override
   protected void defineSynchedData() {
      this.getEntityData().define(COLOR, 0xFFFFFF);
   }

   public void setColor(int color) { this.getEntityData().set(COLOR, color); }
   public int getColor() { return this.getEntityData().get(COLOR); }
   public void setEffect(MobEffectInstance effect) { this.effect = effect; }
   public void setMeteorCount(int n) { this.meteorCount = Math.max(1, n); }
   public void setCaster(@Nullable LivingEntity caster) { this.casterUuid = caster == null ? null : caster.getUUID(); }

   @Override
   public void tick() {
      super.tick();
      if (this.level().isClientSide) {
         java.awt.Color col = new java.awt.Color(this.getColor() & 0xFFFFFF);
         for (int i = 0; i < 3; i++) {
            com.paleimitations.schoolsofmagic.client.effects.EffectHelper.createColoredPuffParticle(this.level(),
               this.getX() + (this.random.nextDouble() - 0.5D) * 4.0D,
               this.getY() + (this.random.nextDouble() - 0.5D) * 1.5D,
               this.getZ() + (this.random.nextDouble() - 0.5D) * 4.0D, col);
         }
         return;
      }
      if (this.dropTicks == null) {
         this.dropTicks = new int[this.meteorCount];
         for (int i = 0; i < this.meteorCount; i++) this.dropTicks[i] = 10 + this.random.nextInt(this.lifeTicks - 20);
         java.util.Arrays.sort(this.dropTicks);
      }
      while (this.dropped < this.meteorCount && this.dropTicks[this.dropped] <= this.tickCount) {
         dropMeteor();
         this.dropped++;
      }
      if (this.tickCount >= this.lifeTicks) this.discard();
   }

   private void dropMeteor() {
      double ox = (this.random.nextDouble() - 0.5D) * 5.0D;
      double oz = (this.random.nextDouble() - 0.5D) * 5.0D;
      EntityMagicMeteor meteor = new EntityMagicMeteor(this.level(), this.getX() + ox, this.getY(), this.getZ() + oz);
      meteor.setColor(this.getColor());
      meteor.setEffect(this.effect);
      meteor.setDeltaMovement((this.random.nextDouble() - 0.5D) * 0.1D, -1.2D, (this.random.nextDouble() - 0.5D) * 0.1D);
      this.level().addFreshEntity(meteor);
   }

   @Override
   public AABB getBoundingBoxForCulling() {
      return this.getBoundingBox().inflate(6.0D);
   }

   @Override
   protected void readAdditionalSaveData(CompoundTag nbt) {
      this.setColor(nbt.getInt("Color"));
      this.meteorCount = nbt.getInt("Count");
      this.lifeTicks = nbt.getInt("Life");
      this.dropped = nbt.getInt("Dropped");
      this.effect = nbt.contains("Effect") ? MobEffectInstance.load(nbt.getCompound("Effect")) : null;
      if (nbt.hasUUID("Caster")) this.casterUuid = nbt.getUUID("Caster");
   }

   @Override
   protected void addAdditionalSaveData(CompoundTag nbt) {
      nbt.putInt("Color", this.getColor());
      nbt.putInt("Count", this.meteorCount);
      nbt.putInt("Life", this.lifeTicks);
      nbt.putInt("Dropped", this.dropped);
      if (this.effect != null) nbt.put("Effect", this.effect.save(new CompoundTag()));
      if (this.casterUuid != null) nbt.putUUID("Caster", this.casterUuid);
   }

   @Override
   public Packet<ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }
}
