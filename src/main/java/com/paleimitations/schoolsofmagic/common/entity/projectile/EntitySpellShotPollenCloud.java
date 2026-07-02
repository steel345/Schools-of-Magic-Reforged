package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.particles.SOMParticleType;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class EntitySpellShotPollenCloud extends AbstractSpellShot {
   public EntitySpellShotPollenCloud(EntityType<? extends EntitySpellShotPollenCloud> type, Level worldIn) {
      super(type, worldIn);
   }

   public EntitySpellShotPollenCloud(Level worldIn, LivingEntity throwerIn) {
      super(EntityRegistry.POLLEN_CLOUD_SPELL.get(), worldIn, throwerIn);
   }

   public EntitySpellShotPollenCloud(Level worldIn, double x, double y, double z) {
      super(EntityRegistry.POLLEN_CLOUD_SPELL.get(), worldIn, x, y, z);
   }

   @Override
   public int getColor() {
      return PotionRegistry.sneezing.get().getColor();
   }

   @Override
   public void tick() {
      if (this.level().isClientSide) {
         SchoolsOfMagic.proxy.spawnParticle(SOMParticleType.AIR, this.getX() - this.getDeltaMovement().x, this.getY() - this.getDeltaMovement().y + 0.15, this.getZ() - this.getDeltaMovement().z, 0.0, 0.0, 0.0, this.getColorColor().getRed(), this.getColorColor().getGreen(), this.getColorColor().getBlue(), this.getColorColor().getAlpha());
      }
      super.tick();
   }

   @Override
   public void performSpell(HitResult result) {
      if (result != null) {
         EntityCloud cloud = new EntityCloud(this.level(), result.getLocation().x, result.getLocation().y, result.getLocation().z);
         if (this.getOwner() instanceof LivingEntity) {
            cloud.setOwner((LivingEntity)this.getOwner());
         }
         cloud.setRadius(3.0F);
         cloud.setDuration(400);
         cloud.setRadiusOnUse(-0.5F);
         cloud.setWaitTime(10);
         cloud.setRadiusPerTick(-cloud.getRadius() / (float)cloud.getDuration());
         cloud.setColor(PotionRegistry.sneezing.get().getColor());
         cloud.addEffect(new MobEffectInstance(PotionRegistry.sneezing.get(), 120));
         if (!this.level().isClientSide) {
            this.level().addFreshEntity(cloud);
         }
      }
   }
}
