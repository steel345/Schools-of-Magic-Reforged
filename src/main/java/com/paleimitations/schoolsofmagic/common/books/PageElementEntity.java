package com.paleimitations.schoolsofmagic.common.books;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// Renders a living entity's 3D model on a book page (a bestiary portrait). x,y is
// the base of the model, scale is roughly its pixel height. The entity turns in a
// continuous full 360 spin. Flat GUI (in-hand / lectern / podium screen) uses the
// vanilla screen-space helper; the podium's in-world 3D book renders the entity
// through the entity dispatcher, billboarded onto the page like the other figures.
public class PageElementEntity extends PageElement {
   private final EntityType<?> type;
   private final int scale;
   private LivingEntity cached;

   public PageElementEntity(EntityType<?> type, int x, int y, int scale, int subpage) {
      super(x, y, subpage);
      this.type = type;
      this.scale = scale;
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int target) {
      if (this.type == null) return;
      Minecraft mc = Minecraft.getInstance();
      if (mc.level == null) return;
      if (this.cached == null || this.cached.level() != mc.level) {
         Entity e = this.type.create(mc.level);
         this.cached = e instanceof LivingEntity le ? le : null;
      }
      if (this.cached == null) return;

      LivingEntity e = this.cached;
      float partial = mc.getFrameTime();
      float spin = ((mc.level.getGameTime() + partial) * 2.0F) % 360.0F;
      e.yBodyRot = spin; e.yBodyRotO = spin;
      e.setYRot(spin); e.yRotO = spin;
      e.yHeadRot = spin; e.yHeadRotO = spin;
      e.setXRot(0.0F); e.xRotO = 0.0F;

      if (isGUI) {
         org.joml.Quaternionf pose = new org.joml.Quaternionf().rotateZ((float) Math.PI);
         org.joml.Quaternionf tilt = new org.joml.Quaternionf().rotateX(0.31F);
         pose.mul(tilt);
         try {
            InventoryScreen.renderEntityInInventory(gg, this.x + xIn, this.y + yIn, this.scale, pose, tilt, e);
         } catch (Exception ignored) {
         }
         return;
      }

      // In-world 3D podium book: the page is drawn at a tiny scale in world space, so
      // render the entity through the dispatcher, billboarded to face the viewer and
      // sized to the page.
      try {
         renderOnBook(gg, mc, e, xIn, yIn, partial);
      } catch (Exception ignored) {
      }
   }

   @OnlyIn(Dist.CLIENT)
   private void renderOnBook(GuiGraphics gg, Minecraft mc, LivingEntity e, int xIn, int yIn, float partial) {
      float bb = Math.max(0.6F, e.getBbHeight());
      // Facing the camera removes the flat-page foreshortening, so it needs a healthy
      // enlargement, sized relative to the entity height so tall/short mobs match.
      float f = this.scale * 4.2F / bb;

      com.mojang.blaze3d.systems.RenderSystem.enableDepthTest();
      com.mojang.blaze3d.systems.RenderSystem.depthMask(true);
      com.mojang.blaze3d.platform.Lighting.setupForEntityInInventory();

      PoseStack pose = gg.pose();
      pose.pushPose();
      pose.translate(this.x + xIn, this.y + yIn, -8.0D);
      PageElement3DModel.faceViewer(pose, mc);
      pose.scale(f, f, f);
      pose.translate(0.0D, -bb / 2.0D, 0.0D);

      EntityRenderDispatcher disp = mc.getEntityRenderDispatcher();
      disp.setRenderShadow(false);
      MultiBufferSource.BufferSource buf = mc.renderBuffers().bufferSource();
      // Constant partial tick (like the flat-book helper) freezes the idle animation
      // so the model doesn't bob up and down; the spin still applies via yBodyRot.
      disp.render(e, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, pose, buf, 0xF000F0);
      buf.endBatch();
      disp.setRenderShadow(true);
      pose.popPose();

      com.mojang.blaze3d.platform.Lighting.setupForFlatItems();
      com.mojang.blaze3d.systems.RenderSystem.depthMask(false);
      com.mojang.blaze3d.systems.RenderSystem.disableDepthTest();
   }
}
