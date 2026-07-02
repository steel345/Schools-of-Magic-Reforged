package com.paleimitations.schoolsofmagic.common.entity.ai;

import com.paleimitations.schoolsofmagic.common.entity.EntityDryad;
import com.paleimitations.schoolsofmagic.common.entity.EntityMagician;
import com.paleimitations.schoolsofmagic.common.entity.EntityNobleTree;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.phys.AABB;

public class EntityAITargetNCallAllies extends HurtByTargetGoal {
   public EntityAITargetNCallAllies(EntityMagician magician) {
      super((PathfinderMob)magician);
      this.setAlertOthers();
   }

   @Override
   protected void alertOthers() {
      double d0 = this.getFollowDistance();
      for (PathfinderMob entitycreature : this.mob.level().getEntitiesOfClass(EntityMagician.class, new AABB(this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.mob.getX() + 1.0, this.mob.getY() + 1.0, this.mob.getZ() + 1.0).inflate(d0, 10.0, d0))) {
         if (this.mob == entitycreature || entitycreature.getTarget() != null || this.mob instanceof TamableAnimal && ((TamableAnimal)this.mob).getOwner() != ((TamableAnimal)entitycreature).getOwner() || entitycreature.isAlliedTo(this.mob.getLastHurtByMob()) || !(entitycreature instanceof EntityDryad) && !(entitycreature instanceof EntityNobleTree)) continue;
         this.alertOther(entitycreature, this.mob.getLastHurtByMob());
      }
   }
}
