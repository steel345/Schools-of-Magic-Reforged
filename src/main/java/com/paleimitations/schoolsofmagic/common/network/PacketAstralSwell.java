package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketAstralSwell {
   private final int number;

   public PacketAstralSwell(int number) {
      this.number = number;
   }

   public PacketAstralSwell(FriendlyByteBuf buf) {
      this.number = buf.readInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.number);
   }

   public static void handle(PacketAstralSwell msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
         () -> () -> com.paleimitations.schoolsofmagic.client.AstralSwell.show(msg.number)));
      context.setPacketHandled(true);
   }
}
