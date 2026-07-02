package com.paleimitations.schoolsofmagic.client.guis;

import java.awt.Color;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.paleimitations.schoolsofmagic.common.IMagicType;
import com.paleimitations.schoolsofmagic.common.containers.ContainerCatalystBasin;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeCatalystBasin;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityCatalystBasin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiCatalystBasin extends AbstractContainerScreen<ContainerCatalystBasin> {
   public static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/gui/container/catalyst_basin.png");
   public static final ResourceLocation TEXTURE_ACTIVE = new ResourceLocation("som", "textures/gui/container/catalyst_basin_active.png");

   public GuiCatalystBasin(ContainerCatalystBasin menu, Inventory playerInventory, Component title) {
      super(menu, playerInventory, title);
      this.imageWidth = 176;
      this.imageHeight = 215;
   }

   @Override
   public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
      this.renderBackground(gg);
      super.render(gg, mouseX, mouseY, partialTicks);

      TileEntityCatalystBasin tb = this.menu.getTile();
      ResourceLocation tex = (tb != null && tb.isActive()) ? TEXTURE_ACTIVE : TEXTURE;

      gg.flush();
      com.mojang.blaze3d.systems.RenderSystem.disableDepthTest();
      gg.blit(tex, this.leftPos + 49, this.topPos + 66, 178, 0, 78, 53);
      gg.flush();
      com.mojang.blaze3d.systems.RenderSystem.enableDepthTest();
      this.renderTooltip(gg, mouseX, mouseY);
   }

   @Override
   protected void renderBg(GuiGraphics gg, float partialTicks, int mouseX, int mouseY) {
      TileEntityCatalystBasin tb = this.menu.getTile();
      ResourceLocation tex = (tb != null && tb.isActive()) ? TEXTURE_ACTIVE : TEXTURE;
      gg.blit(tex, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
      if (tb != null) {
         gg.pose().pushPose();
         gg.pose().scale(2.0F, 2.0F, 2.0F);
         gg.renderItem(tb.handler.getStackInSlot(1), (72 + this.leftPos) / 2, (45 + this.topPos) / 2);
         gg.renderItem(tb.handler.getStackInSlot(0), (52 + this.leftPos) / 2, (60 + this.topPos) / 2);
         gg.renderItem(tb.handler.getStackInSlot(3), (92 + this.leftPos) / 2, (56 + this.topPos) / 2);
         gg.renderItem(tb.handler.getStackInSlot(2), (75 + this.leftPos) / 2, (68 + this.topPos) / 2);
         gg.pose().popPose();
      }

   }

   @Override
   protected void renderLabels(GuiGraphics gg, int mouseX, int mouseY) {
      TileEntityCatalystBasin tb = this.menu.getTile();
      if (tb == null) return;
      Map<IMagicType, Integer> map = Maps.newHashMap();
      RecipeCatalystBasin recipe = tb.getRecipe();
      if (tb.hasValidRecipe() && recipe != null) {
         for (int i = 0; i < 6; i++) {
            if (i < recipe.getSchoolLevels().length && recipe.getSchoolLevels()[i] > 0) {
               map.put(MagicSchoolRegistry.getSchoolFromId(i), recipe.getSchoolLevels()[i]);
            }
         }
         for (int ix = 0; ix < 16; ix++) {
            if (ix < recipe.getElementLevels().length && recipe.getElementLevels()[ix] > 0) {
               map.put(MagicElementRegistry.getElementFromId(ix), recipe.getElementLevels()[ix]);
            }
         }
         Component manaString = Component.translatable("gui.jei.catalyst_basin.requires")
            .append(Component.literal(" " + Math.round(recipe.getManaCost()) + " "))
            .append(Component.translatable("gui.jei.catalyst_basin.mana"));
         gg.drawString(this.font, manaString,
            Math.round(87.5F - this.font.width(manaString) / 2.0F), 7,
            Color.GRAY.getRGB(), false);
         if (!map.isEmpty()) {
            IMagicType magicType = Lists.newArrayList(map.keySet()).get(Minecraft.getInstance().player.tickCount / 30 % map.size());
            int level = map.get(magicType);
            Component req = Component.translatable(magicType.getFormattedName())
               .append(Component.literal(" "))
               .append(Component.translatable("gui.jei.catalyst_basin.skill"))
               .append(Component.literal(" " + (level + 1)));
            gg.pose().pushPose();
            gg.pose().scale(0.7F, 0.7F, 0.7F);
            gg.drawString(this.font, req,
               125 - Math.round(this.font.width(req) / 2.0F),
               26, Color.DARK_GRAY.getRGB(), false);
            gg.pose().popPose();
         }
      }
   }
}
