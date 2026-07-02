package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class PacketSneezeDrop {

   public PacketSneezeDrop() {
   }

   public PacketSneezeDrop(FriendlyByteBuf buf) {
   }

   public void encode(FriendlyByteBuf buf) {
   }

   public static void handle(PacketSneezeDrop msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null) return;
         if (!sender.hasEffect(PotionRegistry.sneezing.get())) return;

         Inventory inv = sender.getInventory();
         int sel = inv.selected;
         ItemStack held = inv.getItem(sel);
         if (held.isEmpty()) return;

         inv.setItem(sel, ItemStack.EMPTY);
         sender.drop(held, false, false);
         sender.inventoryMenu.broadcastChanges();
      });
      context.setPacketHandled(true);
   }
}
