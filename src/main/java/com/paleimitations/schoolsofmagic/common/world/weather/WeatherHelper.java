package com.paleimitations.schoolsofmagic.common.world.weather;

import net.minecraft.nbt.CompoundTag;

public class WeatherHelper {
   public WeatherHelper() {
   }

   public static IWeatherEffect getWeatherEffectFromNBT(CompoundTag nbt) {
      if (nbt.contains("name")) {
         String var1 = nbt.getString("name").toLowerCase();
         switch (var1) {
            case "soul_fog":
               return new WeatherSoulFog(nbt);
            case "wind":
               return new WeatherWind(nbt);
            case "fog":
               return new WeatherFog(nbt);
            case "clear":
               return new WeatherClear(nbt);
         }
      }

      return null;
   }
}
