package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

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
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
         () -> () -> com.paleimitations.schoolsofmagic.client.ToastPagesUnlocked.show(msg.bookItem)));
      context.setPacketHandled(true);
   }
}
