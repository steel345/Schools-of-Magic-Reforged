package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.CapabilityGarmentData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.IGarmentData;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class PacketGarmentSlotClick {
   private final int slot;

   public PacketGarmentSlotClick(int slot) {
      this.slot = slot;
   }

   public PacketGarmentSlotClick(FriendlyByteBuf buf) {
      this.slot = buf.readInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.slot);
   }

   public static void handle(PacketGarmentSlotClick msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer sp = ctx.get().getSender();
         if (sp == null) return;
         IGarmentData data = CapabilityGarmentData.get(sp);
         if (data == null || msg.slot < 0 || msg.slot >= IGarmentData.SLOTS) return;
         AbstractContainerMenu menu = sp.containerMenu;
         ItemStack carried = menu.getCarried();
         ItemStack cur = data.getGarment(msg.slot);

         boolean changed = false;
         if (carried.isEmpty()) {
            if (!cur.isEmpty()) {
               menu.setCarried(cur);
               data.setGarment(msg.slot, ItemStack.EMPTY);
               changed = true;
            }
         } else if (com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots
               .accepts(msg.slot, carried)) {
            changed = true;
            ItemStack one = carried.copy();
            one.setCount(1);
            carried.shrink(1);
            if (cur.isEmpty()) {
               data.setGarment(msg.slot, one);
               menu.setCarried(carried);
            } else if (carried.isEmpty()) {
               data.setGarment(msg.slot, one);
               menu.setCarried(cur);
            } else {
               if (!sp.getInventory().add(cur)) sp.drop(cur, false);
               data.setGarment(msg.slot, one);
               menu.setCarried(carried);
            }
         }

         CapabilityGarmentData.sync(sp);
         if (changed) {
            sp.level().playSound(null, sp.getX(), sp.getY(), sp.getZ(),
               net.minecraft.sounds.SoundEvents.ARMOR_EQUIP_LEATHER, net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);
         }
         menu.broadcastChanges();
      });
      ctx.get().setPacketHandled(true);
   }
}
