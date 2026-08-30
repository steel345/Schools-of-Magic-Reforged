package com.paleimitations.schoolsofmagic.client.guis;

import com.paleimitations.schoolsofmagic.common.network.PacketAlarmSound;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

// a real screen with a real button on it. drawn on the hud it could never be clicked, the cursor
// is held in the middle of the window while nothing is open
public class GuiAlarm extends Screen {
   private static final int WIDE = 176;
   private static final int TALL = 76;
   private static final int LIFT = 40;

   private final int rune;

   public GuiAlarm(int rune) {
      super(Component.translatable("gui.som.alarm"));
      this.rune = rune;
   }

   @Override
   protected void init() {
      super.init();
      int left = (this.width - WIDE) / 2;
      int top = (this.height - TALL) / 2 - LIFT;

      this.addRenderableWidget(Button.builder(
         Component.translatable("gui.som.alarm.sound"), b -> {
            PacketHandler.INSTANCE.sendToServer(new PacketAlarmSound(this.rune));
            this.onClose();
         }).bounds(left + 12, top + 30, WIDE - 24, 20).build());

      this.addRenderableWidget(Button.builder(
         Component.translatable("gui.som.alarm.leave"), b -> this.onClose())
         .bounds(left + 12, top + 52, WIDE - 24, 20).build());
   }

   @Override
   public void render(GuiGraphics gg, int mouseX, int mouseY, float partial) {
      this.renderBackground(gg);

      int top = (this.height - TALL) / 2 - LIFT;

      gg.drawCenteredString(this.font, Component.translatable("message.som.alarm.near"),
         this.width / 2, top + 12, 0xFFFFFF);

      super.render(gg, mouseX, mouseY, partial);
   }

   @Override
   public boolean isPauseScreen() {
      return false;
   }
}
