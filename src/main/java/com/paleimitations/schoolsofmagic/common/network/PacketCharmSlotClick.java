package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.CapabilityCharmData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.ICharmData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class PacketCharmSlotClick {
   public PacketCharmSlotClick() {}

   public PacketCharmSlotClick(FriendlyByteBuf buf) {}

   public void encode(FriendlyByteBuf buf) {}

   public static boolean accepts(ItemStack stack) {
      return stack.getItem() == ItemRegistry.copper_key.get()
         || stack.getCapability(CapabilityBook.BOOK_CAPABILITY).isPresent();
   }

   public static void handle(PacketCharmSlotClick msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer sp = ctx.get().getSender();
         if (sp == null) return;
         ICharmData charm = CapabilityCharmData.get(sp);
         if (charm == null) return;
         AbstractContainerMenu menu = sp.containerMenu;
         ItemStack carried = menu.getCarried();
         ItemStack cur = charm.getCharm();

         if (carried.isEmpty()) {
            if (!cur.isEmpty()) {
               menu.setCarried(cur);
               charm.setCharm(ItemStack.EMPTY);
            }
         } else if (accepts(carried)) {
            ItemStack one = carried.copy();
            one.setCount(1);
            carried.shrink(1);
            if (cur.isEmpty()) {
               charm.setCharm(one);
               menu.setCarried(carried);
            } else if (carried.isEmpty()) {
               charm.setCharm(one);
               menu.setCarried(cur);
            } else {
               if (!sp.getInventory().add(cur)) sp.drop(cur, false);
               charm.setCharm(one);
               menu.setCarried(carried);
            }
         }

         CapabilityCharmData.sync(sp);
         sp.level().playSound(null, sp.getX(), sp.getY(), sp.getZ(),
            net.minecraft.sounds.SoundEvents.ARMOR_EQUIP_LEATHER, net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);
         menu.broadcastChanges();
      });
      ctx.get().setPacketHandled(true);
   }
}
