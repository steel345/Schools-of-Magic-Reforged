package com.paleimitations.schoolsofmagic.common.entity.projectile;

import java.awt.Color;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public abstract class AbstractSpellShot extends Projectile {
   private int xTile = -1;
   private int yTile = -1;
   private int zTile = -1;
   private Block inTile;
   protected boolean inGround;
   public int throwableShake;
   protected boolean homing = false;
   protected double homingBaseSpeed = 0.0D;

   public void setHoming(boolean h) { this.homing = h; }

   protected void homingTick() {
      if (!this.homing || this.level().isClientSide) return;
      Vec3 motion = this.getDeltaMovement();
      double speed = motion.length();
      if (speed < 1.0E-4D) return;
      if (this.homingBaseSpeed <= 0.0D) this.homingBaseSpeed = speed;
      net.minecraft.world.entity.LivingEntity tgt = acquireHomingTarget();
      if (tgt == null) return;
      Vec3 toT = new Vec3(tgt.getX() - this.getX(), (tgt.getY() + tgt.getBbHeight() * 0.5D) - this.getY(), tgt.getZ() - this.getZ());
      if (toT.lengthSqr() < 1.0E-4D) return;
      Vec3 curDir = motion.normalize();
      Vec3 desDir = toT.normalize();
      double turn = (1.0D - curDir.dot(desDir)) * 0.5D;
      Vec3 newDir = curDir.scale(0.7D).add(desDir.scale(0.3D)).normalize();
      double targetSpeed = this.homingBaseSpeed * (1.0D - turn * 0.7D);
      double newSpeed = Math.max(this.homingBaseSpeed * 0.25D, speed * 0.85D + targetSpeed * 0.15D);
      this.setDeltaMovement(newDir.scale(newSpeed));
   }

   @Nullable
   protected net.minecraft.world.entity.LivingEntity acquireHomingTarget() {
      net.minecraft.world.phys.AABB box = this.getBoundingBox().inflate(16.0D);
      net.minecraft.world.entity.LivingEntity best = null;
      double bestD = Double.MAX_VALUE;
      for (net.minecraft.world.entity.LivingEntity le : this.level().getEntitiesOfClass(net.minecraft.world.entity.LivingEntity.class, box)) {
         if (le == this.getOwner()) continue;
         double d = le.distanceToSqr(this);
         if (d < bestD) { bestD = d; best = le; }
      }
      return best;
   }
   protected LivingEntity thrower;
   private String throwerName;
   private int ticksInGround;
   private int ticksInAir;
   public Entity ignoreEntity;
   private int ignoreTime;

   public AbstractSpellShot(EntityType<? extends AbstractSpellShot> type, Level worldIn) {
      super(type, worldIn);
   }

   public AbstractSpellShot(EntityType<? extends AbstractSpellShot> type, Level worldIn, double x, double y, double z) {
      this(type, worldIn);
      this.setPos(x, y, z);
   }

   public AbstractSpellShot(EntityType<? extends AbstractSpellShot> type, Level worldIn, LivingEntity throwerIn) {
      this(type, worldIn, throwerIn.getX(), throwerIn.getY() + (double) throwerIn.getEyeHeight() - (double) 0.1F, throwerIn.getZ());
      this.thrower = throwerIn;
      this.setOwner(throwerIn);
   }

   @Override
   protected void defineSynchedData() {
   }

   @Override
   public boolean shouldRenderAtSqrDistance(double distance) {
      double d0 = this.getBoundingBox().getSize() * 4.0;
      if (Double.isNaN(d0)) {
         d0 = 4.0;
      }
      d0 *= 64.0;
      return distance < d0 * d0;
   }

   public abstract int getColor();

   public Color getColorColor() {
      return new Color(this.getColor());
   }

   @Override
   public void tick() {
      this.xo = this.getX();
      this.yo = this.getY();
      this.zo = this.getZ();
      super.tick();
      if (this.throwableShake > 0) {
         --this.throwableShake;
      }
      this.homingTick();
      Vec3 motion = this.getDeltaMovement();
      if (this.inGround) {
         if (this.level().getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile) {
            ++this.ticksInGround;
            if (this.ticksInGround == 1200) {
               this.discard();
            }
            return;
         }
         this.inGround = false;
         this.setDeltaMovement(motion.x * (double)(this.random.nextFloat() * 0.2F), motion.y * (double)(this.random.nextFloat() * 0.2F), motion.z * (double)(this.random.nextFloat() * 0.2F));
         motion = this.getDeltaMovement();
         this.ticksInGround = 0;
         this.ticksInAir = 0;
      } else {
         ++this.ticksInAir;
      }
      Vec3 vec3d = new Vec3(this.getX(), this.getY(), this.getZ());
      Vec3 vec3d1 = new Vec3(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);
      HitResult raytraceresult = this.level().clip(new net.minecraft.world.level.ClipContext(vec3d, vec3d1, net.minecraft.world.level.ClipContext.Block.COLLIDER, net.minecraft.world.level.ClipContext.Fluid.NONE, this));
      vec3d = new Vec3(this.getX(), this.getY(), this.getZ());
      vec3d1 = new Vec3(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);
      if (raytraceresult.getType() != HitResult.Type.MISS) {
         vec3d1 = raytraceresult.getLocation();
      }
      Entity entity = null;
      List<Entity> list = this.level().getEntities(this, this.getBoundingBox().expandTowards(motion.x, motion.y, motion.z).inflate(1.0));
      double d0 = 0.0;
      boolean flag = false;
      for (int i = 0; i < list.size(); ++i) {
         Entity entity1 = list.get(i);
         if (!entity1.isPickable()) continue;
         if (entity1 == this.ignoreEntity) {
            flag = true;
            continue;
         }
         if (this.thrower != null && this.tickCount < 2 && this.ignoreEntity == null) {
            this.ignoreEntity = entity1;
            flag = true;
            continue;
         }
         flag = false;
         AABB axisalignedbb = entity1.getBoundingBox().inflate((double) 0.3F);
         java.util.Optional<Vec3> opt = axisalignedbb.clip(vec3d, vec3d1);
         if (opt.isEmpty()) continue;
         double d1 = vec3d.distanceToSqr(opt.get());
         if (!(d1 < d0) && d0 != 0.0) continue;
         entity = entity1;
         d0 = d1;
      }
      if (this.ignoreEntity != null) {
         if (flag) {
            this.ignoreTime = 2;
         } else if (this.ignoreTime-- <= 0) {
            this.ignoreEntity = null;
         }
      }
      if (entity != null) {
         raytraceresult = new net.minecraft.world.phys.EntityHitResult(entity);
      }
      if (raytraceresult != null && raytraceresult.getType() != HitResult.Type.MISS) {
         if (raytraceresult.getType() == HitResult.Type.BLOCK && this.level().getBlockState(((BlockHitResult)raytraceresult).getBlockPos()).getBlock() == Blocks.NETHER_PORTAL) {
            this.handleInsidePortal(((BlockHitResult)raytraceresult).getBlockPos());
         } else if (!ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
            this.onImpact(raytraceresult);
         }
      }
      this.setPos(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);
      float f = Mth.sqrt((float)(motion.x * motion.x + motion.z * motion.z));
      this.setYRot((float)(Mth.atan2(motion.x, motion.z) * 57.29577951308232));
      this.setXRot((float)(Mth.atan2(motion.y, (double) f) * 57.29577951308232));
      while (this.getXRot() - this.xRotO < -180.0F) {
         this.xRotO -= 360.0F;
      }
      while (this.getXRot() - this.xRotO >= 180.0F) {
         this.xRotO += 360.0F;
      }
      while (this.getYRot() - this.yRotO < -180.0F) {
         this.yRotO -= 360.0F;
      }
      while (this.getYRot() - this.yRotO >= 180.0F) {
         this.yRotO += 360.0F;
      }
      this.setXRot(this.xRotO + (this.getXRot() - this.xRotO) * 0.2F);
      this.setYRot(this.yRotO + (this.getYRot() - this.yRotO) * 0.2F);
      float f2 = this.getGravityVelocity();
      if (this.isInWater()) {
         for (int j = 0; j < 4; ++j) {
            this.level().addParticle(ParticleTypes.BUBBLE, this.getX() - motion.x * 0.25, this.getY() - motion.y * 0.25, this.getZ() - motion.z * 0.25, motion.x, motion.y, motion.z);
         }
      }
      if (!this.isNoGravity()) {
         this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y - (double) f2, this.getDeltaMovement().z);
      }
      this.setPos(this.getX(), this.getY(), this.getZ());
   }

   public void shoot(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
      float f = -Mth.sin(rotationYawIn * ((float) Math.PI / 180F)) * Mth.cos(rotationPitchIn * ((float) Math.PI / 180F));
      float f1 = -Mth.sin((rotationPitchIn + pitchOffset) * ((float) Math.PI / 180F));
      float f2 = Mth.cos(rotationYawIn * ((float) Math.PI / 180F)) * Mth.cos(rotationPitchIn * ((float) Math.PI / 180F));
      this.shoot((double) f, (double) f1, (double) f2, velocity, inaccuracy);
      Vec3 throwerMotion = entityThrower.getDeltaMovement();
      Vec3 current = this.getDeltaMovement();
      this.setDeltaMovement(current.x + throwerMotion.x, current.y + (entityThrower.onGround() ? 0.0 : throwerMotion.y), current.z + throwerMotion.z);
   }

   @Override
   public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
      float f = Mth.sqrt((float)(x * x + y * y + z * z));
      x /= (double) f;
      y /= (double) f;
      z /= (double) f;
      x += this.random.nextGaussian() * (double) 0.0075F * (double) inaccuracy;
      y += this.random.nextGaussian() * (double) 0.0075F * (double) inaccuracy;
      z += this.random.nextGaussian() * (double) 0.0075F * (double) inaccuracy;
      x *= (double) velocity;
      y *= (double) velocity;
      z *= (double) velocity;
      this.setDeltaMovement(x, y, z);
      float f1 = Mth.sqrt((float)(x * x + z * z));
      this.setYRot((float)(Mth.atan2(x, z) * 57.29577951308232));
      this.setXRot((float)(Mth.atan2(y, (double) f1) * 57.29577951308232));
      this.yRotO = this.getYRot();
      this.xRotO = this.getXRot();
      this.ticksInGround = 0;
   }

   @Override
   public void lerpMotion(double x, double y, double z) {
      this.setDeltaMovement(x, y, z);
      if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
         float f = Mth.sqrt((float)(x * x + z * z));
         this.setYRot((float)(Mth.atan2(x, z) * 57.29577951308232));
         this.setXRot((float)(Mth.atan2(y, (double) f) * 57.29577951308232));
         this.yRotO = this.getYRot();
         this.xRotO = this.getXRot();
      }
   }

   protected float getGravityVelocity() {
      return 0.0F;
   }

   @Override
   protected void onHit(HitResult result) {
      if (!this.level().isClientSide) {
         this.performSpell(result);
         this.discard();
      }
   }

   protected void onImpact(HitResult result) {
      this.onHit(result);
   }

   public abstract void performSpell(HitResult var1);

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      compound.putInt("xTile", this.xTile);
      compound.putInt("yTile", this.yTile);
      compound.putInt("zTile", this.zTile);
      ResourceLocation resourcelocation = this.inTile == null ? null : net.minecraftforge.registries.ForgeRegistries.BLOCKS.getKey(this.inTile);
      compound.putString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
      compound.putByte("shake", (byte) this.throwableShake);
      compound.putBoolean("homing", this.homing);
      compound.putDouble("homingBase", this.homingBaseSpeed);
      compound.putByte("inGround", (byte) (this.inGround ? 1 : 0));
      if ((this.throwerName == null || this.throwerName.isEmpty()) && this.thrower instanceof Player) {
         this.throwerName = this.thrower.getName().getString();
      }
      compound.putString("ownerName", this.throwerName == null ? "" : this.throwerName);
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      this.xTile = compound.getInt("xTile");
      this.yTile = compound.getInt("yTile");
      this.zTile = compound.getInt("zTile");
      if (compound.contains("inTile", 8)) {
         this.inTile = net.minecraftforge.registries.ForgeRegistries.BLOCKS.getValue(new ResourceLocation(compound.getString("inTile")));
      } else {
         this.inTile = Block.stateById(compound.getByte("inTile") & 0xFF).getBlock();
      }
      this.throwableShake = compound.getByte("shake") & 0xFF;
      this.homing = compound.getBoolean("homing");
      this.homingBaseSpeed = compound.getDouble("homingBase");
      this.inGround = compound.getByte("inGround") == 1;
      this.thrower = null;
      this.throwerName = compound.getString("ownerName");
      if (this.throwerName != null && this.throwerName.isEmpty()) {
         this.throwerName = null;
      }
      this.thrower = this.getThrower();
   }

   @Nullable
   public LivingEntity getThrower() {
      if (this.thrower == null && this.throwerName != null && !this.throwerName.isEmpty()) {
         Player player = this.level().getPlayerByUUID(tryParseUUID(this.throwerName));
         if (player != null) {
            this.thrower = player;
         }
         if (this.thrower == null && this.level() instanceof ServerLevel) {
            try {
               Entity entity = ((ServerLevel) this.level()).getEntity(UUID.fromString(this.throwerName));
               if (entity instanceof LivingEntity) {
                  this.thrower = (LivingEntity) entity;
               }
            } catch (Throwable var2) {
               this.thrower = null;
            }
         }
      }
      return this.thrower;
   }

   private static UUID tryParseUUID(String s) {
      try {
         return UUID.fromString(s);
      } catch (Throwable t) {
         return new UUID(0L, 0L);
      }
   }
}
