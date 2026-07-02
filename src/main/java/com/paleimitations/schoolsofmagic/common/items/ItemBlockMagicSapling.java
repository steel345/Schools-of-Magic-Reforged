package com.paleimitations.schoolsofmagic.common.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class ItemBlockMagicSapling extends BlockItem {

   public ItemBlockMagicSapling(Block block, Item.Properties props) {
      super(block, props);
   }

   @Override
   public Component getName(ItemStack stack) {
      CompoundTag tag = stack.getTag();
      if (tag != null && tag.contains("BlockStateTag")) {
         String type = tag.getCompound("BlockStateTag").getString("type");
         if (type != null && !type.isEmpty()) {
            return Component.translatable("block.som.magic_sapling." + type);
         }
      }
      return super.getName(stack);
   }
}
