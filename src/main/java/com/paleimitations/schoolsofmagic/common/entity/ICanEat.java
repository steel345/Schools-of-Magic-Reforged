package com.paleimitations.schoolsofmagic.common.entity;

import net.minecraft.world.entity.LivingEntity;

public interface ICanEat {
   boolean shouldHeal();

   LivingEntity getBase();
}
