package com.paleimitations.schoolsofmagic.common.entity.ai.spells;

import com.paleimitations.schoolsofmagic.common.entity.EntityDryad;
import com.paleimitations.schoolsofmagic.common.entity.EntityMagician;
import com.paleimitations.schoolsofmagic.common.entity.EntityNobleTree;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityTreeCore;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class EntityAISpellHealDryad extends EntityAIUseSpell {
   private EntityDryad dryadTarget;

   public EntityAISpellHealDryad(EntityMagician magician) {
      super(magician);
   }

   @Override
   public boolean canUse() {
      if (!super.canUse()) {
         return false;
      } else {
         if (this.dryadTarget == null) {
            if (this.magician instanceof EntityNobleTree) {
               EntityNobleTree tree = (EntityNobleTree)this.magician;
               if (tree.level().getBlockEntity(tree.getBlock()) instanceof TileEntityTreeCore) {
                  TileEntityTreeCore treecore = (TileEntityTreeCore)tree.level().getBlockEntity(tree.getBlock());
                  this.dryadTarget = treecore.getDryad();
               }
            }

            if (this.dryadTarget == null) {
               for (EntityDryad dryad : this.magician.level().getEntitiesOfClass(EntityDryad.class, this.magician.getBoundingBox().inflate(30.0))) {
                  this.dryadTarget = dryad;
               }
            }
         }

         return this.magician.getRandom().nextInt(20) == 0 && this.dryadTarget != null;
      }
   }

   @Override
   protected int getCastingTime() {
      return 20;
   }

   @Override
   protected int getCastingInterval() {
      return 1200;
   }

   @Override
   protected void castSpell() {
      this.magician.addEffect(new MobEffectInstance(MobEffects.HEAL));
   }

   @Nullable
   @Override
   protected SoundEvent getSpellPrepareSound() {
      return null;
   }

   @Override
   protected EntityMagician.EnumSpellType getSpellType() {
      return EntityMagician.EnumSpellType.GROW_FLOWERS;
   }
}
