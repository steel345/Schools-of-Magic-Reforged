package com.paleimitations.schoolsofmagic.common.books;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PageElement3DModel extends PageElement {
   public final ItemStack stack;
   public final float size;
   public final float tilt;
   public final boolean spin;
   public final float podiumLift;

   public PageElement3DModel(ItemStack stack, int x, int y, int subpage, float size, float tilt, boolean spin) {
      this(stack, x, y, subpage, size, tilt, spin, -8.0F);
   }

   public PageElement3DModel(ItemStack stack, int x, int y, int subpage, float size, float tilt, boolean spin, float podiumLift) {
      super(x, y, subpage);
      this.stack = stack == null ? ItemStack.EMPTY : stack;
      this.size = size;
      this.tilt = tilt;
      this.spin = spin;
      this.podiumLift = podiumLift;
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int target) {
      if (this.stack.isEmpty()) return;
      Minecraft mc = Minecraft.getInstance();

      float partial = mc.getFrameTime();
      float time = (mc.level != null ? mc.level.getGameTime() : 0L) + partial;
      float yaw = this.spin ? (time * 1.6F) % 360.0F : 35.0F;

      com.mojang.blaze3d.systems.RenderSystem.enableDepthTest();
      com.mojang.blaze3d.systems.RenderSystem.depthMask(true);
      com.mojang.blaze3d.platform.Lighting.setupFor3DItems();

      double z = isGUI ? 150.0D : this.podiumLift;
      PoseStack pose = gg.pose();
      pose.pushPose();
      pose.translate(this.x + xIn, this.y + yIn, z);

      float s = this.size;
      float sy = -this.size;
      if (!isGUI) { faceViewer(pose, mc); s = this.size * PODIUM_MAG; sy = this.size * PODIUM_MAG; }
      pose.scale(s, sy, s);
      pose.mulPose(Axis.XP.rotationDegrees(this.tilt));
      pose.mulPose(Axis.YP.rotationDegrees(yaw));

      MultiBufferSource.BufferSource buf = mc.renderBuffers().bufferSource();
      mc.getItemRenderer().renderStatic(this.stack, ItemDisplayContext.GUI,
         0xF000F0, OverlayTexture.NO_OVERLAY, pose, buf, mc.level, 0);
      buf.endBatch();
      pose.popPose();

      com.mojang.blaze3d.platform.Lighting.setupForFlatItems();
      com.mojang.blaze3d.systems.RenderSystem.depthMask(false);
      com.mojang.blaze3d.systems.RenderSystem.disableDepthTest();
   }

   static final float PODIUM_MAG = 2.5F;

   static void faceViewer(PoseStack pose, Minecraft mc) {
      org.joml.Quaternionf q = new org.joml.Quaternionf();
      pose.last().pose().getNormalizedRotation(q);
      pose.mulPose(q.conjugate());
      pose.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
   }
}
