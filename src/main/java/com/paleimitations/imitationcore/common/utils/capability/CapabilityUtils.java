package com.paleimitations.imitationcore.common.utils.capability;

import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class CapabilityUtils {
   public CapabilityUtils() {
   }

   @Nullable
   public static <T> T getCapability(@Nullable ICapabilityProvider provider, Capability<T> capability, @Nullable Direction facing) {
      return provider == null ? null : provider.getCapability(capability, facing).orElse(null);
   }
}
