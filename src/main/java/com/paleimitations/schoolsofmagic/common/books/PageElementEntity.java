package com.paleimitations.schoolsofmagic.common.books;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PageElementEntity extends PageElement {
   private final EntityType<?> type;

   private int scale;
   private LivingEntity cached;

   public PageElementEntity(EntityType<?> type, int x, int y, int scale, int subpage) {
      super(x, y, subpage);
      this.type = type;
      this.scale = scale;
   }

   @OnlyIn(Dist.CLIENT)
   public void setScale(int scale) {
      this.scale = Math.max(1, scale);
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

      float spin = isGUI ? ((mc.level.getGameTime() + partial) * 2.0F) % 360.0F : 0.0F;
      e.yBodyRot = spin; e.yBodyRotO = spin;
      e.setYRot(spin); e.yRotO = spin;
      e.yHeadRot = spin; e.yHeadRotO = spin;
      e.setXRot(0.0F); e.xRotO = 0.0F;

      org.joml.Quaternionf pose = new org.joml.Quaternionf().rotateZ((float) Math.PI);
      org.joml.Quaternionf tilt = new org.joml.Quaternionf().rotateX(0.31F);
      pose.mul(tilt);

      if (!isGUI) {
         gg.pose().pushPose();
         gg.pose().translate(0.0F, 0.0F, -58.0F);

         com.mojang.blaze3d.systems.RenderSystem.depthMask(true);
      }
      try {
         InventoryScreen.renderEntityInInventory(gg, this.x + xIn, this.y + yIn, this.scale, pose, tilt, e);
      } catch (Exception ignored) {
      } finally {
         if (!isGUI) {
            com.mojang.blaze3d.systems.RenderSystem.depthMask(false);
            gg.pose().popPose();

            com.mojang.blaze3d.platform.Lighting.setupForFlatItems();
         }
      }
   }
}
