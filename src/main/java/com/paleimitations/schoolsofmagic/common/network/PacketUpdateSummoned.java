package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.summoned.CapabilitySummoned;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.summoned.ISummoned;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketUpdateSummoned {
   private int entityID;
   private CompoundTag nbt;

   public PacketUpdateSummoned(int entityID, CompoundTag nbt) {
      this.entityID = entityID;
      this.nbt = nbt;
   }

   public PacketUpdateSummoned(FriendlyByteBuf buf) {
      this.entityID = buf.readInt();
      this.nbt = buf.readNbt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.entityID);
      buf.writeNbt(this.nbt);
   }

   public static void handle(PacketUpdateSummoned msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
         Entity entity = Minecraft.getInstance().level.getEntity(msg.entityID);
         if (entity instanceof LivingEntity) {
            ISummoned cap = entity.getCapability(CapabilitySummoned.CAP).orElse(null);
            if (cap != null) {
               cap.deserializeNBT(msg.nbt);
            }
         }
      }));
      context.setPacketHandled(true);
   }
}
