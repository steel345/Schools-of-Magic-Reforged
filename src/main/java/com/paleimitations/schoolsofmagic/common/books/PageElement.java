package com.paleimitations.schoolsofmagic.common.books;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PageElement {
   public final int x;
   public final int y;
   public final int subpage;

   public PageElement(int x, int y) {
      this(x, y, 0);
   }

   public PageElement(int x, int y, int subpage) {
      this.x = x;
      this.y = y;
      this.subpage = subpage;
   }

   public int getSubpage() {
      return this.subpage;
   }

   public boolean isTarget(int i) {
      return i == this.subpage;
   }

   @OnlyIn(Dist.CLIENT)
   public boolean hasSubpage(int subpage) {
      return this.subpage == subpage;
   }

   @OnlyIn(Dist.CLIENT)
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int x, int y, boolean isGUI, int subpage) {
   }

   @OnlyIn(Dist.CLIENT)
   public void drawTexturedModalRect(GuiGraphics gg, ResourceLocation texture, int x, int y, int textureX, int textureY, int width, int height) {

      gg.blit(texture, x, y, textureX, textureY, width, height);
   }

   @OnlyIn(Dist.CLIENT)
   public void drawItemStack(GuiGraphics gg, ItemStack stack, int x, int y, boolean isGUI) {
      if (stack == null || stack.isEmpty()) return;
      Minecraft mc = Minecraft.getInstance();
      Font font = mc.font;
      if (isGUI) {
         gg.renderItem(stack, x, y);
         gg.renderItemDecorations(font, stack, x, y);
      } else {

         com.mojang.blaze3d.systems.RenderSystem.enableDepthTest();
         com.mojang.blaze3d.systems.RenderSystem.depthMask(true);
         com.mojang.blaze3d.platform.Lighting.setupFor3DItems();
         PoseStack pose = gg.pose();
         pose.pushPose();
         pose.translate((double) (x + 8), (double) (y + 8), 0.0D);
         pose.scale(16.0F, -16.0F, -16.0F);
         MultiBufferSource.BufferSource buf = mc.renderBuffers().bufferSource();
         mc.getItemRenderer().renderStatic(stack, ItemDisplayContext.GUI, 0xF000F0, OverlayTexture.NO_OVERLAY, pose, buf, mc.level, 0);
         buf.endBatch();
         pose.popPose();
         com.mojang.blaze3d.platform.Lighting.setupForFlatItems();
         com.mojang.blaze3d.systems.RenderSystem.depthMask(false);
         com.mojang.blaze3d.systems.RenderSystem.disableDepthTest();
      }
   }
}
