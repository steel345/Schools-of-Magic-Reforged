package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.handlers.RingCastHandler;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;

public class PacketRingChannel {
   public PacketRingChannel() {}

   public PacketRingChannel(FriendlyByteBuf buf) {}

   public void encode(FriendlyByteBuf buf) {}

   public static void handle(PacketRingChannel msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer sp = ctx.get().getSender();
         if (sp == null) return;
         if (RingCastHandler.isRingChanneling(sp)) {
            RingCastHandler.keepChannel(sp);
         } else {
            RingCastHandler.castFromRing(sp, InteractionHand.MAIN_HAND);
         }
      });
      ctx.get().setPacketHandled(true);
   }
}
