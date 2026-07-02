package com.paleimitations.schoolsofmagic.common.registries;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.client_mana_data.IClientManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.ICreatureBehavior;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.effect_data.IEffectVariables;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.quests.IQuestData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.page.IPage;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_modifier.ISpellModifier;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.ISpellNotes;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Bus.MOD)
public class CapabilityRegistry {

   @SubscribeEvent
   public static void registerCapabilities(RegisterCapabilitiesEvent event) {

      event.register(IWandData.class);
      event.register(IBook.class);
      event.register(IPage.class);
      event.register(ISpellModifier.class);
      event.register(ISpellNotes.class);
      event.register(IQuestData.class);
      event.register(com.paleimitations.schoolsofmagic.common.entity.capabilities.meteoric_data.IMeteoricData.class);
      event.register(com.paleimitations.schoolsofmagic.common.entity.capabilities.player_quests.IPlayerQuests.class);
      event.register(com.paleimitations.schoolsofmagic.common.entity.capabilities.spell_button.ISpellButton.class);
      event.register(com.paleimitations.schoolsofmagic.common.entity.capabilities.summoned.ISummoned.class);
      event.register(com.paleimitations.schoolsofmagic.common.entity.capabilities.transfigured.ITransfigured.class);
      event.register(com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.IPotionData.class);
      event.register(com.paleimitations.schoolsofmagic.common.tileentity.capabilities.worker.IWork.class);
      event.register(com.paleimitations.schoolsofmagic.common.tileentity.capabilities.entitystorage.IEntityStorage.class);
      event.register(com.paleimitations.schoolsofmagic.common.world.capabilities.banishedblocks.IBanishedBlocks.class);
      event.register(com.paleimitations.schoolsofmagic.common.world.capabilities.cursecords.ICurseCords.class);
      event.register(com.paleimitations.schoolsofmagic.common.world.weather.IWeatherStorage.class);
   }
}
