package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.CapabilityGarmentData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.IGarmentData;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class PacketToggleSlotShow {
   private final int slot;

   public PacketToggleSlotShow(int slot) {
      this.slot = slot;
   }

   public PacketToggleSlotShow(FriendlyByteBuf buf) {
      this.slot = buf.readVarInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeVarInt(this.slot);
   }

   public static void handle(PacketToggleSlotShow msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer sp = ctx.get().getSender();
         if (sp == null || msg.slot < 0 || msg.slot >= IGarmentData.SHOW_SLOTS) return;
         IGarmentData data = CapabilityGarmentData.get(sp);
         if (data == null) return;
         data.toggleHidden(msg.slot);
         CapabilityGarmentData.sync(sp);
      });
      ctx.get().setPacketHandled(true);
   }
}
