package com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.common.util.INBTSerializable;

public class PotionData implements IPotionData, INBTSerializable<CompoundTag> {
   private List<MobEffectInstance> effects = Lists.newArrayList();
   private int radius = 0;
   private int length = 0;
   private int filter = 0;
   private boolean isLingering = false;
   private boolean isDecorative = false;
   private int drinkTime = 35;
   private int drinkNum = 1;

   public PotionData() {
   }

   @Override
   public List<MobEffectInstance> getPotionEffects() {
      return this.effects;
   }

   @Override
   public ItemStack addPotionEffect(ItemStack stack, MobEffectInstance potion) {
      this.effects.add(potion);
      return stack;
   }

   @Override
   public void setPotionEffects(List<MobEffectInstance> effects) {
      this.effects = effects;
   }

   @Override
   public int getDuration(MobEffect potion) {
      int dur = 0;

      for (MobEffectInstance entry : this.effects) {
         if (entry.getEffect().equals(potion)) {
            dur = entry.getDuration();
         }
      }

      return dur;
   }

   @Override
   public int getAmplifier(MobEffect potion) {
      int amp = 0;

      for (MobEffectInstance entry : this.effects) {
         if (entry.getEffect().equals(potion)) {
            amp = entry.getAmplifier();
         }
      }

      return amp;
   }

   @Override
   public int getRadius() {
      return this.radius;
   }

   @Override
   public void setRadius(int radius) {
      this.radius = radius;
   }

   @Override
   public int getFilter() {
      return this.filter;
   }

   @Override
   public void setFilter(int filter) {
      this.filter = filter;
   }

   @Override
   public int getLength() {
      return this.length;
   }

   @Override
   public void setLength(int length) {
      this.length = length;
   }

   @Override
   public boolean isLingering() {
      return this.isLingering;
   }

   @Override
   public void setLingering(boolean isLingering) {
      this.isLingering = isLingering;
   }

   @Override
   public boolean isDecorative() {
      return this.isDecorative;
   }

   @Override
   public void setDecorative(boolean isDecorative) {
      this.isDecorative = isDecorative;
   }

   @Override
   public int getColor() {
      return PotionUtils.getColor(this.effects);
   }

   @Override
   public int getDrinkTime() {
      return this.drinkTime;
   }

   @Override
   public void setDrinkTime(int drinkTime) {
      this.drinkTime = drinkTime;
   }

   @Override
   public int getDrinkNumber() {
      return this.drinkNum;
   }

   @Override
   public void setDrinkNumber(int drinkNum) {
      this.drinkNum = drinkNum;
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      nbt.putInt("radius", this.radius);
      nbt.putInt("filter", this.filter);
      nbt.putInt("length", this.length);
      nbt.putInt("drinkTime", this.drinkTime);
      nbt.putInt("drinkNum", this.drinkNum);
      nbt.putBoolean("isLingering", this.isLingering);
      nbt.putBoolean("isDecorative", this.isDecorative);
      if (!this.effects.isEmpty()) {
         ListTag nbttaglist = new ListTag();

         for (MobEffectInstance potioneffect : this.effects) {
            nbttaglist.add(potioneffect.save(new CompoundTag()));
         }

         nbt.put("Effects", nbttaglist);
      }

      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      this.radius = nbt.getInt("radius");
      this.filter = nbt.getInt("filter");
      this.length = nbt.getInt("length");
      this.drinkTime = nbt.getInt("drinkTime");
      this.drinkNum = nbt.getInt("drinkNum");
      this.isLingering = nbt.getBoolean("isLingering");
      this.isDecorative = nbt.getBoolean("isDecorative");
      if (nbt.contains("Effects", 9)) {
         ListTag nbttaglist = nbt.getList("Effects", 10);
         List<MobEffectInstance> effects = Lists.newArrayList();

         for (int i = 0; i < nbttaglist.size(); i++) {
            MobEffectInstance potioneffect = MobEffectInstance.load(nbttaglist.getCompound(i));
            if (potioneffect != null) {
               effects.add(potioneffect);
            }
         }

         this.effects = effects;
      }
   }
}
