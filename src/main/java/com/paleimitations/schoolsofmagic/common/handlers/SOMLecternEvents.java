package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.page.CapabilityPage;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketLecternPage;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.ChunkWatchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class SOMLecternEvents {

   private static boolean isPage(ItemStack stack) {
      com.paleimitations.schoolsofmagic.common.items.capabilities.page.IPage p =
         stack.getCapability(CapabilityPage.PAGE_CAPABILITY).orElse(null);
      return p != null;
   }

   private static boolean hasPageContent(ItemStack stack) {
      com.paleimitations.schoolsofmagic.common.items.capabilities.page.IPage p =
         stack.getCapability(CapabilityPage.PAGE_CAPABILITY).orElse(null);
      return p != null && p.getBookPage() != null;
   }

   private static boolean isAcceptable(ItemStack stack) {
      return isPage(stack);
   }

   // copyWithCount()/copy() can drop the live page capability data, so copy the
   // BookPage across explicitly (and stamp it into NBT so it survives storage/sync).
   private static ItemStack copyWithPage(ItemStack src, int count) {
      ItemStack out = src.copy();
      out.setCount(count);
      com.paleimitations.schoolsofmagic.common.items.capabilities.page.IPage s =
         src.getCapability(CapabilityPage.PAGE_CAPABILITY).orElse(null);
      com.paleimitations.schoolsofmagic.common.items.capabilities.page.IPage d =
         out.getCapability(CapabilityPage.PAGE_CAPABILITY).orElse(null);
      if (s != null && s.getBookPage() != null) {
         if (d != null) d.setBookPage(s.getBookPage());
         out.getOrCreateTag().put("page_data", s.serializeNBT());
      }
      return out;
   }

   private static void syncTo(ServerPlayer player, BlockPos pos, ItemStack stack) {
      PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new PacketLecternPage(pos, stack));
   }

   private static void syncNear(Level level, BlockPos pos, ItemStack stack) {
      if (level instanceof net.minecraft.server.level.ServerLevel sl) {
         PacketHandler.INSTANCE.send(
            PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(
               pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 128.0, sl.dimension())),
            new PacketLecternPage(pos, stack));
      }
   }

   @SubscribeEvent
   public static void onChunkWatch(ChunkWatchEvent.Watch event) {
      LevelChunk chunk = event.getLevel().getChunk(event.getPos().x, event.getPos().z);
      for (java.util.Map.Entry<BlockPos, BlockEntity> entry : chunk.getBlockEntities().entrySet()) {
         if (entry.getValue() instanceof LecternBlockEntity lectern) {
            ItemStack book = lectern.getBook();
            // Always sync (empty for books) so the client cache never keeps a stale page.
            syncTo(event.getPlayer(), entry.getKey(), isAcceptable(book) ? copyWithPage(book, 1) : ItemStack.EMPTY);
         }
      }
   }

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
         boolean isBook = held.getCapability(CapabilityBook.BOOK_CAPABILITY).isPresent();
         boolean acceptable = isAcceptable(held);
         if (held.isEmpty() || (!isBook && !acceptable)) return;
         event.setCanceled(true);
         event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
         if (!level.isClientSide) {
            ItemStack placed = copyWithPage(held, 1);
            LecternBlock.tryPlaceBook(player, level, pos, state, placed);
            if (!player.getAbilities().instabuild) held.shrink(1);
            // Always sync (empty for books) so the client cache is accurate.
            syncNear(level, pos, isAcceptable(placed) ? placed : ItemStack.EMPTY);
         }
         return;
      }

      BlockEntity be = level.getBlockEntity(pos);
      if (!(be instanceof LecternBlockEntity lectern)) return;
      ItemStack book = lectern.getBook();

      // Sneak-right-click removes whatever is on the lectern.
      if (isPage(book) && player.isShiftKeyDown()) {
         event.setCanceled(true);
         event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
         if (!level.isClientSide) {
            if (!player.getInventory().add(copyWithPage(book, book.getCount()))) {
               player.drop(copyWithPage(book, book.getCount()), false);
            }
            lectern.setBook(ItemStack.EMPTY);
            level.setBlock(pos, state.setValue(LecternBlock.HAS_BOOK, false).setValue(LecternBlock.POWERED, false), 3);
            level.updateNeighbourForOutputSignal(pos, state.getBlock());
            syncNear(level, pos, ItemStack.EMPTY);
         }
         return;
      }

      // A grimoire page opens its viewer screen.
      if (isPage(book)) {
         event.setCanceled(true);
         event.setCancellationResult(InteractionResult.SUCCESS);
         if (!level.isClientSide && player instanceof ServerPlayer sp) {
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> sp),
               new com.paleimitations.schoolsofmagic.common.network.PacketOpenLecternPage(pos, copyWithPage(book, 1)));
         }
         return;
      }

      if (!book.getCapability(CapabilityBook.BOOK_CAPABILITY).isPresent()) return;

      event.setCanceled(true);
      event.setCancellationResult(InteractionResult.SUCCESS);

      if (!level.isClientSide && player instanceof ServerPlayer sp) {
         ItemStack copy = book.copy();
         com.paleimitations.schoolsofmagic.common.items.ItemBookBase.ensureInitialized(copy);
         com.paleimitations.schoolsofmagic.common.items.ItemBookBase.refreshIfPristine(copy);
         PacketHandler.INSTANCE.send(
            PacketDistributor.PLAYER.with(() -> sp),
            new com.paleimitations.schoolsofmagic.common.network.PacketOpenLecternBook(pos, copy));
      }
   }
}
