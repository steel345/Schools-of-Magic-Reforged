package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.items.ItemBookBase;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientLecternBook {
   public static void open(ItemStack book, BlockPos pos) {
      LocalPlayer player = Minecraft.getInstance().player;
      if (player == null || book.isEmpty()) return;
      ItemBookBase.ensureInitialized(book);
      ItemBookBase.refreshIfPristine(book);
      SchoolsOfMagic.proxy.openStandardBook(player, book, pos);
   }
}
