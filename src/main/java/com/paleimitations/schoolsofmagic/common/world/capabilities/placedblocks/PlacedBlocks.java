package com.paleimitations.schoolsofmagic.common.world.capabilities.placedblocks;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class PlacedBlocks implements IPlacedBlocks, INBTSerializable<CompoundTag> {
   private final Set<Long> placed = new HashSet<>();

   @Override
   public boolean isPlaced(BlockPos pos) {
      return this.placed.contains(pos.asLong());
   }

   @Override
   public void add(BlockPos pos) {
      this.placed.add(pos.asLong());
   }

   @Override
   public void remove(BlockPos pos) {
      this.placed.remove(pos.asLong());
   }

   @Override
   public int size() {
      return this.placed.size();
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag tag = new CompoundTag();
      long[] out = new long[this.placed.size()];
      int i = 0;
      for (long pos : this.placed) out[i++] = pos;
      tag.putLongArray("placed", out);
      return tag;
   }

   @Override
   public void deserializeNBT(CompoundTag tag) {
      this.placed.clear();
      for (long pos : tag.getLongArray("placed")) this.placed.add(pos);
   }
}
