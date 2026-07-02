package com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes;

import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface ISpellNotes {
   List<ItemStack> getOptions();

   SpellNotes getSpellNotes();

   CompoundTag serializeNBT();

   void deserializeNBT(CompoundTag var1);
}
