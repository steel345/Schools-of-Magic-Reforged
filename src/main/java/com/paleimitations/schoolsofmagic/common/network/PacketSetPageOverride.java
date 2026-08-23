package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.books.BookTextOverride;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public class PacketSetPageOverride {
   private static final int MAX = 8192;

   private final BlockPos pos;
   private final int page;
   private final boolean clear;
   private final String title;
   private final String body;
   private final String originalTitle;
   private final String originalBody;

   public PacketSetPageOverride(BlockPos pos, int page, boolean clear,
         String title, String body, String originalTitle, String originalBody) {
      this.pos = pos;
      this.page = page;
      this.clear = clear;
      this.title = title;
      this.body = body;
      this.originalTitle = originalTitle;
      this.originalBody = originalBody;
   }

   public PacketSetPageOverride(FriendlyByteBuf buf) {
      this.pos = buf.readBlockPos();
      this.page = buf.readInt();
      this.clear = buf.readBoolean();
      this.title = buf.readUtf(MAX);
      this.body = buf.readUtf(MAX);
      this.originalTitle = buf.readUtf(MAX);
      this.originalBody = buf.readUtf(MAX);
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeBlockPos(this.pos);
      buf.writeInt(this.page);
      buf.writeBoolean(this.clear);
      buf.writeUtf(this.title, MAX);
      buf.writeUtf(this.body, MAX);
      buf.writeUtf(this.originalTitle, MAX);
      buf.writeUtf(this.originalBody, MAX);
   }

   public static void handle(PacketSetPageOverride msg, Supplier<NetworkEvent.Context> ctx) {
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
         ItemStack ink = podium.handler.getStackInSlot(1);
         if (!ink.is(Items.INK_SAC) && !ink.is(Items.BLACK_DYE)) {
            return;
         }
         book.setTextOverride(msg.page, msg.clear ? null
            : new BookTextOverride(msg.title, msg.body, msg.originalTitle, msg.originalBody));
         book.setEdited(true);
         ink.shrink(1);
         podium.sendUpdates();
      });
      context.setPacketHandled(true);
   }
}
