package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityCauldron;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public class PacketLidCauldron {
   private BlockPos pos;
   private boolean lid;

   public PacketLidCauldron(boolean lid, BlockPos pos) {
      this.pos = pos;
      this.lid = lid;
   }

   public PacketLidCauldron(FriendlyByteBuf buf) {
      this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
      this.lid = buf.readBoolean();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.pos.getX());
      buf.writeInt(this.pos.getY());
      buf.writeInt(this.pos.getZ());
      buf.writeBoolean(this.lid);
   }

   public static void handle(PacketLidCauldron msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null) {
            return;
         }
         BlockEntity te = sender.serverLevel().getBlockEntity(msg.pos);
         if (te instanceof TileEntityCauldron) {
            TileEntityCauldron cauldron = (TileEntityCauldron)te;
            cauldron.setLidded(msg.lid);
         }
      });
      context.setPacketHandled(true);
   }
}
