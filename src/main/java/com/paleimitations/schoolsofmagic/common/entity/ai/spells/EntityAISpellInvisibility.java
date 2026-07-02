package com.paleimitations.schoolsofmagic.common.entity.ai.spells;

import com.paleimitations.schoolsofmagic.common.entity.EntityMagician;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class EntityAISpellInvisibility extends EntityAIUseSpell {
   public EntityAISpellInvisibility(EntityMagician magician) {
      super(magician);
   }

   @Override
   public boolean canUse() {
      if (!super.canUse()) {
         return false;
      }
      return !this.magician.hasEffect(MobEffects.INVISIBILITY) && this.magician.getLevel() > 1 && this.magician.getRandom().nextInt(40) == 0;
   }

   @Override
   protected int getCastingTime() {
      return 40 + (100 - 20 * this.magician.getLevel());
   }

   @Override
   protected int getCastingInterval() {
      return 1000 + (600 - 120 * this.magician.getLevel());
   }

   @Override
   protected void castSpell() {
      this.magician.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 400 + this.magician.getLevel() * 100));
   }

   @Nullable
   @Override
   protected SoundEvent getSpellPrepareSound() {
      return SoundEvents.ILLUSIONER_PREPARE_MIRROR;
   }

   @Override
   protected EntityMagician.EnumSpellType getSpellType() {
      return EntityMagician.EnumSpellType.THORNS;
   }
}
