package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.containers.ContainerRift;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class PacketRiftView {
   private final String query;
   private final int scroll;

   public PacketRiftView(String query, int scroll) {
      this.query = query == null ? "" : query;
      this.scroll = scroll;
   }

   public PacketRiftView(FriendlyByteBuf buf) {
      this.query = buf.readUtf(64);
      this.scroll = buf.readInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeUtf(this.query, 64);
      buf.writeInt(this.scroll);
   }

   public static void handle(PacketRiftView msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer player = context.getSender();
         if (player == null) return;
         if (player.containerMenu instanceof ContainerRift rift) {
            rift.search(msg.query, msg.scroll);
         }
      });
      context.setPacketHandled(true);
   }
}
