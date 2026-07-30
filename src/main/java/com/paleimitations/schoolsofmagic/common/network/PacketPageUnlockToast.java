package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

// Tells a client that book pages just unlocked, so it can show the toast with the
// owning book's icon.
public class PacketPageUnlockToast {
   private final ResourceLocation bookItem;

   public PacketPageUnlockToast(ResourceLocation bookItem) {
      this.bookItem = bookItem;
   }

   public PacketPageUnlockToast(FriendlyByteBuf buf) {
      this.bookItem = buf.readResourceLocation();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeResourceLocation(this.bookItem);
   }

   public static void handle(PacketPageUnlockToast msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         Item item = ForgeRegistries.ITEMS.getValue(msg.bookItem);
         ItemStack icon = item == null ? ItemStack.EMPTY : new ItemStack(item);
         Minecraft.getInstance().getToasts().addToast(
            new com.paleimitations.schoolsofmagic.client.ToastPagesUnlocked(icon));
      });
      ctx.get().setPacketHandled(true);
   }
}
