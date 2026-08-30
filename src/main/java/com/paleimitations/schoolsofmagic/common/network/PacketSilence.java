package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketSilence {
   private final int who;
   private final boolean on;

   public PacketSilence(int who, boolean on) {
      this.who = who;
      this.on = on;
   }

   public PacketSilence(FriendlyByteBuf buf) {
      this.who = buf.readInt();
      this.on = buf.readBoolean();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.who);
      buf.writeBoolean(this.on);
   }

   public static void handle(PacketSilence msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
         () -> () -> com.paleimitations.schoolsofmagic.client.SilenceClient.set(msg.who, msg.on)));
      context.setPacketHandled(true);
   }
}
