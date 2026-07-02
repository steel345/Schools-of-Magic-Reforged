package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.client.ClientEffectEvents;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketLightningEffect {
   private int entityID;

   public PacketLightningEffect(int entityID) {
      this.entityID = entityID;
   }

   public PacketLightningEffect(FriendlyByteBuf buf) {
      this.entityID = buf.readInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.entityID);
   }

   public static void handle(PacketLightningEffect msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
         Entity entity = Minecraft.getInstance().level.getEntity(msg.entityID);
         ClientEffectEvents.addElectricEntity(entity);
      }));
      context.setPacketHandled(true);
   }
}
