package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data.CapabilityTalismanData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data.ITalismanData;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
      ctx.get().enqueueWork(() -> apply(msg));
      ctx.get().setPacketHandled(true);
   }

   private static void apply(PacketSyncTalismanData msg) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.level == null) return;
      Entity entity = mc.level.getEntity(msg.playerId);
      if (entity instanceof Player player) {
         ITalismanData data = CapabilityTalismanData.get(player);
         if (data != null) {
            data.setTalisman(msg.talisman);
         }
      }
   }
}
