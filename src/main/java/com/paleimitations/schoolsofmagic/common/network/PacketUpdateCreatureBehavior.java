package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.CapabilityCreatureBehavior;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.ICreatureBehavior;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketUpdateCreatureBehavior {
   private int entityID;
   private CompoundTag nbt;

   public PacketUpdateCreatureBehavior(int entityID, CompoundTag nbt) {
      this.entityID = entityID;
      this.nbt = nbt;
   }

   public PacketUpdateCreatureBehavior(FriendlyByteBuf buf) {
      this.entityID = buf.readInt();
      this.nbt = buf.readNbt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.entityID);
      buf.writeNbt(this.nbt);
   }

   public static void handle(PacketUpdateCreatureBehavior msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
         Entity entity = Minecraft.getInstance().level.getEntity(msg.entityID);
         if (entity instanceof LivingEntity) {
            ICreatureBehavior cap = entity.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
            if (cap != null) {
               cap.deserializeNBT(msg.nbt);
               cap.setShouldUpdateClient(false);
            }
         }
      }));
      context.setPacketHandled(true);
   }
}
