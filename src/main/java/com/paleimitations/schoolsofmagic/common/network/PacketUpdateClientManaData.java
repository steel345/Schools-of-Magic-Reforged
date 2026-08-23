package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.client_mana_data.CapabilityClientManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.client_mana_data.IClientManaData;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketUpdateClientManaData {
   private int entityID;
   private CompoundTag data;

   public PacketUpdateClientManaData(int entityID, CompoundTag data) {
      this.entityID = entityID;
      this.data = data;
   }

   public PacketUpdateClientManaData(FriendlyByteBuf buf) {
      this.entityID = buf.readInt();
      this.data = buf.readNbt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.entityID);
      buf.writeNbt(this.data);
   }

   public static void handle(PacketUpdateClientManaData msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
         com.paleimitations.schoolsofmagic.client.ClientManaSync.applyClientMana(msg.entityID, msg.data)));
      context.setPacketHandled(true);
   }
}
