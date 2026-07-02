package com.paleimitations.schoolsofmagic.client.guis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SOMSettingsScreen extends Screen {
   private final Screen previous;

   public SOMSettingsScreen(Screen previous) {
      super(Component.translatable("screen.som.settings"));
      this.previous = previous;
   }

   @Override
   protected void init() {
      int cx = this.width / 2;
      int row = this.height / 6 + 24;

      Button options = Button.builder(
            Component.translatable("options.som.mana_bar"),
            b -> {
               net.minecraft.client.player.LocalPlayer p = Minecraft.getInstance().player;
               if (p != null) Minecraft.getInstance().setScreen(new GuiManaOptions(p, this));
            })
         .pos(cx - 75, row).size(150, 20).build();
      if (Minecraft.getInstance().player == null) options.active = false;
      this.addRenderableWidget(options);

      this.addRenderableWidget(Button.builder(
            Component.translatable(GuiManaBar.hidden
               ? "options.som.mana_bar.show"
               : "options.som.mana_bar.hide"),
            b -> {
               GuiManaBar.hidden = !GuiManaBar.hidden;

               net.minecraft.client.player.LocalPlayer pl = Minecraft.getInstance().player;
               if (pl != null) {
                  com.paleimitations.schoolsofmagic.common.entity.capabilities.client_mana_data.IClientManaData cap =
                     pl.getCapability(com.paleimitations.schoolsofmagic.common.entity.capabilities.client_mana_data.CapabilityClientManaData.CAP).orElse(null);
                  if (cap != null) cap.setHidden(GuiManaBar.hidden);
                  com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.sendToServer(
                     new com.paleimitations.schoolsofmagic.common.network.PacketSetManaHidden(pl, GuiManaBar.hidden));
               }
               b.setMessage(Component.translatable(GuiManaBar.hidden
                  ? "options.som.mana_bar.show"
                  : "options.som.mana_bar.hide"));
            })
         .pos(cx - 75, row + 28).size(150, 20).build());

      this.addRenderableWidget(Button.builder(
            Component.translatable("gui.done"),
            b -> Minecraft.getInstance().setScreen(this.previous))
         .pos(cx - 100, this.height - 27).size(200, 20).build());
   }

   @Override
   public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTick) {
      this.renderBackground(gg);
      gg.drawCenteredString(this.font, this.title, this.width / 2, 16, 0xFFFFFF);
      super.render(gg, mouseX, mouseY, partialTick);
   }

   @Override
   public void onClose() {
      Minecraft.getInstance().setScreen(this.previous);
   }
}
