package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class PacketSetPotionBagSlot {
   private final int slot;

   public PacketSetPotionBagSlot(int slot) {
      this.slot = slot;
   }

   public PacketSetPotionBagSlot(FriendlyByteBuf buf) {
      this.slot = buf.readInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.slot);
   }

   public static void handle(PacketSetPotionBagSlot msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null) return;

         int s = Math.max(0, Math.min(9, msg.slot));

         ItemStack main = sender.getMainHandItem();
         if (main.getItem() == ItemRegistry.potion_bag.get()) {
            main.setDamageValue(s);
         }
         ItemStack off = sender.getOffhandItem();
         if (off.getItem() == ItemRegistry.potion_bag.get()) {
            off.setDamageValue(s);
         }
      });
      context.setPacketHandled(true);
   }
}
