package com.paleimitations.schoolsofmagic.client.guis;

import com.paleimitations.schoolsofmagic.common.items.ItemPageBase;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

public final class LecternPageOpener {
   private LecternPageOpener() {
   }

   public static void open(BlockPos pos, ItemStack stack) {
      if (stack.isEmpty()) return;
      ItemPageBase.ensurePage(stack);
      Minecraft.getInstance().setScreen(new GuiLecternPage(stack, pos));
   }
}
