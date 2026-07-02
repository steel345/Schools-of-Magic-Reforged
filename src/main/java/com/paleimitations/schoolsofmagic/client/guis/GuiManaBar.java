package com.paleimitations.schoolsofmagic.client.guis;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.client_mana_data.CapabilityClientManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.client_mana_data.IClientManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.items.ItemBaseWand;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketQueueUpdateClientManaData;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class GuiManaBar {
   private static final ResourceLocation METAL_FANCY_NS = new ResourceLocation("som", "textures/gui/mana_bar_metal_fancy_ns.png");
   private static final ResourceLocation METAL_FANCY_EW = new ResourceLocation("som", "textures/gui/mana_bar_metal_fancy_ew.png");
   private static final ResourceLocation METAL_SIMPLE_NS = new ResourceLocation("som", "textures/gui/mana_bar_metal_simple_ns.png");
   private static final ResourceLocation METAL_SIMPLE_EW = new ResourceLocation("som", "textures/gui/mana_bar_metal_simple_ew.png");
   private static final ResourceLocation DARK_FANCY_NS = new ResourceLocation("som", "textures/gui/mana_bar_dark_fancy_ns.png");
   private static final ResourceLocation DARK_FANCY_EW = new ResourceLocation("som", "textures/gui/mana_bar_dark_fancy_ew.png");
   private static final ResourceLocation DARK_SIMPLE_NS = new ResourceLocation("som", "textures/gui/mana_bar_dark_simple_ns.png");
   private static final ResourceLocation DARK_SIMPLE_EW = new ResourceLocation("som", "textures/gui/mana_bar_dark_simple_ew.png");
   private static final ResourceLocation LIGHT_FANCY_NS = new ResourceLocation("som", "textures/gui/mana_bar_light_fancy_ns.png");
   private static final ResourceLocation LIGHT_FANCY_EW = new ResourceLocation("som", "textures/gui/mana_bar_light_fancy_ew.png");
   private static final ResourceLocation LIGHT_SIMPLE_NS = new ResourceLocation("som", "textures/gui/mana_bar_light_simple_ns.png");
   private static final ResourceLocation LIGHT_SIMPLE_EW = new ResourceLocation("som", "textures/gui/mana_bar_light_simple_ew.png");

   private SOMProgressBar progressBarMana;
   private SOMProgressBar progressBarDeadMana;
   private SOMProgressBar progressBarXP;

   public static boolean hidden = false;

   public GuiManaBar() {}

   @SubscribeEvent
   public void renderManaBar(RenderGuiOverlayEvent.Post event) {
      if (event.getOverlay() != VanillaGuiOverlay.HOTBAR.type()) return;
      if (hidden) return;
      Minecraft mc = Minecraft.getInstance();
      LocalPlayer player = mc.player;
      if (player == null || player.isSpectator() || mc.options.hideGui) return;
      boolean wandHeld = player.getMainHandItem().getItem() instanceof ItemBaseWand
            || mc.screen instanceof GuiManaOptions
            || com.paleimitations.schoolsofmagic.common.handlers.RingCastHandler.isRingActive(player)
            || (player.getMainHandItem().getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemSpellbook
                && com.paleimitations.schoolsofmagic.common.items.ItemSpellbook.isCastingMode(player.getMainHandItem()));
      if (!wandHeld) return;

      IManaData mana = player.getCapability(CapabilityManaData.CAP).orElse(null);
      IClientManaData clientMana = player.getCapability(CapabilityClientManaData.CAP).orElse(null);
      if (mana == null || clientMana == null) return;
      if (!clientMana.isLoadedToClient()) {
         PacketHandler.INSTANCE.sendToServer(new PacketQueueUpdateClientManaData());
      }
      if (mana.getMaxMana() == 0 || player.isSpectator()) return;

      GuiGraphics gg = event.getGuiGraphics();
      float screenWidth = gg.guiWidth();
      float screenHeight = gg.guiHeight();

      boolean isSimpleGUI = clientMana.isSimpleGui() || GuiSpellCharges.isHudOpen();
      int guiStyle = clientMana.getGuiStyle();
      int guiColor = clientMana.getGuiColor();
      int guiOrientation = clientMana.getGuiOrientation();
      int guiXPos = clientMana.getGuiXPos();
      int guiYPos = clientMana.getGuiYPos();

      boolean isVertical = false;
      int tex_width = 136;
      int tex_height = 149;
      int backgroundTextX = 0;
      int backgroundTextY = 0;
      ResourceLocation loctaion = METAL_FANCY_NS;
      int xPos = Math.round(screenWidth * 0.5F + (screenWidth - (float) tex_width) * 0.5F * ((float) guiXPos / 50.0F)) - Math.round((float) tex_width / 2.0F);
      int yPos = Math.round(screenHeight * 0.5F + (screenHeight - (float) tex_height) * 0.5F * ((float) guiYPos / 50.0F)) - Math.round((float) tex_height / 2.0F);
      int gemWidth = 7;
      int gemHeight = 8;
      int gemTextX = 141;
      int gemTextY = 0;
      int gemX = 14;
      int gemY = 27;
      int poolWidth = 13;
      int poolHeight = 10;
      int poolTextX = 139;
      int poolTextY = 52;
      int poolX = 11;
      int poolY = 134;
      int manaTextColor = 0;
      switch (guiColor) {
         case 0: manaTextColor = 2841507; break;
         case 1: manaTextColor = 2332982; break;
         case 2: manaTextColor = 10387999; break;
         case 3: manaTextColor = 10638866; break;
         case 4: manaTextColor = 11151134; break;
         case 5: manaTextColor = 5707887;
      }

      int levelTextColor = 13088328;
      float levelTextX = 18.0F;
      float levelTextY = 22.0F;
      float levelBox = 13.0F;
      this.progressBarMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.DOWN_TO_UP, 5, 96, 15 + xPos, 35 + yPos, 5 * guiColor, 155);
      this.progressBarDeadMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.DOWN_TO_UP, 5, 96, 15 + xPos, 35 + yPos, 30 + 5 * guiColor, 155);
      this.progressBarXP = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.RIGHT_TO_LEFT, 97, 5, 34 + xPos, 20 + yPos, 139, 20 + 5 * guiColor);
      if (guiStyle == 0) {
         if (isSimpleGUI) {
            if (guiOrientation == 0) {
               loctaion = METAL_SIMPLE_NS; tex_width = 23; tex_height = 112;
               gemTextX = 159; gemTextY = 36; gemX = 8; gemY = 16;
               poolTextX = 159; poolTextY = 24; poolX = 5; poolY = 98;
               levelTextX = 12.0F; levelTextY = 11.0F;
               xPos = Math.round(screenWidth * 0.5F + (screenWidth - (float) tex_width) * 0.5F * ((float) guiXPos / 50.0F)) - Math.round((float) tex_width / 2.0F);
               yPos = Math.round(screenHeight * 0.5F + (screenHeight - (float) tex_height) * 0.5F * ((float) guiYPos / 50.0F)) - Math.round((float) tex_height / 2.0F);
               this.progressBarMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.DOWN_TO_UP, 2, 70, 9 + xPos, 24 + yPos, 23 + 2 * guiColor, 24);
               this.progressBarDeadMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.DOWN_TO_UP, 2, 70, 9 + xPos, 24 + yPos, 35 + 2 * guiColor, 24);
               this.progressBarXP = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.DOWN_TO_UP, 2, 70, 12 + xPos, 24 + yPos, 47 + 2 * guiColor, 24);
            } else if (guiOrientation == 1) {
               loctaion = METAL_SIMPLE_EW; tex_width = 118; tex_height = 23; backgroundTextY = 95; isVertical = true;
               gemTextX = 109; gemTextY = 29; gemX = 19; gemY = 8; gemWidth = 8; gemHeight = 7;
               poolTextX = 118; poolTextY = 0; poolX = 101; poolY = 6;
               levelTextX = 11.5F; levelTextY = 12.0F;
               xPos = Math.round(screenWidth * 0.5F + (screenWidth - (float) tex_width) * 0.5F * ((float) guiXPos / 50.0F)) - Math.round((float) tex_width / 2.0F);
               yPos = Math.round(screenHeight * 0.5F + (screenHeight - (float) tex_height) * 0.5F * ((float) guiYPos / 50.0F)) - Math.round((float) tex_height / 2.0F);
               this.progressBarMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.RIGHT_TO_LEFT, 70, 2, 27 + xPos, 12 + yPos, 21, 59 + 2 * guiColor);
               this.progressBarDeadMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.RIGHT_TO_LEFT, 70, 2, 27 + xPos, 12 + yPos, 21, 71 + 2 * guiColor);
               this.progressBarXP = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.RIGHT_TO_LEFT, 70, 2, 27 + xPos, 9 + yPos, 21, 83 + 2 * guiColor);
            } else if (guiOrientation == 2) {
               loctaion = METAL_SIMPLE_NS; tex_width = 23; tex_height = 112; backgroundTextX = 136;
               gemTextX = 159; gemTextY = 36; gemX = 8; gemY = 88;
               poolTextX = 159; poolTextY = 24; poolX = 5; poolY = 4;
               levelTextX = 12.0F; levelTextY = 102.0F;
               xPos = Math.round(screenWidth * 0.5F + (screenWidth - (float) tex_width) * 0.5F * ((float) guiXPos / 50.0F)) - Math.round((float) tex_width / 2.0F);
               yPos = Math.round(screenHeight * 0.5F + (screenHeight - (float) tex_height) * 0.5F * ((float) guiYPos / 50.0F)) - Math.round((float) tex_height / 2.0F);
               this.progressBarMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.UP_TO_DOWN, 2, 70, 12 + xPos, 18 + yPos, 59 + 2 * guiColor, 24);
               this.progressBarDeadMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.UP_TO_DOWN, 2, 70, 12 + xPos, 18 + yPos, 71 + 2 * guiColor, 24);
               this.progressBarXP = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.UP_TO_DOWN, 2, 70, 9 + xPos, 18 + yPos, 83 + 2 * guiColor, 24);
            } else if (guiOrientation == 3) {
               loctaion = METAL_SIMPLE_EW; tex_width = 118; tex_height = 23; isVertical = true;
               gemTextX = 109; gemTextY = 29; gemX = 91; gemY = 8; gemWidth = 8; gemHeight = 7;
               poolTextX = 118; poolTextY = 0; poolX = 4; poolY = 6;
               levelTextX = 106.5F; levelTextY = 12.0F;
               xPos = Math.round(screenWidth * 0.5F + (screenWidth - (float) tex_width) * 0.5F * ((float) guiXPos / 50.0F)) - Math.round((float) tex_width / 2.0F);
               yPos = Math.round(screenHeight * 0.5F + (screenHeight - (float) tex_height) * 0.5F * ((float) guiYPos / 50.0F)) - Math.round((float) tex_height / 2.0F);
               this.progressBarMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.LEFT_TO_RIGHT, 70, 2, 21 + xPos, 9 + yPos, 21, 23 + 2 * guiColor);
               this.progressBarDeadMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.LEFT_TO_RIGHT, 70, 2, 21 + xPos, 9 + yPos, 21, 35 + 2 * guiColor);
               this.progressBarXP = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.LEFT_TO_RIGHT, 70, 2, 21 + xPos, 12 + yPos, 21, 47 + 2 * guiColor);
            }
         } else if (guiOrientation == 1) {
            loctaion = METAL_FANCY_EW; backgroundTextX = 120;
            gemTextX = 5; gemTextY = 0; gemX = 115; gemY = 27;
            poolTextX = 3; poolTextY = 52; poolX = 112; poolY = 134;
            levelTextX = 119.0F; levelTextY = 22.0F;
            xPos = Math.round(screenWidth * 0.5F + (screenWidth - (float) tex_width) * 0.5F * ((float) guiXPos / 50.0F)) - Math.round((float) tex_width / 2.0F);
            yPos = Math.round(screenHeight * 0.5F + (screenHeight - (float) tex_height) * 0.5F * ((float) guiYPos / 50.0F)) - Math.round((float) tex_height / 2.0F);
            this.progressBarMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.DOWN_TO_UP, 5, 96, 116 + xPos, 35 + yPos, 136 + 5 * guiColor, 155);
            this.progressBarDeadMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.DOWN_TO_UP, 5, 96, 116 + xPos, 35 + yPos, 166 + 5 * guiColor, 155);
            this.progressBarXP = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.LEFT_TO_RIGHT, 97, 5, 5 + xPos, 20 + yPos, 3, 20 + 5 * guiColor);
         } else if (guiOrientation == 2) {
            backgroundTextX = 120; backgroundTextY = 107;
            gemX = 115; gemY = 124; poolX = 112; poolY = 15;
            levelTextX = 119.0F; levelTextY = 138.0F;
            xPos = Math.round(screenWidth * 0.5F + (screenWidth - (float) tex_width) * 0.5F * ((float) guiXPos / 50.0F)) - Math.round((float) tex_width / 2.0F);
            yPos = Math.round(screenHeight * 0.5F + (screenHeight - (float) tex_height) * 0.5F * ((float) guiYPos / 50.0F)) - Math.round((float) tex_height / 2.0F);
            this.progressBarMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.UP_TO_DOWN, 5, 96, 116 + xPos, 29 + yPos, 60 + 5 * guiColor, 155);
            this.progressBarDeadMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.UP_TO_DOWN, 5, 96, 116 + xPos, 29 + yPos, 90 + 5 * guiColor, 155);
            this.progressBarXP = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.LEFT_TO_RIGHT, 97, 5, 5 + xPos, 134 + yPos, 139, 20 + 5 * guiColor);
         } else if (guiOrientation == 3) {
            loctaion = METAL_FANCY_EW; backgroundTextY = 107;
            gemTextX = 5; gemTextY = 0; gemY = 124;
            poolTextX = 3; poolTextY = 52; poolY = 15;
            levelTextY = 138.0F;
            xPos = Math.round(screenWidth * 0.5F + (screenWidth - (float) tex_width) * 0.5F * ((float) guiXPos / 50.0F)) - Math.round((float) tex_width / 2.0F);
            yPos = Math.round(screenHeight * 0.5F + (screenHeight - (float) tex_height) * 0.5F * ((float) guiYPos / 50.0F)) - Math.round((float) tex_height / 2.0F);
            this.progressBarMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.UP_TO_DOWN, 5, 96, 15 + xPos, 29 + yPos, 196 + 5 * guiColor, 155);
            this.progressBarDeadMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.UP_TO_DOWN, 5, 96, 15 + xPos, 29 + yPos, 226 + 5 * guiColor, 155);
            this.progressBarXP = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.RIGHT_TO_LEFT, 97, 5, 34 + xPos, 134 + yPos, 3, 20 + 5 * guiColor);
         }
      } else if (guiStyle == 1) {
         levelTextColor = 13224393;
         switch (guiColor) {
            case 0: manaTextColor = 2260097; break;
            case 1: manaTextColor = 3703074; break;
            case 2: manaTextColor = 8665375; break;
            case 3: manaTextColor = 8392214; break;
            case 4: manaTextColor = 8853583; break;
            case 5: manaTextColor = 3021655;
         }
         gemWidth = 11; gemHeight = 11;
         if (isSimpleGUI) {
            if (guiOrientation == 0) {
               loctaion = DARK_SIMPLE_NS; tex_width = 23; tex_height = 112;
               gemTextX = 159; gemTextY = 36; gemX = 6; gemY = 19;
               poolTextX = 159; poolTextY = 24; poolX = 5; poolY = 98;
               levelTextX = 12.0F; levelTextY = 11.0F;
               xPos = Math.round(screenWidth * 0.5F + (screenWidth - (float) tex_width) * 0.5F * ((float) guiXPos / 50.0F)) - Math.round((float) tex_width / 2.0F);
               yPos = Math.round(screenHeight * 0.5F + (screenHeight - (float) tex_height) * 0.5F * ((float) guiYPos / 50.0F)) - Math.round((float) tex_height / 2.0F);
               this.progressBarMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.DOWN_TO_UP, 2, 65, 9 + xPos, 29 + yPos, 23 + 2 * guiColor, 29);
               this.progressBarDeadMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.DOWN_TO_UP, 2, 65, 9 + xPos, 29 + yPos, 35 + 2 * guiColor, 29);
               this.progressBarXP = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.DOWN_TO_UP, 2, 65, 12 + xPos, 29 + yPos, 47 + 2 * guiColor, 29);
            } else if (guiOrientation == 1) {
               loctaion = DARK_SIMPLE_EW; tex_width = 118; tex_height = 23; backgroundTextY = 95; isVertical = true;
               gemTextX = 117; gemTextY = 29; gemX = 22; gemY = 6;
               poolTextX = 118; poolTextY = 0; poolX = 101; poolY = 6;
               levelTextX = 11.5F; levelTextY = 12.0F;
               xPos = Math.round(screenWidth * 0.5F + (screenWidth - (float) tex_width) * 0.5F * ((float) guiXPos / 50.0F)) - Math.round((float) tex_width / 2.0F);
               yPos = Math.round(screenHeight * 0.5F + (screenHeight - (float) tex_height) * 0.5F * ((float) guiYPos / 50.0F)) - Math.round((float) tex_height / 2.0F);
               this.progressBarMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.RIGHT_TO_LEFT, 65, 2, 32 + xPos, 12 + yPos, 24, 23 + 2 * guiColor);
               this.progressBarDeadMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.RIGHT_TO_LEFT, 65, 2, 32 + xPos, 12 + yPos, 24, 35 + 2 * guiColor);
               this.progressBarXP = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.RIGHT_TO_LEFT, 65, 2, 32 + xPos, 9 + yPos, 24, 47 + 2 * guiColor);
            } else if (guiOrientation == 2) {
               loctaion = DARK_SIMPLE_NS; tex_width = 23; tex_height = 112; backgroundTextX = 136;
               gemTextX = 159; gemTextY = 36; gemX = 6; gemY = 83;
               poolTextX = 159; poolTextY = 24; poolX = 5; poolY = 4;
               levelTextX = 12.0F; levelTextY = 102.0F;
               xPos = Math.round(screenWidth * 0.5F + (screenWidth - (float) tex_width) * 0.5F * ((float) guiXPos / 50.0F)) - Math.round((float) tex_width / 2.0F);
               yPos = Math.round(screenHeight * 0.5F + (screenHeight - (float) tex_height) * 0.5F * ((float) guiYPos / 50.0F)) - Math.round((float) tex_height / 2.0F);
               this.progressBarMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.UP_TO_DOWN, 2, 65, 12 + xPos, 18 + yPos, 23 + 2 * guiColor, 29);
               this.progressBarDeadMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.UP_TO_DOWN, 2, 65, 12 + xPos, 18 + yPos, 35 + 2 * guiColor, 29);
               this.progressBarXP = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.UP_TO_DOWN, 2, 65, 9 + xPos, 18 + yPos, 47 + 2 * guiColor, 29);
            } else if (guiOrientation == 3) {
               loctaion = DARK_SIMPLE_EW; tex_width = 118; tex_height = 23; isVertical = true;
               gemTextX = 106; gemTextY = 29; gemX = 85; gemY = 6;
               poolTextX = 118; poolTextY = 0; poolX = 4; poolY = 6;
               levelTextX = 106.5F; levelTextY = 12.0F;
               xPos = Math.round(screenWidth * 0.5F + (screenWidth - (float) tex_width) * 0.5F * ((float) guiXPos / 50.0F)) - Math.round((float) tex_width / 2.0F);
               yPos = Math.round(screenHeight * 0.5F + (screenHeight - (float) tex_height) * 0.5F * ((float) guiYPos / 50.0F)) - Math.round((float) tex_height / 2.0F);
               this.progressBarMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.LEFT_TO_RIGHT, 65, 2, 21 + xPos, 12 + yPos, 24, 23 + 2 * guiColor);
               this.progressBarDeadMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.LEFT_TO_RIGHT, 65, 2, 21 + xPos, 12 + yPos, 24, 35 + 2 * guiColor);
               this.progressBarXP = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.LEFT_TO_RIGHT, 65, 2, 21 + xPos, 9 + yPos, 24, 47 + 2 * guiColor);
            }
         } else if (guiOrientation == 0) {
            loctaion = DARK_FANCY_NS; tex_width = 144; tex_height = 148;
            gemTextX = 144; gemTextY = 0; gemX = 14; gemY = 30;
            poolTextX = 144; poolTextY = 52; poolX = 13; poolY = 134;
            levelTextX = 20.0F; levelTextY = 23.5F;
            xPos = Math.round(screenWidth * 0.5F + (screenWidth - (float) tex_width) * 0.5F * ((float) guiXPos / 50.0F)) - Math.round((float) tex_width / 2.0F);
            yPos = Math.round(screenHeight * 0.5F + (screenHeight - (float) tex_height) * 0.5F * ((float) guiYPos / 50.0F)) - Math.round((float) tex_height / 2.0F);
            this.progressBarMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.DOWN_TO_UP, 5, 90, 17 + xPos, 40 + yPos, 15 + 5 * guiColor, 160);
            this.progressBarDeadMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.DOWN_TO_UP, 5, 90, 17 + xPos, 40 + yPos, 45 + 5 * guiColor, 160);
            this.progressBarXP = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.RIGHT_TO_LEFT, 97, 5, 43 + xPos, 20 + yPos, 144, 20 + 5 * guiColor);
         } else if (guiOrientation == 1) {
            loctaion = DARK_FANCY_EW; backgroundTextX = 111; tex_width = 145; tex_height = 148;
            gemTextX = 3; gemTextY = 0; gemX = 120; gemY = 30;
            poolTextX = 3; poolTextY = 52; poolX = 119; poolY = 134;
            levelTextX = 126.0F; levelTextY = 23.5F;
            xPos = Math.round(screenWidth * 0.5F + (screenWidth - (float) tex_width) * 0.5F * ((float) guiXPos / 50.0F)) - Math.round((float) tex_width / 2.0F);
            yPos = Math.round(screenHeight * 0.5F + (screenHeight - (float) tex_height) * 0.5F * ((float) guiYPos / 50.0F)) - Math.round((float) tex_height / 2.0F);
            this.progressBarMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.DOWN_TO_UP, 5, 90, 123 + xPos, 40 + yPos, 196 + 5 * guiColor, 155);
            this.progressBarDeadMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.DOWN_TO_UP, 5, 90, 123 + xPos, 40 + yPos, 226 + 5 * guiColor, 155);
            this.progressBarXP = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.LEFT_TO_RIGHT, 97, 5, 5 + xPos, 20 + yPos, 3, 20 + 5 * guiColor);
         } else if (guiOrientation == 2) {
            loctaion = DARK_FANCY_NS; tex_width = 138; backgroundTextX = 118; backgroundTextY = 107;
            gemTextX = 144; gemTextY = 0; gemX = 113; gemY = 119;
            poolTextX = 144; poolTextY = 52; poolX = 112; poolY = 15;
            levelTextX = 119.0F; levelTextY = 138.0F;
            xPos = Math.round(screenWidth * 0.5F + (screenWidth - (float) tex_width) * 0.5F * ((float) guiXPos / 50.0F)) - Math.round((float) tex_width / 2.0F);
            yPos = Math.round(screenHeight * 0.5F + (screenHeight - (float) tex_height) * 0.5F * ((float) guiYPos / 50.0F)) - Math.round((float) tex_height / 2.0F);
            this.progressBarMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.UP_TO_DOWN, 5, 90, 116 + xPos, 29 + yPos, 15 + 5 * guiColor, 160);
            this.progressBarDeadMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.UP_TO_DOWN, 5, 90, 116 + xPos, 29 + yPos, 45 + 5 * guiColor, 160);
            this.progressBarXP = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.LEFT_TO_RIGHT, 97, 5, 5 + xPos, 134 + yPos, 144, 20 + 5 * guiColor);
         } else if (guiOrientation == 3) {
            loctaion = DARK_FANCY_EW; backgroundTextY = 107; tex_width = 138; tex_height = 148;
            gemTextX = 3; gemTextY = 0; gemX = 14; gemY = 119;
            poolTextX = 3; poolTextY = 52; poolX = 13; poolY = 15;
            levelTextX = 20.0F; levelTextY = 138.0F;
            xPos = Math.round(screenWidth * 0.5F + (screenWidth - (float) tex_width) * 0.5F * ((float) guiXPos / 50.0F)) - Math.round((float) tex_width / 2.0F);
            yPos = Math.round(screenHeight * 0.5F + (screenHeight - (float) tex_height) * 0.5F * ((float) guiYPos / 50.0F)) - Math.round((float) tex_height / 2.0F);
            this.progressBarMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.UP_TO_DOWN, 5, 90, 17 + xPos, 29 + yPos, 196 + 5 * guiColor, 155);
            this.progressBarDeadMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.UP_TO_DOWN, 5, 90, 17 + xPos, 29 + yPos, 226 + 5 * guiColor, 155);
            this.progressBarXP = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.RIGHT_TO_LEFT, 97, 5, 36 + xPos, 134 + yPos, 3, 20 + 5 * guiColor);
         }
      } else if (guiStyle == 2) {
         levelTextColor = 16701633;
         switch (guiColor) {
            case 0: manaTextColor = 5033869; break;
            case 1: manaTextColor = 10866508; break;
            case 2: manaTextColor = 13846107; break;
            case 3: manaTextColor = 13575032; break;
            case 4: manaTextColor = 13973711; break;
            case 5: manaTextColor = 5404596;
         }
         if (isSimpleGUI) {
            if (guiOrientation == 0) {
               loctaion = LIGHT_SIMPLE_NS; tex_width = 23; tex_height = 112;
               gemTextX = 159; gemTextY = 36; gemX = 8; gemY = 18;
               poolTextX = 159; poolTextY = 24; poolX = 5; poolY = 98;
               levelTextX = 12.0F; levelTextY = 11.0F;
               xPos = Math.round(screenWidth * 0.5F + (screenWidth - (float) tex_width) * 0.5F * ((float) guiXPos / 50.0F)) - Math.round((float) tex_width / 2.0F);
               yPos = Math.round(screenHeight * 0.5F + (screenHeight - (float) tex_height) * 0.5F * ((float) guiYPos / 50.0F)) - Math.round((float) tex_height / 2.0F);
               this.progressBarMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.DOWN_TO_UP, 2, 68, 9 + xPos, 26 + yPos, 23 + 2 * guiColor, 24);
               this.progressBarDeadMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.DOWN_TO_UP, 2, 68, 9 + xPos, 26 + yPos, 35 + 2 * guiColor, 24);
               this.progressBarXP = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.DOWN_TO_UP, 2, 68, 12 + xPos, 26 + yPos, 47 + 2 * guiColor, 24);
            } else if (guiOrientation == 1) {
               loctaion = LIGHT_SIMPLE_EW; tex_width = 118; tex_height = 23; backgroundTextY = 95; isVertical = true;
               gemTextX = 109; gemTextY = 29; gemX = 21; gemY = 8; gemWidth = 8; gemHeight = 7;
               poolTextX = 118; poolTextY = 0; poolX = 101; poolY = 6;
               levelTextX = 11.5F; levelTextY = 12.0F;
               xPos = Math.round(screenWidth * 0.5F + (screenWidth - (float) tex_width) * 0.5F * ((float) guiXPos / 50.0F)) - Math.round((float) tex_width / 2.0F);
               yPos = Math.round(screenHeight * 0.5F + (screenHeight - (float) tex_height) * 0.5F * ((float) guiYPos / 50.0F)) - Math.round((float) tex_height / 2.0F);
               this.progressBarMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.RIGHT_TO_LEFT, 68, 2, 29 + xPos, 12 + yPos, 21, 59 + 2 * guiColor);
               this.progressBarDeadMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.RIGHT_TO_LEFT, 68, 2, 29 + xPos, 12 + yPos, 21, 71 + 2 * guiColor);
               this.progressBarXP = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.RIGHT_TO_LEFT, 68, 2, 29 + xPos, 9 + yPos, 21, 83 + 2 * guiColor);
            } else if (guiOrientation == 2) {
               loctaion = LIGHT_SIMPLE_NS; tex_width = 23; tex_height = 112; backgroundTextX = 136;
               gemTextX = 159; gemTextY = 36; gemX = 8; gemY = 86;
               poolTextX = 159; poolTextY = 24; poolX = 5; poolY = 4;
               levelTextX = 12.0F; levelTextY = 102.0F;
               xPos = Math.round(screenWidth * 0.5F + (screenWidth - (float) tex_width) * 0.5F * ((float) guiXPos / 50.0F)) - Math.round((float) tex_width / 2.0F);
               yPos = Math.round(screenHeight * 0.5F + (screenHeight - (float) tex_height) * 0.5F * ((float) guiYPos / 50.0F)) - Math.round((float) tex_height / 2.0F);
               this.progressBarMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.UP_TO_DOWN, 2, 68, 12 + xPos, 18 + yPos, 59 + 2 * guiColor, 24);
               this.progressBarDeadMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.UP_TO_DOWN, 2, 68, 12 + xPos, 18 + yPos, 71 + 2 * guiColor, 24);
               this.progressBarXP = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.UP_TO_DOWN, 2, 68, 9 + xPos, 18 + yPos, 83 + 2 * guiColor, 24);
            } else if (guiOrientation == 3) {
               loctaion = LIGHT_SIMPLE_EW; tex_width = 118; tex_height = 23; isVertical = true;
               gemTextX = 109; gemTextY = 29; gemX = 89; gemY = 8; gemWidth = 8; gemHeight = 7;
               poolTextX = 118; poolTextY = 0; poolX = 4; poolY = 6;
               levelTextX = 106.5F; levelTextY = 12.0F;
               xPos = Math.round(screenWidth * 0.5F + (screenWidth - (float) tex_width) * 0.5F * ((float) guiXPos / 50.0F)) - Math.round((float) tex_width / 2.0F);
               yPos = Math.round(screenHeight * 0.5F + (screenHeight - (float) tex_height) * 0.5F * ((float) guiYPos / 50.0F)) - Math.round((float) tex_height / 2.0F);
               this.progressBarMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.LEFT_TO_RIGHT, 68, 2, 21 + xPos, 9 + yPos, 21, 23 + 2 * guiColor);
               this.progressBarDeadMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.LEFT_TO_RIGHT, 68, 2, 21 + xPos, 9 + yPos, 21, 35 + 2 * guiColor);
               this.progressBarXP = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.LEFT_TO_RIGHT, 68, 2, 21 + xPos, 12 + yPos, 21, 47 + 2 * guiColor);
            }
         } else if (guiOrientation == 0) {
            loctaion = LIGHT_FANCY_NS; tex_width = 144; tex_height = 148;
            gemTextX = 146; gemTextY = 0; gemX = 16; gemY = 30;
            poolTextX = 144; poolTextY = 52; poolX = 13; poolY = 134;
            levelTextX = 20.0F; levelTextY = 23.5F;
            xPos = Math.round(screenWidth * 0.5F + (screenWidth - (float) tex_width) * 0.5F * ((float) guiXPos / 50.0F)) - Math.round((float) tex_width / 2.0F);
            yPos = Math.round(screenHeight * 0.5F + (screenHeight - (float) tex_height) * 0.5F * ((float) guiYPos / 50.0F)) - Math.round((float) tex_height / 2.0F);
            this.progressBarMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.DOWN_TO_UP, 5, 92, 17 + xPos, 38 + yPos, 0 + 5 * guiColor, 158);
            this.progressBarDeadMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.DOWN_TO_UP, 5, 92, 17 + xPos, 38 + yPos, 30 + 5 * guiColor, 158);
            this.progressBarXP = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.RIGHT_TO_LEFT, 97, 5, 43 + xPos, 20 + yPos, 144, 20 + 5 * guiColor);
         } else if (guiOrientation == 1) {
            loctaion = LIGHT_FANCY_EW; tex_width = 143; tex_height = 148; backgroundTextX = 112;
            gemTextX = 5; gemTextY = 0; gemX = 121; gemY = 30;
            poolTextX = 3; poolTextY = 52; poolX = 118; poolY = 134;
            levelTextX = 125.0F; levelTextY = 23.0F;
            xPos = Math.round(screenWidth * 0.5F + (screenWidth - (float) tex_width) * 0.5F * ((float) guiXPos / 50.0F)) - Math.round((float) tex_width / 2.0F);
            yPos = Math.round(screenHeight * 0.5F + (screenHeight - (float) tex_height) * 0.5F * ((float) guiYPos / 50.0F)) - Math.round((float) tex_height / 2.0F);
            this.progressBarMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.DOWN_TO_UP, 5, 92, 122 + xPos, 38 + yPos, 136 + 5 * guiColor, 155);
            this.progressBarDeadMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.DOWN_TO_UP, 5, 92, 122 + xPos, 38 + yPos, 166 + 5 * guiColor, 155);
            this.progressBarXP = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.LEFT_TO_RIGHT, 97, 5, 4 + xPos, 20 + yPos, 3, 20 + 5 * guiColor);
         } else if (guiOrientation == 2) {
            loctaion = LIGHT_FANCY_NS; tex_width = 136; tex_height = 145; backgroundTextX = 120; backgroundTextY = 111;
            gemTextX = 146; gemTextY = 0; gemX = 114; gemY = 117;
            poolTextX = 144; poolTextY = 52; poolX = 111; poolY = 11;
            levelTextX = 118.0F; levelTextY = 133.0F;
            xPos = Math.round(screenWidth * 0.5F + (screenWidth - (float) tex_width) * 0.5F * ((float) guiXPos / 50.0F)) - Math.round((float) tex_width / 2.0F);
            yPos = Math.round(screenHeight * 0.5F + (screenHeight - (float) tex_height) * 0.5F * ((float) guiYPos / 50.0F)) - Math.round((float) tex_height / 2.0F);
            this.progressBarMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.UP_TO_DOWN, 5, 92, 115 + xPos, 25 + yPos, 60 + 5 * guiColor, 158);
            this.progressBarDeadMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.UP_TO_DOWN, 5, 92, 115 + xPos, 25 + yPos, 90 + 5 * guiColor, 158);
            this.progressBarXP = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.LEFT_TO_RIGHT, 97, 5, 4 + xPos, 130 + yPos, 144, 20 + 5 * guiColor);
         } else if (guiOrientation == 3) {
            loctaion = LIGHT_FANCY_EW; tex_width = 136; tex_height = 145; backgroundTextY = 111;
            gemTextX = 5; gemTextY = 0; gemX = 15; gemY = 117;
            poolTextX = 3; poolTextY = 52; poolX = 12; poolY = 11;
            levelTextX = 19.0F; levelTextY = 133.0F;
            xPos = Math.round(screenWidth * 0.5F + (screenWidth - (float) tex_width) * 0.5F * ((float) guiXPos / 50.0F)) - Math.round((float) tex_width / 2.0F);
            yPos = Math.round(screenHeight * 0.5F + (screenHeight - (float) tex_height) * 0.5F * ((float) guiYPos / 50.0F)) - Math.round((float) tex_height / 2.0F);
            this.progressBarMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.UP_TO_DOWN, 5, 92, 16 + xPos, 25 + yPos, 196 + 5 * guiColor, 155);
            this.progressBarDeadMana = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.UP_TO_DOWN, 5, 92, 16 + xPos, 25 + yPos, 226 + 5 * guiColor, 155);
            this.progressBarXP = new SOMProgressBar(loctaion, SOMProgressBar.ProgressBarDirection.RIGHT_TO_LEFT, 97, 5, 35 + xPos, 130 + yPos, 3, 20 + 5 * guiColor);
         }
      }

      xPos = Math.round(screenWidth * 0.5F + (screenWidth - (float) tex_width) * 0.5F * ((float) guiXPos / 50.0F)) - Math.round((float) tex_width / 2.0F);
      yPos = Math.round(screenHeight * 0.5F + (screenHeight - (float) tex_height) * 0.5F * ((float) guiYPos / 50.0F)) - Math.round((float) tex_height / 2.0F);

      com.mojang.blaze3d.systems.RenderSystem.enableBlend();
      com.mojang.blaze3d.systems.RenderSystem.defaultBlendFunc();
      com.mojang.blaze3d.systems.RenderSystem.disableDepthTest();
      com.mojang.blaze3d.systems.RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

      gg.pose().pushPose();
      if (GuiSpellCharges.isHudOpen()) {
         int target = Math.round(screenWidth) - tex_width - 4;
         gg.pose().translate((float) (target - xPos), 0.0F, 0.0F);
      } else if (!isSimpleGUI) {
         float s = 0.85F;
         float cx = (float) xPos + (float) tex_width / 2.0F;
         float cy = (float) yPos + (float) tex_height / 2.0F;
         gg.pose().translate(-10.0F, -10.0F, 0.0F);
         gg.pose().translate(cx, cy, 0.0F);
         gg.pose().scale(s, s, 1.0F);
         gg.pose().translate(-cx, -cy, 0.0F);
      }

      gg.blit(loctaion, xPos, yPos, backgroundTextX, backgroundTextY, tex_width, tex_height);
      this.progressBarMana.setMin(Math.round(mana.getMana() + mana.getDeadMana())).setMax(mana.getMaxMana());
      this.progressBarMana.draw(gg);
      this.progressBarDeadMana.setMin(Math.round(mana.getDeadMana())).setMax(mana.getMaxMana());
      this.progressBarDeadMana.draw(gg);
      this.progressBarXP
         .setMin(Math.round(mana.getMagicianXPToNextLevel().getA()))
         .setMax(Math.round(mana.getMagicianXPToNextLevel().getB()));
      this.progressBarXP.draw(gg);
      this.drawManaPool(gg, loctaion, poolX + xPos, poolY + yPos, poolTextX, poolTextY, poolWidth, poolHeight, guiColor, manaTextColor, mana.getMana());
      this.drawCenteredTextWithinBox(gg, levelBox, levelTextColor, (float) xPos + levelTextX, (float) yPos + levelTextY, String.valueOf(mana.getLevel() + 1));
      if (mana.getMana() + mana.getDeadMana() == (float) mana.getMaxMana()) {
         this.drawGem(gg, isVertical, loctaion, gemX + xPos, gemY + yPos, gemTextX, gemTextY, gemWidth, gemHeight, guiColor);
      }
      gg.pose().popPose();

      com.mojang.blaze3d.systems.RenderSystem.enableDepthTest();
      com.mojang.blaze3d.systems.RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   private void drawCenteredTextWithinBox(GuiGraphics gg, float boxSize, int textColor, float x, float y, String s) {
      Font font = Minecraft.getInstance().font;
      int stringWidth = font.width(s);
      int stringHeight = font.lineHeight;
      float scaler = 1.0F;
      if ((float) stringWidth > boxSize) {
         scaler = boxSize / (float) stringWidth;
      }
      gg.pose().pushPose();
      gg.pose().scale(scaler, scaler, scaler);
      gg.drawString(font, s,
         Math.round(x / scaler) - Math.round((float) (stringWidth / 2)),
         Math.round(y / scaler) - Math.round((float) (stringHeight / 2)),
         textColor, false);
      gg.pose().popPose();
   }

   private void drawManaPool(GuiGraphics gg, ResourceLocation texture, int x, int y, int textX, int textY, int width, int height, int color, int manaTextColor, float mana) {
      this.drawElement(gg, texture, x, y, textX + color * width, textY, width, height);
      float manaTextX = 6.75F;
      float manaTextY = 5.5F;
      float manaBox = 9.0F;
      this.drawCenteredTextWithinBox(gg, manaBox, manaTextColor, (float) x + manaTextX, (float) y + manaTextY, String.valueOf(Math.round(mana)));
   }

   private void drawGem(GuiGraphics gg, boolean isVertical, ResourceLocation texture, int x, int y, int textX, int textY, int width, int height, int color) {
      if (isVertical) {
         this.drawElement(gg, texture, x, y, textX, textY + color * height, width, height);
      } else {
         this.drawElement(gg, texture, x, y, textX + color * width, textY, width, height);
      }
   }

   private void drawElement(GuiGraphics gg, ResourceLocation texture, int x, int y, int textX, int textY, int width, int height) {
      gg.blit(texture, x, y, textX, textY, width, height);
   }
}
