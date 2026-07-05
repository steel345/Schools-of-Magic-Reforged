package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketSmokeScry {
   private final BlockPos pos;
   private final int ticks;

   public PacketSmokeScry(BlockPos pos, int ticks) {
      this.pos = pos;
      this.ticks = ticks;
   }

   public PacketSmokeScry(FriendlyByteBuf buf) {
      this.pos = buf.readBlockPos();
      this.ticks = buf.readInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeBlockPos(this.pos);
      buf.writeInt(this.ticks);
   }

   public static void handle(PacketSmokeScry msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
         () -> () -> com.paleimitations.schoolsofmagic.client.ClientSmokeScry.receive(msg.pos, msg.ticks)));
      context.setPacketHandled(true);
   }
}
