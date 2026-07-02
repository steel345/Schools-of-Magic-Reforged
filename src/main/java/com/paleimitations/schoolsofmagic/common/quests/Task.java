package com.paleimitations.schoolsofmagic.common.quests;

import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;

public class Task implements INBTSerializable<CompoundTag> {
   public final Task.EnumTaskType taskType;
   public boolean started = false;
   public boolean completed = false;
   public boolean failed = false;
   public boolean isTimed = false;
   public boolean dead = false;
   public int countdown = 0;
   public ItemStack icon;
   public String name;
   public Predicate<Quest> prerequisite = null;
   public Tuple<Integer, Integer> progress = null;

   public Task(Task.EnumTaskType taskType) {
      this.taskType = taskType;
   }

   public void onCompletion(Player player) {
   }

   public void onClaim(Player player) {
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public boolean check(Player player, Object object) {
      return false;
   }

   public boolean tryStart(Quest quest) {
      if (!this.started && quest != null && this.canStart(quest)) {
         this.started = true;
         return true;
      } else {
         return false;
      }
   }

   public void checkEvent(Player player, Object object) {
      if (this.isOngoing() && this.check(player, object)) {
         this.completed = true;
         this.onCompletion(player);
         player.playSound(SoundEvents.PLAYER_LEVELUP, 1.0F, 1.0F);
         System.out.println("Task Checked");
      }
   }

   public void setIcon(ItemStack icon) {
      this.icon = icon;
   }

   public boolean canClaim() {
      return this.completed && !this.dead;
   }

   public void claim(Player player) {
      if (this.canClaim()) {
         this.onClaim(player);
         this.dead = true;
      }
   }

   public boolean isOngoing() {
      return this.started && !this.completed && !this.failed;
   }

   public void update(Player player) {
      if (this.isTimed && this.started && !this.completed) {
         if (this.countdown > 0) {
            this.countdown--;
         } else {
            this.failed = true;
            this.onFailure(player);
         }
      }
   }

   public void onFailure(Player player) {
   }

   public boolean isFailed() {
      return this.failed;
   }

   public void setFailed(boolean failed) {
      this.failed = failed;
   }

   public boolean canStart(Quest quest) {
      return !this.started && (this.prerequisite == null || this.prerequisite.test(quest));
   }

   public void setPrerequisite(Predicate<Quest> prerequisite) {
      this.prerequisite = prerequisite;
   }

   public Predicate<Quest> getPrerequisite() {
      return this.prerequisite;
   }

   public boolean isStarted() {
      return this.started;
   }

   public void setStarted(boolean started) {
      this.started = started;
   }

   public int getCountdown() {
      return this.countdown;
   }

   public void setCountdown(int countdown) {
      this.countdown = countdown;
   }

   public boolean isTimed() {
      return this.isTimed;
   }

   public void setTimed(boolean timed) {
      this.isTimed = timed;
   }

   public boolean isCompleted() {
      return this.completed;
   }

   public void setCompleted(boolean completed) {
      this.completed = completed;
   }

   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      nbt.putBoolean("Timed", this.isTimed);
      nbt.putBoolean("Completed", this.completed);
      nbt.putBoolean("Failed", this.failed);
      nbt.putBoolean("Started", this.started);
      nbt.putInt("Countdown", this.countdown);
      if (this.name != null) {
         nbt.putString("Name", this.name);
      }

      if (this.icon != null) {
         nbt.put("Icon", this.icon.save(new CompoundTag()));
      }

      if (this.progress != null) {
         nbt.putInt("ProgressMin", this.progress.getA());
         nbt.putInt("ProgressMax", this.progress.getB());
      }

      return nbt;
   }

   public void deserializeNBT(CompoundTag nbt) {
      this.isTimed = nbt.getBoolean("Timed");
      this.completed = nbt.getBoolean("Completed");
      this.failed = nbt.getBoolean("Failed");
      this.started = nbt.getBoolean("Started");
      this.countdown = nbt.getInt("Countdown");
      if (nbt.contains("Name")) {
         this.name = nbt.getString("Name");
      }

      if (nbt.contains("Icon")) {
         this.icon = ItemStack.of(nbt.getCompound("Icon"));
      }

      if (nbt.contains("ProgressMin") && nbt.contains("ProgressMax")) {
         this.progress = new Tuple<>(nbt.getInt("ProgressMin"), nbt.getInt("ProgressMax"));
      }
   }

   public static enum EnumTaskType implements StringRepresentable {
      RETRIEVE,
      BATTLE,
      BEFRIEND,
      BUILD,
      BREAK,
      GO,
      POTION_BREW,
      MORTAR,
      BASIN,
      BREED,
      ENCHANT,
      SPELL,
      LIGHT_BRAZIER,
      RITUAL_RECIPE,
      OTHER;

      private EnumTaskType() {
      }

      public String getSerializedName() {
         return this.name().toLowerCase();
      }
   }
}
