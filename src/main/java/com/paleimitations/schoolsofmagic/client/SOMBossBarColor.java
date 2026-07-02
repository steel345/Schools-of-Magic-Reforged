package com.paleimitations.schoolsofmagic.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.BossEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SOMBossBarColor {

   private static final ResourceLocation BARS = new ResourceLocation("textures/gui/bars.png");

   @SubscribeEvent
   public static void onBossBar(CustomizeGuiOverlayEvent.BossEventProgress event) {
      try {
         BossEvent be = event.getBossEvent();
         Component name = be.getName();
         if (name == null || !"The Demon of the Ziggurat".equals(name.getString())) return;
         event.setCanceled(true);

         GuiGraphics g = event.getGuiGraphics();
         int x = event.getX();
         int y = event.getY();
         int width = 182;
         RenderSystem.enableBlend();
         RenderSystem.setShaderColor(1.0F, 0.5F, 0.0F, 1.0F);
         g.blit(BARS, x, y, 0, 60, width, 5);
         int filled = (int) (be.getProgress() * (float) width);
         if (filled > 0) g.blit(BARS, x, y, 0, 65, filled, 5);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

         int nameW = Minecraft.getInstance().font.width(name);
         g.drawString(Minecraft.getInstance().font, name, x + width / 2 - nameW / 2, y - 9, 0xFFFFFF);
      } catch (Throwable ignored) {}
   }
}
