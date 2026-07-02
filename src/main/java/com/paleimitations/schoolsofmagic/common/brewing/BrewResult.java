package com.paleimitations.schoolsofmagic.common.brewing;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

/** The resolved outcome of a dynamic brew. Serialisable onto the brewed tea item. */
public final class BrewResult {

   public String primaryEffect;
   public String secondaryEffect;   // nullable
   public int amplifier;
   public int durationSeconds;
   public int totalStability;
   public int totalToxicity;
   public String modifierDescriptor; // "" when none
   public final List<String> herbsUsed = new ArrayList<>();
   public int finalTeaColor;         // 0xRRGGBB
   public String displayName = "Tea";
   public final List<String> sideEffects = new ArrayList<>();

   public CompoundTag toNbt() {
      CompoundTag tag = new CompoundTag();
      if (primaryEffect != null) tag.putString("primary", primaryEffect);
      if (secondaryEffect != null) tag.putString("secondary", secondaryEffect);
      tag.putInt("amplifier", amplifier);
      tag.putInt("duration", durationSeconds);
      tag.putInt("stability", totalStability);
      tag.putInt("toxicity", totalToxicity);
      tag.putString("modifier", modifierDescriptor == null ? "" : modifierDescriptor);
      tag.putInt("color", finalTeaColor);
      tag.putString("name", displayName);
      tag.put("herbs", stringList(herbsUsed));
      tag.put("sideEffects", stringList(sideEffects));
      return tag;
   }

   public static BrewResult fromNbt(CompoundTag tag) {
      BrewResult r = new BrewResult();
      r.primaryEffect = tag.contains("primary") ? tag.getString("primary") : null;
      r.secondaryEffect = tag.contains("secondary") ? tag.getString("secondary") : null;
      r.amplifier = tag.getInt("amplifier");
      r.durationSeconds = tag.getInt("duration");
      r.totalStability = tag.getInt("stability");
      r.totalToxicity = tag.getInt("toxicity");
      r.modifierDescriptor = tag.getString("modifier");
      r.finalTeaColor = tag.getInt("color");
      r.displayName = tag.getString("name");
      readStringList(tag.getList("herbs", 8), r.herbsUsed);
      readStringList(tag.getList("sideEffects", 8), r.sideEffects);
      return r;
   }

   /** Builds the {@link MobEffectInstance}s to apply when this tea is drunk. */
   public List<MobEffectInstance> buildEffects() {
      List<MobEffectInstance> list = new ArrayList<>();
      int ticks = durationSeconds * 20;
      addEffect(list, primaryEffect, ticks, amplifier);
      addEffect(list, secondaryEffect, ticks, Math.max(0, amplifier - 1));
      for (String side : sideEffects) {
         // side effects are short and weak
         addEffect(list, side, Math.max(100, ticks / 3), 0);
      }
      return list;
   }

   private static void addEffect(List<MobEffectInstance> list, String key, int ticks, int amp) {
      if (key == null) return;
      MobEffect effect = BrewEffects.get(key);
      if (effect != null) {
         list.add(new MobEffectInstance(effect, effect.isInstantenous() ? 1 : ticks, amp));
      }
   }

   private static ListTag stringList(List<String> values) {
      ListTag list = new ListTag();
      for (String v : values) list.add(StringTag.valueOf(v));
      return list;
   }

   private static void readStringList(ListTag list, List<String> out) {
      out.clear();
      for (int i = 0; i < list.size(); i++) out.add(list.getString(i));
   }
}
