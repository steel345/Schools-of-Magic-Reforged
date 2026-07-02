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

public class PacketRingConcentration {
   public PacketRingConcentration() {}

   public PacketRingConcentration(FriendlyByteBuf buf) {}

   public void encode(FriendlyByteBuf buf) {}

   public static void handle(PacketRingConcentration msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer player = ctx.get().getSender();
         if (player == null || !RingCastHandler.isRingActive(player)) return;
         IManaData mana = player.getCapability(CapabilityManaData.CAP).orElse(null);
         IRingData ring = CapabilityRingData.get(player);
         if (mana == null || ring == null || ring.getRing().isEmpty()) return;
         Spell spell = mana.getCurrentSpell();
         if (!(spell instanceof SpellCustom sc) || !sc.isConcentration()) return;
         if (player.getCooldowns().isOnCooldown(ring.getRing().getItem())) return;
         spell.finishHoldEffect(ring.getRing(), player.level(), player);
         player.getCooldowns().addCooldown(ring.getRing().getItem(), sc.getCooldownTicks());
      });
      ctx.get().setPacketHandled(true);
   }
}
