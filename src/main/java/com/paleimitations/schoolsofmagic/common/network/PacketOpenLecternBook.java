package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketOpenLecternBook {
   private final BlockPos pos;
   private final ItemStack book;

   public PacketOpenLecternBook(BlockPos pos, ItemStack book) {
      this.pos = pos;
      this.book = book;
   }

   public PacketOpenLecternBook(FriendlyByteBuf buf) {
      this.pos = buf.readBlockPos();
      this.book = buf.readItem();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeBlockPos(this.pos);
      buf.writeItem(this.book);
   }

   public static void handle(PacketOpenLecternBook msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
         com.paleimitations.schoolsofmagic.client.ClientLecternBook.open(msg.book, msg.pos)));
      context.setPacketHandled(true);
   }
}
