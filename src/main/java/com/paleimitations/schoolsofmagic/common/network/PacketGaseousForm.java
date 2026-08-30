package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketGaseousForm {
   private final int entityId;
   private final int ticks;
   private final int max;

   public PacketGaseousForm(int entityId, int ticks, int max) {
      this.entityId = entityId;
      this.ticks = ticks;
      this.max = max;
   }

   public PacketGaseousForm(FriendlyByteBuf buf) {
      this.entityId = buf.readInt();
      this.ticks = buf.readInt();
      this.max = buf.readInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.entityId);
      buf.writeInt(this.ticks);
      buf.writeInt(this.max);
   }

   public static void handle(PacketGaseousForm msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
         com.paleimitations.schoolsofmagic.client.GaseousFormClient.apply(msg.entityId, msg.ticks, msg.max)));
      context.setPacketHandled(true);
   }
}
