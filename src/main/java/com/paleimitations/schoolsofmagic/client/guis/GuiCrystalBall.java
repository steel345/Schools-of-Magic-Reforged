package com.paleimitations.schoolsofmagic.client.guis;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;

import org.lwjgl.glfw.GLFW;

public class GuiCrystalBall extends Screen {
   public static final ResourceLocation GUI_BACKGROUND = new ResourceLocation("som", "textures/gui/crystal_ball_base.png");
   private static final ResourceLocation GUI_FOREGROUND = new ResourceLocation("som", "textures/gui/crystal_ball_top.png");
   private static final ResourceLocation GUI_ICONS = new ResourceLocation("som", "textures/gui/crystal_ball_icons.png");

   private final Player player;
   private int slot = 0;
   private int delay = 0;
   private boolean isElemental;
   private SwitchButton switchButton;

   public GuiCrystalBall(Player playerIn) {
      super(Component.empty());
      this.player = playerIn;
      this.isElemental = true;
   }

   @Override
   protected void init() {
      super.init();
      int xPos = this.width / 2 - 93;
      int yPos = this.height / 2 - 118;
      this.switchButton = new SwitchButton(this, xPos + 8, yPos + 197);
      this.addRenderableWidget(this.switchButton);
   }

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
   public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
      int dW = (int) Math.signum(delta);
      if (dW != 0 && this.delay == 0) {
         int spells = this.isElemental ? 16 : 10;
         int j = this.slot + dW;
         this.delay = 20;
         if (j < 0) j += spells;
         this.slot = j % spells;
      }
      return super.mouseScrolled(mouseX, mouseY, delta);
   }

   @Override
   public boolean isPauseScreen() { return false; }

   @Override
   public void render(GuiGraphics gg, int parWidth, int parHeight, float partialTicks) {
      int xPos = this.width / 2 - 93;
      int yPos = this.height / 2 - 118;
      gg.blit(GUI_BACKGROUND, xPos, yPos, 0, 0, 186, 236);
      if (this.delay > 0) this.delay--;
      int spells = this.isElemental ? 16 : 10;
      for (int i = -2; i <= 2; i++) {
         gg.pose().pushPose();
         gg.pose().translate(this.width / 2.0F, yPos + 72, 100.0F + (2.0F - Math.abs(i)));
         this.drawSlot(gg,
            (float) (54.0 * Math.sin(i * Math.PI * 0.25)), 0.0F,
            (this.slot + i + spells) % spells,
            (float) (Math.pow(2.0, 2 - Math.abs(i)) / 2.0));
         gg.pose().popPose();
      }

      gg.pose().pushPose();
      gg.pose().translate(0.0F, 0.0F, 200.0F);
      gg.blit(GUI_FOREGROUND, xPos, yPos, 0, 0, 186, 236);
      gg.pose().popPose();

      IManaData data = Minecraft.getInstance().player.getCapability(CapabilityManaData.CAP).orElse(null);
      if (data == null) { super.render(gg, parWidth, parHeight, partialTicks); return; }
      int level = 1;
      int xpWidth = 164;
      Color color = Color.WHITE;
      Tuple<Float, Float> xp = new Tuple<>(0.0F, 0.0F);
      if (this.isElemental) {
         level += data.getElementLevel(MagicElementRegistry.getElementFromId(this.slot));
         xp = data.getElementXPToNextLevel(MagicElementRegistry.getElementFromId(this.slot));
         color = new Color(MagicElementRegistry.getElementFromId(this.slot).getColor());
      } else if (this.slot < 6) {
         level += data.getSchoolLevel(MagicSchoolRegistry.getSchoolFromId(this.slot));
         xp = data.getSchoolXPToNextLevel(MagicSchoolRegistry.getSchoolFromId(this.slot));
         color = switch (this.slot) {
            case 0 -> new Color(215, 168, 168);
            case 1 -> new Color(215, 186, 168);
            case 2 -> new Color(228, 222, 192);
            case 3 -> new Color(190, 220, 190);
            case 4 -> new Color(211, 236, 241);
            case 5 -> new Color(227, 217, 232);
            default -> Color.WHITE;
         };
      } else {
         switch (this.slot) {
            case 6 -> { level += data.getLevel(); xp = data.getMagicianXPToNextLevel(); }
            case 7 -> { level += data.getSpellLevel(); xp = data.getSpellXPToNextLevel(); color = new Color(180, 106, 133); }
            case 8 -> { level += data.getPotionLevel(); xp = data.getPotionXPToNextLevel(); color = new Color(198, 155, 70); }
            case 9 -> { level += data.getRitualLevel(); xp = data.getRitualXPToNextLevel(); color = new Color(70, 174, 198); }
         }
      }

      RenderSystem.setShaderColor(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
      gg.blit(GUI_BACKGROUND, xPos + 11, yPos + 211, 0, 236,
         Math.round((float) xp.getA() / (float) xp.getB() * (float) xpWidth), 10);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      String s = Math.round(xp.getA()) + "/" + Math.round(xp.getB());
      Color textColor = this.isElemental ? color : Color.BLACK;
      gg.drawString(Minecraft.getInstance().font, s,
         Math.round(this.width / 2.0F) - Minecraft.getInstance().font.width(s) / 2,
         yPos + 224, textColor.getRGB(), false);

      Component s1 = Component.empty();
      if (this.isElemental) {
         s1 = Component.translatable(MagicElementRegistry.getElementFromId(this.slot).getFormattedName())
            .append(Component.literal(" "))
            .append(Component.translatable("gui.jei.catalyst_basin.skill"))
            .append(Component.literal(" " + level));
      } else if (this.slot < 6) {
         s1 = Component.translatable(MagicSchoolRegistry.getSchoolFromId(this.slot).getFormattedName())
            .append(Component.literal(" "))
            .append(Component.translatable("gui.jei.catalyst_basin.skill"))
            .append(Component.literal(" " + level));
      } else {
         String key = switch (this.slot) {
            case 6 -> "magic.magician.name";
            case 7 -> "magic.spell.name";
            case 8 -> "magic.potion.name";
            case 9 -> "magic.ritual.name";
            default -> "";
         };
         s1 = Component.translatable(key)
            .append(Component.literal(" "))
            .append(Component.translatable("gui.jei.catalyst_basin.skill"))
            .append(Component.literal(" " + level));
      }
      gg.drawString(Minecraft.getInstance().font, s1, xPos + 28, yPos + 197, color.getRGB(), false);
      super.render(gg, parWidth, parHeight, partialTicks);
   }

   public void drawSlot(GuiGraphics gg, float xPos, float yPos, int i, float scale) {
      gg.pose().pushPose();
      gg.pose().scale(scale, scale, 1.0F);
      if (this.isElemental) {
         gg.blit(GUI_ICONS, Math.round(xPos / scale - 15.0F), Math.round(yPos / scale - 15.0F),
            i % 8 * 30, i / 8 * 30, 30, 30);
      } else {
         gg.blit(GUI_ICONS, Math.round(xPos / scale - 15.0F), Math.round(yPos / scale - 15.0F),
            i % 6 * 30, 60 + i / 6 * 30, 30, 30);
      }
      gg.pose().popPose();
   }

   static class SwitchButton extends AbstractButton {
      private final GuiCrystalBall ball;

      public SwitchButton(GuiCrystalBall ball, int posX, int posY) {
         super(posX, posY, 17, 8, Component.empty());
         this.ball = ball;
      }

      @Override
      public void onPress() {
         ball.isElemental = !ball.isElemental;
         ball.slot = 0;
      }

      @Override protected void updateWidgetNarration(NarrationElementOutput out) { defaultButtonNarrationText(out); }

      @Override
      public void renderWidget(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
         if (!this.visible) return;
         boolean hovered = mouseX >= this.getX() && mouseY >= this.getY()
               && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
         int tx = hovered ? 186 + 17 : 186;
         int ty = ball.isElemental ? 0 : 8;
         gg.blit(GUI_BACKGROUND, this.getX(), this.getY(), tx, ty, 17, 8);
      }
   }
}
