package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BrewItemUtil {

   public static void giveFlask(Player player) {
      ItemStack flask = new ItemStack(ItemRegistry.bottle_empty.get());
      if (!player.getInventory().add(flask)) {
         player.drop(flask, false);
      }
   }

   public static void breakToFlask(LivingEntity entity, InteractionHand hand) {
      entity.broadcastBreakEvent(hand);
      if (entity instanceof Player player) {
         giveFlask(player);
      }
   }
}
