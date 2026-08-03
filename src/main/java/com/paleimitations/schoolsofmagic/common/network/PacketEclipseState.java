package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

// Tells clients how far the eclipse has progressed, so the sky can be drawn to match.
public class PacketEclipseState {
   private final boolean running;
   private final int stage;
   private final int elapsed;

   public PacketEclipseState(boolean running, int stage, int elapsed) {
      this.running = running;
      this.stage = stage;
      this.elapsed = elapsed;
   }

   public PacketEclipseState(FriendlyByteBuf buf) {
      this.running = buf.readBoolean();
      this.stage = buf.readVarInt();
      this.elapsed = buf.readVarInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeBoolean(this.running);
      buf.writeVarInt(this.stage);
      buf.writeVarInt(this.elapsed);
   }

   public static void handle(PacketEclipseState msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
         com.paleimitations.schoolsofmagic.client.ClientEclipse.set(msg.running, msg.stage, msg.elapsed)));
      context.setPacketHandled(true);
   }
}
