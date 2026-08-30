package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityPrecisionBolt extends AbstractSpellShot implements IBoltTrail {
   public static final float DAMAGE = 4.0F;
   public static final int TRAIL_POINTS = 16;
   private static final int LIFETIME = 300;
   private static final double SPEED = 0.9D;
   private static final double MAX_TURN_RADIANS = Math.toRadians(14.0D);
   private static final double TURN_CLOSE = 8.0D;
   private static final double STRIKE_RANGE = 1.2D;
   private static final int ASCEND_TICKS = 7;
   private static final double ASCEND_SPEED = 0.55D;
   private static final double SPARK_R = 0.451D;
   private static final double SPARK_G = 0.153D;
   private static final double SPARK_B = 0.694D;

   public final List<Vec3> trail = Lists.newArrayList();

   @Override
   public List<Vec3> boltTrail() {
      return this.trail;
   }

   private UUID targetUUID;
   private LivingEntity target;

   public EntityPrecisionBolt(EntityType<? extends EntityPrecisionBolt> type, Level level) {
      super(type, level);
      this.setNoGravity(true);
   }

   public EntityPrecisionBolt(Level level, LivingEntity thrower) {
      super(EntityRegistry.PRECISION_BOLT.get(), level, thrower);
      this.setNoGravity(true);
   }

   public void setTarget(LivingEntity target) {
      this.target = target;
      this.targetUUID = target == null ? null : target.getUUID();
   }

   public void aimAt(Vec3 heading) {
      float yaw = (float) (Mth.atan2(heading.x, heading.z) * 57.29577951308232D);
      float pitch = (float) (Mth.atan2(heading.y, heading.horizontalDistance()) * 57.29577951308232D);
      this.setYRot(yaw);
      this.setXRot(pitch);
      this.yRotO = yaw;
      this.xRotO = pitch;
   }

   @Nullable
   public LivingEntity getTarget() {
      if (this.target != null && this.target.isAlive()) return this.target;
      if (this.targetUUID != null && this.level() instanceof ServerLevel server) {
         Entity found = server.getEntity(this.targetUUID);
         if (found instanceof LivingEntity living) {
            this.target = living;
            return living;
         }
      }
      return null;
   }

   @Override
   public int getColor() {
      return 0x7327B1;
   }

   @Override
   protected float getGravityVelocity() {
      return 0.0F;
   }

   @Override
   public void tick() {
      super.tick();
      this.recordTrail();

      if (this.level().isClientSide) return;

      if (this.tickCount > LIFETIME) {
         this.fizzle();
         return;
      }

      if (this.tickCount <= ASCEND_TICKS) {
         this.ascend();
         return;
      }

      LivingEntity aim = this.getTarget();
      if (aim != null && this.reached(aim)) {
         this.performSpell(new EntityHitResult(aim));
         this.discard();
         return;
      }
      if (aim == null) {
         this.fizzle();
         return;
      }
      this.steerTowards(aim);
   }

   private void recordTrail() {
      this.trail.add(0, this.position());
      while (this.trail.size() > TRAIL_POINTS) {
         this.trail.remove(this.trail.size() - 1);
      }
   }

   private void ascend() {
      double slow = 1.0D - (double) this.tickCount / (ASCEND_TICKS + 2);
      this.setDeltaMovement(0.0D, ASCEND_SPEED * slow, 0.0D);
   }

   private boolean reached(LivingEntity aim) {
      return aim.getBoundingBox().inflate(0.35D).contains(this.position())
         || this.position().distanceTo(aim.position().add(0.0D, aim.getBbHeight() * 0.5D, 0.0D)) <= 0.6D;
   }

   private void steerTowards(LivingEntity aim) {
      Vec3 centre = aim.position().add(0.0D, aim.getBbHeight() * 0.5D, 0.0D);
      Vec3 to = centre.subtract(this.position());
      double distance = to.length();
      if (distance < 1.0E-4D) return;

      if (distance <= STRIKE_RANGE) {
         this.setDeltaMovement(to);
         return;
      }

      Vec3 desired = to.normalize();
      Vec3 motion = this.getDeltaMovement();
      if (motion.lengthSqr() < 1.0E-6D) {
         this.setDeltaMovement(desired.scale(SPEED));
         return;
      }

      Vec3 current = motion.normalize();
      double dot = Mth.clamp(current.dot(desired), -1.0D, 1.0D);
      double angle = Math.acos(dot);
      double allowed = MAX_TURN_RADIANS * Math.max(1.0D, TURN_CLOSE / Math.max(1.0D, distance));

      Vec3 heading;
      if (angle <= allowed || angle < 1.0E-4D) {
         heading = desired;
      } else {
         double t = allowed / angle;
         heading = current.scale(1.0D - t).add(desired.scale(t)).normalize();
      }

      double speed = Math.min(SPEED, Math.max(distance, Mth.lerp(0.25D, motion.length(), SPEED)));
      this.setDeltaMovement(heading.scale(speed));
   }

   @Override
   protected void onImpact(HitResult result) {
      if (result instanceof EntityHitResult ehr) {
         LivingEntity aim = this.getTarget();
         if (aim != null && ehr.getEntity() == aim) super.onImpact(result);
      }
   }

   @Override
   public void performSpell(HitResult result) {
      LivingEntity aim = this.getTarget();
      if (aim != null) {
         aim.hurt(this.level().damageSources().indirectMagic(this, this.getOwner()), DAMAGE);
      }
      if (this.level() instanceof ServerLevel server) {
         server.playSound(null, this.getX(), this.getY(), this.getZ(),
            SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.PLAYERS, 0.7F, 0.8F);
         this.sparkleBurst(server, 20, 0.35D);
      }
   }

   private void sparkleBurst(ServerLevel server, int count, double radius) {
      for (int i = 0; i < count; i++) {
         double theta = this.random.nextDouble() * Math.PI * 2.0D;
         double phi = Math.acos(2.0D * this.random.nextDouble() - 1.0D);
         server.sendParticles(
            com.paleimitations.schoolsofmagic.common.registries.ParticleTypeRegistry.SPARKLE_STAR.get(),
            this.getX() + Math.sin(phi) * Math.cos(theta) * radius,
            this.getY() + Math.cos(phi) * radius,
            this.getZ() + Math.sin(phi) * Math.sin(theta) * radius,
            0, SPARK_R, SPARK_G, SPARK_B, 1.0D);
      }
   }

   private void fizzle() {
      if (this.level() instanceof ServerLevel server) {
         this.sparkleBurst(server, 8, 0.2D);
      }
      this.discard();
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      if (this.targetUUID != null) compound.putUUID("BoltTarget", this.targetUUID);
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.targetUUID = compound.hasUUID("BoltTarget") ? compound.getUUID("BoltTarget") : null;
      this.target = null;
   }
}
