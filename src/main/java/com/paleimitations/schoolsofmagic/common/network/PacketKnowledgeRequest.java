package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.handlers.KnowledgeGather;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.function.Supplier;

public class PacketKnowledgeRequest {
   private final BlockPos origin;

   public PacketKnowledgeRequest(BlockPos origin) {
      this.origin = origin;
   }

   public PacketKnowledgeRequest(FriendlyByteBuf buf) {
      this.origin = buf.readBlockPos();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeBlockPos(this.origin);
   }

   public static void handle(PacketKnowledgeRequest msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null) return;
         List<KnowledgeGather.Found> found = KnowledgeGather.gather(sender.serverLevel(), msg.origin);
         PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> sender),
            new PacketKnowledgeResponse(found));
      });
      context.setPacketHandled(true);
   }
}
