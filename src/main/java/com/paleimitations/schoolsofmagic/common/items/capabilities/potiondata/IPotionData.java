package com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata;

import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

public interface IPotionData {
   List<MobEffectInstance> getPotionEffects();

   ItemStack addPotionEffect(ItemStack var1, MobEffectInstance var2);

   void setPotionEffects(List<MobEffectInstance> var1);

   int getDuration(MobEffect var1);

   int getAmplifier(MobEffect var1);

   int getColor();

   void setDrinkTime(int var1);

   int getDrinkTime();

   void setDrinkNumber(int var1);

   int getDrinkNumber();

   void setRadius(int var1);

   int getRadius();

   void setFilter(int var1);

   int getFilter();

   void setLength(int var1);

   int getLength();

   boolean isLingering();

   void setLingering(boolean var1);

   boolean isDecorative();

   void setDecorative(boolean var1);

   CompoundTag serializeNBT();

   void deserializeNBT(CompoundTag var1);
}
