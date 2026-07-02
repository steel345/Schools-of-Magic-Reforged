package com.paleimitations.schoolsofmagic.common.books;

import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketTurnPage;
import net.minecraft.core.BlockPos;

public class PageElementPageButton extends PageElement {
   public final int pageNumber;
   public final int width;
   public final int height;

   public PageElementPageButton(int pageNumber, int x, int y, int target, int width, int height) {
      super(x, y, target);
      this.pageNumber = pageNumber;
      this.width = width;
      this.height = height;
   }

   public void click(float x, float y, int subpage, IBook book, BlockPos pos) {
      if (x > this.x && x < this.x + this.width && y > this.y && y < this.y + this.height && this.isTarget(subpage)) {
         book.setPage(this.pageNumber);
         book.setSubPage(0);
         if (pos != null) {
            PacketHandler.INSTANCE.sendToServer(new PacketTurnPage(this.pageNumber, 0, pos));
         }
      }
   }
}
