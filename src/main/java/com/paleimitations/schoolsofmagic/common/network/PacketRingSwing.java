package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.CapabilityRingData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.IRingData;
import com.paleimitations.schoolsofmagic.common.handlers.RingCastHandler;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class PacketRingSwing {
   public PacketRingSwing() {}

   public PacketRingSwing(FriendlyByteBuf buf) {}

   public void encode(FriendlyByteBuf buf) {}

   public static void handle(PacketRingSwing msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer sp = ctx.get().getSender();
         if (sp == null || !RingCastHandler.isRingActive(sp)) return;
         IManaData mana = sp.getCapability(CapabilityManaData.CAP).orElse(null);
         IRingData ring = CapabilityRingData.get(sp);
         if (mana == null || ring == null || ring.getRing().isEmpty()) return;
         Spell spell = mana.getCurrentSpell();
         if (spell != null) {
            spell.swingEffect(sp, ring.getRing());
         }
      });
      ctx.get().setPacketHandled(true);
   }
}
