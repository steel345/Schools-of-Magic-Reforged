package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.books.BookPageSpell;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.page.CapabilityPage;
import com.paleimitations.schoolsofmagic.common.items.capabilities.page.IPage;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_modifier.CapabilitySpellModifier;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_modifier.ISpellModifier;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public class PacketModifySpell {
   private BlockPos pos;

   public PacketModifySpell(BlockPos pos) {
      this.pos = pos;
   }

   public PacketModifySpell(FriendlyByteBuf buf) {
      this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.pos.getX());
      buf.writeInt(this.pos.getY());
      buf.writeInt(this.pos.getZ());
   }

   public static void handle(PacketModifySpell msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null) {
            return;
         }
         BlockEntity te = sender.serverLevel().getBlockEntity(msg.pos);
         if (te instanceof TileEntityPodium) {
            TileEntityPodium podium = (TileEntityPodium)te;
            Spell spell = null;
            IBook book = podium.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
            if (book != null && book.getCurrentPage() instanceof BookPageSpell) {
               spell = ((BookPageSpell)book.getCurrentPage()).getSpell();
            }

            IPage page = podium.handler.getStackInSlot(0).getCapability(CapabilityPage.PAGE_CAPABILITY).orElse(null);
            if (page != null && page.getBookPage() instanceof BookPageSpell) {
               spell = ((BookPageSpell)page.getBookPage()).getSpell();
            }

            ISpellModifier mod = podium.handler.getStackInSlot(7).getCapability(CapabilitySpellModifier.SPELL_MODIFIER_CAPABILITY).orElse(null);
            if (mod != null
               && mod.getSpellModifier() != null
               && mod.getModifierStat() != null
               && spell != null
               && spell.applyModifier(mod.getSpellModifier(), mod.getModifierStat()).getResult() == InteractionResult.SUCCESS) {
               podium.handler.getStackInSlot(7).shrink(1);
               if (book != null) book.setEdited(true);
               podium.sendUpdates();
            }
         }
      });
      context.setPacketHandled(true);
   }
}
