package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.registries.CapabilityRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.capabilities.worker.IWork;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public class PacketGetWorker {
   private BlockPos pos;
   private Direction side;
   private String className;
   private String cooldownFieldName;
   private String maxCooldownFieldName;

   public PacketGetWorker(BlockPos pos, Direction side, String className, String cooldownFieldName, String maxCooldownFieldName) {
      this.pos = pos;
      this.side = side;
      this.className = className;
      this.cooldownFieldName = cooldownFieldName;
      this.maxCooldownFieldName = maxCooldownFieldName;
   }

   public PacketGetWorker(FriendlyByteBuf buf) {
      this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
      this.side = Direction.byName(buf.readUtf());
      this.className = buf.readUtf();
      this.cooldownFieldName = buf.readUtf();
      this.maxCooldownFieldName = buf.readUtf();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.pos.getX());
      buf.writeInt(this.pos.getY());
      buf.writeInt(this.pos.getZ());
      buf.writeUtf(this.side.getName());
      buf.writeUtf(this.className);
      buf.writeUtf(this.cooldownFieldName);
      buf.writeUtf(this.maxCooldownFieldName);
   }

   public static void handle(PacketGetWorker msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null) {
            return;
         }
         BlockEntity te = sender.serverLevel().getBlockEntity(msg.pos);
         if (te == null) {
            return;
         }
         IWork worker = te.getCapability(com.paleimitations.schoolsofmagic.common.tileentity.capabilities.worker.CapabilityWorker.WORKER, msg.side).orElse(null);
         if (worker != null) {
            PacketHandler.INSTANCE.sendTo(
               new PacketReturnWorker(worker.getWorkDone(), worker.getMaxWork(), msg.className, msg.cooldownFieldName, msg.maxCooldownFieldName),
               sender.connection.connection,
               NetworkDirection.PLAY_TO_CLIENT
            );
         }
      });
      context.setPacketHandled(true);
   }
}
