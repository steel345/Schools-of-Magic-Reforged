package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.CapabilityRingData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.IRingData;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class PacketSyncRingData {
   private final int playerId;
   private final ItemStack ring;
   private final int spellSlots;

   public PacketSyncRingData(int playerId, ItemStack ring, int spellSlots) {
      this.playerId = playerId;
      this.ring = ring;
      this.spellSlots = spellSlots;
   }

   public PacketSyncRingData(FriendlyByteBuf buf) {
      this.playerId = buf.readInt();
      this.ring = buf.readItem();
      this.spellSlots = buf.readVarInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.playerId);
      buf.writeItem(this.ring);
      buf.writeVarInt(this.spellSlots);
   }

   public static void handle(PacketSyncRingData msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> apply(msg));
      ctx.get().setPacketHandled(true);
   }

   private static void apply(PacketSyncRingData msg) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.level == null) return;
      Entity entity = mc.level.getEntity(msg.playerId);
      if (entity instanceof Player player) {
         IRingData data = CapabilityRingData.get(player);
         if (data != null) {
            data.setRing(msg.ring);
            data.setSpellSlots(msg.spellSlots);
         }
      }
   }
}
