package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.items.ItemMagicMirror;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class PacketSetMirrorCoords {
   private final int x;
   private final int y;
   private final int z;
   private final boolean set;

   public PacketSetMirrorCoords(int x, int y, int z, boolean set) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.set = set;
   }

   public PacketSetMirrorCoords(FriendlyByteBuf buf) {
      this.x = buf.readInt();
      this.y = buf.readInt();
      this.z = buf.readInt();
      this.set = buf.readBoolean();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.x);
      buf.writeInt(this.y);
      buf.writeInt(this.z);
      buf.writeBoolean(this.set);
   }

   public static void handle(PacketSetMirrorCoords msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sp = context.getSender();
         if (sp == null) {
            return;
         }
         ItemStack held = sp.getMainHandItem();
         if (!(held.getItem() instanceof ItemMagicMirror)) {
            held = sp.getItemInHand(InteractionHand.OFF_HAND);
         }
         if (!(held.getItem() instanceof ItemMagicMirror)) {
            return;
         }
         if (msg.set) {
            ItemMagicMirror.setBoundPos(held, msg.x, msg.y, msg.z);
         } else {
            ItemMagicMirror.clearBoundPos(held);
         }
      });
      context.setPacketHandled(true);
   }
}
