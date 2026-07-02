package com.paleimitations.schoolsofmagic.common.entity.ai.spells;

import com.paleimitations.schoolsofmagic.common.entity.EntityMagician;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityPotionShot;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class EntityAISpellFlamibilityShot extends EntityAIUseSpell {
   public EntityAISpellFlamibilityShot(EntityMagician magician) {
      super(magician);
   }

   @Override
   public boolean canUse() {
      return super.canUse();
   }

   @Override
   protected int getCastingTime() {
      return 40 + (100 - 20 * this.magician.getLevel());
   }

   @Override
   protected int getCastingInterval() {
      return 100 + (300 - 60 * this.magician.getLevel());
   }

   @Override
   protected void castSpell() {
      LivingEntity base = this.magician.getTarget();
      BlockPos pos = base.blockPosition();
      EntityPotionShot entityspell = new EntityPotionShot(this.magician.level(), this.magician);
      entityspell.addEffect(new MobEffectInstance(PotionRegistry.flamability.get(), 200));
      entityspell.setPos(this.magician.getX(), this.magician.getY() + (double)(this.magician.getBbHeight() / 2.0F), this.magician.getZ());
      double d0 = base.getX() - this.magician.getX();
      double d1 = base.getY() + (double)(base.getBbHeight() / 2.0F) - this.magician.getY();
      double d2 = base.getZ() - this.magician.getZ();
      entityspell.shoot(d0, d1, d2, 0.75F + 0.15F * (float)this.magician.getLevel(), 14 - this.magician.level().getDifficulty().getId() * 4);
      this.magician.playSound(SoundEvents.SPLASH_POTION_THROW, 1.0F, 1.0F / (this.magician.getRandom().nextFloat() * 0.4F + 0.8F));
      this.magician.level().addFreshEntity(entityspell);
   }

   @Override
   protected SoundEvent getSpellPrepareSound() {
      return SoundEvents.EVOKER_PREPARE_ATTACK;
   }

   @Override
   protected EntityMagician.EnumSpellType getSpellType() {
      return EntityMagician.EnumSpellType.HALLUCINATION;
   }
}
