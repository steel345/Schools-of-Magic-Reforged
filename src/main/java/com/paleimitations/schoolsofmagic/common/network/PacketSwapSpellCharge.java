package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class PacketSwapSpellCharge {
   private int spellSlot;
   private int chargeLevel;

   public PacketSwapSpellCharge(int spellSlot, int chargeLevel) {
      this.spellSlot = spellSlot;
      this.chargeLevel = chargeLevel;
   }

   public PacketSwapSpellCharge(FriendlyByteBuf buf) {
      this.spellSlot = buf.readInt();
      this.chargeLevel = buf.readInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.spellSlot);
      buf.writeInt(this.chargeLevel);
   }

   public static void handle(PacketSwapSpellCharge msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null) {
            return;
         }
         IManaData cap = sender.getCapability(CapabilityManaData.CAP).orElse(null);
         if (cap != null) {
            Spell spell = cap.getSpell(msg.spellSlot);
            if (spell != null) {
               spell.currentSpellChargeLevel = msg.chargeLevel;
            }
         }
      });
      context.setPacketHandled(true);
   }
}
