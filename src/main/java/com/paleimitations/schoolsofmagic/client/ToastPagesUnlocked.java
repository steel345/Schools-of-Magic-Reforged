package com.paleimitations.schoolsofmagic.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// The "New pages unlocked" toast. It opens showing just the title, then fades into
// the full toast with a "Check your <book>" subtitle beneath it. The frame is tinted
// old-book orange; long book names are scaled down rather than clipped.
@OnlyIn(Dist.CLIENT)
public class ToastPagesUnlocked implements Toast {
   private static final long DISPLAY_MS = 5000L;
   private static final long FADE_START_MS = 600L;
   private static final long FADE_END_MS = 1100L;
   private static final int TITLE_COLOR = 0x000000;
   private static final int DESC_COLOR = 0x7A3B0A;
   private static final int TEXT_X = 30;
   private static final int TEXT_AVAIL = 124;

   private final ItemStack icon;
   private final Component title;
   private final Component desc;

   public ToastPagesUnlocked(ItemStack icon) {
      this.icon = icon;
      this.title = Component.translatable("toast.som.pages_unlocked");
      this.desc = icon.isEmpty()
         ? Component.translatable("toast.som.pages_unlocked.desc.generic")
         : Component.translatable("toast.som.pages_unlocked.desc", icon.getHoverName());
   }

   @Override
   public Visibility render(GuiGraphics gg, ToastComponent component, long timeSinceLastVisible) {
      // Tint the vanilla toast frame to an old-book orange.
      gg.setColor(1.0F, 0.72F, 0.36F, 1.0F);
      gg.blit(TEXTURE, 0, 0, 0, 32, this.width(), this.height());
      gg.setColor(1.0F, 1.0F, 1.0F, 1.0F);

      if (!this.icon.isEmpty()) {
         gg.renderItem(this.icon, 8, 8);
      }

      // Start as a title-only toast, then fade the subtitle in while the title
      // slides up to make room.
      float progress = timeSinceLastVisible <= FADE_START_MS ? 0.0F
         : timeSinceLastVisible >= FADE_END_MS ? 1.0F
         : (float) (timeSinceLastVisible - FADE_START_MS) / (float) (FADE_END_MS - FADE_START_MS);

      Font font = component.getMinecraft().font;
      int titleY = Math.round(12.0F - 5.0F * progress);
      drawFitted(gg, font, this.title, titleY, TITLE_COLOR);

      if (progress > 0.0F) {
         int alpha = Math.max(4, Math.round(progress * 255.0F));
         drawFitted(gg, font, this.desc, 18, (alpha << 24) | DESC_COLOR);
      }

      return timeSinceLastVisible >= DISPLAY_MS ? Visibility.HIDE : Visibility.SHOW;
   }

   // Draws the line at the text column, shrinking it just enough to fit the frame so
   // long book names are never cut off.
   private static void drawFitted(GuiGraphics gg, Font font, Component text, int y, int color) {
      int w = font.width(text);
      if (w <= TEXT_AVAIL) {
         gg.drawString(font, text, TEXT_X, y, color, false);
         return;
      }
      float scale = (float) TEXT_AVAIL / (float) w;
      gg.pose().pushPose();
      gg.pose().scale(scale, scale, 1.0F);
      gg.drawString(font, text, Math.round(TEXT_X / scale), Math.round(y / scale), color, false);
      gg.pose().popPose();
   }
}
