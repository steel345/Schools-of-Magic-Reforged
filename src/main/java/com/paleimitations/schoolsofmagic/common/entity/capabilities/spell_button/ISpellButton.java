package com.paleimitations.schoolsofmagic.common.entity.capabilities.spell_button;

import net.minecraft.nbt.CompoundTag;

public interface ISpellButton {
   boolean isPressed();

   void setPressed(boolean var1);

   CompoundTag serializeNBT();

   void deserializeNBT(CompoundTag var1);
}
