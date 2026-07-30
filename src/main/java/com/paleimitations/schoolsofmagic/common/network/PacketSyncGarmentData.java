package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.CapabilityGarmentData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.IGarmentData;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

// Mirrors a player's worn garments to their client so the slots draw what is in them.
public class PacketSyncGarmentData {
   private final int playerId;
   private final CompoundTag data;

   public PacketSyncGarmentData(int playerId, CompoundTag data) {
      this.playerId = playerId;
      this.data = data;
   }

   public PacketSyncGarmentData(FriendlyByteBuf buf) {
      this.playerId = buf.readInt();
      this.data = buf.readNbt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.playerId);
      buf.writeNbt(this.data);
   }

   public static void handle(PacketSyncGarmentData msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         Minecraft mc = Minecraft.getInstance();
         if (mc.level == null || msg.data == null) return;
         Entity entity = mc.level.getEntity(msg.playerId);
         if (!(entity instanceof Player player)) return;
         IGarmentData data = CapabilityGarmentData.get(player);
         if (data != null) data.deserializeNBT(msg.data);
      });
      ctx.get().setPacketHandled(true);
   }
}
