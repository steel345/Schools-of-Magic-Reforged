package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketKnowledgeAnimate;
import com.paleimitations.schoolsofmagic.common.network.PacketLecternFloat;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;

// Returns a borrowed book from a workstation: the found book flies back to its
// shelf and the workstation's own book floats back into place.
public class KnowledgeReverse {

   public static boolean reverse(ServerLevel level, BlockPos station) {
      KnowledgeLoans.Loan loan = KnowledgeLoans.get(station);
      if (loan == null) return false;
      KnowledgeLoans.remove(station);

      BlockEntity be = level.getBlockEntity(station);
      ItemStack knowledge = loan.knowledge;
      ItemStack found;
      if (be instanceof TileEntityPodium p) {
         found = p.handler.getStackInSlot(0).copy();
         p.handler.setStackInSlot(0, knowledge.copy());
         p.floated = false;
         p.floatedBook = ItemStack.EMPTY;
         p.sendUpdates();
      } else if (be instanceof LecternBlockEntity lec) {
         found = lec.getBook().copy();
         BlockState ls = level.getBlockState(station);
         lec.clearContent();
         level.setBlock(station, ls.setValue(LecternBlock.HAS_BOOK, Boolean.FALSE), 3);
         LecternBlock.tryPlaceBook(null, level, station, level.getBlockState(station), knowledge.copy());
         PacketHandler.INSTANCE.send(near(level, station), new PacketLecternFloat(station, ItemStack.EMPTY));
      } else {
         return false;
      }
      if (found.isEmpty()) return true;

      final int duration = 34;
      PacketHandler.INSTANCE.send(near(level, station),
         new PacketKnowledgeAnimate(loan.shelf, station, station, found.copy(), knowledge.copy(), duration, true));
      level.playSound(null, station, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);

      final ItemStack ret = found.copy();
      KnowledgeAnimations.schedule(duration, () -> {
         if (level.getBlockEntity(loan.shelf) instanceof ChiseledBookShelfBlockEntity shelf
               && loan.slot < shelf.getContainerSize() && shelf.getItem(loan.slot).isEmpty()) {
            shelf.setItem(loan.slot, ret);
            BlockState ss = level.getBlockState(loan.shelf);
            if (ss.getBlock() instanceof ChiseledBookShelfBlock
                  && loan.slot < ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.size()) {
               level.setBlock(loan.shelf,
                  ss.setValue(ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.get(loan.slot), true), 3);
            }
            shelf.setChanged();
            level.playSound(null, loan.shelf, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
         } else {
            Block.popResource(level, loan.shelf, ret);
         }
      });
      return true;
   }

   private static PacketDistributor.PacketTarget near(ServerLevel level, BlockPos pos) {
      return PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(
         pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 96.0, level.dimension()));
   }
}
