package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketTeleportPuff {
   private final double x;
   private final double y;
   private final double z;
   private final double tx;
   private final double ty;
   private final double tz;

   public PacketTeleportPuff(double x, double y, double z, double tx, double ty, double tz) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.tx = tx;
      this.ty = ty;
      this.tz = tz;
   }

   public PacketTeleportPuff(FriendlyByteBuf buf) {
      this.x = buf.readDouble();
      this.y = buf.readDouble();
      this.z = buf.readDouble();
      this.tx = buf.readDouble();
      this.ty = buf.readDouble();
      this.tz = buf.readDouble();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeDouble(this.x);
      buf.writeDouble(this.y);
      buf.writeDouble(this.z);
      buf.writeDouble(this.tx);
      buf.writeDouble(this.ty);
      buf.writeDouble(this.tz);
   }

   public static void handle(PacketTeleportPuff msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
         com.paleimitations.schoolsofmagic.client.TeleportPuffs.spawn(msg.x, msg.y, msg.z, msg.tx, msg.ty, msg.tz)));
      context.setPacketHandled(true);
   }
}
