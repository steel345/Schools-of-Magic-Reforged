package com.paleimitations.schoolsofmagic.common.entity.ai.spells;

import com.paleimitations.schoolsofmagic.common.entity.EntityMagician;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class EntityAISpellLevitate extends EntityAIUseSpell {
   public EntityAISpellLevitate(EntityMagician magician) {
      super(magician);
   }

   @Override
   public boolean canUse() {
      return super.canUse() && this.magician.getRandom().nextInt(20) == 0;
   }

   @Override
   protected int getCastingTime() {
      return 120;
   }

   @Override
   protected int getCastingInterval() {
      return 500;
   }

   @Override
   public void tick() {
      super.tick();
      LivingEntity target = this.magician.getTarget();
      target.setPos(target.xo, target.getY(), target.zo);
      Vec3 dm = target.getDeltaMovement();
      target.setDeltaMovement(dm.x, dm.y + 0.082, dm.z);
      target.setOnGround(true);
      target.level().addParticle(ParticleTypes.SMOKE, target.getX(), target.getY(), target.getZ(), 0.0, 0.0, 0.0);
      target.hurtMarked = true;
   }

   @Nullable
   @Override
   protected SoundEvent getSpellPrepareSound() {
      return SoundEvents.ILLUSIONER_PREPARE_MIRROR;
   }

   @Override
   protected EntityMagician.EnumSpellType getSpellType() {
      return EntityMagician.EnumSpellType.BLINDING;
   }

   @Override
   protected void castSpell() {
   }
}
