package com.paleimitations.schoolsofmagic.common.books;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.paleimitations.schoolsofmagic.common.IMagicType;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeRitualCrafting;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import java.util.List;
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

public class PageElementRitualRecipe extends PageElement {
   public static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/gui/books/crafting_ritual_recipe.png");
   public final RecipeRitualCrafting recipe;
   public final Map<IMagicType, Integer> map = Maps.newHashMap();

   public PageElementRitualRecipe(RecipeRitualCrafting recipe, int x, int y, int target) {
      super(x, y, target);
      this.recipe = recipe;
   }

   @Override
   @OnlyIn(Dist.CLIENT)
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int target) {
      Minecraft mc = Minecraft.getInstance();
      this.drawTexturedModalRect(gg, TEXTURE, this.x + xIn, this.y + yIn, 0, 0, 106, 123);
      List<Ingredient> inputs = this.recipe.getInputs();
      if (this.map.isEmpty()) {
         for (int i = 0; i < 6; ++i) {
            if (i < this.recipe.getMinSchoolLevels().length && this.recipe.getMinSchoolLevels()[i] > 0) {
               this.map.put(MagicSchoolRegistry.getSchoolFromId(i), this.recipe.getMinSchoolLevels()[i]);
            }
         }
         for (int i = 0; i < 16; ++i) {
            if (i < this.recipe.getMinElementLevels().length && this.recipe.getMinElementLevels()[i] > 0) {
               this.map.put(MagicElementRegistry.getElementFromId(i), this.recipe.getMinElementLevels()[i]);
            }
         }
      }
      int tick = mc.player != null ? mc.player.tickCount : 0;
      int[][] offs = {
         {46, 63}, {25, 52}, {4, 45}, {2, 24}, {23, 17},
         {44, 23}, {65, 33}, {86, 23}, {80, 2}
      };
      for (int i = 0; i < offs.length && i < inputs.size(); ++i) {
         Ingredient ing = inputs.get(i);
         if (ing != null && ing.getItems().length > 0) {
            this.drawItemStack(gg, ing.getItems()[tick / 20 % ing.getItems().length],
               this.x + xIn + offs[i][0], this.y + yIn + offs[i][1], isGUI);
         }
      }
      this.drawItemStack(gg, this.recipe.getOutput(), this.x + xIn + 44, this.y + yIn + 105, isGUI);
      Font font = mc.font;
      if (this.recipe.getManaUsage() > 0.0F) {
         gg.drawString(font, String.valueOf(Math.round(this.recipe.getManaUsage())),
            this.x + xIn + 71, this.y + yIn + 57, 0, false);
      }
      if (!this.map.isEmpty()) {
         IMagicType magicType = Lists.newArrayList(this.map.keySet()).get(tick / 30 % this.map.size());
         int level = this.map.get(magicType);
         String requirements = I18n.get("gui.jei.catalyst_basin.requires") + " " + I18n.get(magicType.getFormattedName())
            + "\n" + I18n.get("gui.jei.catalyst_basin.skill") + " " + (level + 1);
         gg.pose().pushPose();
         gg.pose().scale(0.75F, 0.75F, 0.75F);
         gg.drawWordWrap(font, Component.literal(requirements),
            Math.round((2.0F + this.x + xIn) / 0.75F), Math.round((124.0F + this.y + yIn) / 0.75F),
            Math.round(133.33333F), 0);
         gg.pose().popPose();
      }
   }
}
