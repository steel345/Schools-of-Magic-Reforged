package com.paleimitations.schoolsofmagic.common.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class CauldronBlockItem extends BlockItem {
   public CauldronBlockItem(Block block, Properties props) {
      super(block, props);
   }

   @Override
   public Component getName(ItemStack stack) {
      if (stack.hasCustomHoverName()) {
         return super.getName(stack);
      }
      String variant = "normal";
      CompoundTag tag = stack.getTag();
      if (tag != null && tag.contains("BlockStateTag")) {
         String t = tag.getCompound("BlockStateTag").getString("type");
         if (!t.isEmpty()) variant = t;
      }
      return Component.translatable(this.getDescriptionId() + "." + variant);
   }
}
