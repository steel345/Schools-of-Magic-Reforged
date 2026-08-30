package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketAlarmAlert {
   private final int rune;
   private final boolean on;

   public PacketAlarmAlert(int rune, boolean on) {
      this.rune = rune;
      this.on = on;
   }

   public PacketAlarmAlert(FriendlyByteBuf buf) {
      this.rune = buf.readInt();
      this.on = buf.readBoolean();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.rune);
      buf.writeBoolean(this.on);
   }

   public static void handle(PacketAlarmAlert msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
         () -> () -> com.paleimitations.schoolsofmagic.SchoolsOfMagic.proxy.openAlarm(msg.rune, msg.on)));
      context.setPacketHandled(true);
   }
}
