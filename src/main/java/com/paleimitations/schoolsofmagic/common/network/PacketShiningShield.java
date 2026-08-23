package com.paleimitations.schoolsofmagic.common.network;

import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketShiningShield {
   private final UUID player;
   private final int shields;

   public PacketShiningShield(UUID player, int shields) {
      this.player = player;
      this.shields = shields;
   }

   public PacketShiningShield(FriendlyByteBuf buf) {
      this.player = buf.readUUID();
      this.shields = buf.readVarInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeUUID(this.player);
      buf.writeVarInt(this.shields);
   }

   public static void handle(PacketShiningShield msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
         com.paleimitations.schoolsofmagic.client.ClientShiningShields.set(msg.player, msg.shields)));
      context.setPacketHandled(true);
   }
}
