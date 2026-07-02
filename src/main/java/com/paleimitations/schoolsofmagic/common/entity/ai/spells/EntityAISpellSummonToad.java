package com.paleimitations.schoolsofmagic.common.entity.ai.spells;

import com.paleimitations.schoolsofmagic.common.entity.EntityToad;
import com.paleimitations.schoolsofmagic.common.entity.EntityMagician;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.summoned.CapabilitySummoned;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.summoned.ISummoned;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class EntityAISpellSummonToad extends EntityAIUseSpell {
   public EntityAISpellSummonToad(EntityMagician magician) {
      super(magician);
   }

   @Override
   public boolean canUse() {
      return super.canUse() && this.magician.getRandom().nextInt(20) == 0;
   }

   @Override
   protected int getCastingTime() {
      return 40 + (60 - 4 * this.magician.getLevel());
   }

   @Override
   protected int getCastingInterval() {
      return 100 + (200 - 40 * this.magician.getLevel());
   }

   @Override
   protected void castSpell() {
      BlockPos blockpos = this.magician.blockPosition()
         .offset(-2 + this.magician.getRandom().nextInt(5), 1, -2 + this.magician.getRandom().nextInt(5));

      while (!this.magician.level().isEmptyBlock(blockpos)) {
         blockpos = this.magician.blockPosition().offset(-2 + this.magician.getRandom().nextInt(5), 1, -2 + this.magician.getRandom().nextInt(5));
      }

      EntityToad entity = EntityRegistry.TOAD.get().create(this.magician.level());
      entity.setToadType(7);
      entity.setPos((double)blockpos.getX() + 0.5, (double)blockpos.getY(), (double)blockpos.getZ() + 0.5);
      entity.setTarget(this.magician.getTarget());
      if (!this.magician.level().isClientSide) {
         this.magician.level().addFreshEntity(entity);
      }

      ISummoned summoned = entity.getCapability(CapabilitySummoned.CAP).orElse(null);
      if (summoned != null) {
         summoned.setSummoned(entity, true);
         summoned.setDespawnCountdown(entity, 200 + this.magician.getRandom().nextInt(200));
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
