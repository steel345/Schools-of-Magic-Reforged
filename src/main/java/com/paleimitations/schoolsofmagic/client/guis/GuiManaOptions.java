package com.paleimitations.schoolsofmagic.client.guis;

import com.paleimitations.schoolsofmagic.common.books.PageElementStandardText;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.client_mana_data.CapabilityClientManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.client_mana_data.IClientManaData;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketQueueUpdateClientManaData;
import com.paleimitations.schoolsofmagic.common.network.PacketSetIsFancy;
import com.paleimitations.schoolsofmagic.common.network.PacketSetManaColor;
import com.paleimitations.schoolsofmagic.common.network.PacketSetManaOrientation;
import com.paleimitations.schoolsofmagic.common.network.PacketSetManaPosition;
import com.paleimitations.schoolsofmagic.common.network.PacketSetManaStyle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class GuiManaOptions extends Screen {
   public static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/gui/gui_config.png");

   private final Player player;
   private final Screen previousScreen;

   public GuiManaOptions(Player player, Screen previousScreen) {
      super(Component.empty());
      this.player = player;
      this.previousScreen = previousScreen;
   }

   @Override
   protected void init() {
      super.init();
      int guiLeft = this.width / 2 - 76;
      int guiTop = this.height / 2 - 45;
      this.addRenderableWidget(new ArrowButton(48 + guiLeft, 18 + guiTop, 0, 165, 11, 7, () -> changeStyle(1)));
      this.addRenderableWidget(new ArrowButton(64 + guiLeft, 18 + guiTop, 0, 153, 11, 7, () -> changeStyle(-1)));
      this.addRenderableWidget(new ArrowButton(48 + guiLeft, 42 + guiTop, 0, 165, 11, 7, () -> changeColor(1)));
      this.addRenderableWidget(new ArrowButton(64 + guiLeft, 42 + guiTop, 0, 153, 11, 7, () -> changeColor(-1)));
      this.addRenderableWidget(new ArrowButton(112 + guiLeft, 4 + guiTop, 0, 209, 11, 11, () -> changeY(-1)));
      this.addRenderableWidget(new ArrowButton(112 + guiLeft, 56 + guiTop, 0, 221, 11, 11, () -> changeY(1)));
      this.addRenderableWidget(new ArrowButton(86 + guiLeft, 30 + guiTop, 0, 185, 11, 11, () -> changeX(-1)));
      this.addRenderableWidget(new ArrowButton(138 + guiLeft, 30 + guiTop, 0, 196, 11, 11, () -> changeX(1)));
      this.addRenderableWidget(new ArrowButton(59 + guiLeft, 66 + guiTop, 0, 177, 7, 7, this::toggleFancy));
      this.addRenderableWidget(new ArrowButton(87 + guiLeft, 5 + guiTop, 0, 233, 11, 11, () -> setOrientation(0)));
      this.addRenderableWidget(new ArrowButton(137 + guiLeft, 5 + guiTop, 0, 233, 11, 11, () -> setOrientation(1)));
      this.addRenderableWidget(new ArrowButton(87 + guiLeft, 55 + guiTop, 0, 233, 11, 11, () -> setOrientation(3)));
      this.addRenderableWidget(new ArrowButton(137 + guiLeft, 55 + guiTop, 0, 233, 11, 11, () -> setOrientation(2)));
   }

   private IClientManaData mana() {
      return this.player.getCapability(CapabilityClientManaData.CAP).orElse(null);
   }

   private void changeStyle(int dir) {
      IClientManaData m = mana(); if (m == null) return;
      int style = (m.getGuiStyle() + dir + 3) % 3;
      m.setGuiStyle(style);
      PacketHandler.INSTANCE.sendToServer(new PacketSetManaStyle(this.player, style));
   }
   private void changeColor(int dir) {
      IClientManaData m = mana(); if (m == null) return;
      int color = (m.getGuiColor() + dir + 6) % 6;
      m.setGuiColor(color);
      PacketHandler.INSTANCE.sendToServer(new PacketSetManaColor(this.player, color));
   }
   private void changeY(int delta) {
      IClientManaData m = mana(); if (m == null) return;
      int pos = Math.max(-50, Math.min(50, m.getGuiYPos() + delta));
      PacketHandler.INSTANCE.sendToServer(new PacketSetManaPosition(this.player, m.getGuiXPos(), pos));
      m.setGuiYPos(pos);
   }
   private void changeX(int delta) {
      IClientManaData m = mana(); if (m == null) return;
      int pos = Math.max(-50, Math.min(50, m.getGuiXPos() + delta));
      PacketHandler.INSTANCE.sendToServer(new PacketSetManaPosition(this.player, pos, m.getGuiYPos()));
      m.setGuiXPos(pos);
   }
   private void toggleFancy() {
      IClientManaData m = mana(); if (m == null) return;
      PacketHandler.INSTANCE.sendToServer(new PacketSetIsFancy(this.player, !m.isSimpleGui()));
      m.setSimpleGui(!m.isSimpleGui());
   }
   private void setOrientation(int o) {
      IClientManaData m = mana(); if (m == null) return;
      m.setGuiOrientation(o);
      PacketHandler.INSTANCE.sendToServer(new PacketSetManaOrientation(this.player, o));
   }

   @Override
   public boolean isPauseScreen() { return true; }

   @Override
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (keyCode == GLFW.GLFW_KEY_ESCAPE
            || Minecraft.getInstance().options.keyInventory.matches(keyCode, scanCode)) {
         Minecraft.getInstance().player.closeContainer();
         return true;
      }
      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   @Override
   public void render(GuiGraphics gg, int parWidth, int parHeight, float partialTicks) {
      IClientManaData clientMana = Minecraft.getInstance().player.getCapability(CapabilityClientManaData.CAP).orElse(null);
      if (clientMana != null && !clientMana.isLoadedToClient()) {
         PacketHandler.INSTANCE.sendToServer(new PacketQueueUpdateClientManaData());
      }
      int guiLeft = this.width / 2 - 76;
      int guiTop = this.height / 2 - 45;
      gg.blit(TEXTURE, guiLeft, guiTop, 0, 0, 153, 91);
      super.render(gg, parWidth, parHeight, partialTicks);
      new PageElementStandardText("gui.mana_style.options", guiLeft + 25, guiTop + 22, 41, 10, 0, true).drawElement(gg, 0.0F, 0.0F, 0, 0, true, 0);
      new PageElementStandardText("gui.mana_color.options", guiLeft + 25, guiTop + 46, 41, 10, 0, true).drawElement(gg, 0.0F, 0.0F, 0, 0, true, 0);
      new PageElementStandardText("gui.mana_fancy.options", guiLeft + 30, guiTop + 70, 51, 10, 0, true).drawElement(gg, 0.0F, 0.0F, 0, 0, true, 0);
      new PageElementStandardText("gui.mana_pos.options",   guiLeft + 117, guiTop + 82, 61, 9, 0, true).drawElement(gg, 0.0F, 0.0F, 0, 0, true, 0);
   }

   @OnlyIn(Dist.CLIENT)
   public static class ArrowButton extends AbstractButton {
      private final int textX;
      private final int textY;
      private final Runnable onPress;

      public ArrowButton(int posX, int posY, int textX, int textY, int width, int height, Runnable onPress) {
         super(posX, posY, width, height, Component.empty());
         this.textX = textX; this.textY = textY; this.onPress = onPress;
      }

      @Override public void onPress() { onPress.run(); }
      @Override protected void updateWidgetNarration(NarrationElementOutput out) { defaultButtonNarrationText(out); }

      @Override
      public void renderWidget(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
         if (!this.visible) return;
         boolean hovered = mouseX >= this.getX() && mouseY >= this.getY()
               && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
         if (hovered) gg.blit(TEXTURE, this.getX(), this.getY(), this.textX, this.textY, this.width, this.height);
      }
   }
}
