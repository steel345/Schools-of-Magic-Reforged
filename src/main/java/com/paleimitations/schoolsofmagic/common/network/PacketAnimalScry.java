package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.handlers.BiomeScryHandler;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class PacketAnimalScry {
   private final ResourceLocation beast;

   public PacketAnimalScry(ResourceLocation beast) {
      this.beast = beast;
   }

   public PacketAnimalScry(FriendlyByteBuf buf) {
      this.beast = buf.readResourceLocation();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeResourceLocation(this.beast);
   }

   public static void handle(PacketAnimalScry msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer player = context.getSender();
         if (player == null) return;
         if (!BiomeScryHandler.scryBeast(player, msg.beast)) {
            player.displayClientMessage(Component.translatable("message.som.animal_scry.nothing"), true);
         }
      });
      context.setPacketHandled(true);
   }
}
