package com.paleimitations.schoolsofmagic.common.entity.ai.spells;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.particles.SOMParticleType;
import com.paleimitations.schoolsofmagic.common.entity.EntityMagician;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

public class EntityAISpellSprayWater extends EntityAIUseSpell {
   public EntityAISpellSprayWater(EntityMagician magician) {
      super(magician);
   }

   @Override
   public boolean canUse() {
      if (this.magician.isCasting()) {
         return false;
      } else {
         if (this.magician.tickCount >= this.spellCooldown) {
            if (this.magician.isOnFire()) {
               return true;
            }

            Level world = this.magician.level();

            for (BlockPos pos : BlockPos.betweenClosed(
               this.magician.blockPosition().offset(10, 10, 10), this.magician.blockPosition().offset(-10, -4, -10)
            )) {
               if (world.getBlockState(pos).getBlock() == Blocks.FIRE) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   @Override
   public boolean canContinueToUse() {
      return this.spellWarmup > 0;
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
      BlockPos pos = BlockPos.ZERO;

      for (BlockPos posit : BlockPos.betweenClosed(
         this.magician.blockPosition().offset(10, 10, 10), this.magician.blockPosition().offset(-10, -4, -10)
      )) {
         if (this.magician.level().getBlockState(posit).getBlock() == Blocks.FIRE
            && Utils.getDistance(posit, this.magician.blockPosition()) < Utils.getDistance(pos, this.magician.blockPosition())) {
            pos = posit.immutable();
         }
      }

      for (int i = 0; i < 30; i++) {
         this.shoot(
            (double)pos.getX() + 0.5 - this.magician.getX(),
            (double)pos.getY() - (this.magician.getY() + (double)(this.magician.getBbHeight() / 2.0F)),
            (double)pos.getZ() + 0.5 - this.magician.getZ(),
            1.0F,
            14.0F,
            this.magician.getRandom()
         );
      }

      this.magician.playSound(SoundEvents.GENERIC_EXTINGUISH_FIRE, 1.0F, this.magician.getRandom().nextFloat() * 0.4F + 0.8F);

      for (int j = -1 - this.magician.getLevel(); j <= 1 + this.magician.getLevel(); j++) {
         for (int k = -1 - this.magician.getLevel(); k <= 1 + this.magician.getLevel(); k++) {
            for (int l = -1 - this.magician.getLevel(); l <= 1 + this.magician.getLevel(); l++) {
               if (this.magician.level().getBlockState(pos.offset(j, k, l)).getBlock() == Blocks.FIRE) {
                  this.magician.level().removeBlock(pos.offset(j, k, l), false);
                  this.magician
                     .level()
                     .playLocalSound(
                        (double)pos.offset(j, k, l).getX(),
                        (double)pos.offset(j, k, l).getY(),
                        (double)pos.offset(j, k, l).getZ(),
                        SoundEvents.FIRE_EXTINGUISH,
                        SoundSource.BLOCKS,
                        1.0F,
                        this.magician.getRandom().nextFloat() * 0.4F + 0.8F,
                        false
                     );
               }
            }
         }
      }

      for (Entity entity : this.magician.level().getEntitiesOfClass(Entity.class, new AABB(pos.offset(2, 2, 2), pos.offset(-2, -2, -2)))) {
         if (entity.isOnFire()) {
            entity.clearFire();
         }
      }

      if (this.magician.isOnFire()) {
         this.magician.clearFire();
      }
   }

   public void shoot(double x, double y, double z, float velocity, float inaccuracy, RandomSource rand) {
      float f = Mth.sqrt((float)(x * x + y * y + z * z));
      x /= (double)f;
      y /= (double)f;
      z /= (double)f;
      x += rand.nextGaussian() * 0.0075F * (double)inaccuracy;
      y += rand.nextGaussian() * 0.0075F * (double)inaccuracy;
      z += rand.nextGaussian() * 0.0075F * (double)inaccuracy;
      x *= (double)velocity;
      y *= (double)velocity;
      z *= (double)velocity;
      this.magician
         .level()
         .addParticle(
            ParticleTypes.SPIT,
            this.magician.getX(),
            this.magician.getY() + (double)(this.magician.getBbHeight() / 2.0F),
            this.magician.getZ(),
            x,
            y,
            z
         );
      SchoolsOfMagic.proxy
         .spawnParticle(
            SOMParticleType.WATER,
            this.magician.getX(),
            this.magician.getY() + (double)(this.magician.getBbHeight() / 2.0F),
            this.magician.getZ(),
            x,
            y,
            z
         );
   }

   @Override
   protected SoundEvent getSpellPrepareSound() {
      return SoundEvents.EVOKER_PREPARE_SUMMON;
   }

   @Override
   protected EntityMagician.EnumSpellType getSpellType() {
      return EntityMagician.EnumSpellType.SPLASH;
   }
}
