package com.paleimitations.schoolsofmagic.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketLecternFloat {
   private final BlockPos pos;
   private final ItemStack book;

   public PacketLecternFloat(BlockPos pos, ItemStack book) {
      this.pos = pos;
      this.book = book;
   }

   public PacketLecternFloat(FriendlyByteBuf buf) {
      this.pos = buf.readBlockPos();
      this.book = buf.readItem();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeBlockPos(this.pos);
      buf.writeItem(this.book);
   }

   public static void handle(PacketLecternFloat msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
         () -> () -> com.paleimitations.schoolsofmagic.client.LecternKnowledgeCache.set(msg.pos, msg.book)));
      context.setPacketHandled(true);
   }
}
