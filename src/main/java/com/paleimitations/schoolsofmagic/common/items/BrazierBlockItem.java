package com.paleimitations.schoolsofmagic.common.items;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class BrazierBlockItem extends BlockItem {
   public BrazierBlockItem(Block block, Properties props) {
      super(block, props);
   }

   @Override
   public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
      String owner = "None";
      CompoundTag tag = stack.getTag();
      if (tag != null && tag.contains("playerOwnerName") && !tag.getString("playerOwnerName").isEmpty()) {
         owner = tag.getString("playerOwnerName");
      }
      tooltip.add(Component.literal("Owner: " + owner).withStyle(ChatFormatting.GRAY));
      super.appendHoverText(stack, level, tooltip, flag);
   }
}
