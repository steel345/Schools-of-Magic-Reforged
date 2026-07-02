package com.paleimitations.schoolsofmagic.common.items;

import javax.annotation.Nullable;

import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.CapabilityPotionData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.IPotionData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PotionBase extends Item {
   public PotionBase(Item.Properties props) {
      super(props);
   }

   @Override
   public boolean shouldOverrideMultiplayerNbt() {
      return true;
   }

   @Override
   public CompoundTag getShareTag(ItemStack stack) {
      CompoundTag nbt = new CompoundTag();
      if (stack.getTag() != null) {
         nbt.put("tag", stack.getTag().copy());
      }
      IPotionData data = CapabilityPotionData.getCapability(stack);
      if (data != null) {
         nbt.put("PotionData", data.serializeNBT());
      }
      return nbt.isEmpty() ? null : nbt;
   }

   @Override
   public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
      stack.setTag(nbt != null && nbt.contains("tag", 10) ? nbt.getCompound("tag") : null);
      if (nbt != null && nbt.contains("PotionData", 10)) {
         IPotionData data = CapabilityPotionData.getCapability(stack);
         if (data != null) {
            data.deserializeNBT(nbt.getCompound("PotionData"));
         }
      }
   }
}
