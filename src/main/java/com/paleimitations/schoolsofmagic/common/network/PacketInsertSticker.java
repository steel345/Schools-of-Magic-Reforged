package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.books.BookElementSticker;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public class PacketInsertSticker {
   private BlockPos pos;
   private int page;
   private int subpage;
   private float x;
   private float y;
   private float rot;
   private String sticker;

   public PacketInsertSticker(int page, int subpage, float x, float y, float rot, String sticker, BlockPos pos) {
      this.pos = pos;
      this.page = page;
      this.subpage = subpage;
      this.x = x;
      this.y = y;
      this.rot = rot;
      this.sticker = sticker;
   }

   public PacketInsertSticker(FriendlyByteBuf buf) {
      this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
      this.page = buf.readInt();
      this.subpage = buf.readInt();
      this.x = buf.readFloat();
      this.y = buf.readFloat();
      this.rot = buf.readFloat();
      this.sticker = buf.readUtf();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.pos.getX());
      buf.writeInt(this.pos.getY());
      buf.writeInt(this.pos.getZ());
      buf.writeInt(this.page);
      buf.writeInt(this.subpage);
      buf.writeFloat(this.x);
      buf.writeFloat(this.y);
      buf.writeFloat(this.rot);
      buf.writeUtf(this.sticker);
   }

   public static void handle(PacketInsertSticker msg, Supplier<NetworkEvent.Context> ctx) {
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
            if (book != null
               && BookElementSticker.EnumSticker.getSticker(msg.sticker) != null
               && podium.handler.getStackInSlot(1).getItem() == ItemRegistry.sticker.get()) {
               BookElementSticker sticker = new BookElementSticker(
                  BookElementSticker.EnumSticker.getSticker(msg.sticker), msg.rot, msg.x, msg.y, msg.page, msg.subpage
               );
               book.getStickers().add(sticker);
               podium.handler.getStackInSlot(1).shrink(1);
               book.setEdited(true);
               podium.sendUpdates();
            }
         }
      });
      context.setPacketHandled(true);
   }
}
