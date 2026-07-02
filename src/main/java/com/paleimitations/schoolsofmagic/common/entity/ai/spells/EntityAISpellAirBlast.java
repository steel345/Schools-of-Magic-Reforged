package com.paleimitations.schoolsofmagic.common.entity.ai.spells;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.particles.SOMParticleType;
import com.paleimitations.schoolsofmagic.common.entity.EntityMagician;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;

public class EntityAISpellAirBlast extends EntityAIUseSpell {
   public EntityAISpellAirBlast(EntityMagician magician) {
      super(magician);
   }

   @Override
   public boolean canUse() {
      Entity entity;
      if (!super.canUse()) {
         return false;
      }
      Vec3 vec3d1 = new Vec3(this.magician.getX(), this.magician.getY() + (double)(this.magician.getBbHeight() * 0.66F), this.magician.getZ());
      Vec3 vec3d = new Vec3(this.magician.getTarget().getX(), this.magician.getTarget().getY() + (double)(this.magician.getTarget().getBbHeight() * 0.66F), this.magician.getTarget().getZ());
      HitResult raytraceresult = this.magician.level().clip(new ClipContext(vec3d1, vec3d, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.magician));
      if (raytraceresult != null) {
         vec3d = new Vec3(raytraceresult.getLocation().x, raytraceresult.getLocation().y, raytraceresult.getLocation().z);
      }
      return (entity = this.findEntityOnPath(vec3d1, vec3d)) != null && entity == this.magician.getTarget() && this.magician.getRandom().nextInt(20) == 0;
   }

   @Override
   protected int getCastingTime() {
      return 10;
   }

   @Override
   protected int getCastingInterval() {
      return 100 + (150 - 30 * this.magician.getLevel());
   }

   @Nullable
   protected Entity findEntityOnPath(Vec3 start, Vec3 end) {
      Entity entity = null;
      List<Entity> list = this.magician.level().getEntitiesOfClass(Entity.class, this.magician.getBoundingBox().expandTowards(10.0, 10.0, 10.0).inflate(1.0));
      double d0 = 0.0;
      for (int i = 0; i < list.size(); ++i) {
         double d1;
         AABB axisalignedbb;
         Entity entity1 = list.get(i);
         if (entity1 == this.magician) continue;
         axisalignedbb = entity1.getBoundingBox().inflate(0.3D);
         java.util.Optional<Vec3> raytraceresult = axisalignedbb.clip(start, end);
         if (raytraceresult.isEmpty() || !((d1 = start.distanceToSqr(raytraceresult.get())) < d0) && d0 != 0.0) continue;
         entity = entity1;
         d0 = d1;
      }
      return entity;
   }

   @Override
   protected void castSpell() {
      Entity entity;
      Vec3 vec3d1 = new Vec3(this.magician.getX(), this.magician.getY() + (double)(this.magician.getBbHeight() * 0.66F), this.magician.getZ());
      Vec3 vec3d = new Vec3(this.magician.getTarget().getX(), this.magician.getTarget().getY() + (double)(this.magician.getTarget().getBbHeight() * 0.66F), this.magician.getTarget().getZ());
      HitResult raytraceresult = this.magician.level().clip(new ClipContext(vec3d1, vec3d, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.magician));
      if (raytraceresult != null) {
         vec3d = new Vec3(raytraceresult.getLocation().x, raytraceresult.getLocation().y, raytraceresult.getLocation().z);
      }
      if ((entity = this.findEntityOnPath(vec3d1, vec3d)) != null && entity == this.magician.getTarget()) {
         this.knockBack(this.magician.getTarget(), this.magician, 2.0F + 0.5F * (float)this.magician.getLevel(), this.magician.getX() - this.magician.getTarget().getX(), this.magician.getZ() - this.magician.getTarget().getZ());
      }
      for (int i = 0; i < 30; ++i) {
         this.shoot(this.magician.getTarget().getX() - this.magician.getX(), this.magician.getTarget().getY() + (double)(this.magician.getTarget().getBbHeight() * 0.66F) - (this.magician.getY() + (double)(this.magician.getBbHeight() * 0.66F)), this.magician.getTarget().getZ() - this.magician.getZ(), 1.0F, 14.0F, this.magician.getRandom());
      }
   }

   public void knockBack(LivingEntity target, Entity attacker, float strength, double xRatio, double zRatio) {
      LivingKnockBackEvent event = ForgeHooks.onLivingKnockBack(target, strength, xRatio, zRatio);
      if (event.isCanceled()) {
         return;
      }
      strength = event.getStrength();
      xRatio = event.getRatioX();
      zRatio = event.getRatioZ();
      if (target.getRandom().nextDouble() * 1.25 >= target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue()) {
         target.hasImpulse = true;
         float f = Mth.sqrt((float)(xRatio * xRatio + zRatio * zRatio));
         Vec3 dm = target.getDeltaMovement();
         double mx = dm.x / 2.0 - xRatio / (double)f * (double)strength;
         double my = dm.y;
         double mz = dm.z / 2.0 - zRatio / (double)f * (double)strength;
         if (target.onGround()) {
            my = my / 2.0 + (double)strength;
            if (my > 0.4) {
               my = 0.4;
            }
         }
         target.setDeltaMovement(mx, my, mz);
         target.hurtMarked = true;
      }
   }

   public void shoot(double x, double y, double z, float velocity, float inaccuracy, RandomSource rand) {
      float f = Mth.sqrt((float)(x * x + y * y + z * z));
      x /= (double)f;
      y /= (double)f;
      z /= (double)f;
      x += rand.nextGaussian() * (double)0.0075F * (double)inaccuracy;
      y += rand.nextGaussian() * (double)0.0075F * (double)inaccuracy;
      z += rand.nextGaussian() * (double)0.0075F * (double)inaccuracy;
      if (this.magician.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
         serverLevel.sendParticles(com.paleimitations.schoolsofmagic.common.registries.ParticleTypeRegistry.FLOWER.get(),
            this.magician.getX(), this.magician.getY() + (double)(this.magician.getBbHeight() * 0.66F), this.magician.getZ(),
            0, x * (double) velocity, y * (double) velocity, z * (double) velocity, 1.0);
      }
   }

   @Override
   protected SoundEvent getSpellPrepareSound() {
      return SoundEvents.EVOKER_PREPARE_ATTACK;
   }

   @Override
   protected EntityMagician.EnumSpellType getSpellType() {
      return EntityMagician.EnumSpellType.SHOOT_CACTUS;
   }
}
