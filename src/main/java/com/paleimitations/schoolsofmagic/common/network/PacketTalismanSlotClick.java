package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data.CapabilityTalismanData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data.ITalismanData;
import com.paleimitations.schoolsofmagic.common.items.ItemPotionCharm;
import com.paleimitations.schoolsofmagic.common.items.ItemTalismanOfKnowledge;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class PacketTalismanSlotClick {
   public PacketTalismanSlotClick() {}

   public PacketTalismanSlotClick(FriendlyByteBuf buf) {}

   public void encode(FriendlyByteBuf buf) {}

   public static void handle(PacketTalismanSlotClick msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer sp = ctx.get().getSender();
         if (sp == null) return;
         ITalismanData talisman = CapabilityTalismanData.get(sp);
         if (talisman == null) return;
         AbstractContainerMenu menu = sp.containerMenu;
         ItemStack carried = menu.getCarried();
         ItemStack cur = talisman.getTalisman();

         if (carried.isEmpty()) {
            if (!cur.isEmpty()) {
               menu.setCarried(cur);
               talisman.setTalisman(ItemStack.EMPTY);
            }
         } else if (carried.getItem() instanceof ItemPotionCharm || carried.getItem() instanceof ItemTalismanOfKnowledge) {
            ItemStack one = carried.copy();
            one.setCount(1);
            carried.shrink(1);
            if (cur.isEmpty()) {
               talisman.setTalisman(one);
               menu.setCarried(carried);
            } else if (carried.isEmpty()) {
               talisman.setTalisman(one);
               menu.setCarried(cur);
            } else {
               if (!sp.getInventory().add(cur)) sp.drop(cur, false);
               talisman.setTalisman(one);
               menu.setCarried(carried);
            }
         }

         CapabilityTalismanData.sync(sp);
         sp.level().playSound(null, sp.getX(), sp.getY(), sp.getZ(),
            net.minecraft.sounds.SoundEvents.ARMOR_EQUIP_LEATHER, net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);
         menu.broadcastChanges();
      });
      ctx.get().setPacketHandled(true);
   }
}
