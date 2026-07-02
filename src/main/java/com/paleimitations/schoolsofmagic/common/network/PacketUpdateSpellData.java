package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

public class PacketUpdateSpellData {
   private int entityId;
   private int spellSlot;

   public PacketUpdateSpellData(int entityId, int spellSlot) {
      this.entityId = entityId;
      this.spellSlot = spellSlot;
   }

   public PacketUpdateSpellData(FriendlyByteBuf buf) {
      this.entityId = buf.readInt();
      this.spellSlot = buf.readInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.entityId);
      buf.writeInt(this.spellSlot);
   }

   public static void handle(PacketUpdateSpellData msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null) {
            return;
         }
         Entity entity = sender.serverLevel().getEntity(msg.entityId);
         if (entity instanceof LivingEntity) {
            IManaData cap = entity.getCapability(CapabilityManaData.CAP).orElse(null);
            if (cap != null) {
               cap.setCurrentSpellSlot(msg.spellSlot);
            }
         }
      });
      context.setPacketHandled(true);
   }
}
