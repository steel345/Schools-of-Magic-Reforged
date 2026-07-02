package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.items.ItemSpellbook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class PacketSetBookPage {
   private final int page;
   private final int subpage;

   public PacketSetBookPage(int page, int subpage) {
      this.page = page;
      this.subpage = subpage;
   }

   public PacketSetBookPage(FriendlyByteBuf buf) {
      this.page = buf.readInt();
      this.subpage = buf.readInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.page);
      buf.writeInt(this.subpage);
   }

   public static void handle(PacketSetBookPage msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer sp = ctx.get().getSender();
         if (sp == null) {
            return;
         }
         ItemStack held = sp.getMainHandItem();
         if (!(held.getItem() instanceof ItemSpellbook)) {
            held = sp.getOffhandItem();
         }
         if (!(held.getItem() instanceof ItemSpellbook)) {
            return;
         }
         IBook book = CapabilityBook.getCapability(held);
         if (book != null) {
            book.setPage(msg.page);
            book.setSubPage(msg.subpage);
         }
      });
      ctx.get().setPacketHandled(true);
   }
}
