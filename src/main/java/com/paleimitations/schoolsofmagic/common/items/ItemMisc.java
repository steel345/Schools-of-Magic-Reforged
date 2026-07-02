package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ItemMisc extends Item implements IMiscType {
   public ItemMisc(Item.Properties props) {
      super(props);
   }

   @Override
   public Component getName(ItemStack stack) {
      int meta = Math.max(0, Math.min(stack.getDamageValue(), this.getDamage() - 1));
      return Component.translatable(this.getDescriptionId() + "_" + this.handleMeta(meta));
   }

   public InteractionResult useOn(UseOnContext context) {
      Level worldIn = context.getLevel();
      BlockPos pos = context.getClickedPos();
      ItemStack stack = context.getItemInHand();

      BlockPos target = pos.relative(context.getClickedFace());
      if (stack.getItem() == ItemRegistry.item.get()
         && stack.getDamageValue() == com.paleimitations.schoolsofmagic.common.blocks.EnumMisc.HYDRANGEA_FLOWERS.getIndex()
         && BlockRegistry.hydrangea.get().defaultBlockState().canSurvive(worldIn, target)) {
         worldIn.setBlockAndUpdate(target, BlockRegistry.hydrangea.get().defaultBlockState());
         if (context.getPlayer() != null && !context.getPlayer().isCreative()) {
            stack.shrink(1);
         }
      }

      return super.useOn(context);
   }
}
