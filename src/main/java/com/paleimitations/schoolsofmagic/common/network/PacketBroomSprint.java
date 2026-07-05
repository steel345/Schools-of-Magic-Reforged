package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.EntityMagicBroom;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

public class PacketBroomSprint {
   private final int entityId;
   private final boolean sprint;

   public PacketBroomSprint(int entityId, boolean sprint) {
      this.entityId = entityId;
      this.sprint = sprint;
   }

   public PacketBroomSprint(FriendlyByteBuf buf) {
      this.entityId = buf.readInt();
      this.sprint = buf.readBoolean();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.entityId);
      buf.writeBoolean(this.sprint);
   }

   public static void handle(PacketBroomSprint msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null) {
            return;
         }
         Entity e = sender.serverLevel().getEntity(msg.entityId);
         if (e instanceof EntityMagicBroom broom && broom.getControllingPassenger() == sender) {
            broom.broomSprint = msg.sprint;
         }
      });
      context.setPacketHandled(true);
   }
}
