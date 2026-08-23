package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data.CapabilityTalismanData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data.ITalismanData;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketSyncTalismanData {
   private final int playerId;
   private final ItemStack talisman;

   public PacketSyncTalismanData(int playerId, ItemStack talisman) {
      this.playerId = playerId;
      this.talisman = talisman;
   }

   public PacketSyncTalismanData(FriendlyByteBuf buf) {
      this.playerId = buf.readInt();
      this.talisman = buf.readItem();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.playerId);
      buf.writeItem(this.talisman);
   }

   public static void handle(PacketSyncTalismanData msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
         Entity entity = com.paleimitations.schoolsofmagic.client.ClientEntityLookup.byId(msg.playerId);
         if (entity == null) return;
         if (entity instanceof Player player) {
            ITalismanData data = CapabilityTalismanData.get(player);
            if (data != null) {
               data.setTalisman(msg.talisman);
            }
         }
      }));
      context.setPacketHandled(true);
   }
}
