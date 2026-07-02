package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data.CapabilityTalismanData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data.ITalismanData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemTalismanOfKnowledge extends Item {
   public ItemTalismanOfKnowledge(Item.Properties props) {
      super(props);
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack held = player.getItemInHand(hand);
      if (hand != InteractionHand.MAIN_HAND) {
         return InteractionResultHolder.pass(held);
      }
      if (!level.isClientSide) {
         ITalismanData talisman = CapabilityTalismanData.get(player);
         if (talisman != null && talisman.getTalisman().isEmpty()) {
            ItemStack one = held.copy();
            one.setCount(1);
            talisman.setTalisman(one);
            held.shrink(1);
            if (player instanceof ServerPlayer sp) CapabilityTalismanData.sync(sp);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
               net.minecraft.sounds.SoundEvents.ARMOR_EQUIP_LEATHER, net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);
            return InteractionResultHolder.success(held);
         }
      }
      return InteractionResultHolder.sidedSuccess(held, level.isClientSide);
   }
}
