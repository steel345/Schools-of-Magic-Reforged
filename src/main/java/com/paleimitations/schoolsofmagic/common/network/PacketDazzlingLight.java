package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketDazzlingLight {
   private final double x;
   private final double y;
   private final double z;

   public PacketDazzlingLight(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public PacketDazzlingLight(FriendlyByteBuf buf) {
      this.x = buf.readDouble();
      this.y = buf.readDouble();
      this.z = buf.readDouble();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeDouble(this.x);
      buf.writeDouble(this.y);
      buf.writeDouble(this.z);
   }

   public static void handle(PacketDazzlingLight msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
         com.paleimitations.schoolsofmagic.client.DazzlingLightShow.start(msg.x, msg.y, msg.z)));
      context.setPacketHandled(true);
   }
}
