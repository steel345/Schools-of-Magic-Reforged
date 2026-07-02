package com.paleimitations.schoolsofmagic.common.registries;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.quests.Quest;
import com.paleimitations.schoolsofmagic.common.quests.QuestHelper;
import com.paleimitations.schoolsofmagic.common.quests.quests.QuestAdvancedArcana;
import com.paleimitations.schoolsofmagic.common.quests.quests.QuestBrewPotion;
import com.paleimitations.schoolsofmagic.common.quests.quests.QuestBuildGolem;
import com.paleimitations.schoolsofmagic.common.quests.quests.QuestEnchantItem;
import com.paleimitations.schoolsofmagic.common.quests.quests.QuestIntermediateArcana;

import java.util.List;
import java.util.function.Supplier;

public class QuestRegistry {
    public static final List<Quest> QUESTS = Lists.newArrayList();

    private static void tryRegister(String name, Supplier<Quest> supplier) {
        try {
            QuestHelper.registerQuestHelpers(supplier.get());
        } catch (Throwable t) {
            com.paleimitations.schoolsofmagic.common.util.Utils.getLogger()
                .error("Failed to register quest '{}': {}", name, t.toString());
        }
    }

    public static void init() {
        tryRegister("brew_potion",         QuestBrewPotion::new);
        tryRegister("build_golem",         QuestBuildGolem::new);
        tryRegister("enchant_item",        QuestEnchantItem::new);
        tryRegister("intermediate_arcana", QuestIntermediateArcana::new);
        tryRegister("advanced_arcana",     QuestAdvancedArcana::new);
    }

    public static Quest getQuest(String name) {
        for (Quest quest : QUESTS) {
            if (quest.getResourceLocation().toString().equalsIgnoreCase(name)) return quest;
        }
        return null;
    }
}
