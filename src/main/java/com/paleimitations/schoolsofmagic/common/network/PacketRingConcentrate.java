package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.handlers.RingCastHandler;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class PacketRingConcentrate {
   public PacketRingConcentrate() {}

   public PacketRingConcentrate(FriendlyByteBuf buf) {}

   public void encode(FriendlyByteBuf buf) {}

   public static void handle(PacketRingConcentrate msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer sp = ctx.get().getSender();
         if (sp != null && RingCastHandler.isRingActive(sp)) {
            RingCastHandler.keepConcentration(sp);
         }
      });
      ctx.get().setPacketHandled(true);
   }
}
