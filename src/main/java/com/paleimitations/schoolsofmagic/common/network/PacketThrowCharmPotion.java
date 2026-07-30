package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.CapabilityCharmData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.ICharmData;
import com.paleimitations.schoolsofmagic.common.items.PotionBag;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

// Client asks the server to throw the selected potion from the potion bag charm.
public class PacketThrowCharmPotion {
   private final int slot;

   public PacketThrowCharmPotion(int slot) {
      this.slot = slot;
   }

   public PacketThrowCharmPotion(FriendlyByteBuf buf) {
      this.slot = buf.readInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.slot);
   }

   public static void handle(PacketThrowCharmPotion msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer sp = ctx.get().getSender();
         if (sp == null) return;
         ItemStack bag = com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots
            .findWornPouch(sp, s -> s.getItem() == ItemRegistry.potion_bag.get());
         if (bag.isEmpty()) return;
         if (msg.slot >= 0) bag.setDamageValue(msg.slot);
         if (PotionBag.throwSelected(sp.level(), sp, bag)) {
            sp.swing(net.minecraft.world.InteractionHand.MAIN_HAND, true); // right-click hand animation
            // The bag may hang from either slot, so refresh both views of it.
            ICharmData charm = CapabilityCharmData.get(sp);
            if (charm != null && charm.getCharm() == bag) charm.setCharm(bag);
            CapabilityCharmData.sync(sp);
            com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.CapabilityGarmentData.sync(sp);
         }
      });
      ctx.get().setPacketHandled(true);
   }
}
