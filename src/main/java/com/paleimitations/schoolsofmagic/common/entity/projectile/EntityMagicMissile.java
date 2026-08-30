package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityMagicMissile extends AbstractSpellShot {
   public static final float MAGIC_DAMAGE = 3.5F;
   public static final float IMPACT_DAMAGE = 3.5F;
   public static final float DAY_MAGIC_DAMAGE = 1.75F;
   public static final float DAY_IMPACT_DAMAGE = 1.75F;
   private static final int LIFETIME = 120;

   public EntityMagicMissile(EntityType<? extends EntityMagicMissile> type, Level level) {
      super(type, level);
   }

   public EntityMagicMissile(Level level, LivingEntity thrower) {
      super(EntityRegistry.MAGIC_MISSILE.get(), level, thrower);
   }

   @Override
   public int getColor() {
      return 0xFFFFFF;
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
   }

   private void trail() {
      Vec3 motion = this.getDeltaMovement();
      for (int i = 0; i < 3; i++) {
         double back = this.random.nextDouble();
         double ax = (this.random.nextDouble() - 0.5D) * 0.3D;
         double ay = (this.random.nextDouble() - 0.5D) * 0.3D;
         double az = (this.random.nextDouble() - 0.5D) * 0.3D;
         this.level().addParticle(ParticleTypes.END_ROD,
            this.getX() - motion.x * back + ax,
            this.getY() - motion.y * back + ay,
            this.getZ() - motion.z * back + az,
            -motion.x * 0.02D, -motion.y * 0.02D, -motion.z * 0.02D);
      }
   }

   public boolean atNight() {
      Level level = this.level();
      if (level.dimensionType().hasFixedTime()) return false;
      long time = level.getDayTime() % 24000L;
      return time >= 13000L && time < 23000L;
   }

   @Override
   public void performSpell(HitResult result) {
      Entity owner = this.getOwner();
      if (result instanceof EntityHitResult ehr && ehr.getEntity() instanceof LivingEntity hit && hit != owner) {
         boolean night = this.atNight();
         float magic = night ? MAGIC_DAMAGE : DAY_MAGIC_DAMAGE;
         float impact = night ? IMPACT_DAMAGE : DAY_IMPACT_DAMAGE;
         hit.hurt(this.level().damageSources().indirectMagic(this, owner), magic);
         hit.invulnerableTime = 0;
         hit.hurt(this.level().damageSources().thrown(this, owner), impact);
      }
      this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
         SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.PLAYERS, 0.8F, 1.6F);
      if (this.level() instanceof net.minecraft.server.level.ServerLevel server) {
         server.sendParticles(ParticleTypes.END_ROD, this.getX(), this.getY(), this.getZ(), 24, 0.2D, 0.2D, 0.2D, 0.12D);
         server.sendParticles(ParticleTypes.FIREWORK, this.getX(), this.getY(), this.getZ(), 12, 0.1D, 0.1D, 0.1D, 0.05D);
      }
   }
}
