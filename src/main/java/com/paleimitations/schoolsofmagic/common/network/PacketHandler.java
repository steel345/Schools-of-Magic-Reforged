package com.paleimitations.schoolsofmagic.common.network;

import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
   private static final String PROTOCOL_VERSION = "1";
   public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
      new ResourceLocation("som", "main"),
      () -> PROTOCOL_VERSION,
      PROTOCOL_VERSION::equals,
      PROTOCOL_VERSION::equals
   );
   private static int ID = 0;

   private static int nextID() {
      return ID++;
   }

   public static void registerMessages() {
      INSTANCE.registerMessage(nextID(), PacketGetWorker.class, PacketGetWorker::encode, PacketGetWorker::new, PacketGetWorker::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketReturnWorker.class, PacketReturnWorker::encode, PacketReturnWorker::new, PacketReturnWorker::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketUpdateManaData.class, PacketUpdateManaData::encode, PacketUpdateManaData::new, PacketUpdateManaData::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketUpdateQuestData.class, PacketUpdateQuestData::encode, PacketUpdateQuestData::new, PacketUpdateQuestData::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketUpdateClientManaData.class, PacketUpdateClientManaData::encode, PacketUpdateClientManaData::new, PacketUpdateClientManaData::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketUpdateSummoned.class, PacketUpdateSummoned::encode, PacketUpdateSummoned::new, PacketUpdateSummoned::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketUpdateCreatureBehavior.class, PacketUpdateCreatureBehavior::encode, PacketUpdateCreatureBehavior::new, PacketUpdateCreatureBehavior::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketUpdateBook.class, PacketUpdateBook::encode, PacketUpdateBook::new, PacketUpdateBook::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketBreakCurse.class, PacketBreakCurse::encode, PacketBreakCurse::new, PacketBreakCurse::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketLightningEffect.class, PacketLightningEffect::encode, PacketLightningEffect::new, PacketLightningEffect::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketQueueUpdateClientManaData.class, PacketQueueUpdateClientManaData::encode, PacketQueueUpdateClientManaData::new, PacketQueueUpdateClientManaData::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketGetMortNPest.class, PacketGetMortNPest::encode, PacketGetMortNPest::new, PacketGetMortNPest::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketStirCauldron.class, PacketStirCauldron::encode, PacketStirCauldron::new, PacketStirCauldron::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketSwitchPodiumGui.class, PacketSwitchPodiumGui::encode, PacketSwitchPodiumGui::new, PacketSwitchPodiumGui::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketTurnPage.class, PacketTurnPage::encode, PacketTurnPage::new, PacketTurnPage::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketAddScore.class, PacketAddScore::encode, PacketAddScore::new, PacketAddScore::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketInsertPage.class, PacketInsertPage::encode, PacketInsertPage::new, PacketInsertPage::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketInsertSticker.class, PacketInsertSticker::encode, PacketInsertSticker::new, PacketInsertSticker::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketModifySpell.class, PacketModifySpell::encode, PacketModifySpell::new, PacketModifySpell::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketUpdateSpellFromClient.class, PacketUpdateSpellFromClient::encode, PacketUpdateSpellFromClient::new, PacketUpdateSpellFromClient::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketInsertSpellPage.class, PacketInsertSpellPage::encode, PacketInsertSpellPage::new, PacketInsertSpellPage::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketSelectNoteOption.class, PacketSelectNoteOption::encode, PacketSelectNoteOption::new, PacketSelectNoteOption::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketRemovePage.class, PacketRemovePage::encode, PacketRemovePage::new, PacketRemovePage::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketLidCauldron.class, PacketLidCauldron::encode, PacketLidCauldron::new, PacketLidCauldron::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketUpdateSpellData.class, PacketUpdateSpellData::encode, PacketUpdateSpellData::new, PacketUpdateSpellData::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketSwapSpellCharge.class, PacketSwapSpellCharge::encode, PacketSwapSpellCharge::new, PacketSwapSpellCharge::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketRingBlockCast.class, PacketRingBlockCast::encode, PacketRingBlockCast::new, PacketRingBlockCast::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketSetPotionBagSlot.class, PacketSetPotionBagSlot::encode, PacketSetPotionBagSlot::new, PacketSetPotionBagSlot::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketSetManaStyle.class, PacketSetManaStyle::encode, PacketSetManaStyle::new, PacketSetManaStyle::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketSetManaColor.class, PacketSetManaColor::encode, PacketSetManaColor::new, PacketSetManaColor::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketSetManaPosition.class, PacketSetManaPosition::encode, PacketSetManaPosition::new, PacketSetManaPosition::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketSetIsFancy.class, PacketSetIsFancy::encode, PacketSetIsFancy::new, PacketSetIsFancy::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketSetManaHidden.class, PacketSetManaHidden::encode, PacketSetManaHidden::new, PacketSetManaHidden::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketSetManaOrientation.class, PacketSetManaOrientation::encode, PacketSetManaOrientation::new, PacketSetManaOrientation::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketLetter.class, PacketLetter::encode, PacketLetter::new, PacketLetter::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketNameSpell.class, PacketNameSpell::encode, PacketNameSpell::new, PacketNameSpell::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketQuestNote.class, PacketQuestNote::encode, PacketQuestNote::new, PacketQuestNote::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketReturnIsPressed.class, PacketReturnIsPressed::encode, PacketReturnIsPressed::new, PacketReturnIsPressed::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketTakeLecternBook.class, PacketTakeLecternBook::encode, PacketTakeLecternBook::new, PacketTakeLecternBook::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketOpenLecternBook.class, PacketOpenLecternBook::encode, PacketOpenLecternBook::new, PacketOpenLecternBook::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketSneezeDrop.class, PacketSneezeDrop::encode, PacketSneezeDrop::new, PacketSneezeDrop::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketPhoenixFlight.class, PacketPhoenixFlight::encode, PacketPhoenixFlight::new, PacketPhoenixFlight::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketPhoenixOpenInv.class, PacketPhoenixOpenInv::encode, PacketPhoenixOpenInv::new, PacketPhoenixOpenInv::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketSyncRingData.class, PacketSyncRingData::encode, PacketSyncRingData::new, PacketSyncRingData::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketRingSlotClick.class, PacketRingSlotClick::encode, PacketRingSlotClick::new, PacketRingSlotClick::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketRingCast.class, PacketRingCast::encode, PacketRingCast::new, PacketRingCast::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketRingChannel.class, PacketRingChannel::encode, PacketRingChannel::new, PacketRingChannel::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketRingBind.class, PacketRingBind::encode, PacketRingBind::new, PacketRingBind::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketRingBindPodium.class, PacketRingBindPodium::encode, PacketRingBindPodium::new, PacketRingBindPodium::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketRingConcentration.class, PacketRingConcentration::encode, PacketRingConcentration::new, PacketRingConcentration::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketRingConcentrate.class, PacketRingConcentrate::encode, PacketRingConcentrate::new, PacketRingConcentrate::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketRingUseBlock.class, PacketRingUseBlock::encode, PacketRingUseBlock::new, PacketRingUseBlock::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketRingHold.class, PacketRingHold::encode, PacketRingHold::new, PacketRingHold::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketRingSetSlot.class, PacketRingSetSlot::encode, PacketRingSetSlot::new, PacketRingSetSlot::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketRingSwing.class, PacketRingSwing::encode, PacketRingSwing::new, PacketRingSwing::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketSyncTalismanData.class, PacketSyncTalismanData::encode, PacketSyncTalismanData::new, PacketSyncTalismanData::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketTalismanSlotClick.class, PacketTalismanSlotClick::encode, PacketTalismanSlotClick::new, PacketTalismanSlotClick::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketTalismanActivate.class, PacketTalismanActivate::encode, PacketTalismanActivate::new, PacketTalismanActivate::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketSyncCharmData.class, PacketSyncCharmData::encode, PacketSyncCharmData::new, PacketSyncCharmData::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketCharmSlotClick.class, PacketCharmSlotClick::encode, PacketCharmSlotClick::new, PacketCharmSlotClick::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketSetGrimoireSpell.class, PacketSetGrimoireSpell::encode, PacketSetGrimoireSpell::new, PacketSetGrimoireSpell::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketSetBookPage.class, PacketSetBookPage::encode, PacketSetBookPage::new, PacketSetBookPage::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketSetCastingMode.class, PacketSetCastingMode::encode, PacketSetCastingMode::new, PacketSetCastingMode::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
   }
}
