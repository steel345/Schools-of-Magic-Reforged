package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.CapabilitySpellNotes;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.ISpellNotes;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.SpellNotes;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public class PacketSelectNoteOption {
   private BlockPos pos;
   private int entityID;
   private int option;

   public PacketSelectNoteOption(Player player, BlockPos pos, int option) {
      this.pos = pos;
      this.entityID = player.getId();
      this.option = option;
   }

   public PacketSelectNoteOption(FriendlyByteBuf buf) {
      this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
      this.entityID = buf.readInt();
      this.option = buf.readInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.pos.getX());
      buf.writeInt(this.pos.getY());
      buf.writeInt(this.pos.getZ());
      buf.writeInt(this.entityID);
      buf.writeInt(this.option);
   }

   public static void handle(PacketSelectNoteOption msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null) {
            return;
         }
         BlockEntity te = sender.serverLevel().getBlockEntity(msg.pos);
         if (te instanceof TileEntityPodium) {
            TileEntityPodium podium = (TileEntityPodium)te;
            ISpellNotes spellNotes = podium.handler.getStackInSlot(6).getCapability(CapabilitySpellNotes.SPELL_NOTES_CAPABILITY).orElse(null);
            if (podium.handler.getStackInSlot(6).getItem() == ItemRegistry.spell_note.get() && spellNotes != null) {
               SpellNotes note = spellNotes.getSpellNotes();
               if (note.getOptions().size() > msg.option) {
                  podium.handler.setStackInSlot(6, note.getOptions().get(msg.option));
                  podium.sendUpdates();
               }
            }
         }
      });
      context.setPacketHandled(true);
   }
}
