package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class SOMLecternEvents {

   @SubscribeEvent
   public static void onRightClickLectern(PlayerInteractEvent.RightClickBlock event) {
      Level level = event.getLevel();
      BlockPos pos = event.getPos();
      BlockState state = level.getBlockState(pos);
      if (!(state.getBlock() instanceof LecternBlock)) return;
      Player player = event.getEntity();
      ItemStack held = event.getItemStack();

      if (!state.getValue(LecternBlock.HAS_BOOK)) {
         if (player.isShiftKeyDown()) return;
         if (held.isEmpty() || !held.getCapability(CapabilityBook.BOOK_CAPABILITY).isPresent()) return;
         event.setCanceled(true);
         event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
         if (!level.isClientSide) {
            LecternBlock.tryPlaceBook(player, level, pos, state, held.copyWithCount(1));
            if (!player.getAbilities().instabuild) held.shrink(1);
         }
         return;
      }

      BlockEntity be = level.getBlockEntity(pos);
      if (!(be instanceof LecternBlockEntity lectern)) return;
      ItemStack book = lectern.getBook();
      if (!book.getCapability(CapabilityBook.BOOK_CAPABILITY).isPresent()) return;

      event.setCanceled(true);
      event.setCancellationResult(InteractionResult.SUCCESS);

      if (!level.isClientSide && player instanceof net.minecraft.server.level.ServerPlayer sp) {
         ItemStack copy = book.copy();
         com.paleimitations.schoolsofmagic.common.items.ItemBookBase.ensureInitialized(copy);
         com.paleimitations.schoolsofmagic.common.items.ItemBookBase.refreshIfPristine(copy);
         com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.send(
            net.minecraftforge.network.PacketDistributor.PLAYER.with(() -> sp),
            new com.paleimitations.schoolsofmagic.common.network.PacketOpenLecternBook(pos, copy));
      }
   }
}
