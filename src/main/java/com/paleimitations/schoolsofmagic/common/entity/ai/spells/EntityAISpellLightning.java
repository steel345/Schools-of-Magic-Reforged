package com.paleimitations.schoolsofmagic.common.entity.ai.spells;

import com.paleimitations.schoolsofmagic.common.entity.EntityDemon;
import com.paleimitations.schoolsofmagic.common.entity.EntityMagician;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraftforge.event.ForgeEventFactory;

public class EntityAISpellLightning extends EntityAIUseSpell {
   public EntityAISpellLightning(EntityMagician magician) {
      super(magician);
   }

   @Override
   public boolean canUse() {
      return super.canUse()
         && (this.magician.level().isThundering() || this.magician instanceof EntityDemon)
         && this.magician.getRandom().nextInt(20) == 0;
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
      LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(this.magician.level());
      bolt.moveTo(this.magician.getTarget().getX(), this.magician.getTarget().getY(), this.magician.getTarget().getZ());
      bolt.setVisualOnly(true);
      this.magician.level().addFreshEntity(bolt);
      LightningBolt bolt2 = EntityType.LIGHTNING_BOLT.create(this.magician.level());
      bolt2.moveTo(this.magician.getTarget().getX(), this.magician.getTarget().getY(), this.magician.getTarget().getZ());
      if (!ForgeEventFactory.onEntityStruckByLightning(this.magician.getTarget(), bolt2)) {
         LightningBolt bolt3 = EntityType.LIGHTNING_BOLT.create(this.magician.level());
         bolt3.moveTo(this.magician.getTarget().getX(), this.magician.getTarget().getY(), this.magician.getTarget().getZ());
         if (this.magician.level() instanceof ServerLevel) {
            this.magician.getTarget().thunderHit((ServerLevel)this.magician.level(), bolt3);
         }
      }
   }

   @Nullable
   @Override
   protected SoundEvent getSpellPrepareSound() {
      return SoundEvents.ILLUSIONER_PREPARE_MIRROR;
   }

   @Override
   protected EntityMagician.EnumSpellType getSpellType() {
      return EntityMagician.EnumSpellType.LIGHTNING;
   }
}
