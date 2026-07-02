package com.paleimitations.schoolsofmagic.common.entity.capabilities.effect_data;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IEffectVariables {

   void startSneeze(float var1, float var2, float var3);

   float getSneezeOffset();

   void setSneezeOffset(float var1);

   float getTimeToSneeze();

   void setTimeToSneeze(float var1);

   float getReturnFromSneeze();

   void setReturnFromSneeze(float var1);
}
