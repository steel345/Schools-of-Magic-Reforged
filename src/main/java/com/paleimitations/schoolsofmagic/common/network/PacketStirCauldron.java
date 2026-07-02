package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityCauldron;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public class PacketStirCauldron {
   private BlockPos pos;
   private int stir;

   public PacketStirCauldron(int stir, BlockPos pos) {
      this.pos = pos;
      this.stir = stir;
   }

   public PacketStirCauldron(FriendlyByteBuf buf) {
      this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
      this.stir = buf.readInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.pos.getX());
      buf.writeInt(this.pos.getY());
      buf.writeInt(this.pos.getZ());
      buf.writeInt(this.stir);
   }

   public static void handle(PacketStirCauldron msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null) {
            return;
         }
         BlockEntity te = sender.serverLevel().getBlockEntity(msg.pos);
         if (te instanceof TileEntityCauldron) {
            TileEntityCauldron cauldron = (TileEntityCauldron)te;
            cauldron.setStirCounter(msg.stir);
            cauldron.setCounter(0);
         }
      });
      context.setPacketHandled(true);
   }
}
