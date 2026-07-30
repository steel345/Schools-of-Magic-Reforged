package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;

import com.paleimitations.schoolsofmagic.common.containers.ContainerHerbPouch;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.CapabilityCharmData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.ICharmData;
import com.paleimitations.schoolsofmagic.common.items.ItemHerbPouch;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

// Client asks the server to open the herb pouch worn in the charm slot.
public class PacketOpenHerbPouch {
   public PacketOpenHerbPouch() {}

   public PacketOpenHerbPouch(FriendlyByteBuf buf) {}

   public void encode(FriendlyByteBuf buf) {}

   public static void handle(PacketOpenHerbPouch msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer sp = ctx.get().getSender();
         if (sp == null) return;
         if (!(com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots
               .findWornPouch(sp, s -> s.getItem() instanceof ItemHerbPouch).getItem() instanceof ItemHerbPouch)) return;
         NetworkHooks.openScreen(sp,
            new SimpleMenuProvider((id, inv, p) -> new ContainerHerbPouch(id, inv, p),
               Component.translatable("container.gui_herb_pouch")));
      });
      ctx.get().setPacketHandled(true);
   }
}
