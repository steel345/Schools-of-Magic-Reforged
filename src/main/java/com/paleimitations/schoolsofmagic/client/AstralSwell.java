package com.paleimitations.schoolsofmagic.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// the count down while you stand in the rift. each number drops in big and fades back out over
// its own second
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AstralSwell {
   private static final int HOLD = 20;

   private static int number;
   private static int age;

   public static void show(int value) {
      if (value <= 0) {
         number = 0;
         age = 0;
         return;
      }
      number = value;
      age = 0;
   }

   @SubscribeEvent
   public static void onClientTick(TickEvent.ClientTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      if (Minecraft.getInstance().isPaused()) return;
      if (number > 0 && ++age > HOLD) show(0);
   }

   @SubscribeEvent
   public static void onRender(RenderGuiEvent.Post event) {
      if (number <= 0) return;
      Minecraft mc = Minecraft.getInstance();
      if (mc.level == null || mc.options.hideGui) return;

      float step = Mth.clamp((age + mc.getPartialTick()) / HOLD, 0.0F, 1.0F);
      float scale = 5.5F - step * 1.4F;
      float alpha = step < 0.7F ? 1.0F : 1.0F - (step - 0.7F) / 0.3F;

      int w = mc.getWindow().getGuiScaledWidth();
      int h = mc.getWindow().getGuiScaledHeight();
      Component text = Component.literal(Integer.toString(number));
      int half = mc.font.width(text) / 2;

      GuiGraphics gg = event.getGuiGraphics();
      PoseStack pose = gg.pose();
      pose.pushPose();
      pose.translate(w / 2.0F, h / 2.0F - 34.0F, 0.0F);
      pose.scale(scale, scale, 1.0F);

      RenderSystem.enableBlend();
      int shade = Math.round(alpha * 255.0F) << 24;
      gg.drawString(mc.font, text, -half + 1, 1, shade | 0x000000, false);
      gg.drawString(mc.font, text, -half, 0, shade | 0xFFFFFF, false);
      RenderSystem.disableBlend();

      pose.popPose();
   }
}
