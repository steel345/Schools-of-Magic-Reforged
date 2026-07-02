package com.paleimitations.schoolsofmagic.common.containers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class BookFrameHandler extends ItemStackHandler {

   public static final int SIZE = 6;
   private final ItemStack book;

   public BookFrameHandler(ItemStack book) {
      super(SIZE);
      this.book = book;
      CompoundTag tag = book.getTag();
      if (tag != null && tag.contains("FrameItems")) {
         this.deserializeNBT(tag.getCompound("FrameItems"));
      }
   }

   @Override
   protected void onContentsChanged(int slot) {
      this.book.getOrCreateTag().put("FrameItems", this.serializeNBT());
   }
}
