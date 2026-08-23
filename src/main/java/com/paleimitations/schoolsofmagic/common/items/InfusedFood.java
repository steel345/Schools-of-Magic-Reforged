package com.paleimitations.schoolsofmagic.common.items;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

public final class InfusedFood {
   private static final String KEY = "SomInfused";
   private static final String EFFECTS = "Effects";
   private static final String INFUSER = "Infuser";

   private InfusedFood() {
   }

   public static void infuse(ItemStack food, List<MobEffectInstance> effects, UUID infuser) {
      if (effects == null || effects.isEmpty()) {
         return;
      }
      CompoundTag tag = food.getOrCreateTagElement(KEY);
      ListTag list = new ListTag();
      for (MobEffectInstance e : effects) {
         list.add(e.save(new CompoundTag()));
      }
      tag.put(EFFECTS, list);
      if (infuser != null) {
         tag.putUUID(INFUSER, infuser);
      }
   }

   public static boolean isInfused(ItemStack s) {
      CompoundTag tag = s.getTagElement(KEY);
      return tag != null && tag.contains(EFFECTS);
   }

   public static List<MobEffectInstance> getEffects(ItemStack s) {
      List<MobEffectInstance> out = new ArrayList<>();
      CompoundTag tag = s.getTagElement(KEY);
      if (tag == null) {
         return out;
      }
      ListTag list = tag.getList(EFFECTS, Tag.TAG_COMPOUND);
      for (int i = 0; i < list.size(); i++) {
         MobEffectInstance e = MobEffectInstance.load(list.getCompound(i));
         if (e != null) {
            out.add(e);
         }
      }
      return out;
   }

   public static UUID getInfuser(ItemStack s) {
      CompoundTag tag = s.getTagElement(KEY);
      return tag != null && tag.hasUUID(INFUSER) ? tag.getUUID(INFUSER) : null;
   }
}
