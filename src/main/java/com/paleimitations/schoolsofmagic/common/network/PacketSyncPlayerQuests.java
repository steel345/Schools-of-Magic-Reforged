package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.player_quests.CapabilityPlayerQuests;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.player_quests.IPlayerQuests;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.NetworkEvent;

// Mirrors a player's dryad-quest progress to their client, so the quest page can
// show the countdown and the right buttons.
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
      ctx.get().enqueueWork(() -> {
         Minecraft mc = Minecraft.getInstance();
         if (mc.level == null || msg.data == null) return;
         Entity entity = mc.level.getEntity(msg.playerId);
         if (!(entity instanceof Player player)) return;
         IPlayerQuests quests = CapabilityPlayerQuests.getPlayerQuests(player);
         if (quests instanceof INBTSerializable<?>) {
            ((INBTSerializable<CompoundTag>) quests).deserializeNBT(msg.data);
         }
      });
      ctx.get().setPacketHandled(true);
   }
}
