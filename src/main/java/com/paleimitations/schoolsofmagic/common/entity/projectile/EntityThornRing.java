package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class EntityThornRing extends Entity {
   private int warmupDelayTicks;
   private boolean sentSpikeEvent;
   private int lifeTicks = 22;
   private boolean clientSideAttackStarted;
   private LivingEntity caster;
   private UUID casterUuid;

   public EntityThornRing(EntityType<? extends EntityThornRing> type, Level worldIn) {
      super(type, worldIn);
   }

   public EntityThornRing(Level worldIn) {
      this(EntityRegistry.THORN_RING.get(), worldIn);
   }

   public EntityThornRing(Level worldIn, double x, double y, double z, float p_i47276_8_, int p_i47276_9_, LivingEntity casterIn) {
      this(worldIn);
      this.warmupDelayTicks = p_i47276_9_;
      this.setCaster(casterIn);
      this.setYRot(p_i47276_8_ * 57.295776F);
      this.lifeTicks = 22;
      this.setPos(x, y, z);
   }

   @Override
   protected void defineSynchedData() {
   }

   @Override
   public net.minecraft.world.phys.AABB getBoundingBoxForCulling() {
      return this.getBoundingBox().inflate(1.5D);
   }

   public void setCaster(@Nullable LivingEntity p_190549_1_) {
      this.caster = p_190549_1_;
      this.casterUuid = p_190549_1_ == null ? null : p_190549_1_.getUUID();
   }

   @Nullable
   public LivingEntity getCaster() {
      Entity entity;
      if (this.caster == null && this.casterUuid != null && this.level() instanceof ServerLevel && (entity = ((ServerLevel)this.level()).getEntity(this.casterUuid)) instanceof LivingEntity) {
         this.caster = (LivingEntity)entity;
      }
      return this.caster;
   }

   @Override
   protected void readAdditionalSaveData(CompoundTag compound) {
      this.warmupDelayTicks = compound.getInt("Warmup");
      this.casterUuid = compound.hasUUID("OwnerUUID") ? compound.getUUID("OwnerUUID") : null;
   }

   @Override
   protected void addAdditionalSaveData(CompoundTag compound) {
      compound.putInt("Warmup", this.warmupDelayTicks);
      if (this.casterUuid != null) {
         compound.putUUID("OwnerUUID", this.casterUuid);
      }
   }

   @Override
   public void tick() {
      super.tick();
      if (this.level().isClientSide) {
         if (this.clientSideAttackStarted) {
            --this.lifeTicks;
            if (this.lifeTicks == 14) {
               for (int i = 0; i < 12; ++i) {
                  double d0 = this.getX() + (this.random.nextDouble() * 2.0 - 1.0) * (double)this.getBbWidth() * 0.5;
                  double d1 = this.getY() + 0.05 + this.random.nextDouble() * 1.0;
                  double d2 = this.getZ() + (this.random.nextDouble() * 2.0 - 1.0) * (double)this.getBbWidth() * 0.5;
                  double d3 = (this.random.nextDouble() * 2.0 - 1.0) * 0.3;
                  double d4 = 0.3 + this.random.nextDouble() * 0.3;
                  double d5 = (this.random.nextDouble() * 2.0 - 1.0) * 0.3;
                  this.level().addParticle(ParticleTypes.CRIT, d0, d1 + 1.0, d2, d3, d4, d5);
               }
            }
         }
      } else if (--this.warmupDelayTicks < 0) {
         if (this.warmupDelayTicks == -8) {
            for (LivingEntity entitylivingbase : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.2, 0.0, 0.2))) {
               this.damage(entitylivingbase);
            }
         }
         if (!this.sentSpikeEvent) {
            this.level().broadcastEntityEvent(this, (byte)4);
            this.sentSpikeEvent = true;
         }
         if (--this.lifeTicks < 0) {
            this.discard();
         }
      }
   }

   private void damage(LivingEntity p_190551_1_) {
      LivingEntity entitylivingbase = this.getCaster();
      if (p_190551_1_.isAlive() && !p_190551_1_.isInvulnerable() && p_190551_1_ != entitylivingbase) {
         if (entitylivingbase == null) {
            p_190551_1_.hurt(this.level().damageSources().magic(), 6.0F);
         } else {
            if (entitylivingbase.isAlliedTo(p_190551_1_)) {
               return;
            }
            p_190551_1_.hurt(this.level().damageSources().indirectMagic(this, entitylivingbase), 6.0F);
         }
      }
   }

   @Override
   @OnlyIn(Dist.CLIENT)
   public void handleEntityEvent(byte id) {
      super.handleEntityEvent(id);
      if (id == 4) {
         this.clientSideAttackStarted = true;
         if (!this.isSilent()) {
            this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SOMSoundHandler.THORN_RING.get(), this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.2F + 0.85F, false);
         }
      }
   }

   @OnlyIn(Dist.CLIENT)
   public float getAnimationProgress(float partialTicks) {
      if (!this.clientSideAttackStarted) {
         return 0.0F;
      }
      int i = this.lifeTicks - 2;
      return i <= 0 ? 1.0F : 1.0F - ((float)i - partialTicks) / 20.0F;
   }
}
