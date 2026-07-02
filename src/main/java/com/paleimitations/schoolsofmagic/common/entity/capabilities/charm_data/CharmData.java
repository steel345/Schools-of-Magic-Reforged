package com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class CharmData implements ICharmData {
   private ItemStack charm = ItemStack.EMPTY;

   @Override
   public ItemStack getCharm() {
      return this.charm;
   }

   @Override
   public void setCharm(ItemStack stack) {
      this.charm = stack == null ? ItemStack.EMPTY : stack;
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag tag = new CompoundTag();
      if (!this.charm.isEmpty()) {
         tag.put("charm", this.charm.save(new CompoundTag()));
      }
      return tag;
   }

   @Override
   public void deserializeNBT(CompoundTag tag) {
      this.charm = tag.contains("charm") ? ItemStack.of(tag.getCompound("charm")) : ItemStack.EMPTY;
   }
}
