package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.CapabilityCharmData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.ICharmData;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketSyncCharmData {
   private final int playerId;
   private final ItemStack charm;

   public PacketSyncCharmData(int playerId, ItemStack charm) {
      this.playerId = playerId;
      this.charm = charm;
   }

   public PacketSyncCharmData(FriendlyByteBuf buf) {
      this.playerId = buf.readInt();
      this.charm = buf.readItem();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.playerId);
      buf.writeItem(this.charm);
   }

   public static void handle(PacketSyncCharmData msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
         Entity entity = com.paleimitations.schoolsofmagic.client.ClientEntityLookup.byId(msg.playerId);
         if (entity == null) return;
         if (entity instanceof Player player) {
            ICharmData data = CapabilityCharmData.get(player);
            if (data != null) {
               data.setCharm(msg.charm);
            }
         }
      }));
      context.setPacketHandled(true);
   }
}
