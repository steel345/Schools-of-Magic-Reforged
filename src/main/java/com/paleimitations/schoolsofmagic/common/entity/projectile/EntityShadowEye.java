package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityShadowEye extends AbstractMagicCircle {
   public EntityShadowEye(EntityType<? extends EntityShadowEye> type, Level worldIn) {
      super(type, worldIn);
   }

   public EntityShadowEye(Level worldIn) {
      super(EntityRegistry.SHADOW_EYE.get(), worldIn);
   }

   @Override
   public void performSpell() {
   }
}
