package com.paleimitations.schoolsofmagic.common.entity.capabilities.player_quests;

import com.paleimitations.schoolsofmagic.common.entity.EntityDryad;
import com.paleimitations.schoolsofmagic.common.entity.Quest;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class PlayerQuests implements INBTSerializable<CompoundTag>, IPlayerQuests {
   private boolean success;
   private boolean onQuest;
   private int questID;
   private UUID questGiver;
   private int timer;
   private int holder;
   private int maxHolder;

   @Override
   public int getHolder() {
      return this.holder;
   }

   @Override
   public void setHolder(int holder) {
      this.holder = holder;
   }

   @Override
   public int getMaxHolder() {
      return this.maxHolder;
   }

   @Override
   public void setMaxHolder(int maxHolder) {
      this.maxHolder = maxHolder;
   }

   @Override
   public boolean isOnQuest() {
      return this.onQuest;
   }

   @Override
   public void setOnQuest(boolean quest) {
      this.onQuest = quest;
   }

   @Override
   public boolean hasSucceeded() {
      return this.success;
   }

   @Override
   public void setSuccess(boolean hasSucceeded) {
      this.success = hasSucceeded;
   }

   @Override
   public int getQuestID() {
      return this.questID;
   }

   @Override
   public void setQuestID(int i) {
      this.questID = i;
   }

   @Override
   public void setQuest(Quest quest, EntityDryad questGiver) {
      if (!this.onQuest) {
         this.setOnQuest(true);
         this.setSuccess(false);
         this.setQuestID(quest.getId());
         this.setTimer(quest.getTime());
         this.setQuestGiver(questGiver.getUUID());
         this.setMaxHolder(quest.getMaxHolder());
      }
   }

   @Override
   public void reset() {
      this.setOnQuest(false);
      this.setSuccess(false);
      this.setQuestGiver(null);
      this.setQuestID(0);
      this.setTimer(0);
      this.setHolder(0);
      this.setMaxHolder(0);
   }

   @Override
   public int getTimer() {
      return this.timer;
   }

   @Override
   public void setTimer(int i) {
      this.timer = i;
   }

   @Override
   public UUID getQuestGiver() {
      return this.questGiver;
   }

   @Override
   public void setQuestGiver(UUID questGiver) {
      this.questGiver = questGiver;
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      nbt.putBoolean("onQuest", this.isOnQuest());
      nbt.putBoolean("success", this.hasSucceeded());
      nbt.putInt("questID", this.getQuestID());
      nbt.putInt("timer", this.getTimer());
      if (this.getQuestGiver() != null) {
         nbt.putString("questGiver", this.getQuestGiver().toString());
      }
      nbt.putInt("maxHolder", this.getMaxHolder());
      nbt.putInt("holder", this.getHolder());
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      this.setOnQuest(nbt.getBoolean("onQuest"));
      this.setSuccess(nbt.getBoolean("success"));
      this.setQuestID(nbt.getInt("questID"));
      this.setTimer(nbt.getInt("timer"));
      if (nbt.contains("questGiver")) {
         this.setQuestGiver(UUID.fromString(nbt.getString("questGiver")));
      }
      this.setHolder(nbt.getInt("holder"));
      this.setMaxHolder(nbt.getInt("maxHolder"));
   }
}
