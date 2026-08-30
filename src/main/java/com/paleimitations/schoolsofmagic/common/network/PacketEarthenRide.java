package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketEarthenRide {
   private final boolean active;
   private final int ticks;
   private final int max;

   public PacketEarthenRide(boolean active, int ticks, int max) {
      this.active = active;
      this.ticks = ticks;
      this.max = max;
   }

   public PacketEarthenRide(FriendlyByteBuf buf) {
      this.active = buf.readBoolean();
      this.ticks = buf.readInt();
      this.max = buf.readInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeBoolean(this.active);
      buf.writeInt(this.ticks);
      buf.writeInt(this.max);
   }

   public static void handle(PacketEarthenRide msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
         com.paleimitations.schoolsofmagic.client.EarthenRideSound.setActive(msg.active, msg.ticks, msg.max)));
      context.setPacketHandled(true);
   }
}
