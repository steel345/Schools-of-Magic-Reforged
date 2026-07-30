package com.paleimitations.schoolsofmagic.common.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketDummyDamage {
   private final int entityId;
   private final float damage;
   private final float dps;
   private final float yaw;
   private final float wobble;

   public PacketDummyDamage(int entityId, float damage, float dps, float yaw, float wobble) {
      this.entityId = entityId;
      this.damage = damage;
      this.dps = dps;
      this.yaw = yaw;
      this.wobble = wobble;
   }

   public PacketDummyDamage(FriendlyByteBuf buf) {
      this.entityId = buf.readInt();
      this.damage = buf.readFloat();
      this.dps = buf.readFloat();
      this.yaw = buf.readFloat();
      this.wobble = buf.readFloat();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.entityId);
      buf.writeFloat(this.damage);
      buf.writeFloat(this.dps);
      buf.writeFloat(this.yaw);
      buf.writeFloat(this.wobble);
   }

   public static void handle(PacketDummyDamage msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
         () -> () -> com.paleimitations.schoolsofmagic.client.ClientDummyDamage.receive(
            msg.entityId, msg.damage, msg.dps, msg.yaw, msg.wobble)));
      context.setPacketHandled(true);
   }
}
