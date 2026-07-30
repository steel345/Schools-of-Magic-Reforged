package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.handlers.KnowledgeGather;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

// Server -> Client: the books gathered near the podium/lectern (with their source
// shelf position + slot), for the client to search.
public class PacketKnowledgeResponse {

   private final List<KnowledgeGather.Found> found;

   public PacketKnowledgeResponse(List<KnowledgeGather.Found> found) {
      this.found = found;
   }

   public PacketKnowledgeResponse(FriendlyByteBuf buf) {
      int n = buf.readVarInt();
      this.found = new ArrayList<>();
      for (int i = 0; i < n; i++) {
         ItemStack stack = buf.readItem();
         BlockPos shelf = buf.readBlockPos();
         int slot = buf.readVarInt();
         this.found.add(new KnowledgeGather.Found(stack, shelf, slot));
      }
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeVarInt(this.found.size());
      for (KnowledgeGather.Found f : this.found) {
         buf.writeItem(f.stack);
         buf.writeBlockPos(f.shelf);
         buf.writeVarInt(f.slot);
      }
   }

   public static void handle(PacketKnowledgeResponse msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
         () -> () -> com.paleimitations.schoolsofmagic.client.KnowledgeSearchClient.onCandidates(msg.found)));
      context.setPacketHandled(true);
   }
}
