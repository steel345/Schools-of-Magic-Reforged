package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.handlers.RingCastHandler;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class PacketRingBind {
   public PacketRingBind() {}

   public PacketRingBind(FriendlyByteBuf buf) {}

   public void encode(FriendlyByteBuf buf) {}

   public static void handle(PacketRingBind msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer sp = ctx.get().getSender();
         if (sp != null) RingCastHandler.tryBind(sp);
      });
      ctx.get().setPacketHandled(true);
   }
}
