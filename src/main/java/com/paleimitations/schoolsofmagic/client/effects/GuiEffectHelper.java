package com.paleimitations.schoolsofmagic.client.effects;

import com.paleimitations.imitationcore.client.gui_effects.GuiEffect;
import com.paleimitations.imitationcore.client.gui_effects.GuiEffectHandler;
import java.awt.Color;
import java.util.Random;

public class GuiEffectHelper {
   public GuiEffectHelper() {
   }

   public static GuiEffect createGuiSparkleParticle(float x, float y, float vX, float vY, Color color) {
      Random rand = new Random();
      GuiEffect p = new GuiEffect(ImitationSpriteLibrary.star, x, y, vX, vY, 5.0F + rand.nextFloat() * 5.0F, rand.nextInt(10), 30, 0, color);
      GuiEffectHandler.getInstance().registerEffect(p);
      return p;
   }
}
