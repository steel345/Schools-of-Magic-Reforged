package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketScryTrail {
   private final BlockPos mark;
   private final boolean on;
   private final int rgb;

   public PacketScryTrail(BlockPos mark, boolean on, int rgb) {
      this.mark = mark;
      this.on = on;
      this.rgb = rgb;
   }

   public PacketScryTrail(FriendlyByteBuf buf) {
      this.mark = buf.readBlockPos();
      this.on = buf.readBoolean();
      this.rgb = buf.readInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeBlockPos(this.mark);
      buf.writeBoolean(this.on);
      buf.writeInt(this.rgb);
   }

   public static void handle(PacketScryTrail msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
         () -> () -> com.paleimitations.schoolsofmagic.client.BiomeScryClient.mark(msg.mark, msg.on, msg.rgb)));
      context.setPacketHandled(true);
   }
}
