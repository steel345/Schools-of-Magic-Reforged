package com.paleimitations.schoolsofmagic.common.books;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.List;
import net.minecraft.ChatFormatting;

public class BookPageSpell extends BookPage {
   public final Spell spell;

   public BookPageSpell(Spell spell) {
      super(spell.getName(), buildElements(spell));
      this.spell = spell;

      if (spell instanceof com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom) {
         return;
      }

      for (Spell.EnumSpellModifier mod : spell.modifiers.keySet()) {
         this.addElement(new PageElementDescription("modifier." + mod.getSerializedName() + ".name", ChatFormatting.GOLD));
      }

      SchoolsOfMagic.proxy.loadBookPageText(this);
   }

   private static List<PageElement> buildElements(Spell spell) {
      if (spell instanceof com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom sc) {
         return Lists.newArrayList(new PageElement[]{ new PageElementCustomSpell(sc) });
      }
      return Lists.newArrayList(new PageElement[]{
         new PageElementSpellInfo(spell),
         new PageElementParagraphs("spell_" + spell.getName(), 0.75F, 0, 1,
            new ParagraphBox(134, 50, 0, 99, 140),
            new ParagraphBox(23, 50, 1, 99, 140),
            new ParagraphBox(134, 50, 1, 99, 140)),
         new PageElementDescription("page.spell_" + spell.getName() + ".desc")
      });
   }

   @Override
   public void addPageToRegistry() {
   }

   public static int getColorFromElementList(List<MagicElement> elements) {
      if (elements.isEmpty()) return 3694022;
      float f = 0.0F, f1 = 0.0F, f2 = 0.0F;
      int j = 0;
      for (MagicElement element : elements) {
         int k = element.getColor();
         f += (float)(k >> 16 & 0xFF) / 255.0F;
         f1 += (float)(k >> 8 & 0xFF) / 255.0F;
         f2 += (float)(k & 0xFF) / 255.0F;
         j++;
      }
      if (j == 0) return 0;
      f = f / (float)j * 255.0F;
      f1 = f1 / (float)j * 255.0F;
      f2 = f2 / (float)j * 255.0F;
      return (int)f << 16 | (int)f1 << 8 | (int)f2;
   }

   public Spell getSpell() {
      return this.spell;
   }
}
