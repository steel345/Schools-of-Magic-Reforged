package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.client_mana_data.CapabilityClientManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.client_mana_data.IClientManaData;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class PacketSetManaOrientation {
   private int playerId;
   private int orientation;

   public PacketSetManaOrientation(Player player, int orientation) {
      this.playerId = player.getId();
      this.orientation = orientation;
   }

   public PacketSetManaOrientation(FriendlyByteBuf buf) {
      this.playerId = buf.readInt();
      this.orientation = buf.readInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.playerId);
      buf.writeInt(this.orientation);
   }

   public static void handle(PacketSetManaOrientation msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null) {
            return;
         }
         Entity entity = sender.serverLevel().getEntity(msg.playerId);
         if (entity instanceof Player) {
            IClientManaData cap = entity.getCapability(CapabilityClientManaData.CAP).orElse(null);
            if (cap != null) {
               cap.setGuiOrientation(msg.orientation);
            }
         }
      });
      context.setPacketHandled(true);
   }
}
