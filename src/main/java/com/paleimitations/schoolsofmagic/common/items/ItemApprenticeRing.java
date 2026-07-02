package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.CapabilityRingData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.IRingData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemApprenticeRing extends Item {
   public ItemApprenticeRing(Item.Properties props) {
      super(props);
   }

   @Override
   public void appendHoverText(ItemStack stack, Level level, java.util.List<net.minecraft.network.chat.Component> tooltip,
         net.minecraft.world.item.TooltipFlag flag) {
      com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumGemType gem =
         RingItemHelper.getGem(stack);
      if (gem != null) {
         net.minecraft.network.chat.Component buff =
            com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandGemBuff.buffTooltip(gem);
         if (buff != null) tooltip.add(buff);
      }
      net.minecraft.network.chat.Component perk =
         com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandMetalPerk.perkTooltip(
            com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandMetalPerk.readMetal(stack));
      if (perk != null) tooltip.add(perk);
      super.appendHoverText(stack, level, tooltip, flag);
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack held = player.getItemInHand(hand);
      if (hand != InteractionHand.MAIN_HAND) {
         return InteractionResultHolder.pass(held);
      }
      if (!level.isClientSide) {
         IRingData ring = CapabilityRingData.get(player);
         if (ring != null && ring.getRing().isEmpty()) {
            ItemStack one = held.copy();
            one.setCount(1);
            ring.setRing(one);
            held.shrink(1);
            if (player instanceof ServerPlayer sp) CapabilityRingData.sync(sp);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
               net.minecraft.sounds.SoundEvents.ARMOR_EQUIP_LEATHER, net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);
            return InteractionResultHolder.success(held);
         }
      }
      return InteractionResultHolder.sidedSuccess(held, level.isClientSide);
   }
}
