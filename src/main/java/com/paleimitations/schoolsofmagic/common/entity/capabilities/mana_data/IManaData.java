package com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data;

import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IManaData {

   int getCurrentSpellSlot();

   void setCurrentSpellSlot(int var1);

   int getLastCastTick();

   void setLastCastTick(int var1);

   Spell getCurrentSpell();

   void setCurrentSpell(Spell var1);

   Spell getSpell(int var1);

   void setSpell(int var1, Spell var2);

   List<Spell> getSpells();

   int[] getCharges();

   int[] getCountdowns();

   boolean hasChargeLevel(int var1);

   void useCharge(int var1);

   void addCharge(int var1);

   void resetCharges();

   boolean canAddCharge(int var1);

   int getMaxCharges(int var1, int var2);

   int getLargestChargeLevel();

   void tickCharges();

   float getMana();

   void setMana(float var1);

   void addMana(float var1);

   void removeMana(float var1);

   void useMana(float var1, List<MagicElement> var2, List<MagicSchool> var3, @Nullable EnumMagicTool var4);

   float getManaDiscountRate();

   void setManaDiscountRate(float var1);

   float getDeadMana();

   void setDeadMana(float var1);

   void addDeadMana(float var1);

   void removeDeadMana(float var1);

   boolean isDeadManaImmune();

   void setDeadManaImmune(boolean var1);

   int[] getSparkArray();

   boolean addSpark(MagicElement var1);

   boolean removeSpark(MagicElement var1);

   boolean hasSpark(MagicElement var1);

   boolean canAcceptSpark(MagicElement var1);

   int getSparkLimit();

   float getXPBonusRate();

   void setXPBonusRate(float var1);

   float getMagicianXP();

   void setMagicianXP(float var1);

   void addMagicianXP(float var1);

   void removeMagicianXP(float var1);

   Tuple<Float, Float> getMagicianXPToNextLevel();

   int getLevel();

   int getLevelBonus();

   void setLevelBonus(int var1);

   int getMaxMana();

   int getMaxManaBonus();

   void setMaxManaBonus(int var1);

   float[] getElementXPs();

   void setElementXPs(float[] var1);

   float getElementXP(MagicElement var1);

   void setElementXP(MagicElement var1, float var2);

   void addElementXP(MagicElement var1, float var2);

   void removeElementXP(MagicElement var1, float var2);

   int getElementLevel(MagicElement var1);

   Tuple<Float, Float> getElementXPToNextLevel(MagicElement var1);

   float[] getSchoolXPs();

   void setSchoolXPs(float[] var1);

   float getSchoolXP(MagicSchool var1);

   void setSchoolXP(MagicSchool var1, float var2);

   void addSchoolXP(MagicSchool var1, float var2);

   void removeSchoolXP(MagicSchool var1, float var2);

   int getSchoolLevel(MagicSchool var1);

   Tuple<Float, Float> getSchoolXPToNextLevel(MagicSchool var1);

   float getPotionXP();

   void setPotionXP(float var1);

   void addPotionXP(float var1);

   void removePotionXP(float var1);

   int getPotionLevel();

   Tuple<Float, Float> getPotionXPToNextLevel();

   float getSpellXP();

   void setSpellXP(float var1);

   void addSpellXP(float var1);

   void removeSpellXP(float var1);

   int getSpellLevel();

   Tuple<Float, Float> getSpellXPToNextLevel();

   float getRitualXP();

   void setRitualXP(float var1);

   void addRitualXP(float var1);

   void removeRitualXP(float var1);

   int getRitualLevel();

   Tuple<Float, Float> getRitualXPToNextLevel();

   CompoundTag serializeNBT();

   void deserializeNBT(CompoundTag var1);

   public static enum EnumMagicTool implements StringRepresentable {
      SPELL,
      RITUAL,
      POTION;

      @Override
      public String getSerializedName() {
         return this.name().toLowerCase();
      }

      public static EnumMagicTool fromName(String name) {
         for (EnumMagicTool tool : EnumMagicTool.values()) {
            if (tool.getSerializedName().equalsIgnoreCase(name)) {
               return tool;
            }
         }
         return null;
      }
   }
}
