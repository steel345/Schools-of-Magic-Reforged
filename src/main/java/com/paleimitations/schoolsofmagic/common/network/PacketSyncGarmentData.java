package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.CapabilityGarmentData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.IGarmentData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

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
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
         if (msg.data == null) return;
         Entity entity = com.paleimitations.schoolsofmagic.client.ClientEntityLookup.byId(msg.playerId);
         if (entity == null) return;
         if (!(entity instanceof Player player)) return;
         IGarmentData data = CapabilityGarmentData.get(player);
         if (data != null) data.deserializeNBT(msg.data);
      }));
      context.setPacketHandled(true);
   }
}
