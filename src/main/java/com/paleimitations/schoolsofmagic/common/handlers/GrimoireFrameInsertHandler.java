package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.containers.BookFrameHandler;
import com.paleimitations.schoolsofmagic.common.items.BookDecorations;
import com.paleimitations.schoolsofmagic.common.items.ItemSpellbook;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GrimoireFrameInsertHandler {

   @SubscribeEvent(priority = EventPriority.HIGH)
   public static void onRightClick(PlayerInteractEvent.RightClickItem event) {
      if (event.getHand() != InteractionHand.MAIN_HAND) {
         return;
      }
      Player player = event.getEntity();
      ItemStack off = player.getOffhandItem();
      if (!(off.getItem() instanceof ItemSpellbook) || !BookDecorations.hasFrame(off)) {
         return;
      }
      ItemStack main = event.getItemStack();
      if (main.isEmpty() || main.getItem() instanceof ItemSpellbook) {
         return;
      }
      if (!player.level().isClientSide) {
         BookFrameHandler handler = new BookFrameHandler(off);
         ItemStack remaining = ItemHandlerHelper.insertItemStacked(handler, main.copy(), false);
         player.setItemInHand(InteractionHand.MAIN_HAND, remaining);
         player.level().playSound(null, player.blockPosition(), SoundEvents.BUNDLE_INSERT, SoundSource.PLAYERS, 0.7F, 1.0F);
      }
      event.setCanceled(true);
      event.setCancellationResult(InteractionResult.SUCCESS);
   }
}
