package com.paleimitations.schoolsofmagic.common.items.capabilities.spell_modifier;

import com.paleimitations.schoolsofmagic.common.spells.Spell;
import net.minecraft.nbt.CompoundTag;

public interface ISpellModifier {
   Spell.EnumSpellModifier getSpellModifier();

   Object getModifierStat();

   void setSpellModifier(Spell.EnumSpellModifier var1, Object var2);

   CompoundTag serializeNBT();

   void deserializeNBT(CompoundTag var1);
}
