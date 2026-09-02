package com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface IGarmentData {
   int CROWN = 0;
   int CAPE = 1;
   int BELT = 2;
   int GRIMOIRE = 3;
   int SLOTS = 4;

   // which slots are told not to show what they hold. one bit each, ring 4 talisman 5 charm 6
   int SHOW_RING = 4;
   int SHOW_TALISMAN = 5;
   int SHOW_CHARM = 6;
   int SHOW_SLOTS = 7;

   default int getHidden() { return 0; }

   default void setHidden(int mask) {}

   default boolean isHidden(int slot) { return (this.getHidden() & (1 << slot)) != 0; }

   default void toggleHidden(int slot) { this.setHidden(this.getHidden() ^ (1 << slot)); }

   ItemStack getGarment(int slot);

   void setGarment(int slot, ItemStack stack);

   CompoundTag serializeNBT();

   void deserializeNBT(CompoundTag tag);
}
