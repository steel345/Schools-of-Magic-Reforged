package com.paleimitations.schoolsofmagic.common.entity.ai.spells;

import com.paleimitations.schoolsofmagic.common.entity.EntityDryad;
import com.paleimitations.schoolsofmagic.common.entity.EntityMagician;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityJumpingCactus;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class EntityAISpellShootCactus extends EntityAIUseSpell {
   public EntityAISpellShootCactus(EntityMagician magician) {
      super(magician);
   }

   @Override
   public boolean canUse() {
      return super.canUse()
         && this.magician instanceof EntityDryad
         && (((EntityDryad)this.magician).getDryadType() == 5 || ((EntityDryad)this.magician).getDryadType() == 11)
         && this.magician.getRandom().nextInt(20) == 0;
   }

   @Override
   protected int getCastingTime() {
      return 20;
   }

   @Override
   protected int getCastingInterval() {
      return 100 + (150 - 30 * this.magician.getLevel());
   }

   @Override
   protected void castSpell() {
      for (int i = 0; i < this.magician.getRandom().nextInt(4) + 4; i++) {
         LivingEntity base = this.magician.getTarget();
         EntityJumpingCactus entityspell = new EntityJumpingCactus(this.magician.level(), this.magician);
         entityspell.setDamage(5.0);
         entityspell.setPos(this.magician.getX(), this.magician.getY() + (double)(this.magician.getBbHeight() / 2.0F), this.magician.getZ());
         double d0 = base.getX() - entityspell.getX();
         double d1 = base.getBoundingBox().minY + (double)(base.getBbHeight() / 3.0F) - entityspell.getY();
         double d2 = base.getZ() - entityspell.getZ();
         double d3 = (double)Mth.sqrt((float)(d0 * d0 + d2 * d2));
         float speed = 0.75F + 0.15F * (float)this.magician.getLevel();
         entityspell.shoot(d0, d1 + d3 * 0.21000000298023225, d2, speed, (float)(14 - this.magician.level().getDifficulty().getId() * 4));
         this.magician.playSound(SoundEvents.EVOKER_FANGS_ATTACK, 1.0F, 1.0F / (this.magician.getRandom().nextFloat() * 0.4F + 0.8F));
         this.magician.level().addFreshEntity(entityspell);
      }
   }

   @Override
   protected SoundEvent getSpellPrepareSound() {
      return SoundEvents.EVOKER_PREPARE_SUMMON;
   }

   @Override
   protected EntityMagician.EnumSpellType getSpellType() {
      return EntityMagician.EnumSpellType.SHOOT_CACTUS;
   }
}
