package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.client_mana_data.CapabilityClientManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.client_mana_data.IClientManaData;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class PacketSetIsFancy {
   private int playerId;
   private boolean isFancy;

   public PacketSetIsFancy(Player player, boolean isFancy) {
      this.playerId = player.getId();
      this.isFancy = isFancy;
   }

   public PacketSetIsFancy(FriendlyByteBuf buf) {
      this.playerId = buf.readInt();
      this.isFancy = buf.readBoolean();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.playerId);
      buf.writeBoolean(this.isFancy);
   }

   public static void handle(PacketSetIsFancy msg, Supplier<NetworkEvent.Context> ctx) {
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
               cap.setSimpleGui(msg.isFancy);
            }
         }
      });
      context.setPacketHandled(true);
   }
}
