package com.paleimitations.schoolsofmagic.common.items.capabilities.spell_modifier;

import com.paleimitations.schoolsofmagic.common.spells.Spell;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class SpellModifier implements ISpellModifier, INBTSerializable<CompoundTag> {
   private Spell.EnumSpellModifier modifier;
   private Object stat;

   public SpellModifier() {
   }

   @Override
   public Spell.EnumSpellModifier getSpellModifier() {
      return this.modifier;
   }

   @Override
   public Object getModifierStat() {
      return this.stat;
   }

   @Override
   public void setSpellModifier(Spell.EnumSpellModifier modifier, Object stat) {
      this.modifier = modifier;
      this.stat = stat;
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      if (this.modifier != null) {
         nbt.putString("modifier", this.modifier.getSerializedName());
      }

      if (this.stat != null) {
         if (this.stat instanceof String) {
            nbt.putString("modifiers_info", (String)this.stat);
            nbt.putInt("modifiers_info_num", 0);
         } else if (this.stat instanceof Float) {
            nbt.putFloat("modifiers_info", (Float)this.stat);
            nbt.putInt("modifiers_info_num", 1);
         } else if (this.stat instanceof Integer) {
            nbt.putInt("modifiers_info", (Integer)this.stat);
            nbt.putInt("modifiers_info_num", 2);
         } else if (this.stat instanceof Byte) {
            nbt.putByte("modifiers_info", (Byte)this.stat);
            nbt.putInt("modifiers_info_num", 3);
         } else if (this.stat instanceof Long) {
            nbt.putLong("modifiers_info", (Long)this.stat);
            nbt.putInt("modifiers_info_num", 4);
         } else {
            nbt.putString("modifiers_info", "null");
            nbt.putInt("modifiers_info_num", 5);
         }
      }

      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      if (nbt.contains("modifier")) {
         this.modifier = Spell.EnumSpellModifier.fromName(nbt.getString("modifier"));
      }

      if (nbt.contains("modifiers_info_num")) {
         switch (nbt.getInt("modifiers_info_num")) {
            case 0:
               this.stat = nbt.getString("modifiers_info");
               break;
            case 1:
               this.stat = nbt.getFloat("modifiers_info");
               break;
            case 2:
               this.stat = nbt.getInt("modifiers_info");
               break;
            case 3:
               this.stat = nbt.getByte("modifiers_info");
               break;
            case 4:
               this.stat = nbt.getLong("modifiers_info");
               break;
            case 5:
               this.stat = "null";
         }
      }
   }
}
