package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketLecternPage {
   private final BlockPos pos;
   private final ItemStack stack;

   public PacketLecternPage(BlockPos pos, ItemStack stack) {
      this.pos = pos;
      this.stack = stack;
   }

   public PacketLecternPage(FriendlyByteBuf buf) {
      this.pos = buf.readBlockPos();
      this.stack = buf.readItem();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeBlockPos(this.pos);
      buf.writeItem(this.stack);
   }

   public static void handle(PacketLecternPage msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
         if (!msg.stack.isEmpty()) {
            com.paleimitations.schoolsofmagic.common.items.ItemPageBase.ensurePage(msg.stack);
         }
         com.paleimitations.schoolsofmagic.client.LecternPageCache.set(msg.pos, msg.stack);
      }));
      context.setPacketHandled(true);
   }
}
