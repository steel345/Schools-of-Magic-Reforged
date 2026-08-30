package com.paleimitations.schoolsofmagic.common.entity.capabilities.rift_storage;

import com.paleimitations.schoolsofmagic.common.compat.SOMConfig;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;

// one list, one owner, saved with the player. the rift entity holds nothing itself, so closing it,
// relogging or opening a second one can never fork the contents into two copies
public class RiftStorage implements IRiftStorage, INBTSerializable<CompoundTag> {
   public static final int PAGE = 54;

   private final List<ItemStack> items = new ArrayList<>();

   @Override
   public int size() {
      return this.items.size();
   }

   @Override
   public ItemStack get(int index) {
      if (index < 0 || index >= this.items.size()) return ItemStack.EMPTY;
      return this.items.get(index);
   }

   @Override
   public void set(int index, ItemStack stack) {
      if (index < 0) return;
      if (index >= SOMConfig.riftSlotCap()) return;
      while (this.items.size() <= index) this.items.add(ItemStack.EMPTY);
      this.items.set(index, stack == null ? ItemStack.EMPTY : stack);
   }

   @Override
   public List<ItemStack> all() {
      return this.items;
   }

   // the page on screen fills up long before the rift does, so this drops onto the end of the pile
   @Override
   public boolean add(ItemStack stack) {
      if (stack == null || stack.isEmpty()) return false;
      this.trim();
      if (this.items.size() >= SOMConfig.riftSlotCap()) return false;
      this.items.add(stack);
      return true;
   }

   // the tail is only ever empty padding for the view, it does not need saving
   @Override
   public void trim() {
      int last = this.items.size() - 1;
      while (last >= 0 && this.items.get(last).isEmpty()) {
         this.items.remove(last);
         last--;
      }
   }

   @Override
   public CompoundTag serializeNBT() {
      this.trim();
      CompoundTag tag = new CompoundTag();
      ListTag list = new ListTag();
      for (int i = 0; i < this.items.size(); i++) {
         ItemStack stack = this.items.get(i);
         if (stack.isEmpty()) continue;
         CompoundTag entry = new CompoundTag();
         entry.putInt("Slot", i);
         stack.save(entry);
         list.add(entry);
      }
      tag.put("Items", list);
      tag.putInt("Size", this.items.size());
      return tag;
   }

   @Override
   public void deserializeNBT(CompoundTag tag) {
      this.items.clear();
      int size = tag.getInt("Size");
      for (int i = 0; i < size; i++) this.items.add(ItemStack.EMPTY);

      ListTag list = tag.getList("Items", 10);
      for (int i = 0; i < list.size(); i++) {
         CompoundTag entry = list.getCompound(i);
         int slot = entry.getInt("Slot");
         while (this.items.size() <= slot) this.items.add(ItemStack.EMPTY);
         this.items.set(slot, ItemStack.of(entry));
      }
   }
}
