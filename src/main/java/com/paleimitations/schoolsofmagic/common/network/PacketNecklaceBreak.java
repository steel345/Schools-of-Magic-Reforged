package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketNecklaceBreak {
   private final int entity;
   private final ItemStack shown;

   public PacketNecklaceBreak(int entity, ItemStack shown) {
      this.entity = entity;
      this.shown = shown;
   }

   public PacketNecklaceBreak(FriendlyByteBuf buf) {
      this.entity = buf.readVarInt();
      this.shown = buf.readItem();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeVarInt(this.entity);
      buf.writeItem(this.shown);
   }

   public static void handle(PacketNecklaceBreak msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
         com.paleimitations.schoolsofmagic.client.NecklaceBreak.play(msg.entity, msg.shown)));
      context.setPacketHandled(true);
   }
}
