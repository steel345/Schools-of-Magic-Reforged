package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.quests.CapabilityQuestData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.quests.IQuestData;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketUpdateQuestData {
   private int entityID;
   private CompoundTag data;

   public PacketUpdateQuestData(int entityID, CompoundTag data) {
      this.entityID = entityID;
      this.data = data;
   }

   public PacketUpdateQuestData(FriendlyByteBuf buf) {
      this.entityID = buf.readInt();
      this.data = buf.readNbt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.entityID);
      buf.writeNbt(this.data);
   }

   public static void handle(PacketUpdateQuestData msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
         Entity entity = Minecraft.getInstance().level.getEntity(msg.entityID);
         if (entity instanceof LivingEntity) {
            IQuestData cap = entity.getCapability(CapabilityQuestData.CAP).orElse(null);
            if (cap != null) {
               cap.deserializeNBT(msg.data);
            }
         }
      }));
      context.setPacketHandled(true);
   }
}
