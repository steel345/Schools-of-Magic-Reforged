package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;

import com.paleimitations.schoolsofmagic.common.books.PageUnlocks;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class PacketMarkPageRead {
   private final String key;

   public PacketMarkPageRead(String key) {
      this.key = key;
   }

   public PacketMarkPageRead(FriendlyByteBuf buf) {
      this.key = buf.readUtf();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeUtf(this.key);
   }

   public static void handle(PacketMarkPageRead msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer sp = ctx.get().getSender();
         if (sp == null) return;
         PageUnlocks.markRead(sp, msg.key);
      });
      ctx.get().setPacketHandled(true);
   }
}
