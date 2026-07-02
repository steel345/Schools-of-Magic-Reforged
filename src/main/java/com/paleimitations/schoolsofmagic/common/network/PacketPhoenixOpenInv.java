package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.EntityPhoenix;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

public class PacketPhoenixOpenInv {
   private final int entityId;

   public PacketPhoenixOpenInv(int entityId) {
      this.entityId = entityId;
   }

   public PacketPhoenixOpenInv(FriendlyByteBuf buf) {
      this.entityId = buf.readInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.entityId);
   }

   public static void handle(PacketPhoenixOpenInv msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null) return;
         Entity e = sender.serverLevel().getEntity(msg.entityId);
         if (e instanceof EntityPhoenix phoenix && phoenix.isTame() && phoenix.isOwnedBy(sender)) {
            phoenix.openInventory(sender);
         }
      });
      context.setPacketHandled(true);
   }
}
