package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class PacketUpdateSpellFromClient {
   private int playerID;
   private int spell;
   private CompoundTag spellData;

   public PacketUpdateSpellFromClient(int playerID, int spell, CompoundTag spellData) {
      this.playerID = playerID;
      this.spell = spell;
      this.spellData = spellData;
   }

   public PacketUpdateSpellFromClient(FriendlyByteBuf buf) {
      this.playerID = buf.readInt();
      this.spell = buf.readInt();
      this.spellData = buf.readNbt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.playerID);
      buf.writeInt(this.spell);
      buf.writeNbt(this.spellData);
   }

   public static void handle(PacketUpdateSpellFromClient msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null) {
            return;
         }
         ServerLevel world = sender.serverLevel();
         Entity entity = world.getEntity(msg.playerID);
         if (entity instanceof Player) {
            IManaData manaData = entity.getCapability(CapabilityManaData.CAP).orElse(null);
            if (manaData != null && manaData.getSpell(msg.spell) != null) {
               manaData.getSpell(msg.spell).deserializeNBT(msg.spellData);
            }
         }
      });
      context.setPacketHandled(true);
   }
}
