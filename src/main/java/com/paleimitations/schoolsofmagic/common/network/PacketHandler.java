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
      INSTANCE.registerMessage(nextID(), PacketKnowledgeRequest.class, PacketKnowledgeRequest::encode, PacketKnowledgeRequest::new, PacketKnowledgeRequest::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketKnowledgeResponse.class, PacketKnowledgeResponse::encode, PacketKnowledgeResponse::new, PacketKnowledgeResponse::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketKnowledgeFetch.class, PacketKnowledgeFetch::encode, PacketKnowledgeFetch::new, PacketKnowledgeFetch::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketKnowledgeAnimate.class, PacketKnowledgeAnimate::encode, PacketKnowledgeAnimate::new, PacketKnowledgeAnimate::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketLecternFloat.class, PacketLecternFloat::encode, PacketLecternFloat::new, PacketLecternFloat::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
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
      INSTANCE.registerMessage(nextID(), PacketLecternPage.class, PacketLecternPage::encode, PacketLecternPage::new, PacketLecternPage::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketOpenLecternPage.class, PacketOpenLecternPage::encode, PacketOpenLecternPage::new, PacketOpenLecternPage::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketSneezeDrop.class, PacketSneezeDrop::encode, PacketSneezeDrop::new, PacketSneezeDrop::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketPhoenixFlight.class, PacketPhoenixFlight::encode, PacketPhoenixFlight::new, PacketPhoenixFlight::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketBroomSprint.class, PacketBroomSprint::encode, PacketBroomSprint::new, PacketBroomSprint::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
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
      INSTANCE.registerMessage(nextID(), PacketOpenHerbPouch.class, PacketOpenHerbPouch::encode, PacketOpenHerbPouch::new, PacketOpenHerbPouch::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketOpenPotionBag.class, PacketOpenPotionBag::encode, PacketOpenPotionBag::new, PacketOpenPotionBag::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketThrowCharmPotion.class, PacketThrowCharmPotion::encode, PacketThrowCharmPotion::new, PacketThrowCharmPotion::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketPageUnlockToast.class, PacketPageUnlockToast::encode, PacketPageUnlockToast::new, PacketPageUnlockToast::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketPageUpdateToast.class, PacketPageUpdateToast::encode, PacketPageUpdateToast::new, PacketPageUpdateToast::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketRenameBook.class, PacketRenameBook::encode, PacketRenameBook::new, PacketRenameBook::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketWritePage.class, PacketWritePage::encode, PacketWritePage::new, PacketWritePage::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketSetPageOverride.class, PacketSetPageOverride::encode, PacketSetPageOverride::new, PacketSetPageOverride::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketSetPageLayout.class, PacketSetPageLayout::encode, PacketSetPageLayout::new, PacketSetPageLayout::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketSyncPageUnlocks.class, PacketSyncPageUnlocks::encode, PacketSyncPageUnlocks::new, PacketSyncPageUnlocks::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketSyncWandDisplay.class, PacketSyncWandDisplay::encode, PacketSyncWandDisplay::new, PacketSyncWandDisplay::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketMarkPageRead.class, PacketMarkPageRead::encode, PacketMarkPageRead::new, PacketMarkPageRead::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketDryadQuest.class, PacketDryadQuest::encode, PacketDryadQuest::new, PacketDryadQuest::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketSyncPlayerQuests.class, PacketSyncPlayerQuests::encode, PacketSyncPlayerQuests::new, PacketSyncPlayerQuests::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketSyncGarmentData.class, PacketSyncGarmentData::encode, PacketSyncGarmentData::new, PacketSyncGarmentData::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketGarmentSlotClick.class, PacketGarmentSlotClick::encode, PacketGarmentSlotClick::new, PacketGarmentSlotClick::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketSunBeam.class, PacketSunBeam::encode, PacketSunBeam::new, PacketSunBeam::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketSetGrimoireSpell.class, PacketSetGrimoireSpell::encode, PacketSetGrimoireSpell::new, PacketSetGrimoireSpell::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketSetBookPage.class, PacketSetBookPage::encode, PacketSetBookPage::new, PacketSetBookPage::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketSetCastingMode.class, PacketSetCastingMode::encode, PacketSetCastingMode::new, PacketSetCastingMode::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketDummyDamage.class, PacketDummyDamage::encode, PacketDummyDamage::new, PacketDummyDamage::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketSmokeScry.class, PacketSmokeScry::encode, PacketSmokeScry::new, PacketSmokeScry::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketIceShell.class, PacketIceShell::encode, PacketIceShell::new, PacketIceShell::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketEclipseState.class, PacketEclipseState::encode, PacketEclipseState::new, PacketEclipseState::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketSetMirrorCoords.class, PacketSetMirrorCoords::encode, PacketSetMirrorCoords::new, PacketSetMirrorCoords::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketRiftView.class, PacketRiftView::encode, PacketRiftView::new, PacketRiftView::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketGaianWarriorBar.class, PacketGaianWarriorBar::encode, PacketGaianWarriorBar::new, PacketGaianWarriorBar::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketEarthenRide.class, PacketEarthenRide::encode, PacketEarthenRide::new, PacketEarthenRide::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketDazzlingLight.class, PacketDazzlingLight::encode, PacketDazzlingLight::new, PacketDazzlingLight::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketTeleportPuff.class, PacketTeleportPuff::encode, PacketTeleportPuff::new, PacketTeleportPuff::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketGaseousForm.class, PacketGaseousForm::encode, PacketGaseousForm::new, PacketGaseousForm::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketAlarmAlert.class, PacketAlarmAlert::encode, PacketAlarmAlert::new, PacketAlarmAlert::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketAlarmSound.class, PacketAlarmSound::encode, PacketAlarmSound::new, PacketAlarmSound::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketSilence.class, PacketSilence::encode, PacketSilence::new, PacketSilence::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketAnimalScry.class, PacketAnimalScry::encode, PacketAnimalScry::new, PacketAnimalScry::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketBiomeScry.class, PacketBiomeScry::encode, PacketBiomeScry::new, PacketBiomeScry::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      INSTANCE.registerMessage(nextID(), PacketScryTrail.class, PacketScryTrail::encode, PacketScryTrail::new, PacketScryTrail::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketAstralSwell.class, PacketAstralSwell::encode, PacketAstralSwell::new, PacketAstralSwell::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketShiningShield.class, PacketShiningShield::encode, PacketShiningShield::new, PacketShiningShield::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
      INSTANCE.registerMessage(nextID(), PacketNecklaceBreak.class, PacketNecklaceBreak::encode, PacketNecklaceBreak::new, PacketNecklaceBreak::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
   }
}
