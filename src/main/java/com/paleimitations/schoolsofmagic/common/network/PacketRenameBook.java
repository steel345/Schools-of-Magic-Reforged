package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public class PacketRenameBook {
   private static final int MAX_LENGTH = 48;

   private final BlockPos pos;
   private final String name;

   public PacketRenameBook(BlockPos pos, String name) {
      this.pos = pos;
      this.name = name;
   }

   public PacketRenameBook(FriendlyByteBuf buf) {
      this.pos = buf.readBlockPos();
      this.name = buf.readUtf(MAX_LENGTH);
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeBlockPos(this.pos);
      buf.writeUtf(this.name, MAX_LENGTH);
   }

   public static void handle(PacketRenameBook msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null || !sender.level().isLoaded(msg.pos)) {
            return;
         }
         BlockEntity te = sender.serverLevel().getBlockEntity(msg.pos);
         if (!(te instanceof TileEntityPodium podium)) {
            return;
         }
         ItemStack book = podium.handler.getStackInSlot(0);
         if (book.isEmpty()) {
            return;
         }
         ItemStack ink = podium.handler.getStackInSlot(1);
         if (!ink.is(Items.INK_SAC) && !ink.is(Items.BLACK_DYE)) {
            return;
         }
         String trimmed = msg.name.trim();
         if (trimmed.isEmpty()) {
            book.resetHoverName();
         } else {
            book.setHoverName(Component.literal(trimmed)
               .withStyle(s -> s.withItalic(false)));
         }
         ink.shrink(1);
         podium.sendUpdates();
      });
      context.setPacketHandled(true);
   }
}
