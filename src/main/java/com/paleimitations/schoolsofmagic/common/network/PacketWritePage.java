package com.paleimitations.schoolsofmagic.common.network;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.registries.BookPageRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public class PacketWritePage {
   private static final int MAX_LENGTH = 8192;

   private final BlockPos pos;
   private final int page;
   private final String serialized;

   public PacketWritePage(BlockPos pos, int page, String serialized) {
      this.pos = pos;
      this.page = page;
      this.serialized = serialized;
   }

   public PacketWritePage(FriendlyByteBuf buf) {
      this.pos = buf.readBlockPos();
      this.page = buf.readInt();
      this.serialized = buf.readUtf(MAX_LENGTH);
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeBlockPos(this.pos);
      buf.writeInt(this.page);
      buf.writeUtf(this.serialized, MAX_LENGTH);
   }

   public static void handle(PacketWritePage msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null || !sender.level().isLoaded(msg.pos)) {
            return;
         }
         BlockEntity te = sender.serverLevel().getBlockEntity(msg.pos);
         if (!(te instanceof TileEntityPodium podium)) {
            return;
         }
         IBook book = podium.handler.getStackInSlot(0)
            .getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
         if (book == null) {
            return;
         }
         List<BookPage> pages = Lists.newArrayList(book.getBookPages());
         if (msg.page < 0 || msg.page >= pages.size()) {
            return;
         }
         ItemStack ink = podium.handler.getStackInSlot(1);
         if (!ink.is(Items.INK_SAC) && !ink.is(Items.BLACK_DYE)) {
            return;
         }
         BookPage rewritten = BookPageRegistry.getBookPage(msg.serialized);
         if (rewritten == null) {
            return;
         }
         pages.set(msg.page, rewritten);
         book.setBookPages(pages);
         book.setEdited(true);
         ink.shrink(1);
         podium.sendUpdates();
      });
      context.setPacketHandled(true);
   }
}
