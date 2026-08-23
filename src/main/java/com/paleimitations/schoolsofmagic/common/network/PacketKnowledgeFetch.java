package com.paleimitations.schoolsofmagic.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketKnowledgeFetch {
   private final BlockPos shelf;
   private final int slot;
   private final BlockPos reading;

   public PacketKnowledgeFetch(BlockPos shelf, int slot, BlockPos reading) {
      this.shelf = shelf;
      this.slot = slot;
      this.reading = reading;
   }

   public PacketKnowledgeFetch(FriendlyByteBuf buf) {
      this.shelf = buf.readBlockPos();
      this.slot = buf.readVarInt();
      this.reading = buf.readBlockPos();
   }

   public void encode(FriendlyByteBuf buf) {
      buf.writeBlockPos(this.shelf);
      buf.writeVarInt(this.slot);
      buf.writeBlockPos(this.reading);
   }

   public static void handle(PacketKnowledgeFetch msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
         ServerPlayer sender = context.getSender();
         if (sender == null) return;
         ServerLevel level = sender.serverLevel();

         BlockEntity be = level.getBlockEntity(msg.shelf);
         if (!(be instanceof ChiseledBookShelfBlockEntity shelfBe)) return;
         if (msg.slot < 0 || msg.slot >= shelfBe.getContainerSize()) return;
         if (shelfBe.getItem(msg.slot).isEmpty()) return;

         final BlockPos station = msg.reading;
         BlockEntity readingBe = level.getBlockEntity(station);
         boolean podium = readingBe instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;
         boolean lectern = readingBe instanceof net.minecraft.world.level.block.entity.LecternBlockEntity;
         if (!podium && !lectern) return;
         if (com.paleimitations.schoolsofmagic.common.handlers.KnowledgeLoans.get(station) != null) return;
         final ItemStack knowledge = readingBook(level, station);
         if (knowledge.isEmpty()) return;

         ItemStack moved = shelfBe.removeItem(msg.slot, 1);
         if (moved.isEmpty()) return;
         BlockState shelfState = level.getBlockState(msg.shelf);
         if (shelfState.getBlock() instanceof ChiseledBookShelfBlock
               && msg.slot < ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.size()) {
            level.setBlock(msg.shelf,
               shelfState.setValue(ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.get(msg.slot), false), 3);
         }
         shelfBe.setChanged();
         level.playSound(null, msg.shelf, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);

         level.playSound(null, station,
            com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler.BOOK_CLOSE.get(),
            SoundSource.BLOCKS, 1.0F, 1.0F);

         final int duration = 34;
         final ItemStack found = moved.copy();
         final BlockPos shelfPos = msg.shelf;
         final int shelfSlot = msg.slot;

         PacketHandler.INSTANCE.send(
            net.minecraftforge.network.PacketDistributor.NEAR.with(() ->
               new net.minecraftforge.network.PacketDistributor.TargetPoint(
                  station.getX() + 0.5, station.getY() + 0.5, station.getZ() + 0.5, 96.0, level.dimension())),
            new PacketKnowledgeAnimate(msg.shelf, station, station,
               found.copy(), knowledge.copy(), duration, false));

         com.paleimitations.schoolsofmagic.common.handlers.KnowledgeAnimations.schedule(duration, () -> {
            BlockEntity rbe = level.getBlockEntity(station);
            if (rbe instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium p) {
               p.floatedBook = knowledge.copy();
               p.handler.setStackInSlot(0, found.copy());
               p.floated = true;

               p.bookState = com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium.EnumState.CLOSED;
               p.animationTick = 0;
               p.sendUpdates();
            } else if (rbe instanceof net.minecraft.world.level.block.entity.LecternBlockEntity lec) {
               BlockState ls = level.getBlockState(station);
               lec.clearContent();
               level.setBlock(station, ls.setValue(LecternBlock.HAS_BOOK, Boolean.FALSE), 3);
               LecternBlock.tryPlaceBook(sender, level, station, level.getBlockState(station), found.copy());
               PacketHandler.INSTANCE.send(
                  net.minecraftforge.network.PacketDistributor.NEAR.with(() ->
                     new net.minecraftforge.network.PacketDistributor.TargetPoint(
                        station.getX() + 0.5, station.getY() + 0.5, station.getZ() + 0.5, 96.0, level.dimension())),
                  new PacketLecternFloat(station, knowledge.copy()));
            } else {
               net.minecraft.world.level.block.Block.popResource(level, station, found);
               return;
            }
            level.playSound(null, station, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
            com.paleimitations.schoolsofmagic.common.handlers.KnowledgeLoans.add(station, shelfPos, shelfSlot, knowledge);
         });
      });
      context.setPacketHandled(true);
   }

   private static ItemStack readingBook(ServerLevel level, BlockPos reading) {
      BlockEntity be = level.getBlockEntity(reading);
      if (be instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium podium) {
         return podium.handler.getStackInSlot(0);
      }
      if (be instanceof net.minecraft.world.level.block.entity.LecternBlockEntity lectern) {
         return lectern.getBook();
      }
      return ItemStack.EMPTY;
   }
}
