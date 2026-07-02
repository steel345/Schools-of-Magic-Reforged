package com.paleimitations.schoolsofmagic.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.Level;

public class EntitySphinx extends Chicken {
   public EntitySphinx(EntityType<? extends Chicken> type, Level level) {
      super(type, level);
   }
}
