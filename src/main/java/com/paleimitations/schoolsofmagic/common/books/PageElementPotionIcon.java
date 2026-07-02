package com.paleimitations.schoolsofmagic.common.books;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PageElementPotionIcon extends PageElement {
   public final MobEffectInstance potion;

   public PageElementPotionIcon(MobEffectInstance potion, int x, int y) {
      super(x, y);
      this.potion = potion;
   }

   @Override
   @OnlyIn(Dist.CLIENT)
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int target) {
      MobEffect effect = this.potion.getEffect();
      if (effect == null) return;
      Minecraft mc = Minecraft.getInstance();
      TextureAtlasSprite sprite = mc.getMobEffectTextures().get(effect);
      if (sprite == null) return;
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      gg.blit(this.x + xIn, this.y + yIn, 0, 18, 18, sprite);
   }
}
