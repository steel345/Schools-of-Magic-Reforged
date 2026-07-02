package com.paleimitations.schoolsofmagic.common.items.capabilities.book;

import com.paleimitations.schoolsofmagic.common.books.BookElementSticker;
import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.books.BookPageChapter;
import com.paleimitations.schoolsofmagic.common.items.capabilities.IBookPageHolder;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IBook extends IBookPageHolder {
   int getPage();

   int getSubPage();

   BookPage getCurrentPage();

   void setPage(int var1);

   void setSubPage(int var1);

   BookPage getBookPage(int var1);

   List<BookPage> getBookPages();

   void setBookPages(List<BookPage> var1);

   void addBookPages(List<BookPage> var1);

   default boolean isEdited() { return true; }

   default void setEdited(boolean edited) {}

   boolean hasConnection(Level var1, BlockPos var2, BlockState var3);

   void connect(Level var1, BlockPos var2, BlockState var3);

   int getConnectTick();

   void setConnectTick(int var1);

   List<BookPageChapter> getChapters();

   List<BookElementSticker> getStickers();

   void setStickers(List<BookElementSticker> var1);

   DyeColor getColor();

   void setColor(DyeColor var1);

   ResourceLocation getCover();

   int getLinks();

   void setLinks(int var1);

   ResourceLocation getLinkLocation();

   CompoundTag serializeNBT();

   void deserializeNBT(CompoundTag var1);
}
