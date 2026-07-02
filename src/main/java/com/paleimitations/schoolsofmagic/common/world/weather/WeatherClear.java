package com.paleimitations.schoolsofmagic.common.world.weather;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

public class WeatherClear extends WeatherBase {
   public WeatherClear(Level world, int duration, int countdown, boolean isNatural) {
      super("clear", world, duration, countdown, isNatural);
   }

   public WeatherClear(CompoundTag nbt) {
      super(nbt);
   }
}
