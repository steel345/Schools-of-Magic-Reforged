package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.paleimitations.schoolsofmagic.common.entity.EntityDryad;
import com.paleimitations.schoolsofmagic.common.entity.EntityNobleTree;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.level.Level;

public class EntityWisp extends ShulkerBullet {
   public EntityWisp(EntityType<? extends EntityWisp> type, Level worldIn) {
      super(type, worldIn);
   }

   public EntityWisp(Level worldIn, LivingEntity ownerIn, Entity targetIn, Direction.Axis axisIn) {
      super(worldIn, ownerIn, targetIn, axisIn);
   }

   @Override
   public void tick() {
      ProjectileUtil.rotateTowardsMovement(this, 0.5F);
      super.tick();
   }

   @Override
   public void push(Entity entityIn) {
      if (!(entityIn instanceof EntityNobleTree) && !(entityIn instanceof EntityDryad)) {
         super.push(entityIn);
      }
   }
}
