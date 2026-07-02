package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.guis.SOMSettingsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class SOMSettingsButtonInjector {

   @SubscribeEvent
   public static void onScreenInit(ScreenEvent.Init.Post event) {
      if (!(event.getScreen() instanceof OptionsScreen options)) return;

      String videoLabel = Component.translatable("options.videoTitle").getString();
      AbstractWidget videoButton = null;
      for (GuiEventListener child : options.children()) {
         if (!(child instanceof AbstractWidget widget)) continue;
         String msg = widget.getMessage().getString();
         if (msg == null) continue;
         if (msg.equals(videoLabel) || msg.toLowerCase().contains("video")) {
            videoButton = widget;
            break;
         }
      }

      int x;
      int y;
      int width;
      if (videoButton != null) {
         width = videoButton.getWidth() / 2;
         x = videoButton.getX() - width - 2;
         y = videoButton.getY();
      } else {
         width = 75;
         x = options.width / 2 + 5;
         y = options.height / 6 + 144 - 6;
      }

      Button launcher = Button.builder(
            Component.translatable("options.som.settings"),
            b -> Minecraft.getInstance().setScreen(new SOMSettingsScreen(options)))
         .pos(x, y).size(width, 20).build();
      event.addListener(launcher);
   }
}
