package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.client.guis.GuiCatalystBasin;
import com.paleimitations.schoolsofmagic.client.guis.GuiCauldron;
import com.paleimitations.schoolsofmagic.client.guis.GuiEntityIntelligent;
import com.paleimitations.schoolsofmagic.client.guis.GuiEntitySqueakard;
import com.paleimitations.schoolsofmagic.client.guis.GuiMortNPest;
import com.paleimitations.schoolsofmagic.client.guis.GuiPotionBag;
import com.paleimitations.schoolsofmagic.client.guis.GuiSpellForge;
import com.paleimitations.schoolsofmagic.client.guis.GuiTeapot;
import com.paleimitations.schoolsofmagic.client.guis.podium.GuiPodiumCopy;
import com.paleimitations.schoolsofmagic.client.guis.podium.GuiPodiumEdit;
import com.paleimitations.schoolsofmagic.client.guis.podium.GuiPodiumFinalize;
import com.paleimitations.schoolsofmagic.client.guis.podium.GuiPodiumNote;
import com.paleimitations.schoolsofmagic.client.guis.podium.GuiPodiumRead;
import com.paleimitations.schoolsofmagic.client.guis.podium.GuiPodiumSpell;
import com.paleimitations.schoolsofmagic.common.registries.MenuTypeRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GuiHandler {
   @OnlyIn(Dist.CLIENT)
   public static void register() {
      MenuScreens.register(MenuTypeRegistry.CAULDRON.get(), GuiCauldron::new);
      MenuScreens.register(MenuTypeRegistry.POTION_BAG.get(), GuiPotionBag::new);
      MenuScreens.register(MenuTypeRegistry.BOOK_FRAME.get(), com.paleimitations.schoolsofmagic.client.guis.GuiBookFrame::new);
      MenuScreens.register(MenuTypeRegistry.MORT_N_PEST.get(), GuiMortNPest::new);
      MenuScreens.register(MenuTypeRegistry.INTELLIGENT.get(), GuiEntityIntelligent::new);
      MenuScreens.register(MenuTypeRegistry.PODIUM_READ.get(), GuiPodiumRead::new);
      MenuScreens.register(MenuTypeRegistry.PODIUM_COPY.get(), GuiPodiumCopy::new);
      MenuScreens.register(MenuTypeRegistry.PODIUM_EDIT.get(), GuiPodiumEdit::new);
      MenuScreens.register(MenuTypeRegistry.PODIUM_FINAL.get(), GuiPodiumFinalize::new);
      MenuScreens.register(MenuTypeRegistry.PODIUM_NOTE.get(), GuiPodiumNote::new);
      MenuScreens.register(MenuTypeRegistry.PODIUM_SPELL.get(), GuiPodiumSpell::new);
      MenuScreens.register(MenuTypeRegistry.SPELL_FORGE.get(), GuiSpellForge::new);
      MenuScreens.register(MenuTypeRegistry.CATALYST_BASIN.get(), GuiCatalystBasin::new);
      MenuScreens.register(MenuTypeRegistry.TEAPOT.get(), GuiTeapot::new);
      MenuScreens.register(MenuTypeRegistry.SQUEAKARD.get(), GuiEntitySqueakard::new);
      MenuScreens.register(MenuTypeRegistry.PHOENIX.get(), com.paleimitations.schoolsofmagic.client.guis.GuiPhoenix::new);
   }
}
