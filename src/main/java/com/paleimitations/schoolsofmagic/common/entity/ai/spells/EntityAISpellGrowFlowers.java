package com.paleimitations.schoolsofmagic.common.entity.ai.spells;

import com.paleimitations.schoolsofmagic.common.entity.EntityMagician;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class EntityAISpellGrowFlowers extends EntityAIUseSpell {
   public EntityAISpellGrowFlowers(EntityMagician magician) {
      super(magician);
   }

   @Override
   public boolean canUse() {
      if (!super.canUse()) {
         return false;
      }
      BlockState state = this.magician.level().getBlockState(this.magician.getTarget().blockPosition().below());
      return (state.getBlock() == Blocks.GRASS_BLOCK || state.getBlock() == Blocks.DIRT) && this.magician.getRandom().nextInt(20) == 0;
   }

   @Override
   protected int getCastingTime() {
      return 20;
   }

   @Override
   protected int getCastingInterval() {
      return 100 + (300 - 60 * this.magician.getLevel());
   }

   @Override
   protected void castSpell() {

   }

   @Override
   protected SoundEvent getSpellPrepareSound() {
      return SoundEvents.EVOKER_PREPARE_SUMMON;
   }

   @Override
   protected EntityMagician.EnumSpellType getSpellType() {
      return EntityMagician.EnumSpellType.GROW_FLOWERS;
   }
}
