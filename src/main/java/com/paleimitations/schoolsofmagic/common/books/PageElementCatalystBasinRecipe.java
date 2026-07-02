package com.paleimitations.schoolsofmagic.common.books;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.paleimitations.schoolsofmagic.common.IMagicType;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeCatalystBasin;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PageElementCatalystBasinRecipe extends PageElement {
   public static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/gui/books/catalyst_basin_recipe.png");
   public final RecipeCatalystBasin recipe;
   public final Map<IMagicType, Integer> map = Maps.newHashMap();

   public PageElementCatalystBasinRecipe(RecipeCatalystBasin recipe, int x, int y, int target) {
      super(x, y, target);
      this.recipe = recipe;
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int target) {
      Minecraft mc = Minecraft.getInstance();
      if (this.map.isEmpty()) {
         for (int i = 0; i < 6; ++i) {
            if (i < this.recipe.getSchoolLevels().length && this.recipe.getSchoolLevels()[i] > 0) {
               this.map.put(MagicSchoolRegistry.getSchoolFromId(i), this.recipe.getSchoolLevels()[i]);
            }
         }
         for (int i = 0; i < 16; ++i) {
            if (i < this.recipe.getElementLevels().length && this.recipe.getElementLevels()[i] > 0) {
               this.map.put(MagicElementRegistry.getElementFromId(i), this.recipe.getElementLevels()[i]);
            }
         }
      }
      this.drawTexturedModalRect(gg, TEXTURE, this.x + xIn, this.y + yIn, 0, 0, 104, 105);
      int tick = mc.player != null ? mc.player.tickCount : 0;
      Ingredient sub = this.recipe.getSubstrate();
      Ingredient cat = this.recipe.getCatalyst();
      Ingredient sol = this.recipe.getSolution();
      Ingredient rea = this.recipe.getReactant();
      if (sub.getItems().length > 0) this.drawItemStack(gg, sub.getItems()[tick / 20 % sub.getItems().length], this.x + xIn + 14, this.y + yIn + 1, isGUI);
      if (cat.getItems().length > 0) this.drawItemStack(gg, cat.getItems()[tick / 20 % cat.getItems().length], this.x + xIn + 14, this.y + yIn + 20, isGUI);
      if (sol.getItems().length > 0) this.drawItemStack(gg, sol.getItems()[tick / 20 % sol.getItems().length], this.x + xIn + 74, this.y + yIn + 1, isGUI);
      if (rea.getItems().length > 0) this.drawItemStack(gg, rea.getItems()[tick / 20 % rea.getItems().length], this.x + xIn + 74, this.y + yIn + 20, isGUI);
      this.drawItemStack(gg, this.recipe.getOutput(), this.x + xIn + 14, this.y + yIn + 65, isGUI);
      if (cat.getItems().length > 0) this.drawItemStack(gg, cat.getItems()[tick / 20 % cat.getItems().length], this.x + xIn + 14, this.y + yIn + 84, isGUI);
      if (sol.getItems().length > 0) this.drawItemStack(gg, sol.getItems()[tick / 20 % sol.getItems().length], this.x + xIn + 74, this.y + yIn + 65, isGUI);

      Font font = mc.font;
      if (this.recipe.getManaCost() > 0.0F) {
         gg.drawString(font, String.valueOf(Math.round(this.recipe.getManaCost())), this.x + xIn + 73, this.y + yIn + 47, 0, false);
      }
      if (!this.map.isEmpty()) {
         IMagicType magicType = Lists.newArrayList(this.map.keySet()).get(tick / 30 % this.map.size());
         int level = this.map.get(magicType);
         String requirements = I18n.get("gui.jei.catalyst_basin.requires") + " " + I18n.get(magicType.getFormattedName())
            + "\n" + I18n.get("gui.jei.catalyst_basin.skill") + " " + (level + 1);
         gg.pose().pushPose();
         gg.pose().scale(0.75F, 0.75F, 0.75F);

         gg.drawWordWrap(font, Component.literal(requirements),
            Math.round((3.0F + this.x + xIn) / 0.75F), Math.round((104.0F + this.y + yIn) / 0.75F),
            Math.round(133.33333F), 0);
         gg.pose().popPose();
      }
   }
}
