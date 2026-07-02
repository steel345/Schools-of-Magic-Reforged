package com.paleimitations.schoolsofmagic.common.world.capabilities.banishedblocks;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.INBTSerializable;

public class BanishedBlocks implements INBTSerializable<CompoundTag>, IBanishedBlocks {
   private Map<BlockPos, BlockState> previousStates = Maps.newHashMap();
   private Map<BlockPos, Integer> countdowns = Maps.newHashMap();

   @Override
   public boolean isBanished(BlockPos pos) {
      if (this.previousStates.containsKey(pos) && this.countdowns.containsKey(pos)) {
         return true;
      }
      if (this.previousStates.containsKey(pos)) {
         this.previousStates.remove(pos);
      }
      if (this.countdowns.containsKey(pos)) {
         this.countdowns.remove(pos);
      }
      return false;
   }

   @Override
   public BlockState getPreviousState(BlockPos pos) {
      return this.previousStates.get(pos);
   }

   @Override
   public int getTimer(BlockPos pos) {
      return this.countdowns.get(pos);
   }

   @Override
   public void setTimer(BlockPos pos, int timer) {
      this.countdowns.replace(pos, timer);
   }

   @Override
   public void addSet(BlockPos pos, BlockState state, int timer) {
      if (!this.isBanished(pos)) {
         this.previousStates.put(pos, state);
         this.countdowns.put(pos, timer);
      }
   }

   @Override
   public void removeSet(BlockPos pos) {
      HashMap<BlockPos, BlockState> previousStatesIn = Maps.newHashMap();
      HashMap<BlockPos, Integer> countdownsIn = Maps.newHashMap();
      for (Map.Entry<BlockPos, BlockState> entry : this.previousStates.entrySet()) {
         if (entry.getKey() == pos) continue;
         previousStatesIn.put(entry.getKey(), entry.getValue());
      }
      for (Map.Entry<BlockPos, Integer> entry : this.countdowns.entrySet()) {
         if (entry.getKey() == pos) continue;
         countdownsIn.put(entry.getKey(), entry.getValue());
      }
      this.previousStates = previousStatesIn;
      this.countdowns = countdownsIn;
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      ListTag nbtTagListIn = new ListTag();
      for (BlockPos pos : this.previousStates.keySet()) {
         CompoundTag comp = new CompoundTag();
         if (!this.isBanished(pos)) continue;
         comp.putLong("BlockPos", pos.asLong());
         comp.putInt("Countdown", this.countdowns.get(pos).intValue());
         BlockState state = this.previousStates.get(pos);
         if (state != null) {

            comp.put("State", NbtUtils.writeBlockState(state));
         }
         nbtTagListIn.add(comp);
      }
      nbt.put("BanishedBlocks", nbtTagListIn);
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      this.clear();
      ListTag nbttaglist = nbt.getList("BanishedBlocks", Tag.TAG_COMPOUND);
      for (int i = 0; i < nbttaglist.size(); ++i) {
         CompoundTag nbttagcompound = nbttaglist.getCompound(i);
         BlockState state = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), nbttagcompound.getCompound("State"));
         this.addSet(BlockPos.of(nbttagcompound.getLong("BlockPos")), state, nbttagcompound.getInt("Countdown"));
      }
   }

   @Override
   public Map<BlockPos, Integer> getCountdowns() {
      return this.countdowns;
   }

   @Override
   public Map<BlockPos, BlockState> getPreviousStates() {
      return this.previousStates;
   }

   @Override
   public void clear() {
      this.countdowns.clear();
      this.previousStates.clear();
   }
}
