package com.paleimitations.schoolsofmagic.common.entity.ai.spells;

import com.paleimitations.schoolsofmagic.common.entity.EntityMagician;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityCloud;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class EntityAISpellPerfumeCloud extends EntityAIUseSpell {
   public EntityAISpellPerfumeCloud(EntityMagician magician) {
      super(magician);
   }

   @Override
   public boolean canUse() {
      if (!super.canUse()) {
         return false;
      }
      return !this.magician.getTarget().hasEffect(MobEffects.CONFUSION) && this.magician.getRandom().nextInt(20) == 0;
   }

   @Override
   protected int getCastingTime() {
      return 60 + (100 - 20 * this.magician.getLevel());
   }

   @Override
   protected int getCastingInterval() {
      return 200 + (200 - 40 * this.magician.getLevel());
   }

   @Override
   protected void castSpell() {
      double inaccuracy = 3.5;
      double xInaccuracy = this.magician.getRandom().nextDouble() * 2.0 * inaccuracy - inaccuracy;
      double yInaccuracy = this.magician.getRandom().nextDouble() * 1.0 - 0.5;
      double zInaccuracy = this.magician.getRandom().nextDouble() * 2.0 * inaccuracy - inaccuracy;
      EntityCloud cloud = new EntityCloud(this.magician.level(), this.magician.getTarget().getX() + xInaccuracy, this.magician.getTarget().getY() + yInaccuracy, this.magician.getTarget().getZ() + zInaccuracy);
      cloud.setOwner(this.magician);
      cloud.setRadius(3.0F + (float)this.magician.getLevel());
      cloud.setDuration(600 + this.magician.getLevel() * 20);
      cloud.setRadiusOnUse(-0.5F);
      cloud.setWaitTime(10);
      cloud.setRadiusPerTick(-cloud.getRadius() / (float)cloud.getDuration());
      cloud.setColor(MobEffects.CONFUSION.getColor());
      cloud.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100 + this.magician.getLevel() * 20));
      if (!this.magician.level().isClientSide) {
         this.magician.level().addFreshEntity(cloud);
      }
   }

   @Nullable
   @Override
   protected SoundEvent getSpellPrepareSound() {
      return SoundEvents.ILLUSIONER_PREPARE_MIRROR;
   }

   @Override
   protected EntityMagician.EnumSpellType getSpellType() {
      return EntityMagician.EnumSpellType.STENCH;
   }
}
