package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketOpenLecternPage {
   private final BlockPos pos;
   private final ItemStack stack;

   public PacketOpenLecternPage(BlockPos pos, ItemStack stack) {
      this.pos = pos;
      this.stack = stack;
   }

   public PacketOpenLecternPage(FriendlyByteBuf buf) {
      this.pos = buf.readBlockPos();
      this.stack = buf.readItem();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeBlockPos(this.pos);
      buf.writeItem(this.stack);
   }

   public static void handle(PacketOpenLecternPage msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
         if (msg.stack.isEmpty()) return;
         com.paleimitations.schoolsofmagic.common.items.ItemPageBase.ensurePage(msg.stack);
         net.minecraft.client.Minecraft.getInstance().setScreen(
            new com.paleimitations.schoolsofmagic.client.guis.GuiLecternPage(msg.stack, msg.pos));
      }));
      context.setPacketHandled(true);
   }
}
