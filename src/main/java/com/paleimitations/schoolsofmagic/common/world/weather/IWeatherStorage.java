package com.paleimitations.schoolsofmagic.common.world.weather;

import java.util.List;

public interface IWeatherStorage {
   IWeatherEffect getGlobalWeatherEffect();

   void setGlobalWeatherEffect(IWeatherEffect var1);

   void createGlobalWeatherEffect(IWeatherEffect var1);

   int getGlobalDuration();

   int getClearTick();

   void setClearTick(int var1);

   void addLocalWeatherEffect(IWeatherEffect var1);

   void setLocalWeatherEffects(List<IWeatherEffect> var1);

   List<IWeatherEffect> getLocalWeatherEffects();
}
