package com.paleimitations.schoolsofmagic.common.items.capabilities.page;

import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.books.BookPageSpell;
import com.paleimitations.schoolsofmagic.common.registries.BookPageRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

public class Page implements IPage, INBTSerializable<CompoundTag> {
   private BookPage bookPage;
   private int subpage = 0;

   public Page() {
   }

   @Override
   public int getSubPage() {
      return this.subpage;
   }

   @Override
   public void setSubPage(int subpage) {
      this.subpage = subpage;
   }

   @Override
   public BookPage getBookPage() {
      return this.bookPage;
   }

   @Override
   public void setBookPage(BookPage bookPage) {
      this.bookPage = bookPage;
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      nbt.putInt("subpage", this.subpage);
      if (this.bookPage != null) {
         if (this.bookPage instanceof BookPageSpell) {
            nbt.putBoolean("is_spell_page", true);
            nbt.putString("spell_location", ((BookPageSpell)this.bookPage).getSpell().getResourceLocation().toString());
            nbt.put("spell_data", ((BookPageSpell)this.bookPage).getSpell().serializeNBT());
         } else {
            nbt.putString("page", this.bookPage.getName());
         }
      }

      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      this.subpage = nbt.getInt("subpage");
      if (nbt.contains("is_spell_page")) {
         Spell spell = SpellHelper.getSpellInstance(new ResourceLocation(nbt.getString("spell_location")), nbt.getCompound("spell_data"));
         if (spell != null) {
            this.bookPage = new BookPageSpell(spell);
         }
      } else if (nbt.contains("page")) {
         this.bookPage = BookPageRegistry.getBookPage(nbt.getString("page"));
      }
   }
}
