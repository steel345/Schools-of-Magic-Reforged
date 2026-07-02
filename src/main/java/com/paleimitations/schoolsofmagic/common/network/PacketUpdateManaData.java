package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class PacketUpdateManaData {
   private int entityID;
   private CompoundTag data;

   public PacketUpdateManaData(int entityID, CompoundTag data) {
      this.entityID = entityID;
      this.data = data;
   }

   public PacketUpdateManaData(FriendlyByteBuf buf) {
      this.entityID = buf.readInt();
      this.data = buf.readNbt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.entityID);
      buf.writeNbt(this.data);
   }

   public static void handle(PacketUpdateManaData msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
         Entity entity = Minecraft.getInstance().level.getEntity(msg.entityID);
         if (entity instanceof LivingEntity) {
            IManaData cap = entity.getCapability(CapabilityManaData.CAP).orElse(null);
            if (cap != null) {

               boolean self = entity == Minecraft.getInstance().player;
               int slot = cap.getCurrentSpellSlot();
               cap.deserializeNBT(msg.data);
               if (self && Minecraft.getInstance().level != null
                     && Minecraft.getInstance().level.getGameTime()
                        - com.paleimitations.schoolsofmagic.client.ClientEffectEvents.lastSpellScrollTime < 10L) {
                  cap.setCurrentSpellSlot(slot);
               }
            }
         }
      }));
      context.setPacketHandled(true);
   }
}
