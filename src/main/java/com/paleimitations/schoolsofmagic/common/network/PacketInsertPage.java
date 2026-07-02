package com.paleimitations.schoolsofmagic.common.network;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.books.BookElementSticker;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public class PacketInsertPage {
   private BlockPos pos;
   private int page;
   private String pageName;
   private boolean addBefore;

   public PacketInsertPage(int page, String pageName, BlockPos pos) {
      this.pos = pos;
      this.page = page;
      this.pageName = pageName;
      this.addBefore = false;
   }

   public PacketInsertPage(int page, String pageName, BlockPos pos, boolean addBefore) {
      this.pos = pos;
      this.page = page;
      this.pageName = pageName;
      this.addBefore = true;
   }

   public PacketInsertPage(FriendlyByteBuf buf) {
      this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
      this.page = buf.readInt();
      this.addBefore = buf.readBoolean();
      this.pageName = buf.readUtf();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.pos.getX());
      buf.writeInt(this.pos.getY());
      buf.writeInt(this.pos.getZ());
      buf.writeInt(this.page);
      buf.writeBoolean(this.addBefore);
      buf.writeUtf(this.pageName);
   }

   public static void handle(PacketInsertPage msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null) {
            return;
         }
         BlockEntity te = sender.serverLevel().getBlockEntity(msg.pos);
         if (te instanceof TileEntityPodium) {
            TileEntityPodium podium = (TileEntityPodium)te;
            IBook book = podium.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
            if (book != null) {
               List<BookPage> pages = Lists.newArrayList();
               List<BookElementSticker> stickers = Lists.newArrayList();
               int page = 0;

               for (int i = 0; i < book.getBookPages().size(); i++) {
                  if (!msg.addBefore) {
                     pages.add(book.getBookPages().get(i));

                     for (BookElementSticker sticker : book.getStickers()) {
                        if (sticker.page == i) {
                           stickers.add(new BookElementSticker(sticker.sticker, sticker.rotation, sticker.x, sticker.y, page, sticker.subpage));
                        }
                     }

                     page++;
                  }

                  if (i == msg.page) {
                     pages.add(BookPageRegistry.getBookPage(msg.pageName));
                     page++;
                  }

                  if (msg.addBefore) {
                     pages.add(book.getBookPages().get(i));

                     for (BookElementSticker stickerx : book.getStickers()) {
                        if (stickerx.page == i) {
                           stickers.add(new BookElementSticker(stickerx.sticker, stickerx.rotation, stickerx.x, stickerx.y, page, stickerx.subpage));
                        }
                     }

                     page++;
                  }
               }

               if (pages.isEmpty()) {
                  pages.add(BookPageRegistry.getBookPage(msg.pageName));
               }

               book.setBookPages(pages);
               book.setStickers(stickers);
               podium.handler.getStackInSlot(5).shrink(1);
               book.setEdited(true);

               int target = Math.min((msg.addBefore ? msg.page : msg.page + 1), pages.size() - 1);
               book.setPage(target);
               book.setSubPage(0);
               podium.sendUpdates();
            }
         }
      });
      context.setPacketHandled(true);
   }
}
