package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketUpdateBook {
   private boolean isMain;
   private int entityId;
   private CompoundTag data;

   public PacketUpdateBook(int entityId, CompoundTag data, boolean isMain) {
      this.data = data;
      this.entityId = entityId;
      this.isMain = isMain;
   }

   public PacketUpdateBook(FriendlyByteBuf buf) {
      this.entityId = buf.readInt();
      this.isMain = buf.readBoolean();
      this.data = buf.readNbt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.entityId);
      buf.writeBoolean(this.isMain);
      buf.writeNbt(this.data);
   }

   public static void handle(PacketUpdateBook msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
         Entity entity = Minecraft.getInstance().level.getEntity(msg.entityId);
         if (entity instanceof Player) {
            Player player = (Player)entity;
            ItemStack stack = player.getItemInHand(msg.isMain ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
            IBook book = stack.getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
            if (book != null) {
               book.deserializeNBT(msg.data);
            }
         }
      }));
      context.setPacketHandled(true);
   }
}
