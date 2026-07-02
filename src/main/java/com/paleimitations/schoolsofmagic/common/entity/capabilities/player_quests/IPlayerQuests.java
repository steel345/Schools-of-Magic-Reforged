package com.paleimitations.schoolsofmagic.common.entity.capabilities.player_quests;

import com.paleimitations.schoolsofmagic.common.entity.EntityDryad;
import com.paleimitations.schoolsofmagic.common.entity.Quest;
import java.util.UUID;

public interface IPlayerQuests {
   boolean isOnQuest();

   void setOnQuest(boolean var1);

   boolean hasSucceeded();

   void setSuccess(boolean var1);

   int getQuestID();

   void setQuestID(int var1);

   void setQuest(Quest var1, EntityDryad var2);

   void reset();

   int getTimer();

   void setTimer(int var1);

   UUID getQuestGiver();

   void setQuestGiver(UUID var1);

   int getHolder();

   void setHolder(int var1);

   int getMaxHolder();

   void setMaxHolder(int var1);
}
