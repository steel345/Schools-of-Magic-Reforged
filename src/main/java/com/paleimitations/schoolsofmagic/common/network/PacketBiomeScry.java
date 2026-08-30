package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.handlers.BiomeScryHandler;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class PacketBiomeScry {
   private final ResourceLocation biome;

   public PacketBiomeScry(ResourceLocation biome) {
      this.biome = biome;
   }

   public PacketBiomeScry(FriendlyByteBuf buf) {
      this.biome = buf.readResourceLocation();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeResourceLocation(this.biome);
   }

   public static void handle(PacketBiomeScry msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer player = context.getSender();
         if (player == null) return;
         if (!BiomeScryHandler.scryBiome(player, msg.biome)) {
            player.displayClientMessage(Component.translatable("message.som.biome_scry.nothing"), true);
         }
      });
      context.setPacketHandled(true);
   }
}
