package com.paleimitations.schoolsofmagic.common.entity;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GrimoireItemEntity extends ItemEntity {

   public GrimoireItemEntity(Level level, double x, double y, double z, ItemStack stack) {
      super(level, x, y, z, stack);
   }

   @Override
   public boolean fireImmune() {
      return true;
   }
}
