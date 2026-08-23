package com.paleimitations.schoolsofmagic.common.books;

import java.util.Map;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PageElementMultiblock extends PageElement {
   private final BlockState[][][] grid;
   private final int w, d, h;
   public final float size;
   public final float tilt;
   public final boolean spin;
   public final boolean build;
   public final float podiumLift;

   public PageElementMultiblock(int x, int y, int subpage, float size, float tilt, boolean spin, boolean build,
                                Map<Character, BlockState> legend, String[][] layers) {
      this(x, y, subpage, size, tilt, spin, build, legend, layers, -11.0F);
   }

   public PageElementMultiblock(int x, int y, int subpage, float size, float tilt, boolean spin, boolean build,
                                Map<Character, BlockState> legend, String[][] layers, float podiumLift) {
      super(x, y, subpage);
      this.size = size;
      this.tilt = tilt;
      this.spin = spin;
      this.build = build;
      this.podiumLift = podiumLift;
      int hh = layers.length;
      int dd = 0, ww = 0;
      for (String[] layer : layers) {
         dd = Math.max(dd, layer.length);
         for (String row : layer) ww = Math.max(ww, row.length());
      }
      this.h = hh; this.d = dd; this.w = ww;
      this.grid = new BlockState[hh][dd][ww];
      for (int yy = 0; yy < hh; yy++) {
         String[] layer = layers[yy];
         for (int zz = 0; zz < layer.length; zz++) {
            String row = layer[zz];
            for (int xx = 0; xx < row.length(); xx++) {
               BlockState st = legend.get(row.charAt(xx));
               if (st != null && !st.isAir()) this.grid[yy][zz][xx] = st;
            }
         }
      }
   }

   @OnlyIn(Dist.CLIENT)
   private int visibleLayers() {
      if (!this.build) return this.h;
      long ticks = Minecraft.getInstance().level != null ? Minecraft.getInstance().level.getGameTime() : 0L;
      int phase = (int) ((ticks / 22L) % (this.h + 3L));
      return phase >= this.h ? this.h : phase + 1;
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int target) {
      if (this.h == 0 || this.w == 0 || this.d == 0) return;
      Minecraft mc = Minecraft.getInstance();
      float partial = mc.getFrameTime();
      float time = (mc.level != null ? mc.level.getGameTime() : 0L) + partial;
      float yaw = this.spin ? (time * 1.1F) % 360.0F : 35.0F;

      com.mojang.blaze3d.systems.RenderSystem.enableDepthTest();
      com.mojang.blaze3d.systems.RenderSystem.depthMask(true);
      com.mojang.blaze3d.platform.Lighting.setupFor3DItems();

      double z = isGUI ? 250.0D : this.podiumLift;
      PoseStack pose = gg.pose();
      pose.pushPose();
      pose.translate(this.x + xIn, this.y + yIn, z);
      float s = this.size;
      float sy = -this.size;
      if (!isGUI) {
         PageElement3DModel.faceViewer(pose, mc);
         s = this.size * PageElement3DModel.PODIUM_MAG;
         sy = this.size * PageElement3DModel.PODIUM_MAG;
      }
      pose.scale(s, sy, s);
      pose.mulPose(Axis.XP.rotationDegrees(this.tilt));
      pose.mulPose(Axis.YP.rotationDegrees(yaw));
      pose.translate(-this.w / 2.0F, -this.h / 2.0F, -this.d / 2.0F);

      BlockRenderDispatcher blockRenderer = mc.getBlockRenderer();
      MultiBufferSource.BufferSource buf = mc.renderBuffers().bufferSource();
      int show = visibleLayers();
      for (int yy = 0; yy < show; yy++) {
         for (int zz = 0; zz < this.d; zz++) {
            for (int xx = 0; xx < this.w; xx++) {
               BlockState st = this.grid[yy][zz][xx];
               if (st == null) continue;
               pose.pushPose();
               pose.translate(xx, yy, zz);
               blockRenderer.renderSingleBlock(st, pose, buf, 0xF000F0, OverlayTexture.NO_OVERLAY);
               pose.popPose();
            }
         }
      }
      buf.endBatch();
      pose.popPose();

      com.mojang.blaze3d.platform.Lighting.setupForFlatItems();
      com.mojang.blaze3d.systems.RenderSystem.depthMask(false);
      com.mojang.blaze3d.systems.RenderSystem.disableDepthTest();
   }
}
