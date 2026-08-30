package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketGaianWarriorBar {
   private final int ticks;
   private final int max;
   private final int which;

   public static final int GAIAN = 0;
   public static final int RIFT = 1;
   public static final int DECOY = 2;
   public static final int WHIRLWIND = 3;
   public static final int FOG = 4;
   public static final int SILENCE = 5;
   public static final int BREATH = 6;

   public PacketGaianWarriorBar(int ticks, int max) {
      this(ticks, max, GAIAN);
   }

   public PacketGaianWarriorBar(int ticks, int max, int which) {
      this.ticks = ticks;
      this.max = max;
      this.which = which;
   }

   public PacketGaianWarriorBar(FriendlyByteBuf buf) {
      this.ticks = buf.readInt();
      this.max = buf.readInt();
      this.which = buf.readInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.ticks);
      buf.writeInt(this.max);
      buf.writeInt(this.which);
   }

   public static void handle(PacketGaianWarriorBar msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
         apply(msg)));
      context.setPacketHandled(true);
   }

   private static void apply(PacketGaianWarriorBar msg) {
      switch (msg.which) {
         case RIFT -> com.paleimitations.schoolsofmagic.common.spells.spells.RiftBar.client(msg.ticks, msg.max);
         case DECOY -> com.paleimitations.schoolsofmagic.common.spells.spells.DecoyBar.client(msg.ticks, msg.max);
         case WHIRLWIND -> com.paleimitations.schoolsofmagic.common.spells.spells.WhirlwindBar.client(msg.ticks, msg.max);
         case FOG -> com.paleimitations.schoolsofmagic.common.spells.spells.FogBar.client(msg.ticks, msg.max);
         case SILENCE -> com.paleimitations.schoolsofmagic.common.spells.spells.SilenceBar.client(msg.ticks, msg.max);
         case BREATH -> com.paleimitations.schoolsofmagic.common.spells.spells.BreathBar.client(msg.ticks, msg.max);
         default -> com.paleimitations.schoolsofmagic.common.spells.spells.GaianWarriorBar.client(msg.ticks, msg.max);
      }
   }
}
