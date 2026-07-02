package com.paleimitations.schoolsofmagic.common.world.capabilities.cursecords;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class CurseCords implements INBTSerializable<CompoundTag>, ICurseCords {
   private List<BlockPos> zigCords = Lists.newArrayList();
   private List<BlockPos> heartCords = Lists.newArrayList();

   @Override
   public List<BlockPos> getHeartCords() {
      return this.heartCords;
   }

   @Override
   public void addHeartCord(BlockPos pos) {
      this.removeHeartCord(pos);
      this.heartCords.add(pos.immutable());
   }

   @Override
   public void removeHeartCord(BlockPos pos) {
      this.heartCords.removeIf(p -> p.equals(pos));
   }

   public CurseCords() {
   }

   @Override
   public List<BlockPos> getZigCurseCords() {
      return this.zigCords;
   }

   @Override
   public void addZigCurseCord(BlockPos pos) {
      this.zigCords.add(pos);
   }

   @Override
   public void removeZigCurseCord(BlockPos pos) {
      List<BlockPos> list = Lists.newArrayList();

      for (BlockPos posit : this.zigCords) {
         if (posit != pos) {
            list.add(posit);
         }
      }

      this.setZigCurseCords(list);
   }

   @Override
   public void setZigCurseCords(List<BlockPos> list) {
      this.zigCords = list;
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      nbt.putInt("zigCords", this.zigCords.size());

      for (int i = 0; i < this.zigCords.size(); i++) {
         nbt.putLong("zig" + String.valueOf(i), this.zigCords.get(i).asLong());
      }

      nbt.putInt("heartCords", this.heartCords.size());
      for (int i = 0; i < this.heartCords.size(); i++) {
         nbt.putLong("heart" + String.valueOf(i), this.heartCords.get(i).asLong());
      }

      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      List<BlockPos> zigCords1 = new ArrayList<>();

      for (int i = 0; i < nbt.getInt("zigCords"); i++) {
         zigCords1.add(BlockPos.of(nbt.getLong("zig" + String.valueOf(i))));
      }

      this.zigCords = zigCords1;

      List<BlockPos> heartCords1 = new ArrayList<>();
      for (int i = 0; i < nbt.getInt("heartCords"); i++) {
         heartCords1.add(BlockPos.of(nbt.getLong("heart" + String.valueOf(i))));
      }
      this.heartCords = heartCords1;
   }
}
