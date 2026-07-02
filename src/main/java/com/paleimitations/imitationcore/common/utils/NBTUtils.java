package com.paleimitations.imitationcore.common.utils;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public final class NBTUtils {
   public NBTUtils() {
   }

   public static boolean matchTag(@Nullable Tag template, @Nullable Tag target) {
      if (template instanceof CompoundTag && target instanceof CompoundTag) {
         return matchTagCompound((CompoundTag) template, (CompoundTag) target);
      } else {
         return template instanceof ListTag && target instanceof ListTag
            ? matchTagList((ListTag) template, (ListTag) target)
            : template == null || target != null && target.equals(template);
      }
   }

   private static boolean matchTagCompound(CompoundTag template, CompoundTag target) {
      if (template.size() > target.size()) {
         return false;
      } else {
         for (String key : template.getAllKeys()) {
            if (!matchTag(template.get(key), target.get(key))) {
               return false;
            }
         }

         return true;
      }
   }

   private static boolean matchTagList(ListTag template, ListTag target) {
      if (template.size() > target.size()) {
         return false;
      } else {
         for (int i = 0; i < template.size(); i++) {
            if (!matchTag(template.get(i), target.get(i))) {
               return false;
            }
         }

         return true;
      }
   }
}
