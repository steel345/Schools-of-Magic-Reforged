package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.EntityAlarmRune;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class PacketAlarmSound {
   private final int rune;

   public PacketAlarmSound(int rune) {
      this.rune = rune;
   }

   public PacketAlarmSound(FriendlyByteBuf buf) {
      this.rune = buf.readInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.rune);
   }

   public static void handle(PacketAlarmSound msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer player = context.getSender();
         if (player == null) return;
         // the server checks who owns it, the button only asks
         if (player.level().getEntity(msg.rune) instanceof EntityAlarmRune rune) rune.sound(player);
      });
      context.setPacketHandled(true);
   }
}
