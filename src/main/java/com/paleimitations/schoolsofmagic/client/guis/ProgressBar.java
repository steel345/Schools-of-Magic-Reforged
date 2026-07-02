package com.paleimitations.schoolsofmagic.client.guis;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ProgressBar {
   private final ResourceLocation texture;
   private final ProgressBarDirection direction;
   private final int positionX;
   private final int positionY;
   private final int width;
   private final int height;
   private final int textureX;
   private final int textureY;
   private float min;
   private float max = 0.0F;

   public ProgressBar(ResourceLocation texture, ProgressBarDirection direction,
                      int width, int height, int positonX, int positionY, int textureX, int textureY) {
      this.texture = texture;
      this.direction = direction;
      this.width = width;
      this.height = height;
      this.positionX = positonX;
      this.positionY = positionY;
      this.textureX = textureX;
      this.textureY = textureY;
   }

   public ProgressBar setMin(int min) { this.min = (float) min; return this; }
   public ProgressBar setMax(int max) { this.max = (float) max; return this; }

   private int getAdjustedWidth() {
      return (int) (this.min != 0.0F && this.max != 0.0F ? this.min / this.max * (float) this.width : 0.0F);
   }
   private int getAdjustedHeight() {
      return (int) (this.min != 0.0F && this.max != 0.0F ? this.min / this.max * (float) this.height : 0.0F);
   }

   public void draw(GuiGraphics gg) {
      switch (this.direction) {
         case DIAGONAL_UP_LEFT:
            gg.blit(texture, positionX, positionY, textureX, textureY, width, height);
            gg.blit(texture, positionX, positionY, positionX, positionY,
               width - getAdjustedWidth(), height - getAdjustedHeight());
            break;
         case DIAGONAL_UP_RIGHT:
            gg.blit(texture, positionX, positionY, textureX, textureY, width, height);
            gg.blit(texture, positionX + getAdjustedWidth(), positionY,
               positionX + getAdjustedWidth(), positionY,
               width - getAdjustedWidth(), height - getAdjustedHeight());
            break;
         case DIAGONAL_DOWN_LEFT:
            gg.blit(texture, positionX, positionY, textureX, textureY, width, height);
            gg.blit(texture, positionX, positionY + getAdjustedHeight(),
               positionX, positionY + getAdjustedHeight(),
               width - getAdjustedWidth(), height - getAdjustedHeight());
            break;
         case DIAGONAL_DOWN_RIGHT:
            gg.blit(texture, positionX, positionY, textureX, textureY,
               getAdjustedWidth(), getAdjustedHeight());
            break;
         case DOWN_TO_UP:
            gg.blit(texture, positionX, positionY, textureX, textureY, width, height);
            gg.blit(texture, positionX, positionY, positionX, positionY,
               width, height - getAdjustedHeight());
            break;
         case LEFT_TO_RIGHT:
            gg.blit(texture, positionX, positionY, textureX, textureY, getAdjustedWidth(), height);
            break;
         case RIGHT_TO_LEFT:
            gg.blit(texture, positionX, positionY, textureX, textureY, width, height);
            gg.blit(texture, positionX, positionY, positionX, positionY,
               width - getAdjustedWidth(), height);
            break;
         case UP_TO_DOWN:
            gg.blit(texture, positionX, positionY, textureX, textureY, width, getAdjustedHeight());
            break;
         default:
            gg.blit(texture, positionX, positionY, textureX, textureY, width, height);
      }
   }

   public static enum ProgressBarDirection {
      LEFT_TO_RIGHT, RIGHT_TO_LEFT, UP_TO_DOWN, DOWN_TO_UP,
      DIAGONAL_UP_RIGHT, DIAGONAL_UP_LEFT, DIAGONAL_DOWN_RIGHT, DIAGONAL_DOWN_LEFT;
   }
}
