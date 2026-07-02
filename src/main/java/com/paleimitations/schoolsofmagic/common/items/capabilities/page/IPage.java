package com.paleimitations.schoolsofmagic.common.items.capabilities.page;

import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.items.capabilities.IBookPageHolder;
import net.minecraft.nbt.CompoundTag;

public interface IPage extends IBookPageHolder {
   int getSubPage();

   void setSubPage(int var1);

   BookPage getBookPage();

   void setBookPage(BookPage var1);

   CompoundTag serializeNBT();

   void deserializeNBT(CompoundTag var1);
}
