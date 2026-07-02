package com.paleimitations.schoolsofmagic.common.world;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class LockedDoorData extends SavedData {

   private static final String NAME = "som_locked_doors";
   private final Set<Long> locked = new HashSet<>();

   public static LockedDoorData get(ServerLevel level) {
      return level.getDataStorage().computeIfAbsent(LockedDoorData::load, LockedDoorData::new, NAME);
   }

   public static LockedDoorData load(CompoundTag tag) {
      LockedDoorData data = new LockedDoorData();
      for (long l : tag.getLongArray("Locked")) {
         data.locked.add(l);
      }
      return data;
   }

   @Override
   public CompoundTag save(CompoundTag tag) {
      tag.putLongArray("Locked", locked.stream().mapToLong(Long::longValue).toArray());
      return tag;
   }

   public void lock(BlockPos pos) {
      if (locked.add(pos.asLong())) {
         setDirty();
      }
   }

   public void unlock(BlockPos pos) {
      if (locked.remove(pos.asLong())) {
         setDirty();
      }
   }

   public boolean isLocked(BlockPos pos) {
      return locked.contains(pos.asLong());
   }
}
