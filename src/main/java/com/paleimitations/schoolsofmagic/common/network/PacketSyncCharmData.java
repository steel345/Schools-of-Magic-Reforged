package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.CapabilityCharmData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.ICharmData;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
      ctx.get().enqueueWork(() -> apply(msg));
      ctx.get().setPacketHandled(true);
   }

   private static void apply(PacketSyncCharmData msg) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.level == null) return;
      Entity entity = mc.level.getEntity(msg.playerId);
      if (entity instanceof Player player) {
         ICharmData data = CapabilityCharmData.get(player);
         if (data != null) {
            data.setCharm(msg.charm);
         }
      }
   }
}
