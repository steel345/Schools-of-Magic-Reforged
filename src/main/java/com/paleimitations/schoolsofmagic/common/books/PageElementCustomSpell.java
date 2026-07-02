package com.paleimitations.schoolsofmagic.common.books;

import com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom;
import java.awt.Color;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PageElementCustomSpell extends PageElement {
   private static final net.minecraft.resources.ResourceLocation EMPTY_CIRCLE =
      new net.minecraft.resources.ResourceLocation("som", "textures/gui/spells/empty_circle.png");
   private final SpellCustom spell;

   public PageElementCustomSpell(SpellCustom spell) {
      super(0, 0);
      this.spell = spell;
   }

   @Override
   @OnlyIn(Dist.CLIENT)
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int target) {
      Color color = new Color(this.spell.getTintColor() & 0xFFFFFF);
      float cr = color.getRed() / 255.0F;
      float cg = color.getGreen() / 255.0F;
      float cb = color.getBlue() / 255.0F;

      gg.setColor(cr, cg, cb, 1.0F);
      gg.blit(EMPTY_CIRCLE, 22 + xIn, 45 + yIn, 0, 0, 32, 32, 32, 32);
      gg.setColor(1.0F, 1.0F, 1.0F, 1.0F);

      if (this.spell.getEffect() != null) {
         TextureAtlasSprite sprite = Minecraft.getInstance().getMobEffectTextures().get(this.spell.getEffect());
         gg.blit(25 + xIn, 48 + yIn, 0, 26, 26, sprite);
      }

      String name = this.spell.hasName() ? this.spell.getCustomName() : "Unnamed Spell";
      PageElementSpellInfo.drawStandardText(gg, name, 58, 14, 86 + xIn, 60.0F + yIn, Color.BLACK.getRGB(), true, false);

      String desc = this.spell.getCustomDescription();
      if (desc != null && !desc.isEmpty()) {
         Font font = Minecraft.getInstance().font;
         List<FormattedCharSequence> lines = font.split(Component.literal(desc), 92);
         int ly = 86 + yIn;
         for (int i = 0; i < lines.size() && i < 9; i++) {
            gg.drawString(font, lines.get(i), 24 + xIn, ly, 0x402000, false);
            ly += font.lineHeight + 1;
         }
      }
   }
}
