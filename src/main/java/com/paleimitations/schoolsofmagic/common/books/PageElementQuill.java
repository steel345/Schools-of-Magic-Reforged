package com.paleimitations.schoolsofmagic.common.books;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class PageElementQuill extends PageElement {
   private static final ResourceLocation ICONS =
      new ResourceLocation("som", "textures/gui/podium/icons.png");
   private static final int SIZE = 23;

   private static final int ICON_U = 0;
   private static final int ICON_V = 84;

   private static double screenX0;
   private static double screenY0;
   private static double screenX1;
   private static double screenY1;
   private static boolean placed;

   private static boolean hidden;

   @OnlyIn(Dist.CLIENT)
   public static void setHidden(boolean value) {
      hidden = value;
   }

   public PageElementQuill(int x, int y) {
      super(x, y);
   }

   public PageElementQuill(int x, int y, int subpage) {
      super(x, y, subpage);
   }

   @OnlyIn(Dist.CLIENT)
   public static boolean isOverScreen(double mouseX, double mouseY) {
      return placed && mouseX >= screenX0 && mouseX < screenX1
         && mouseY >= screenY0 && mouseY < screenY1;
   }

   @OnlyIn(Dist.CLIENT)
   private static double guiMouseX() {
      Minecraft mc = Minecraft.getInstance();
      return mc.mouseHandler.xpos() * (double) mc.getWindow().getGuiScaledWidth()
         / (double) mc.getWindow().getScreenWidth();
   }

   @OnlyIn(Dist.CLIENT)
   private static double guiMouseY() {
      Minecraft mc = Minecraft.getInstance();
      return mc.mouseHandler.ypos() * (double) mc.getWindow().getGuiScaledHeight()
         / (double) mc.getWindow().getScreenHeight();
   }

   @Override
   @OnlyIn(Dist.CLIENT)
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int x, int y,
         boolean isGUI, int subpage) {
      if (hidden) {
         placed = false;
         return;
      }
      boolean hovered = false;
      if (isGUI) {
         Matrix4f matrix = gg.pose().last().pose();
         Vector4f a = matrix.transform(new Vector4f(x + this.x, y + this.y, 0.0F, 1.0F));
         Vector4f b = matrix.transform(new Vector4f(x + this.x + SIZE, y + this.y + SIZE, 0.0F, 1.0F));
         screenX0 = Math.min(a.x(), b.x());
         screenY0 = Math.min(a.y(), b.y());
         screenX1 = Math.max(a.x(), b.x());
         screenY1 = Math.max(a.y(), b.y());
         placed = true;
         hovered = isOverScreen(guiMouseX(), guiMouseY());
      }

      gg.blit(ICONS, x + this.x, y + this.y, hovered ? 29 : 0, 0, SIZE, SIZE);
      gg.blit(ICONS, x + this.x, y + this.y, ICON_U, ICON_V, SIZE, SIZE);
   }

   @OnlyIn(Dist.CLIENT)
   public void press() {
      Minecraft.getInstance().getSoundManager().play(
         net.minecraft.client.resources.sounds.SimpleSoundInstance.forUI(
            net.minecraft.sounds.SoundEvents.UI_BUTTON_CLICK, 1.0F));
   }
}
