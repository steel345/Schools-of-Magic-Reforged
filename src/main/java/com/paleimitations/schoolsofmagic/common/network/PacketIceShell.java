package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketIceShell {
   private final boolean active;

   public PacketIceShell(boolean active) {
      this.active = active;
   }

   public PacketIceShell(FriendlyByteBuf buf) {
      this.active = buf.readBoolean();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeBoolean(this.active);
   }

   public static void handle(PacketIceShell msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
         com.paleimitations.schoolsofmagic.client.IceShellClient.setActive(msg.active)));
      context.setPacketHandled(true);
   }
}
