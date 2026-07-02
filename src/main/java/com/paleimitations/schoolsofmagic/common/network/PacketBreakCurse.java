package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketBreakCurse {
   private BlockPos pos;

   public PacketBreakCurse(BlockPos pos) {
      this.pos = pos;
   }

   public PacketBreakCurse(FriendlyByteBuf buf) {
      this.pos = BlockPos.of(buf.readLong());
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeLong(this.pos.asLong());
   }

   public static void handle(PacketBreakCurse msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
      }));
      context.setPacketHandled(true);
   }
}
