package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.client_mana_data.CapabilityClientManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.client_mana_data.IClientManaData;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class PacketSetManaHidden {
   private int playerId;
   private boolean hidden;

   public PacketSetManaHidden(Player player, boolean hidden) {
      this.playerId = player.getId();
      this.hidden = hidden;
   }

   public PacketSetManaHidden(FriendlyByteBuf buf) {
      this.playerId = buf.readInt();
      this.hidden = buf.readBoolean();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.playerId);
      buf.writeBoolean(this.hidden);
   }

   public static void handle(PacketSetManaHidden msg, Supplier<NetworkEvent.Context> ctx) {
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
               cap.setHidden(msg.hidden);
            }
         }
      });
      context.setPacketHandled(true);
   }
}
