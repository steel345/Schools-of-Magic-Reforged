package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.client_mana_data.CapabilityClientManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.client_mana_data.IClientManaData;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public class PacketQueueUpdateClientManaData {

   public PacketQueueUpdateClientManaData() {
   }

   public PacketQueueUpdateClientManaData(FriendlyByteBuf buf) {
   }

   public void encode(FriendlyByteBuf buf) {
   }

   public static void handle(PacketQueueUpdateClientManaData msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer playerIn = context.getSender();
         if (playerIn != null) {
            IClientManaData handler = playerIn.getCapability(CapabilityClientManaData.CAP).orElse(null);
            if (handler != null) {
               handler.setLoadedToClient(true);
               PacketHandler.INSTANCE.send(
                  PacketDistributor.PLAYER.with(() -> playerIn),
                  new PacketUpdateClientManaData(playerIn.getId(), handler.serializeNBT())
               );
            }
         }
      });
      context.setPacketHandled(true);
   }
}
