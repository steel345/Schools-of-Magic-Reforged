package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public final class BlockInventoryDrops {
   private BlockInventoryDrops() {}

   public static void drop(Level level, BlockPos pos) {
      if (level == null || level.isClientSide) {
         return;
      }
      BlockEntity be = level.getBlockEntity(pos);
      if (be == null) {
         return;
      }
      be.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
         for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (!stack.isEmpty()) {
               Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
            }
         }
      });
   }
}
