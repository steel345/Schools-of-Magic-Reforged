package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.CapabilityRingData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.IRingData;
import com.paleimitations.schoolsofmagic.common.handlers.RingCastHandler;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class PacketRingHold {
   private final int count;
   private final boolean finish;

   public PacketRingHold(int count, boolean finish) {
      this.count = count;
      this.finish = finish;
   }

   public PacketRingHold(FriendlyByteBuf buf) {
      this.count = buf.readVarInt();
      this.finish = buf.readBoolean();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeVarInt(count);
      buf.writeBoolean(finish);
   }

   public static void handle(PacketRingHold msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer sp = ctx.get().getSender();
         if (sp == null || !RingCastHandler.isRingActive(sp)) return;
         IManaData mana = sp.getCapability(CapabilityManaData.CAP).orElse(null);
         IRingData ring = CapabilityRingData.get(sp);
         if (mana == null || ring == null || ring.getRing().isEmpty()) return;
         Spell spell = mana.getCurrentSpell();
         if (spell == null || spell instanceof SpellCustom || spell.getUseLength() <= 1) return;
         RingCastHandler.keepConcentration(sp);
         if (msg.finish) {
            spell.finishHoldEffect(ring.getRing(), sp.level(), sp);
         } else {
            spell.rightHoldEffect(ring.getRing(), sp, msg.count);
         }
      });
      ctx.get().setPacketHandled(true);
   }
}
