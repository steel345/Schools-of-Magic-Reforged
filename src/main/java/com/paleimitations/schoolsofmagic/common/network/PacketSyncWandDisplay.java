package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketSyncWandDisplay {
   private final boolean smallIcons;
   private final boolean flatModel;

   public PacketSyncWandDisplay(boolean smallIcons, boolean flatModel) {
      this.smallIcons = smallIcons;
      this.flatModel = flatModel;
   }

   public PacketSyncWandDisplay(FriendlyByteBuf buf) {
      this.smallIcons = buf.readBoolean();
      this.flatModel = buf.readBoolean();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeBoolean(this.smallIcons);
      buf.writeBoolean(this.flatModel);
   }

   public static void handle(PacketSyncWandDisplay msg, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
         () -> () -> com.paleimitations.schoolsofmagic.client.ClientWandDisplay.receive(
            msg.smallIcons, msg.flatModel)));
      ctx.get().setPacketHandled(true);
   }
}
