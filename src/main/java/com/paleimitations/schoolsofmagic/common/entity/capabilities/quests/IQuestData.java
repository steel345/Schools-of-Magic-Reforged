package com.paleimitations.schoolsofmagic.common.entity.capabilities.quests;

import com.paleimitations.schoolsofmagic.common.quests.Quest;
import java.util.List;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public interface IQuestData {
   List<Quest> getQuests();

   void addQuest(Quest var1);

   void updateQuests(Player var1);

   CompoundTag serializeNBT();

   void deserializeNBT(CompoundTag var1);

   boolean hasQuest(UUID var1);

   Quest getQuestbyQuestGiver(UUID var1);
}
