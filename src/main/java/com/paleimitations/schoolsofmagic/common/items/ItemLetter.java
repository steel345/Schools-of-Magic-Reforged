package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemLetter extends Item {
   public ItemLetter(Item.Properties props) {
      super(props);
   }

   public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
      ItemStack stack = playerIn.getItemInHand(handIn);
      SchoolsOfMagic.proxy.openLetter(playerIn);
      playerIn.playSound(SOMSoundHandler.PAGE_FLIP.get(), 0.1F, 1.0F);
      return InteractionResultHolder.success(stack);
   }
}
