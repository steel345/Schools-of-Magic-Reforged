package com.paleimitations.schoolsofmagic.common.network;

import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public class PacketSwitchPodiumGui {
   private BlockPos pos;
   private int entityID;
   private int gui;

   public PacketSwitchPodiumGui(Player player, BlockPos pos, int gui) {
      this.pos = pos;
      this.entityID = player.getId();
      this.gui = gui;
   }

   public PacketSwitchPodiumGui(FriendlyByteBuf buf) {
      this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
      this.entityID = buf.readInt();
      this.gui = buf.readInt();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeInt(this.pos.getX());
      buf.writeInt(this.pos.getY());
      buf.writeInt(this.pos.getZ());
      buf.writeInt(this.entityID);
      buf.writeInt(this.gui);
   }

   private static void openPodiumMenu(ServerPlayer p, TileEntityPodium podium, int gui) {
      final BlockPos pos = podium.getBlockPos();
      net.minecraft.world.MenuProvider provider = new net.minecraft.world.MenuProvider() {
         @Override
         public net.minecraft.network.chat.Component getDisplayName() {
            return net.minecraft.network.chat.Component.translatable("block.som.podium");
         }
         @Override
         public net.minecraft.world.inventory.AbstractContainerMenu createMenu(
               int windowId, net.minecraft.world.entity.player.Inventory inv,
               Player player) {
            switch (gui) {
               case 0: return new com.paleimitations.schoolsofmagic.common.containers.podium.ContainerPodiumRead(windowId, inv, podium);
               case 1: return new com.paleimitations.schoolsofmagic.common.containers.podium.ContainerPodiumCopy(windowId, inv, podium);
               case 2: return new com.paleimitations.schoolsofmagic.common.containers.podium.ContainerPodiumEdit(windowId, inv, podium);
               case 3: return new com.paleimitations.schoolsofmagic.common.containers.podium.ContainerPodiumFinal(windowId, inv, podium);
               case 4: return new com.paleimitations.schoolsofmagic.common.containers.podium.ContainerPodiumNote(windowId, inv, podium);
               case 5: return new com.paleimitations.schoolsofmagic.common.containers.podium.ContainerPodiumSpell(windowId, inv, podium);
               default: return null;
            }
         }
      };
      net.minecraftforge.network.NetworkHooks.openScreen(p, provider, buf -> buf.writeBlockPos(pos));
   }

   public static void handle(PacketSwitchPodiumGui msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null) {
            return;
         }
         BlockEntity te = sender.serverLevel().getBlockEntity(msg.pos);
         if (te instanceof TileEntityPodium) {
            TileEntityPodium podium = (TileEntityPodium) te;
            Entity entity = sender.serverLevel().getEntity(msg.entityID);
            if (entity instanceof ServerPlayer targetPlayer) {

               openPodiumMenu(targetPlayer, podium, msg.gui);
            }
         }
      });
      context.setPacketHandled(true);
   }
}
