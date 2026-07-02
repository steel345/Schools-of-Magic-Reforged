package com.paleimitations.schoolsofmagic.common.entity.ai.spells;

import com.paleimitations.schoolsofmagic.common.entity.EntityMagician;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;

public class EntityAISpellThorns extends EntityAIUseSpell {
   public EntityAISpellThorns(EntityMagician magician) {
      super(magician);
   }

   @Override
   public boolean canUse() {
      if (!super.canUse()) {
         return false;
      }
      return !this.magician.hasEffect(PotionRegistry.spined.get()) && this.magician.getRandom().nextInt(20) == 0;
   }

   @Override
   protected int getCastingTime() {
      return 20;
   }

   @Override
   protected int getCastingInterval() {
      return 1000 + (600 - 120 * this.magician.getLevel());
   }

   @Override
   protected void castSpell() {
      this.magician.addEffect(new MobEffectInstance(PotionRegistry.spined.get(), 1000 + this.magician.getLevel() * 100));
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
