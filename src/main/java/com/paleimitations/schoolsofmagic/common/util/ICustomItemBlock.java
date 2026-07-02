package com.paleimitations.schoolsofmagic.common.util;

import javax.annotation.Nullable;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ICustomItemBlock {
   @Nullable
   default BlockItem getItemBlock() {
      return getDefaultItemBlock((Block) this);
   }

   static BlockItem getDefaultItemBlock(Block block) {
      if (block.asItem() != Items.AIR) {
         return (BlockItem) block.asItem();
      }
      return new BlockItem(block, new Item.Properties());
   }

   @OnlyIn(Dist.CLIENT)
   default ItemStack getRenderedItem() {
      return ItemStack.EMPTY;
   }
}
