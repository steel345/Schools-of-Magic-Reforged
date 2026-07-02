package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public class PacketTurnPage {
   private BlockPos pos;
   private int page;
   private int subpage;

   public PacketTurnPage(int page, int subpage, BlockPos pos) {
      this.pos = pos;
      this.page = page;
      this.subpage = subpage;
   }

   public PacketTurnPage(FriendlyByteBuf buf) {
      this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
      this.page = buf.readInt();
      this.subpage = buf.readInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.pos.getX());
      buf.writeInt(this.pos.getY());
      buf.writeInt(this.pos.getZ());
      buf.writeInt(this.page);
      buf.writeInt(this.subpage);
   }

   public static void handle(PacketTurnPage msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null) {
            return;
         }
         BlockEntity te = sender.serverLevel().getBlockEntity(msg.pos);
         if (te instanceof TileEntityPodium) {
            TileEntityPodium podium = (TileEntityPodium)te;
            podium.setPage(msg.page);
            podium.setSubPage(msg.subpage);
            podium.getLevel().sendBlockUpdated(msg.pos, podium.getLevel().getBlockState(msg.pos), podium.getLevel().getBlockState(msg.pos), 2);
         }
      });
      context.setPacketHandled(true);
   }
}
