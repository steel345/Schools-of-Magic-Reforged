package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;

import com.paleimitations.schoolsofmagic.common.containers.ContainerPotionBag;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.CapabilityCharmData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.ICharmData;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

// Client asks the server to open the default potion bag GUI for the bag worn in the
// charm slot.
public class PacketOpenPotionBag {
   public PacketOpenPotionBag() {}

   public PacketOpenPotionBag(FriendlyByteBuf buf) {}

   public void encode(FriendlyByteBuf buf) {}

   public static void handle(PacketOpenPotionBag msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer sp = ctx.get().getSender();
         if (sp == null) return;
         ICharmData data = CapabilityCharmData.get(sp);
         if (data == null || data.getCharm().getItem() != ItemRegistry.potion_bag.get()) return;
         NetworkHooks.openScreen(sp,
            new SimpleMenuProvider((id, inv, p) -> new ContainerPotionBag(id, inv, p),
               Component.translatable("container.gui_potion_bag")));
      });
      ctx.get().setPacketHandled(true);
   }
}
