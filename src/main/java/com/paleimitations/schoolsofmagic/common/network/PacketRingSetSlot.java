package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.CapabilityRingData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.IRingData;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class PacketRingSetSlot {
   private final int slot;

   public PacketRingSetSlot(int slot) {
      this.slot = slot;
   }

   public PacketRingSetSlot(FriendlyByteBuf buf) {
      this.slot = buf.readVarInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeVarInt(this.slot);
   }

   public static void handle(PacketRingSetSlot msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer sp = ctx.get().getSender();
         if (sp == null || msg.slot < 0 || msg.slot > 8) return;
         IRingData data = CapabilityRingData.get(sp);
         if (data == null) return;
         data.setSpellSlots(data.getSpellSlots() ^ (1 << msg.slot));
         CapabilityRingData.sync(sp);
      });
      ctx.get().setPacketHandled(true);
   }
}
