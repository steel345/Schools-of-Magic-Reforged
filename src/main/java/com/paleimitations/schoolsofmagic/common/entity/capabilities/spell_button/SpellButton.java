package com.paleimitations.schoolsofmagic.common.entity.capabilities.spell_button;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class SpellButton implements ISpellButton, INBTSerializable<CompoundTag> {
   private boolean isPressed = false;

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      nbt.putBoolean("isPressed", this.isPressed);
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      this.isPressed = nbt.getBoolean("isPressed");
   }

   @Override
   public boolean isPressed() {
      return this.isPressed;
   }

   @Override
   public void setPressed(boolean pressed) {
      this.isPressed = pressed;
   }
}
