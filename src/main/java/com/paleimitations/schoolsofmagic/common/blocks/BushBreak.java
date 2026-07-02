package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

public final class BushBreak {
   private BushBreak() {}

   public static float progress(Player player) {
      ItemStack tool = player.getMainHandItem();
      if (tool.getItem() instanceof SwordItem && tool.isEnchanted()) {
         return 1.0F;
      }

      return 1.0F / (0.5F * 30.0F);
   }
}
