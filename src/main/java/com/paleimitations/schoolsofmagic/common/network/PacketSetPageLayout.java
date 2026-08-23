package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.books.editor.BookPageLayout;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public class PacketSetPageLayout {
   private final BlockPos pos;
   private final int page;
   private final CompoundTag layout;

   public PacketSetPageLayout(BlockPos pos, int page, CompoundTag layout) {
      this.pos = pos;
      this.page = page;
      this.layout = layout == null ? new CompoundTag() : layout;
   }

   public PacketSetPageLayout(FriendlyByteBuf buf) {
      this.pos = buf.readBlockPos();
      this.page = buf.readInt();
      CompoundTag tag = buf.readNbt();
      this.layout = tag == null ? new CompoundTag() : tag;
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeBlockPos(this.pos);
      buf.writeInt(this.page);
      buf.writeNbt(this.layout);
   }

   public static void handle(PacketSetPageLayout msg, Supplier<NetworkEvent.Context> ctx) {
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
         IBook book = podium.handler.getStackInSlot(0)
            .getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
         if (book == null || msg.page < 0 || msg.page >= book.getBookPages().size()) {
            return;
         }

         book.setPageLayout(msg.page, BookPageLayout.load(msg.layout));
         book.setEdited(true);
         podium.sendUpdates();
      });
      context.setPacketHandled(true);
   }
}
