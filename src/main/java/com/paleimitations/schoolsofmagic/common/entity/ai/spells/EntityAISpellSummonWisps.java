package com.paleimitations.schoolsofmagic.common.entity.ai.spells;

import com.paleimitations.schoolsofmagic.common.entity.EntityMagician;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityWisp;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class EntityAISpellSummonWisps extends EntityAIUseSpell {
   public EntityAISpellSummonWisps(EntityMagician magician) {
      super(magician);
   }

   @Override
   public boolean canUse() {
      if (!super.canUse()) {
         return false;
      } else {
         int i = this.magician.level().getEntitiesOfClass(EntityWisp.class, this.magician.getBoundingBox().inflate(16.0)).size();
         return this.magician.getRandom().nextInt(8) + 1 > i && this.magician.getRandom().nextInt(20) == 0;
      }
   }

   @Override
   protected int getCastingTime() {
      return 20;
   }

   @Override
   protected int getCastingInterval() {
      return 200 + (200 - 40 * this.magician.getLevel());
   }

   @Override
   protected void castSpell() {
      for (int i = 0; i < 3; i++) {
         EntityWisp entityvex = new EntityWisp(this.magician.level(), this.magician, this.magician.getTarget(), Direction.Axis.X);
         this.magician.level().addFreshEntity(entityvex);
      }
   }

   @Override
   protected SoundEvent getSpellPrepareSound() {
      return SoundEvents.EVOKER_PREPARE_SUMMON;
   }

   @Override
   protected EntityMagician.EnumSpellType getSpellType() {
      return EntityMagician.EnumSpellType.SUMMON_MINIONS;
   }
}
