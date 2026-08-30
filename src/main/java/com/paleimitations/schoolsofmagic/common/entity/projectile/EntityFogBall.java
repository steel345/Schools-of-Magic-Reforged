package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.paleimitations.schoolsofmagic.common.handlers.FogHandler;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ParticleTypeRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

// the fog travels as a thrown thing and settles wherever it stops. it carries how long the bank
// it makes should last
public class EntityFogBall extends ThrowableProjectile {
   private int bankTicks = 200;

   public EntityFogBall(EntityType<? extends EntityFogBall> type, Level level) {
      super(type, level);
   }

   public EntityFogBall(Level level, LivingEntity thrower) {
      super(EntityRegistry.FOG_BALL.get(), thrower, level);
   }

   public void setBankTicks(int ticks) {
      this.bankTicks = ticks;
   }

   @Override
   protected void defineSynchedData() {
   }

   @Override
   protected float getGravity() {
      return 0.02F;
   }

   @Override
   public void tick() {
      super.tick();
      if (this.level().isClientSide) {
         this.level().addParticle(ParticleTypeRegistry.FOG.get(),
            this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
      } else if (this.tickCount > 120) {
         this.settle(this.position());
      }
   }

   @Override
   protected void onHit(HitResult result) {
      super.onHit(result);
      if (!this.level().isClientSide) this.settle(result.getLocation());
   }

   private void settle(Vec3 at) {
      if (this.level() instanceof ServerLevel server) {
         FogHandler.plant(server, at, this.bankTicks);
      }
      this.discard();
   }

   @Override
   protected void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      this.bankTicks = tag.getInt("BankTicks");
   }

   @Override
   protected void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
      tag.putInt("BankTicks", this.bankTicks);
   }

   @Override
   public Packet<ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }
}
