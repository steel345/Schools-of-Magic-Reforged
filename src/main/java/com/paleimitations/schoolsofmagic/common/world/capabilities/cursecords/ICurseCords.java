package com.paleimitations.schoolsofmagic.common.world.capabilities.cursecords;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public interface ICurseCords {
   List<BlockPos> getZigCurseCords();

   void addZigCurseCord(BlockPos var1);

   void removeZigCurseCord(BlockPos var1);

   void setZigCurseCords(List<BlockPos> var1);

   List<BlockPos> getHeartCords();

   void addHeartCord(BlockPos var1);

   void removeHeartCord(BlockPos var1);

   CompoundTag serializeNBT();

   void deserializeNBT(CompoundTag var1);
}
