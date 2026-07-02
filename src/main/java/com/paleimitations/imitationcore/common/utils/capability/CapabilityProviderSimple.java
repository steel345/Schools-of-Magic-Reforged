package com.paleimitations.imitationcore.common.utils.capability;

import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class CapabilityProviderSimple<HANDLER> implements ICapabilityProvider {
   protected final Capability<HANDLER> capability;
   protected final Direction facing;
   protected final HANDLER instance;
   protected LazyOptional<HANDLER> lazyInstance;

   public CapabilityProviderSimple(HANDLER instance, Capability<HANDLER> capability, @Nullable Direction facing) {
      this.instance = instance;
      this.capability = capability;
      this.facing = facing;
      this.lazyInstance = LazyOptional.of(() -> this.instance);
   }

   @NotNull
   @Override
   public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
      return capability == this.getCapability() ? this.lazyInstance.cast() : LazyOptional.empty();
   }

   public final Capability<HANDLER> getCapability() {
      return this.capability;
   }

   @Nullable
   public Direction getFacing() {
      return this.facing;
   }

   public final HANDLER getInstance() {
      return this.instance;
   }

   public void invalidate() {
      if (this.lazyInstance != null) {
         this.lazyInstance.invalidate();
      }
   }
}
