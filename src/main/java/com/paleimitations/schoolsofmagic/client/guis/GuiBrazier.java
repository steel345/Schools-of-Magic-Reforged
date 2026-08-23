package com.paleimitations.schoolsofmagic.client.guis;

import com.paleimitations.schoolsofmagic.common.containers.ContainerBrazier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiBrazier extends AbstractContainerScreen<ContainerBrazier> {
   public static final ResourceLocation TEXTURE_LIT = new ResourceLocation("som", "textures/gui/container/brazier_gui.png");
   public static final ResourceLocation TEXTURE_UNLIT = new ResourceLocation("som", "textures/gui/container/brazier_nofire_gui.png");

   public GuiBrazier(ContainerBrazier menu, Inventory playerInventory, Component title) {
      super(menu, playerInventory, title);
      this.imageWidth = 176;
      this.imageHeight = 241;
   }

   @Override
   protected void init() {
      super.init();
      this.titleLabelX = -9999;
      this.inventoryLabelX = -9999;
   }

   @Override
   public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
      this.renderBackground(gg);
      super.render(gg, mouseX, mouseY, partialTicks);
      this.renderTooltip(gg, mouseX, mouseY);
   }

   @Override
   protected void renderBg(GuiGraphics gg, float partialTicks, int mouseX, int mouseY) {
      ResourceLocation tex = this.menu.isLit() ? TEXTURE_LIT : TEXTURE_UNLIT;
      gg.blit(tex, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
   }

   @Override
   protected void renderLabels(GuiGraphics gg, int mouseX, int mouseY) {
      com.paleimitations.schoolsofmagic.common.tileentity.TileEntityRitualCenter te = this.menu.getTile();
      if (te == null) {
         return;
      }
      com.paleimitations.schoolsofmagic.common.recipes.RecipeRitualCrafting recipe = null;
      for (com.paleimitations.schoolsofmagic.common.recipes.RecipeRitualCrafting r
            : com.paleimitations.schoolsofmagic.common.registries.RecipeRegistry.ritualRecipes) {
         if (r.matches(te.handler)) {
            recipe = r;
            break;
         }
      }
      if (recipe == null) {
         return;
      }

      net.minecraft.client.gui.Font font = this.font;
      int tick = (net.minecraft.client.Minecraft.getInstance().player != null)
         ? net.minecraft.client.Minecraft.getInstance().player.tickCount : 0;

      gg.pose().pushPose();
      gg.pose().translate(35.0F, 8.0F, 0.0F);

      if (recipe.getNote() != null) {
         float sc = 0.7F;
         String note = recipe.getNote();
         gg.pose().pushPose();
         gg.pose().scale(sc, sc, 1.0F);
         gg.drawString(font, note, Math.round(53 / sc) - font.width(note) / 2, Math.round(126 / sc),
            java.awt.Color.GRAY.getRGB(), false);
         gg.pose().popPose();
      }

      int mana = recipe.getManaUsage();
      if (mana <= 0) {
         gg.pose().popPose();
         return;
      }
      String s = String.valueOf(mana);
      gg.drawString(font, s, 87 - font.width(s) / 2, 65, java.awt.Color.GRAY.getRGB(), false);

      java.util.Map<com.paleimitations.schoolsofmagic.common.IMagicType, Integer> map = com.google.common.collect.Maps.newHashMap();
      for (int i = 0; i < 6; i++) {
         if (i < recipe.getMinSchoolLevels().length && recipe.getMinSchoolLevels()[i] > 0) {
            map.put(com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry.getSchoolFromId(i), recipe.getMinSchoolLevels()[i]);
         }
      }
      for (int i = 0; i < 16; i++) {
         if (i < recipe.getMinElementLevels().length && recipe.getMinElementLevels()[i] > 0) {
            map.put(com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry.getElementFromId(i), recipe.getMinElementLevels()[i]);
         }
      }
      if (map.isEmpty()) {
         gg.pose().popPose();
         return;
      }
      java.util.List<com.paleimitations.schoolsofmagic.common.IMagicType> keys = com.google.common.collect.Lists.newArrayList(map.keySet());
      com.paleimitations.schoolsofmagic.common.IMagicType type = keys.get(tick / 30 % keys.size());
      int level = map.get(type);

      Component line1 = Component.translatable("gui.jei.catalyst_basin.requires");
      Component line2 = Component.translatable(type.getFormattedName());
      Component line3 = Component.translatable("gui.jei.catalyst_basin.skill")
         .append(Component.literal(" " + (level + 1)));
      gg.pose().pushPose();
      gg.pose().scale(0.8F, 0.8F, 1.0F);
      int ty = 132;
      gg.drawString(font, line1, 16, ty, java.awt.Color.DARK_GRAY.getRGB(), false);
      gg.drawString(font, line2, 16, ty + font.lineHeight, java.awt.Color.DARK_GRAY.getRGB(), false);
      gg.drawString(font, line3, 16, ty + 2 * font.lineHeight, java.awt.Color.DARK_GRAY.getRGB(), false);
      gg.pose().popPose();

      gg.pose().popPose();
   }
}
