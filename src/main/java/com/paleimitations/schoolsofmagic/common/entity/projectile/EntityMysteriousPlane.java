package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityMysteriousPlane extends Entity {

   private static final EntityDataAccessor<Float> FADE =
      SynchedEntityData.defineId(EntityMysteriousPlane.class, EntityDataSerializers.FLOAT);

   private double distTraveled = 0.0D;

   public EntityMysteriousPlane(EntityType<? extends EntityMysteriousPlane> type, Level level) {
      super(type, level);
      this.noPhysics = true;
      this.setNoGravity(true);
   }

   public EntityMysteriousPlane(Level level, double x, double y, double z, float yaw) {
      this(EntityRegistry.MYSTERIOUS_PLANE.get(), level);
      this.setPos(x, y, z);
      this.setYRot(yaw);
      this.yRotO = yaw;
   }

   @Override
   protected void defineSynchedData() {
      this.entityData.define(FADE, 0.0F);
   }

   public float getFade() {
      return this.entityData.get(FADE);
   }

   @Override
   public void tick() {
      super.tick();
      if (this.level().isClientSide) {
         if (this.random.nextInt(3) == 0) {
            this.level().addParticle(net.minecraft.core.particles.ParticleTypes.CLOUD,
               this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
         }
         return;
      }

      double speed = 0.85D;
      float yaw = this.getYRot();
      double rad = Math.toRadians(yaw);
      double vx = -Math.sin(rad) * speed;
      double vz = Math.cos(rad) * speed;
      double vy = Math.cos(this.tickCount * 0.16D) * 0.22D + 0.05D;
      this.setDeltaMovement(vx, vy, vz);
      this.move(MoverType.SELF, this.getDeltaMovement());
      this.setYRot(this.getYRot() + (float) Math.sin(this.tickCount * 0.11D) * 3.5F);
      this.distTraveled += Math.sqrt(vx * vx + vz * vz);

      if (this.distTraveled >= 300.0D) {
         this.entityData.set(FADE, Math.min(1.0F, this.getFade() + 0.03F));
      }
      if (this.getFade() >= 1.0F || this.tickCount > 800) {
         this.discard();
      }
   }

   @Override
   public boolean shouldRenderAtSqrDistance(double dist) {
      return true;
   }

   @Override
   protected void readAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {
      this.distTraveled = tag.getDouble("Dist");
   }

   @Override
   protected void addAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {
      tag.putDouble("Dist", this.distTraveled);
   }

   @Override
   public net.minecraft.network.protocol.Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getAddEntityPacket() {
      return new net.minecraft.network.protocol.game.ClientboundAddEntityPacket(this);
   }
}
