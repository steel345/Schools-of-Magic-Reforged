package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.items.BookDecorations;
import com.paleimitations.schoolsofmagic.common.items.ItemSpellbook;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class PacketSetCastingMode {
   private final boolean on;

   public PacketSetCastingMode(boolean on) {
      this.on = on;
   }

   public PacketSetCastingMode(FriendlyByteBuf buf) {
      this.on = buf.readBoolean();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeBoolean(this.on);
   }

   public static void handle(PacketSetCastingMode msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer sp = ctx.get().getSender();
         if (sp == null) {
            return;
         }
         ItemStack held = sp.getMainHandItem();
         if (held.getItem() instanceof ItemSpellbook && BookDecorations.hasJewel(held)) {
            ItemSpellbook.setCastingMode(held, msg.on);
         }
      });
      ctx.get().setPacketHandled(true);
   }
}
