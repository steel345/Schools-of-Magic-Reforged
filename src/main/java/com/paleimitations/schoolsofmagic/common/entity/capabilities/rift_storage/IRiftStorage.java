package com.paleimitations.schoolsofmagic.common.entity.capabilities.rift_storage;

import java.util.List;
import net.minecraft.world.item.ItemStack;

public interface IRiftStorage {
   int size();

   ItemStack get(int var1);

   void set(int var1, ItemStack var2);

   List<ItemStack> all();

   boolean add(ItemStack stack);

   void trim();
}
