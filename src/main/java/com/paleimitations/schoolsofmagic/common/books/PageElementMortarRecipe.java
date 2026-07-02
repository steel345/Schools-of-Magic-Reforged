package com.paleimitations.schoolsofmagic.common.books;

import com.paleimitations.schoolsofmagic.common.recipes.RecipeMortNPest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PageElementMortarRecipe extends PageElement {
   public static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/gui/books/mortnpest_recipe.png");
   public final RecipeMortNPest recipe;

   public PageElementMortarRecipe(RecipeMortNPest recipe, int x, int y) {
      super(x, y);
      this.recipe = recipe;
   }

   public PageElementMortarRecipe(RecipeMortNPest recipe, int x, int y, int target) {
      super(x, y, target);
      this.recipe = recipe;
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int target) {
      this.drawTexturedModalRect(gg, TEXTURE, this.x + xIn, this.y + yIn, 0, 0, 88, 62);
      if (this.recipe != null) {
         int tick = Minecraft.getInstance().player != null ? Minecraft.getInstance().player.tickCount : 0;
         if (this.recipe.getInput().getItems().length > 0) {
            this.drawItemStack(gg,
               this.recipe.getInput().getItems()[tick / 20 % this.recipe.getInput().getItems().length],
               this.x + xIn + 2, this.y + yIn + 11, isGUI);
         }
         if (this.recipe.getInputSecondary().getItems().length > 0) {
            this.drawItemStack(gg,
               this.recipe.getInputSecondary().getItems()[tick / 20 % this.recipe.getInputSecondary().getItems().length],
               this.x + xIn + 2, this.y + yIn + 34, isGUI);
         }
         this.drawItemStack(gg, this.recipe.getOutput(), this.x + xIn + 70, this.y + yIn + 11, isGUI);
         this.drawItemStack(gg, this.recipe.getOutputSecondary(), this.x + xIn + 70, this.y + yIn + 34, isGUI);
      }
   }
}
