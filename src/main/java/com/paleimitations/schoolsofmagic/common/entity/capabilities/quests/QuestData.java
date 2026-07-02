package com.paleimitations.schoolsofmagic.common.entity.capabilities.quests;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.quests.Quest;
import com.paleimitations.schoolsofmagic.common.quests.QuestHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

public class QuestData implements IQuestData, INBTSerializable<CompoundTag> {
   public List<Quest> quests = Lists.newArrayList();

   @Override
   public List<Quest> getQuests() {
      return this.quests;
   }

   @Override
   public void addQuest(Quest quest) {
      this.quests.add(quest);
   }

   @Override
   public void updateQuests(Player player) {
      ArrayList<Quest> reset = Lists.newArrayList();
      for (Quest quest : this.quests) {
         quest.update(player);
         if (quest.dead) {
            continue;
         }
         reset.add(quest);
      }
      this.quests = reset;
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      nbt.putInt("NumberOfQuests", this.quests.size());
      for (int i = 0; i < this.quests.size(); ++i) {
         nbt.putString("QuestLocation" + i, this.quests.get(i).getResourceLocation().toString());
         nbt.put("QuestData" + i, this.quests.get(i).serializeNBT());
      }
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      this.quests.clear();
      for (int i = 0; i < nbt.getInt("NumberOfQuests"); ++i) {
         Quest q;
         if (!nbt.contains("QuestLocation" + i) || !nbt.contains("QuestData" + i)
            || (q = QuestHelper.getQuestInstance(new ResourceLocation(nbt.getString("QuestLocation" + i)), nbt.getCompound("QuestData" + i))) == null) {
            continue;
         }
         if (q.getQuestGiver() == null) {
            System.out.println("bad news");
         }
         this.quests.add(q);
      }
   }

   @Override
   public boolean hasQuest(UUID questGiver) {
      for (Quest quest : this.quests) {
         if (quest.getQuestGiver() == null || !quest.getQuestGiver().equals(questGiver)) {
            continue;
         }
         return true;
      }
      return false;
   }

   @Override
   public Quest getQuestbyQuestGiver(UUID questGiver) {
      for (Quest quest : this.quests) {
         if (quest.getQuestGiver() == null || !quest.getQuestGiver().equals(questGiver)) {
            continue;
         }
         return quest;
      }
      return null;
   }
}
