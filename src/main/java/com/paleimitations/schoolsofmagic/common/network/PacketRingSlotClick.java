package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.CapabilityRingData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.IRingData;
import com.paleimitations.schoolsofmagic.common.items.ItemApprenticeRing;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class PacketRingSlotClick {
   public PacketRingSlotClick() {}

   public PacketRingSlotClick(FriendlyByteBuf buf) {}

   public void encode(FriendlyByteBuf buf) {}

   public static void handle(PacketRingSlotClick msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer sp = ctx.get().getSender();
         if (sp == null) return;
         IRingData ring = CapabilityRingData.get(sp);
         if (ring == null) return;
         AbstractContainerMenu menu = sp.containerMenu;
         ItemStack carried = menu.getCarried();
         ItemStack cur = ring.getRing();

         if (carried.isEmpty()) {
            if (!cur.isEmpty()) {
               menu.setCarried(cur);
               ring.setRing(ItemStack.EMPTY);
            }
         } else if (carried.getItem() instanceof ItemApprenticeRing) {
            ItemStack one = carried.copy();
            one.setCount(1);
            carried.shrink(1);
            if (cur.isEmpty()) {
               ring.setRing(one);
               menu.setCarried(carried);
            } else if (carried.isEmpty()) {
               ring.setRing(one);
               menu.setCarried(cur);
            } else {
               if (!sp.getInventory().add(cur)) sp.drop(cur, false);
               ring.setRing(one);
               menu.setCarried(carried);
            }
         }

         CapabilityRingData.sync(sp);
         sp.level().playSound(null, sp.getX(), sp.getY(), sp.getZ(),
            net.minecraft.sounds.SoundEvents.ARMOR_EQUIP_LEATHER, net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);
         menu.broadcastChanges();
      });
      ctx.get().setPacketHandled(true);
   }
}
