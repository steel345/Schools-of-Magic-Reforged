package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.items.ItemSpellbook;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class PacketSetGrimoireSpell {
   private final String rl;
   private final CompoundTag data;

   public PacketSetGrimoireSpell(String rl, CompoundTag data) {
      this.rl = rl;
      this.data = data;
   }

   public PacketSetGrimoireSpell(FriendlyByteBuf buf) {
      this.rl = buf.readUtf();
      this.data = buf.readNbt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeUtf(this.rl);
      buf.writeNbt(this.data == null ? new CompoundTag() : this.data);
   }

   public static void handle(PacketSetGrimoireSpell msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer sp = ctx.get().getSender();
         if (sp == null) {
            return;
         }
         ItemStack held = sp.getMainHandItem();
         if (held.getItem() instanceof ItemSpellbook) {
            held.getOrCreateTag().putString("SelectedSpellRL", msg.rl);
            held.getTag().put("SelectedSpellData", msg.data == null ? new CompoundTag() : msg.data);
         }
      });
      ctx.get().setPacketHandled(true);
   }
}
