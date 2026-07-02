package com.paleimitations.imitationcore.common.utils.capability;

import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

public class CapabilityProviderSerializable<HANDLER extends INBTSerializable<Tag>>
      extends CapabilityProviderSimple<HANDLER> implements INBTSerializable<Tag> {

   public CapabilityProviderSerializable(Capability<HANDLER> capability, @Nullable Direction facing, HANDLER instance) {
      super(instance, capability, facing);
   }

   @Override
   public Tag serializeNBT() {
      return this.getInstance().serializeNBT();
   }

   @Override
   public void deserializeNBT(Tag nbt) {
      this.getInstance().deserializeNBT(nbt);
   }
}
