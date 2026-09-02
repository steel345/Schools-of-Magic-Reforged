package com.paleimitations.schoolsofmagic.common.books;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BookPageLocked extends BookPage {
   private final String unlockKey;

   public BookPageLocked(String name, String unlockKey, List<PageElement> elements) {
      super(name, elements);
      this.unlockKey = unlockKey;
   }

   public String getUnlockKey() {
      return this.unlockKey;
   }

   @OnlyIn(Dist.CLIENT)
   public boolean isNew() {
      return this.unlockKey != null
         && com.paleimitations.schoolsofmagic.client.ClientPageUnlocks.has(this.unlockKey)
         && com.paleimitations.schoolsofmagic.client.ClientPageUnlocks.isUnread(this.unlockKey);
   }

   @OnlyIn(Dist.CLIENT)
   private boolean unlocked() {
      if (this.unlockKey == null) return true;
      return com.paleimitations.schoolsofmagic.client.ClientPageUnlocks.has(this.unlockKey);
   }

   @OnlyIn(Dist.CLIENT)
   public boolean isContentHidden() {
      return !unlocked();
   }

   @Override
   @OnlyIn(Dist.CLIENT)
   public void drawPage(GuiGraphics gg, float mouseX, float mouseY, int x, int y, boolean isGUI, int subpage) {
      if (unlocked()) {
         if (this.unlockKey != null
            && com.paleimitations.schoolsofmagic.client.ClientPageUnlocks.clearUnread(this.unlockKey)) {
            com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.sendToServer(
               new com.paleimitations.schoolsofmagic.common.network.PacketMarkPageRead(this.unlockKey));
         }
         super.drawPage(gg, mouseX, mouseY, x, y, isGUI, subpage);
         return;
      }
      clearChangedMarker();

      Font font = Minecraft.getInstance().font;
      Component title = Component.translatable("book.page_locked.title");
      Component desc = Component.translatable("book.page_locked.desc");
      int cx = x + 72;
      gg.drawString(font, title, cx - font.width(title) / 2, y + 70, 0x3A2E1E, false);
      for (net.minecraft.util.FormattedCharSequence line : font.split(desc, 96)) {
         gg.drawString(font, line, cx - font.width(line) / 2, y + 88, 0x5A4A38, false);
         y += font.lineHeight + 1;
      }
   }
}
