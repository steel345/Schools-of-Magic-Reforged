package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketSunBeam {
   private final double x, y, z;
   private final float r, g, b;
   private final boolean thins;

   public PacketSunBeam(double x, double y, double z, float[] colour, boolean thins) {
      this(x, y, z, colour[0], colour[1], colour[2], thins);
   }

   public PacketSunBeam(double x, double y, double z, float r, float g, float b, boolean thins) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.r = r;
      this.g = g;
      this.b = b;
      this.thins = thins;
   }

   public PacketSunBeam(FriendlyByteBuf buf) {
      this.x = buf.readDouble();
      this.y = buf.readDouble();
      this.z = buf.readDouble();
      this.r = buf.readFloat();
      this.g = buf.readFloat();
      this.b = buf.readFloat();
      this.thins = buf.readBoolean();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeDouble(this.x);
      buf.writeDouble(this.y);
      buf.writeDouble(this.z);
      buf.writeFloat(this.r);
      buf.writeFloat(this.g);
      buf.writeFloat(this.b);
      buf.writeBoolean(this.thins);
   }

   public static void handle(PacketSunBeam msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
         com.paleimitations.schoolsofmagic.client.SunBeamRenderer.add(
            msg.x, msg.y, msg.z, msg.r, msg.g, msg.b, msg.thins)));
      context.setPacketHandled(true);
   }
}
