package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.client_mana_data.CapabilityClientManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.client_mana_data.IClientManaData;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class PacketSetManaPosition {
   private int playerId;
   private int posX;
   private int posY;

   public PacketSetManaPosition(Player player, int posX, int posY) {
      this.playerId = player.getId();
      this.posX = posX;
      this.posY = posY;
   }

   public PacketSetManaPosition(FriendlyByteBuf buf) {
      this.playerId = buf.readInt();
      this.posX = buf.readInt();
      this.posY = buf.readInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.playerId);
      buf.writeInt(this.posX);
      buf.writeInt(this.posY);
   }

   public static void handle(PacketSetManaPosition msg, Supplier<NetworkEvent.Context> ctx) {
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
               cap.setGuiXPos(msg.posX);
               cap.setGuiYPos(msg.posY);
            }
         }
      });
      context.setPacketHandled(true);
   }
}
