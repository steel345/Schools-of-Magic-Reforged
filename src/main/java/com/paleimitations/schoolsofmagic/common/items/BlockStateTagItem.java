package com.paleimitations.schoolsofmagic.common.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;

public class BlockStateTagItem extends BlockItem {
   public BlockStateTagItem(Block block, Properties props) {
      super(block, props);
   }

   @Override
   public Component getName(ItemStack stack) {

      if (stack.hasCustomHoverName()) {
         return super.getName(stack);
      }
      CompoundTag tag = stack.getTag();
      if (tag != null && tag.contains("BlockStateTag")) {
         CompoundTag bs = tag.getCompound("BlockStateTag");
         String variant = bs.getString("type");
         if (variant.isEmpty()) variant = bs.getString("variant");
         if (!variant.isEmpty()) {
            return Component.translatable(this.getDescriptionId() + "." + variant);
         }
      }
      return super.getName(stack);
   }
}
