package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
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

public class EntityMagicWall extends Entity {
   private static final EntityDataAccessor<Integer> COLOR =
      SynchedEntityData.defineId(EntityMagicWall.class, EntityDataSerializers.INT);
   private MobEffectInstance effect;
   private int lifeTicks = 600;
   private UUID casterUuid;

   public EntityMagicWall(EntityType<? extends EntityMagicWall> type, Level level) {
      super(type, level);
      this.noPhysics = true;
   }

   public EntityMagicWall(Level level) {
      this(EntityRegistry.MAGIC_WALL.get(), level);
   }

   @Override
   protected void defineSynchedData() {
      this.getEntityData().define(COLOR, 0xFFFFFF);
   }

   public void setColor(int color) { this.getEntityData().set(COLOR, color); }
   public int getColor() { return this.getEntityData().get(COLOR); }
   public void setEffect(MobEffectInstance effect) { this.effect = effect; }
   public void setLife(int ticks) { this.lifeTicks = ticks; }
   public void setCaster(@Nullable LivingEntity caster) { this.casterUuid = caster == null ? null : caster.getUUID(); }

   private AABB wallBox() {
      Direction face = Direction.fromYRot(this.getYRot());
      Direction side = face.getClockWise();
      double cx = this.getX(), cy = this.getY(), cz = this.getZ();
      double w = 3.0D, t = 0.7D;
      double dx = Math.abs(side.getStepX()) * w + Math.abs(face.getStepX()) * t;
      double dz = Math.abs(side.getStepZ()) * w + Math.abs(face.getStepZ()) * t;
      return new AABB(cx - dx, cy, cz - dz, cx + dx, cy + 6.0D, cz + dz);
   }

   @Override
   public void tick() {
      super.tick();
      if (!this.level().isClientSide) {
         if (this.effect != null && this.tickCount % 24 == 0) {
            for (LivingEntity le : this.level().getEntitiesOfClass(LivingEntity.class, wallBox())) {
               if (this.casterUuid != null && le.getUUID().equals(this.casterUuid)) continue;
               le.addEffect(new MobEffectInstance(this.effect));
            }
         }
         if (--this.lifeTicks <= 0) this.discard();
      }
   }

   @Override
   public AABB getBoundingBoxForCulling() {
      return this.getBoundingBox().inflate(6.0D);
   }

   @Override
   protected void readAdditionalSaveData(CompoundTag nbt) {
      this.setColor(nbt.getInt("Color"));
      this.lifeTicks = nbt.getInt("Life");
      this.effect = nbt.contains("Effect") ? MobEffectInstance.load(nbt.getCompound("Effect")) : null;
      if (nbt.hasUUID("Caster")) this.casterUuid = nbt.getUUID("Caster");
      this.setYRot(nbt.getFloat("Yaw"));
   }

   @Override
   protected void addAdditionalSaveData(CompoundTag nbt) {
      nbt.putInt("Color", this.getColor());
      nbt.putInt("Life", this.lifeTicks);
      if (this.effect != null) nbt.put("Effect", this.effect.save(new CompoundTag()));
      if (this.casterUuid != null) nbt.putUUID("Caster", this.casterUuid);
      nbt.putFloat("Yaw", this.getYRot());
   }

   @Override
   public Packet<ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }
}
