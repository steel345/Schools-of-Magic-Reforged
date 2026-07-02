package com.paleimitations.schoolsofmagic.common.books;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PageElementFurnaceRecipe extends PageElement {
   public static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/gui/books/furnace_recipe.png");
   public final ItemStack input;
   public final ItemStack output;

   public PageElementFurnaceRecipe(ItemStack input, ItemStack output, int x, int y, int target) {
      super(x, y, target);
      this.input = input;
      this.output = output;
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int target) {
      this.drawTexturedModalRect(gg, TEXTURE, this.x + xIn, this.y + yIn, 0, 0, 100, 123);
      this.drawItemStack(gg, this.input, this.x + xIn + 1, this.y + yIn + 1, isGUI);
      this.drawItemStack(gg, new ItemStack(Blocks.FURNACE), this.x + xIn + 19, this.y + yIn + 1, isGUI);
      this.drawItemStack(gg, this.output, this.x + xIn + 37, this.y + yIn + 1, isGUI);
   }
}
