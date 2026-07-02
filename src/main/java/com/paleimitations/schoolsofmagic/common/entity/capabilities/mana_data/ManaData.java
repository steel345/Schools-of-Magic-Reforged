package com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellHelper;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.util.INBTSerializable;

public class ManaData implements IManaData, INBTSerializable<CompoundTag> {

   private int spellSlot = 0;
   private Spell[] spells = new Spell[20];
   private float mana = SOMConfig.starterMana;
   private int maxManaBonus = 0;
   private float manaDiscountRate = 0.0F;
   private float deadMana = 0.0F;
   private boolean deadManaImmune = false;
   private float xpBonusRate = 0.0F;
   private int levelBonus = 0;
   private float magicianXP = 0.0F;
   private float[] elementXP = new float[MagicElementRegistry.ELEMENTS.size()];
   private float[] schoolXP = new float[MagicSchoolRegistry.SCHOOLS.size()];
   private float spellXP = 0.0F;
   private float potionXP = 0.0F;
   private float ritualXP = 0.0F;
   private int[] sparkArray = new int[MagicElementRegistry.ELEMENTS.size()];
   private boolean giveBook = false;

   public static final int[] MAX_COUNTDOWNS = new int[]{600, 1200, 1800, 2400, 3600, 4800, 6000, 7200, 8400};
   public static boolean PASSIVE_RECHARGE = true;
   private int[] charges = new int[9];
   private int[] countdowns = new int[9];

   @Override
   public List<Spell> getSpells() {
      return Lists.newArrayList(this.spells);
   }

   @Override
   public int getCurrentSpellSlot() {
      return this.spellSlot;
   }

   @Override
   public void setCurrentSpellSlot(int spellSlot) {
      this.spellSlot = spellSlot;
   }

   private transient int lastCastTick = -1;

   @Override
   public int getLastCastTick() {
      return this.lastCastTick;
   }

   @Override
   public void setLastCastTick(int tick) {
      this.lastCastTick = tick;
   }

   @Override
   public Spell getCurrentSpell() {
      if (this.spellSlot < 0) {
         this.spellSlot = 0;
      }
      if (this.spellSlot >= this.spells.length) {
         this.spellSlot = this.spells.length - 1;
      }
      return this.spells[this.spellSlot];
   }

   @Override
   public void setCurrentSpell(Spell spell) {
      this.spells[this.spellSlot] = spell;
   }

   @Override
   public Spell getSpell(int index) {
      return this.spells[index];
   }

   @Override
   public void setSpell(int index, Spell spell) {
      this.spells[index] = spell;
   }

   @Override
   public float getMana() {
      return this.mana;
   }

   @Override
   public void setMana(float mana) {
      this.mana = mana > (float) this.getMaxMana() ? (float) this.getMaxMana() : (mana <= 0.0F ? 0.0F : mana);
   }

   @Override
   public void addMana(float mana) {
      this.mana += mana;
      if (this.mana + this.deadMana > (float) this.getMaxMana()) {
         this.mana = (float) this.getMaxMana() - this.deadMana;
      }
   }

   @Override
   public void removeMana(float mana) {
      this.mana -= mana;
      if (this.mana < 0.0F) {
         this.mana = 0.0F;
      }
   }

   @Override
   public void useMana(float mana, List<MagicElement> elements, List<MagicSchool> schools, @Nullable IManaData.EnumMagicTool magicTool) {
      this.removeMana(mana);
      this.addMagicianXP(mana * (SOMConfig.manaXPRate + this.xpBonusRate));
      for (MagicElement element : elements) {
         this.addElementXP(element, mana * (SOMConfig.manaXPRate + this.xpBonusRate) * 1.3333334F * (1.0F / (float) elements.size()));
      }
      for (MagicSchool school : schools) {

         this.addSchoolXP(school, mana * (SOMConfig.manaXPRate + this.xpBonusRate) * 1.2F * (1.0F / (float) schools.size()));
      }
      if (magicTool != null) {
         switch (magicTool) {
            case SPELL:
               this.addSpellXP(mana * (SOMConfig.manaXPRate + this.xpBonusRate) * 0.6666667F);
               break;
            case RITUAL:
               this.addRitualXP(mana * (SOMConfig.manaXPRate + this.xpBonusRate));
               break;
            case POTION:
               this.addPotionXP(mana * (SOMConfig.manaXPRate + this.xpBonusRate) * 3.3333333F);
         }
      }
   }

   @Override
   public void setManaDiscountRate(float manaDiscountRate) {
      this.manaDiscountRate = manaDiscountRate;
   }

   @Override
   public float getManaDiscountRate() {
      return this.manaDiscountRate;
   }

   @Override
   public void setDeadMana(float deadMana) {
      this.deadMana = deadMana;
   }

   @Override
   public float getDeadMana() {
      return this.deadMana;
   }

   @Override
   public void addDeadMana(float deadMana) {
      this.deadMana += deadMana;
      if (this.deadMana + this.mana > (float) this.getMaxMana()) {
         if (this.deadMana > (float) this.getMaxMana()) {
            this.deadMana = this.getMaxMana();
         } else {
            this.mana = (float) this.getMaxMana() - this.deadMana;
         }
      }
      if (this.deadMana == (float) this.getMaxMana()) {
         this.removeMagicianXP((float) this.getMaxMana() * 0.25F);
         this.deadMana /= 4.0F;
      }
   }

   @Override
   public void removeDeadMana(float deadMana) {
      this.deadMana -= deadMana;
      if (this.deadMana < 0.0F) {
         this.deadMana = 0.0F;
      }
   }

   @Override
   public void setDeadManaImmune(boolean deadManaImmune) {
      this.deadManaImmune = deadManaImmune;
   }

   @Override
   public boolean isDeadManaImmune() {
      return this.deadManaImmune;
   }

   @Override
   public int[] getSparkArray() {
      return this.sparkArray;
   }

   @Override
   public boolean addSpark(MagicElement magicType) {
      if (this.canAcceptSpark(magicType)) {
         int n = magicType.getId();
         this.sparkArray[n] = this.sparkArray[n] + 1;
         return true;
      }
      return false;
   }

   @Override
   public boolean removeSpark(MagicElement magicType) {
      if (this.hasSpark(magicType)) {
         int n = magicType.getId();
         this.sparkArray[n] = this.sparkArray[n] - 1;
         return true;
      }
      return false;
   }

   @Override
   public boolean hasSpark(MagicElement magicType) {
      return this.sparkArray[magicType.getId()] > 0;
   }

   @Override
   public boolean canAcceptSpark(MagicElement magicType) {
      int total = 0;
      for (int i : this.sparkArray) {
         total += i;
      }
      return total < this.getSparkLimit();
   }

   @Override
   public int getSparkLimit() {
      return Math.min(this.getLevel() / 15 + 1, 10);
   }

   @Override
   public void setXPBonusRate(float xpBonusRate) {
      this.xpBonusRate = xpBonusRate;
   }

   @Override
   public float getXPBonusRate() {
      return this.xpBonusRate;
   }

   @Override
   public float getMagicianXP() {
      return this.magicianXP;
   }

   @Override
   public void setMagicianXP(float magicianXP) {
      this.magicianXP = magicianXP;
   }

   @Override
   public void addMagicianXP(float magicianXP) {
      if (this.getLevel() < SOMConfig.maxLevel) {
         this.magicianXP += magicianXP;
      }
   }

   @Override
   public void removeMagicianXP(float magicianXP) {
      this.magicianXP -= magicianXP;
      if (this.magicianXP < 0.0F) {
         this.magicianXP = 0.0F;
      }
   }

   @Override
   public Tuple<Float, Float> getMagicianXPToNextLevel() {
      int level = 0;
      float magicianXPTemp = this.magicianXP;
      int nextLevel = 50;
      while (magicianXPTemp > 0.0F && magicianXPTemp > (float) (nextLevel = 50 + level * 10)) {
         magicianXPTemp -= (float) nextLevel;
         ++level;
      }
      return new Tuple<>(magicianXPTemp, (float) nextLevel);
   }

   @Override
   public int getLevel() {
      int level = 0;
      float magicianXPTemp = this.magicianXP;
      int nextLevel;
      while (magicianXPTemp > 0.0F && magicianXPTemp > (float) (nextLevel = 50 + level * 10)) {
         magicianXPTemp -= (float) nextLevel;
         ++level;
      }
      return level;
   }

   @Override
   public int getLevelBonus() {
      return this.levelBonus;
   }

   @Override
   public void setLevelBonus(int levelBonus) {
      this.levelBonus = levelBonus;
   }

   @Override
   public int getMaxMana() {
      int i = (int) SOMConfig.starterMana;
      for (int k = 0; k < this.getLevel(); ++k) {
         if (k < 5) {
            i += 5;
         } else if (k >= 5 && k < 10) {
            i += 6;
         } else if (k >= 10 && k < 15) {
            i += 8;
         } else if (k >= 15 && k < 20) {
            i += 10;
         } else if (k >= 20 && k < 30) {
            i += 12;
         } else if (k >= 30 && k < 40) {
            i += 15;
         } else if (k >= 40 && k < 50) {
            i += 20;
         } else if (k >= 50 && k < 60) {
            i += 23;
         } else if (k >= 60 && k < 70) {
            i += 25;
         } else if (k >= 70 && k < 80) {
            i += 28;
         } else if (k >= 80 && k < 90) {
            i += 30;
         } else if (k >= 90) {
            i += 32;
         }
      }
      return i + this.maxManaBonus;
   }

   public static int getMaxMana(int level) {
      int i = (int) SOMConfig.starterMana;
      for (int k = 0; k < level; ++k) {
         if (k < 5) {
            i += 5;
         } else if (k >= 5 && k < 10) {
            i += 6;
         } else if (k >= 10 && k < 15) {
            i += 8;
         } else if (k >= 15 && k < 20) {
            i += 10;
         } else if (k >= 20 && k < 30) {
            i += 12;
         } else if (k >= 30 && k < 40) {
            i += 15;
         } else if (k >= 40 && k < 50) {
            i += 20;
         } else if (k >= 50 && k < 60) {
            i += 23;
         } else if (k >= 60 && k < 70) {
            i += 25;
         } else if (k >= 70 && k < 80) {
            i += 28;
         } else if (k >= 80 && k < 90) {
            i += 30;
         } else if (k >= 90) {
            i += 32;
         }
      }
      return i;
   }

   @Override
   public int getMaxManaBonus() {
      return this.maxManaBonus;
   }

   @Override
   public void setMaxManaBonus(int maxManaBonus) {
      this.maxManaBonus = maxManaBonus;
   }

   @Override
   public float[] getElementXPs() {
      return this.elementXP;
   }

   @Override
   public void setElementXPs(float[] xps) {
      this.elementXP = xps;
   }

   @Override
   public float getElementXP(MagicElement element) {
      return this.elementXP[element.getId()];
   }

   @Override
   public void setElementXP(MagicElement element, float elementXP) {
      this.elementXP[element.getId()] = elementXP;
   }

   @Override
   public void addElementXP(MagicElement element, float elementXP) {
      this.elementXP[element.getId()] = Math.min(this.elementXP[element.getId()] + elementXP, this.magicianXP);
   }

   @Override
   public void removeElementXP(MagicElement element, float elementXP) {
      this.elementXP[element.getId()] = Math.max(this.elementXP[element.getId()] - elementXP, 0.0F);
   }

   @Override
   public int getElementLevel(MagicElement element) {
      int level = 0;
      float elementXP = this.getElementXP(element);
      int nextLevel;
      while (elementXP > 0.0F && elementXP > (float) (nextLevel = 50 + level * 10)) {
         elementXP -= (float) nextLevel;
         ++level;
      }
      return level;
   }

   @Override
   public Tuple<Float, Float> getElementXPToNextLevel(MagicElement element) {
      int level = 0;
      float elementXP = this.getElementXP(element);
      int nextLevel = 50;
      while (elementXP > 0.0F && elementXP > (float) (nextLevel = 50 + level * 10)) {
         elementXP -= (float) nextLevel;
         ++level;
      }
      return new Tuple<>(elementXP, (float) nextLevel);
   }

   @Override
   public float[] getSchoolXPs() {
      return this.schoolXP;
   }

   @Override
   public void setSchoolXPs(float[] xps) {
      this.schoolXP = xps;
   }

   @Override
   public float getSchoolXP(MagicSchool school) {
      return this.schoolXP[school.getId()];
   }

   @Override
   public void setSchoolXP(MagicSchool school, float schoolXP) {
      this.schoolXP[school.getId()] = schoolXP;
   }

   @Override
   public void addSchoolXP(MagicSchool school, float schoolXP) {
      this.schoolXP[school.getId()] = Math.min(this.schoolXP[school.getId()] + schoolXP, this.magicianXP);
   }

   @Override
   public void removeSchoolXP(MagicSchool school, float schoolXP) {
      this.schoolXP[school.getId()] = Math.max(this.schoolXP[school.getId()] - schoolXP, 0.0F);
   }

   @Override
   public int getSchoolLevel(MagicSchool school) {
      int level = 0;
      float schoolXP = this.getSchoolXP(school);
      int nextLevel;
      while (schoolXP > 0.0F && schoolXP > (float) (nextLevel = 50 + level * 10)) {
         schoolXP -= (float) nextLevel;
         ++level;
      }
      return level;
   }

   @Override
   public Tuple<Float, Float> getSchoolXPToNextLevel(MagicSchool school) {
      int level = 0;
      float schoolXP = this.getSchoolXP(school);
      int nextLevel = 50;
      while (schoolXP > 0.0F && schoolXP > (float) (nextLevel = 50 + level * 10)) {
         schoolXP -= (float) nextLevel;
         ++level;
      }
      return new Tuple<>(schoolXP, (float) nextLevel);
   }

   @Override
   public float getSpellXP() {
      return this.spellXP;
   }

   @Override
   public void setSpellXP(float spellXP) {
      this.spellXP = spellXP;
   }

   @Override
   public void addSpellXP(float spellXP) {
      this.spellXP += spellXP;
      if (this.spellXP > this.magicianXP) {
         this.spellXP = this.magicianXP;
      }
   }

   @Override
   public void removeSpellXP(float spellXP) {
      this.spellXP -= spellXP;
      if (this.spellXP < 0.0F) {
         this.spellXP = 0.0F;
      }
   }

   @Override
   public int getSpellLevel() {
      int level = 0;
      float spellXPTemp = this.spellXP;
      int nextLevel;
      while (spellXPTemp > 0.0F && spellXPTemp > (float) (nextLevel = 50 + level * 10)) {
         spellXPTemp -= (float) nextLevel;
         ++level;
      }
      return level;
   }

   @Override
   public Tuple<Float, Float> getSpellXPToNextLevel() {
      int level = 0;
      float spellXPTemp = this.spellXP;
      int nextLevel = 50;
      while (spellXPTemp > 0.0F && spellXPTemp > (float) (nextLevel = 50 + level * 10)) {
         spellXPTemp -= (float) nextLevel;
         ++level;
      }
      return new Tuple<>(spellXPTemp, (float) nextLevel);
   }

   @Override
   public float getPotionXP() {
      return this.potionXP;
   }

   @Override
   public void setPotionXP(float potionXP) {
      this.potionXP = potionXP;
   }

   @Override
   public void addPotionXP(float potionXP) {
      this.potionXP += potionXP;
      if (this.potionXP > this.magicianXP) {
         this.potionXP = this.magicianXP;
      }
   }

   @Override
   public void removePotionXP(float potionXP) {
      this.potionXP -= potionXP;
      if (this.potionXP < 0.0F) {
         this.potionXP = 0.0F;
      }
   }

   @Override
   public int getPotionLevel() {
      int level = 0;
      float potionXPTemp = this.potionXP;
      int nextLevel;
      while (potionXPTemp > 0.0F && potionXPTemp > (float) (nextLevel = 50 + level * 10)) {
         potionXPTemp -= (float) nextLevel;
         ++level;
      }
      return level;
   }

   @Override
   public Tuple<Float, Float> getPotionXPToNextLevel() {
      int level = 0;
      float potionXPTemp = this.potionXP;
      int nextLevel = 50;
      while (potionXPTemp > 0.0F && potionXPTemp > (float) (nextLevel = 50 + level * 10)) {
         potionXPTemp -= (float) nextLevel;
         ++level;
      }
      return new Tuple<>(potionXPTemp, (float) nextLevel);
   }

   @Override
   public float getRitualXP() {
      return this.ritualXP;
   }

   @Override
   public void setRitualXP(float ritualXP) {
      this.ritualXP = ritualXP;
   }

   @Override
   public void addRitualXP(float ritualXP) {
      this.ritualXP += ritualXP;
      if (this.ritualXP > this.magicianXP) {
         this.ritualXP = this.magicianXP;
      }
   }

   @Override
   public void removeRitualXP(float ritualXP) {
      this.ritualXP -= ritualXP;
      if (this.ritualXP < 0.0F) {
         this.ritualXP = 0.0F;
      }
   }

   @Override
   public int getRitualLevel() {
      int level = 0;
      float ritualXPTemp = this.ritualXP;
      int nextLevel;
      while (ritualXPTemp > 0.0F && ritualXPTemp > (float) (nextLevel = 50 + level * 10)) {
         ritualXPTemp -= (float) nextLevel;
         ++level;
      }
      return level;
   }

   @Override
   public Tuple<Float, Float> getRitualXPToNextLevel() {
      int level = 0;
      float ritualXPTemp = this.ritualXP;
      int nextLevel = 50;
      while (ritualXPTemp > 0.0F && ritualXPTemp > (float) (nextLevel = 50 + level * 10)) {
         ritualXPTemp -= (float) nextLevel;
         ++level;
      }
      return new Tuple<>(ritualXPTemp, (float) nextLevel);
   }

   @Override
   public int[] getCharges() {
      return this.charges;
   }

   @Override
   public int[] getCountdowns() {
      return this.countdowns;
   }

   @Override
   public boolean hasChargeLevel(int chargeLevel) {
      return chargeLevel >= 0 && chargeLevel < this.charges.length && this.charges[chargeLevel] > 0;
   }

   @Override
   public void useCharge(int chargeLevel) {
      if (chargeLevel < 0 || chargeLevel >= this.charges.length) {
         return;
      }
      if (this.charges[chargeLevel] > 0) {
         this.charges[chargeLevel]--;
      }
      if (this.countdowns[chargeLevel] == 0) {
         this.resetCountdown(chargeLevel);
      }
   }

   private void resetCountdown(int chargeLevel) {
      this.countdowns[chargeLevel] = MAX_COUNTDOWNS[chargeLevel];
   }

   @Override
   public void addCharge(int i) {
      if (i >= 0 && i < this.charges.length) {
         this.charges[i]++;
      }
   }

   @Override
   public void resetCharges() {
      for (int i = 0; i < this.charges.length; ++i) {
         while (this.canAddCharge(i)) {
            this.addCharge(i);
         }
      }
   }

   @Override
   public boolean canAddCharge(int chargeLevel) {
      return this.charges[chargeLevel] < this.getMaxCharges(chargeLevel, this.getLevel());
   }

   @Override
   public int getMaxCharges(int chargeLevel, int level) {
      switch (chargeLevel) {
         case 0:
            return 2 + Math.min(6, (level + 1) / 5);
         case 1:
            return Math.min(6, level / 5);
         case 2:
            return level < 9 ? 0 : Math.min(5, (level - 4) / 5);
         case 3:
            return level < 13 ? 0 : Math.min(5, (level - 8) / 5);
         case 4:
            return level < 16 ? 0 : Math.min(5, (level - 10) / 6);
         case 5:
            return level < 20 ? 0 : Math.min(4, (level - 11) / 8);
         case 6:
            return level < 25 ? 0 : Math.min(4, (level - 11) / 9);
         case 7:
            return level < 30 ? 0 : Math.min(3, (level - 24) / 6);
         case 8:
            return level < 32 ? 0 : (level < 50 ? 1 : 2);
         default:
            return 1;
      }
   }

   @Override
   public int getLargestChargeLevel() {
      int level = this.getLevel();
      for (int i = 0; i < 9; ++i) {
         if (this.getMaxCharges(i, level) == 0) {
            return i - 1;
         }
      }
      return 8;
   }

   @Override
   public void tickCharges() {
      int curLevel = this.getLevel();
      for (int i = 0; i < 9; ++i) {
         int maxHere = this.getMaxCharges(i, curLevel);
         if (this.charges[i] > maxHere) {
            this.charges[i] = maxHere;
         }
      }
      if (!PASSIVE_RECHARGE) {
         return;
      }
      for (int i = 0; i < 9; ++i) {
         if (this.countdowns[i] > 0) {
            this.countdowns[i]--;
         } else if (this.countdowns[i] == 0 && this.canAddCharge(i)) {
            this.addCharge(i);
            if (this.canAddCharge(i)) {
               this.resetCountdown(i);
            }
         }
      }
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      writeNBT(nbt);
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      readNBT(nbt);
   }

   public void writeNBT(CompoundTag nbt) {
      nbt.putInt("spellSlot", this.spellSlot);
      for (int i = 0; i < 20; ++i) {
         if (this.spells[i] != null) {
            nbt.putString("spell" + i, this.spells[i].getResourceLocation().toString());
            nbt.put("spellData" + i, this.spells[i].serializeNBT());
         }
      }
      nbt.putFloat("mana", this.mana);
      nbt.putInt("maxManaBonus", this.maxManaBonus);
      nbt.putFloat("manaDiscountRate", this.manaDiscountRate);
      nbt.putFloat("deadMana", this.deadMana);
      nbt.putBoolean("deadManaImmune", this.deadManaImmune);
      nbt.putFloat("xpBonusRate", this.xpBonusRate);
      nbt.putInt("levelBonus", this.levelBonus);
      nbt.putFloat("magicianXP", this.magicianXP);
      for (MagicElement element : MagicElementRegistry.ELEMENTS) {
         nbt.putFloat(element.getName() + "XP", this.elementXP[element.getId()]);
      }
      for (MagicSchool school : MagicSchoolRegistry.SCHOOLS) {
         nbt.putFloat(school.getName() + "XP", this.schoolXP[school.getId()]);
      }
      nbt.putFloat("spellXP", this.spellXP);
      nbt.putFloat("ritualXP", this.ritualXP);
      nbt.putFloat("potionXP", this.potionXP);
      nbt.putIntArray("sparkArray", this.sparkArray);
      nbt.putIntArray("charges", this.charges);
      nbt.putIntArray("countdowns", this.countdowns);
   }

   public void readNBT(CompoundTag nbt) {
      this.spellSlot = nbt.getInt("spellSlot");
      for (int i = 0; i < 20; ++i) {
         if (nbt.contains("spell" + i) && nbt.contains("spellData" + i)) {
            this.spells[i] = SpellHelper.getSpellInstance(new ResourceLocation(nbt.getString("spell" + i)), nbt.getCompound("spellData" + i));
         }
      }
      this.mana = nbt.getFloat("mana");
      this.maxManaBonus = nbt.getInt("maxManaBonus");
      this.manaDiscountRate = nbt.getFloat("manaDiscountRate");
      this.deadMana = nbt.getFloat("deadMana");
      this.deadManaImmune = nbt.getBoolean("deadManaImmune");
      this.xpBonusRate = nbt.getFloat("xpBonusRate");
      this.levelBonus = nbt.getInt("levelBonus");
      this.magicianXP = nbt.getFloat("magicianXP");
      for (MagicElement element : MagicElementRegistry.ELEMENTS) {
         this.elementXP[element.getId()] = nbt.getFloat(element.getName() + "XP");
      }
      for (MagicSchool school : MagicSchoolRegistry.SCHOOLS) {
         this.schoolXP[school.getId()] = nbt.getFloat(school.getName() + "XP");
      }
      this.spellXP = nbt.getFloat("spellXP");
      this.ritualXP = nbt.getFloat("ritualXP");
      this.potionXP = nbt.getFloat("potionXP");
      this.sparkArray = nbt.getIntArray("sparkArray");
      int[] c = nbt.getIntArray("charges");
      if (c.length == 9) {
         this.charges = c;
      }
      int[] cd = nbt.getIntArray("countdowns");
      if (cd.length == 9) {
         this.countdowns = cd;
      }
   }
}
