package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.EntityPhoenix;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

public class PacketPhoenixFlight {
   private final int entityId;
   private final boolean up;
   private final boolean down;
   private final boolean sprint;

   public PacketPhoenixFlight(int entityId, boolean up, boolean down, boolean sprint) {
      this.entityId = entityId;
      this.up = up;
      this.down = down;
      this.sprint = sprint;
   }

   public PacketPhoenixFlight(FriendlyByteBuf buf) {
      this.entityId = buf.readInt();
      this.up = buf.readBoolean();
      this.down = buf.readBoolean();
      this.sprint = buf.readBoolean();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.entityId);
      buf.writeBoolean(this.up);
      buf.writeBoolean(this.down);
      buf.writeBoolean(this.sprint);
   }

   public static void handle(PacketPhoenixFlight msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null) return;
         Entity e = sender.serverLevel().getEntity(msg.entityId);
         if (e instanceof EntityPhoenix phoenix && phoenix.getControllingPassenger() == sender) {
            phoenix.flyUp = msg.up;
            phoenix.flyDown = msg.down;
            phoenix.flySprint = msg.sprint;
         }
      });
      context.setPacketHandled(true);
   }
}
