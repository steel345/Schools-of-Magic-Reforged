package com.paleimitations.schoolsofmagic.common.books;

import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PageElementPotionRecipe extends PageElement {
   public static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/gui/books/potion_recipe.png");
   public final List<ItemStack> inputs;
   public final ItemStack output;

   public PageElementPotionRecipe(List<ItemStack> inputs, ItemStack output) {
      super(0, 0);
      this.inputs = inputs;
      this.output = output;
   }

   @Override
   @OnlyIn(Dist.CLIENT)
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int target) {
      this.drawTexturedModalRect(gg, TEXTURE, this.x + xIn, this.y + yIn, 0, 0, 256, 256);
      int[][] offs = {
         {180, 131}, {157, 120}, {134, 108}, {136, 85}, {159, 74},
         {182, 86}, {205, 96}, {220, 73}, {197, 61}
      };
      for (int i = 0; i < offs.length && i < this.inputs.size(); ++i) {
         ItemStack s = this.inputs.get(i);
         if (s != null && !s.isEmpty()) {
            this.drawItemStack(gg, s, this.x + xIn + offs[i][0], this.y + yIn + offs[i][1], isGUI);
         }
      }
      this.drawItemStack(gg, this.output, this.x + xIn + 178, this.y + yIn + 163, isGUI);
   }
}
