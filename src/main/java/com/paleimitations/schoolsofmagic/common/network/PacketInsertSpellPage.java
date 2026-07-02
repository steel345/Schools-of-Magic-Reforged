package com.paleimitations.schoolsofmagic.common.network;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.books.BookElementSticker;
import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.books.BookPageSpell;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellHelper;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public class PacketInsertSpellPage {
   private BlockPos pos;
   private int page;
   private ResourceLocation spellLocation;
   private CompoundTag spellData;

   public PacketInsertSpellPage(int page, ResourceLocation spellLocation, CompoundTag spellData, BlockPos pos) {
      this.pos = pos;
      this.page = page;
      this.spellLocation = spellLocation;
      this.spellData = spellData;
   }

   public PacketInsertSpellPage(FriendlyByteBuf buf) {
      this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
      this.page = buf.readInt();
      this.spellLocation = new ResourceLocation(buf.readUtf());
      this.spellData = buf.readNbt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.pos.getX());
      buf.writeInt(this.pos.getY());
      buf.writeInt(this.pos.getZ());
      buf.writeInt(this.page);
      buf.writeUtf(this.spellLocation.toString());
      buf.writeNbt(this.spellData);
   }

   public static void handle(PacketInsertSpellPage msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null) {
            return;
         }
         BlockEntity te = sender.serverLevel().getBlockEntity(msg.pos);
         if (te instanceof TileEntityPodium) {
            TileEntityPodium podium = (TileEntityPodium)te;
            IBook book = podium.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
            if (book != null) {
               Spell spell = SpellHelper.getSpellInstance(msg.spellLocation, msg.spellData);
               List<BookPage> pages = Lists.newArrayList();
               List<BookElementSticker> stickers = Lists.newArrayList();
               int pageN = 0;

               for (int i = 0; i < book.getBookPages().size(); i++) {
                  pages.add(book.getBookPages().get(i));

                  for (BookElementSticker sticker : book.getStickers()) {
                     if (sticker.page == i) {
                        stickers.add(new BookElementSticker(sticker.sticker, sticker.rotation, sticker.x, sticker.y, pageN, sticker.subpage));
                     }
                  }

                  pageN++;
                  if (i == msg.page) {
                     pages.add(new BookPageSpell(spell));
                     pageN++;
                  }
               }

               if (pages.isEmpty()) {
                  pages.add(new BookPageSpell(spell));
               }

               book.setBookPages(pages);
               book.setStickers(stickers);
               podium.handler.getStackInSlot(5).shrink(1);
               book.setEdited(true);

               book.setPage(Math.min(msg.page + 1, pages.size() - 1));
               book.setSubPage(0);
               podium.sendUpdates();
            }
         }
      });
      context.setPacketHandled(true);
   }
}
