package com.paleimitations.schoolsofmagic.common.items;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;

public class CreativeMaker extends Item {
   public CreativeMaker(Item.Properties props) {
      super(props);
   }

   public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
      ItemStack itemstack = playerIn.getItemInHand(handIn);
      if (playerIn instanceof ServerPlayer) {
         ((ServerPlayer)playerIn).setGameMode(GameType.CREATIVE);
      }

      return InteractionResultHolder.success(itemstack);
   }
}
