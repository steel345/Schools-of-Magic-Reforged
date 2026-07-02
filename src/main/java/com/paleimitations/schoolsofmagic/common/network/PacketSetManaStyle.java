package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.client_mana_data.CapabilityClientManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.client_mana_data.IClientManaData;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

public class PacketSetManaStyle {
   private int playerId;
   private int style;

   public PacketSetManaStyle(Player player, int style) {
      this.playerId = player.getId();
      this.style = style;
   }

   public PacketSetManaStyle(FriendlyByteBuf buf) {
      this.playerId = buf.readInt();
      this.style = buf.readInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.playerId);
      buf.writeInt(this.style);
   }

   public static void handle(PacketSetManaStyle msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null) {
            return;
         }
         Level world = sender.level();
         Entity entity = world.getEntity(msg.playerId);
         if (entity instanceof Player) {
            IClientManaData cap = entity.getCapability(CapabilityClientManaData.CAP).orElse(null);
            if (cap != null) {
               cap.setGuiStyle(msg.style);
            }
         }
      });
      context.setPacketHandled(true);
   }
}
