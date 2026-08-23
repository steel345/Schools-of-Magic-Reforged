package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityFireBall extends AbstractSpellShot {
   public static final float BLAST_POWER = 4.0F;
   public static final float DAMAGE = 16.0F;
   private static final double BLAST_RANGE = 4.0D;
   private static final int LIFETIME = 200;

   public EntityFireBall(EntityType<? extends EntityFireBall> type, Level level) {
      super(type, level);
   }

   public EntityFireBall(Level level, LivingEntity thrower) {
      super(EntityRegistry.FIRE_BALL.get(), level, thrower);
   }

   @Override
   public int getColor() {
      return 0xFF8A2B;
   }

   @Override
   protected float getGravityVelocity() {
      return 0.0F;
   }

   @Override
   public void tick() {
      super.tick();
      if (this.tickCount > LIFETIME) {
         this.discard();
         return;
      }
      this.trail();
      if (!this.level().isClientSide && this.tickCount % 4 == 0) {
         this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
            SoundEvents.FIRE_AMBIENT, SoundSource.PLAYERS, 1.6F, 0.7F);
      }
   }

   private void trail() {
      Vec3 motion = this.getDeltaMovement();
      for (int i = 0; i < 6; i++) {
         double back = this.random.nextDouble();
         double ax = (this.random.nextDouble() - 0.5D) * 0.5D;
         double ay = (this.random.nextDouble() - 0.5D) * 0.5D;
         double az = (this.random.nextDouble() - 0.5D) * 0.5D;
         this.level().addParticle(ParticleTypes.FLAME,
            this.getX() - motion.x * back + ax,
            this.getY() - motion.y * back + ay,
            this.getZ() - motion.z * back + az,
            -motion.x * 0.05D, -motion.y * 0.05D, -motion.z * 0.05D);
      }
      for (int i = 0; i < 3; i++) {
         double back = this.random.nextDouble() * 1.5D;
         this.level().addParticle(ParticleTypes.LARGE_SMOKE,
            this.getX() - motion.x * back,
            this.getY() - motion.y * back,
            this.getZ() - motion.z * back,
            0.0D, 0.01D, 0.0D);
      }
      this.level().addParticle(ParticleTypes.LAVA, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
   }

   @Override
   public void performSpell(HitResult result) {
      Entity struck = result instanceof EntityHitResult ehr ? ehr.getEntity() : null;
      this.level().explode(this, this.getX(), this.getY(), this.getZ(), BLAST_POWER, true, Level.ExplosionInteraction.TNT);

      Entity owner = this.getOwner();
      AABB blast = new AABB(this.getX() - BLAST_RANGE, this.getY() - BLAST_RANGE, this.getZ() - BLAST_RANGE,
         this.getX() + BLAST_RANGE, this.getY() + BLAST_RANGE, this.getZ() + BLAST_RANGE);
      for (LivingEntity hit : this.level().getEntitiesOfClass(LivingEntity.class, blast)) {
         if (hit == owner) continue;
         if (hit != struck && hit.distanceToSqr(this.getX(), this.getY(), this.getZ()) > BLAST_RANGE * BLAST_RANGE) continue;
         hit.hurt(this.level().damageSources().explosion(this, owner), DAMAGE);
         Vec3 push = new Vec3(hit.getX() - this.getX(), hit.getY() + hit.getBbHeight() * 0.5D - this.getY(), hit.getZ() - this.getZ());
         if (push.lengthSqr() > 1.0E-4D) {
            hit.push(push.normalize().x * 0.6D, push.normalize().y * 0.4D + 0.2D, push.normalize().z * 0.6D);
         }
      }
   }
}
