package com.paleimitations.schoolsofmagic.common.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public abstract class EntityBlockProjectile extends ThrowableProjectile {

   protected EntityBlockProjectile(EntityType<? extends EntityBlockProjectile> type, Level level) {
      super(type, level);
   }

   protected EntityBlockProjectile(EntityType<? extends EntityBlockProjectile> type, LivingEntity thrower, Level level) {
      super(type, thrower, level);
   }

   @Override
   protected void defineSynchedData() {
   }

   public abstract BlockState getRenderBlock();

   public float getRenderScale() {
      return 1.0F;
   }
}
