package com.paleimitations.schoolsofmagic.common.items;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public interface ICreativeTabFiller {
   void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items);
}
