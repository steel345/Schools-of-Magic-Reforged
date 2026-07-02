package com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata;

import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

public class WandData implements INBTSerializable<CompoundTag>, IWandData {
   private Spell spell;
   private IWandData.EnumCoreType coreType;
   private IWandData.EnumGemType gemType;
   private IWandData.EnumHandleType handleType;
   private boolean limitedSlots;
   private int slots;

   public WandData() {
   }

   @Override
   public boolean hasLimitedSlots() {
      return this.limitedSlots;
   }

   @Override
   public void setLimitedSlots(boolean limitedSlots) {
      this.limitedSlots = limitedSlots;
   }

   @Override
   public void setLimitedSlots(int slots) {
      this.slots = slots;
   }

   @Override
   public int getLimitedSlots() {
      return this.slots;
   }

   @Override
   public Spell getSpell() {
      return this.spell;
   }

   @Override
   public void setCoreType(IWandData.EnumCoreType coreType) {
      this.coreType = coreType;
   }

   @Override
   public IWandData.EnumCoreType getCoreType() {
      return this.coreType;
   }

   @Override
   public void setGemType(IWandData.EnumGemType gemType) {
      this.gemType = gemType;
   }

   @Override
   public IWandData.EnumGemType getGemType() {
      return this.gemType;
   }

   @Override
   public void setHandleType(IWandData.EnumHandleType handleType) {
      this.handleType = handleType;
   }

   @Override
   public IWandData.EnumHandleType getHandleType() {
      return this.handleType;
   }

   @Override
   public void setSpell(Spell spell) {
      this.spell = spell;
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      if (this.spell != null) {
         nbt.putString("spell", this.spell.getResourceLocation().toString());
         nbt.put("spellData", this.spell.serializeNBT());
      }

      if (this.coreType != null) {
         nbt.putString("coreType", this.coreType.getSerializedName());
      }

      if (this.gemType != null) {
         nbt.putString("gemType", this.gemType.getSerializedName());
      }

      if (this.handleType != null) {
         nbt.putString("handleType", this.handleType.getSerializedName());
      }

      nbt.putBoolean("limitedSlots", this.limitedSlots);
      nbt.putInt("slots", this.slots);
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      if (nbt.contains("spell")) {
         this.spell = SpellHelper.getSpellInstance(new ResourceLocation(nbt.getString("spell")), nbt.getCompound("spellData"));
      }

      if (nbt.contains("coreType")) {
         this.coreType = IWandData.EnumCoreType.fromName(nbt.getString("coreType"));
      }

      if (nbt.contains("gemType")) {
         this.gemType = IWandData.EnumGemType.fromName(nbt.getString("gemType"));
      }

      if (nbt.contains("handleType")) {
         this.handleType = IWandData.EnumHandleType.fromName(nbt.getString("handleType"));
      }

      if (this.coreType == null) {
         this.coreType = IWandData.EnumCoreType.DARK_OAK;
      }

      if (this.gemType == null) {
         this.gemType = IWandData.EnumGemType.DIAMOND;
      }

      if (this.handleType == null) {
         this.handleType = IWandData.EnumHandleType.BRASS;
      }

      this.limitedSlots = nbt.getBoolean("limitedSlots");
      this.slots = nbt.getInt("slots");
   }
}
