package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

public class PacketTakeLecternBook {
   private final BlockPos pos;

   public PacketTakeLecternBook(BlockPos pos) {
      this.pos = pos;
   }

   public PacketTakeLecternBook(FriendlyByteBuf buf) {
      this.pos = buf.readBlockPos();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeBlockPos(this.pos);
   }

   public static void handle(PacketTakeLecternBook msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null) return;
         Level level = sender.serverLevel();
         if (!level.isLoaded(msg.pos)) return;
         BlockState state = level.getBlockState(msg.pos);
         if (!(state.getBlock() instanceof LecternBlock)) return;
         if (!state.getValue(LecternBlock.HAS_BOOK)) return;
         BlockEntity be = level.getBlockEntity(msg.pos);
         if (!(be instanceof LecternBlockEntity lectern)) return;
         ItemStack book = lectern.getBook().copy();
         if (book.isEmpty()) return;
         lectern.clearContent();
         level.setBlock(msg.pos, state.setValue(LecternBlock.HAS_BOOK, Boolean.valueOf(false)), 3);
         level.updateNeighbourForOutputSignal(msg.pos, state.getBlock());
         if (!sender.getInventory().add(book)) sender.drop(book, false);
      });
      context.setPacketHandled(true);
   }
}
