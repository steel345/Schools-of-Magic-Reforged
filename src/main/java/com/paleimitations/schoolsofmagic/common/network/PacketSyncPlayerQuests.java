package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.player_quests.CapabilityPlayerQuests;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.player_quests.IPlayerQuests;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketSyncPlayerQuests {
   private final int playerId;
   private final CompoundTag data;

   public PacketSyncPlayerQuests(int playerId, CompoundTag data) {
      this.playerId = playerId;
      this.data = data;
   }

   public PacketSyncPlayerQuests(FriendlyByteBuf buf) {
      this.playerId = buf.readInt();
      this.data = buf.readNbt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.playerId);
      buf.writeNbt(this.data);
   }

   @SuppressWarnings("unchecked")
   public static void handle(PacketSyncPlayerQuests msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
         if (msg.data == null) return;
         Entity entity = com.paleimitations.schoolsofmagic.client.ClientEntityLookup.byId(msg.playerId);
         if (entity == null) return;
         if (!(entity instanceof Player player)) return;
         IPlayerQuests quests = CapabilityPlayerQuests.getPlayerQuests(player);
         if (quests instanceof INBTSerializable<?>) {
            ((INBTSerializable<CompoundTag>) quests).deserializeNBT(msg.data);
         }
      }));
      context.setPacketHandled(true);
   }
}
