package com.paleimitations.schoolsofmagic.common.network;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.books.BookElementSticker;
import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.page.CapabilityPage;
import com.paleimitations.schoolsofmagic.common.items.capabilities.page.IPage;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public class PacketRemovePage {
   private BlockPos pos;
   private int page;

   public PacketRemovePage(int page, BlockPos pos) {
      this.pos = pos;
      this.page = page;
   }

   public PacketRemovePage(FriendlyByteBuf buf) {
      this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
      this.page = buf.readInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.pos.getX());
      buf.writeInt(this.pos.getY());
      buf.writeInt(this.pos.getZ());
      buf.writeInt(this.page);
   }

   public static void handle(PacketRemovePage msg, Supplier<NetworkEvent.Context> ctx) {
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
               ItemStack output = new ItemStack(ItemRegistry.grimoire_page.get());
               IPage page = output.getCapability(CapabilityPage.PAGE_CAPABILITY).orElse(null);
               page.setBookPage(book.getBookPage(msg.page));
               Containers.dropItemStack(
                  podium.getLevel(),
                  (double)podium.getBlockPos().getX() + 0.5,
                  (double)podium.getBlockPos().getY() + 1.0,
                  (double)podium.getBlockPos().getZ() + 0.5,
                  output
               );

               for (BookElementSticker sticker : book.getStickers()) {
                  if (sticker.page == msg.page) {
                     ItemStack stack = new ItemStack(ItemRegistry.sticker.get());
                     CompoundTag nbt = new CompoundTag();
                     nbt.putString("sticker", sticker.sticker.getSerializedName());
                     stack.setTag(nbt);
                     Containers.dropItemStack(
                        podium.getLevel(),
                        (double)podium.getBlockPos().getX() + 0.5,
                        (double)podium.getBlockPos().getY() + 1.0,
                        (double)podium.getBlockPos().getZ() + 0.5,
                        stack
                     );
                  }
               }

               List<BookElementSticker> stickers = Lists.newArrayList();
               int pageN = 0;

               for (int i = 0; i < book.getBookPages().size(); i++) {
                  if (i != msg.page) {
                     pages.add(book.getBookPages().get(i));

                     for (BookElementSticker stickerx : book.getStickers()) {
                        if (stickerx.page == i) {
                           stickers.add(new BookElementSticker(stickerx.sticker, stickerx.rotation, stickerx.x, stickerx.y, pageN, stickerx.subpage));
                        }
                     }

                     pageN++;
                  }
               }

               book.setStickers(stickers);
               book.setBookPages(pages);
               book.setEdited(true);
               podium.sendUpdates();
            }
         }
      });
      context.setPacketHandled(true);
   }
}
