package com.paleimitations.schoolsofmagic.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.paleimitations.schoolsofmagic.common.books.PageElement;
import com.paleimitations.schoolsofmagic.common.books.PageElementEntity;
import com.paleimitations.schoolsofmagic.common.books.PageElementQuill;
import com.paleimitations.schoolsofmagic.common.books.editor.BookPageLayout;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

@OnlyIn(Dist.CLIENT)
public class BookLayoutRenderer {
   private static final PageElement DRAW = new PageElement(0, 0);

   private static final ResourceLocation VANILLA_SLOTS =
      new ResourceLocation("minecraft", "textures/gui/container/generic_54.png");
   private static final int SLOT_U = 7;
   private static final int SLOT_V = 17;

   private static final Map<String, ItemStack> STACKS = new HashMap<>();
   private static final Map<String, PageElementEntity> MOBS = new HashMap<>();

   private static final float BODY_SCALE = 0.75F;

   private static BookPageLayout pending;

   public static void begin(IBook book, int page) {
      pending = book == null ? null : book.getPageLayouts().get(page);
      PageElementQuill.setHidden(pending != null && pending.finished);
   }

   public static void begin(IBook book) {
      begin(book, book == null ? -1 : book.getPage());
   }

   public static void end(GuiGraphics gg, int x, int y, boolean isGUI) {
      BookPageLayout layout = pending;
      pending = null;
      PageElementQuill.setHidden(false);
      draw(layout, gg, x, y, isGUI);
   }

   public static void draw(BookPageLayout layout, GuiGraphics gg, int x, int y, boolean isGUI) {
      if (layout == null) return;
      for (BookPageLayout.Element e : layout.elements) {
         drawElement(gg, e, x, y, isGUI);
      }
   }

   public static void drawElement(GuiGraphics gg, BookPageLayout.Element e, int xIn, int yIn, boolean isGUI) {
      switch (e.kind) {
         case TITLE -> drawTitle(gg, e, xIn, yIn,
            e == editTitleEl && editTitleText != null ? editTitleText : e.value);
         case TEXT -> drawText(gg, e, xIn, yIn,
            e == editTextEl && editBodyText != null ? editBodyText : e.value);
         case ITEM -> {
            boolean scaled = pushScale(gg, e, xIn, yIn);
            DRAW.drawItemStack(gg, stackOf(e.value), xIn + e.x, yIn + e.y, isGUI);
            if (scaled) gg.pose().popPose();
         }
         case SLOT -> {
            boolean scaled = pushScale(gg, e, xIn, yIn);
            drawSlot(gg, xIn + e.x, yIn + e.y);
            DRAW.drawItemStack(gg, stackOf(e.value), xIn + e.x + 1, yIn + e.y + 1, isGUI);
            if (scaled) gg.pose().popPose();
         }
         case ENTITY -> drawEntity(gg, e, xIn, yIn, isGUI);
         case TEMPLATE -> {
            boolean scaled = pushScale(gg, e, xIn, yIn);
            drawTemplate(gg, e, xIn, yIn, isGUI);
            if (scaled) gg.pose().popPose();
         }
      }
   }

   private static BookPageLayout.Element editTitleEl;
   private static BookPageLayout.Element editTextEl;
   private static String editTitleText;
   private static String editBodyText;

   public static void setEditPreview(BookPageLayout.Element titleEl, String title,
         BookPageLayout.Element textEl, String body) {
      editTitleEl = titleEl;
      editTitleText = title;
      editTextEl = textEl;
      editBodyText = body;
   }

   public static void clearEditPreview() {
      editTitleEl = null;
      editTextEl = null;
      editTitleText = null;
      editBodyText = null;
   }

   private static void drawTitle(GuiGraphics gg, BookPageLayout.Element e, int xIn, int yIn, String value) {
      Font font = Minecraft.getInstance().font;
      String t = BookRichText.stripCodes(value);
      if (t.isEmpty()) return;
      int tw = Math.max(1, font.width(t));
      int th = font.lineHeight;
      float sc = Math.min((float) Math.max(1, e.w) / tw, (float) Math.max(1, e.h) / th) * e.scale;
      int drawX = Math.round((e.x + xIn) - tw * sc / 2.0F);
      int drawY = Math.round((e.y + yIn) - th * sc / 2.0F);
      gg.pose().pushPose();
      gg.pose().scale(sc, sc, sc);
      gg.drawString(font, t, Math.round(drawX / sc), Math.round(drawY / sc), e.color, false);
      gg.pose().popPose();
   }

   private static void drawText(GuiGraphics gg, BookPageLayout.Element e, int xIn, int yIn, String value) {
      if (value == null || value.isEmpty()) return;
      float sc = BODY_SCALE * e.scale
         * com.paleimitations.schoolsofmagic.common.config.SOMClientConfig.bookTextScale();
      BookRichText.render(gg,
         Collections.singletonList(value),
         Collections.singletonList(new BookRichText.ParagraphBoxRef(e.x, e.y, 0, e.w, e.h)),
         sc, xIn, yIn, 0, e.color);
   }

   private static boolean pushScale(GuiGraphics gg, BookPageLayout.Element e, int xIn, int yIn) {
      float s = e.scale;
      if (s <= 0.0F || s == 1.0F) return false;
      float ox = xIn + e.x;
      float oy = yIn + e.y;
      gg.pose().pushPose();
      gg.pose().translate(ox, oy, 0.0F);
      gg.pose().scale(s, s, 1.0F);
      gg.pose().translate(-ox, -oy, 0.0F);
      return true;
   }

   private static void drawSlot(GuiGraphics gg, int x, int y) {
      gg.blit(VANILLA_SLOTS, x, y, SLOT_U, SLOT_V, 18, 18);
   }

   private static void drawEntity(GuiGraphics gg, BookPageLayout.Element e, int xIn, int yIn, boolean isGUI) {
      PageElementEntity mob = mobOf(e);

      if (mob != null) mob.drawElement(gg, 0.0F, 0.0F, xIn + e.x, yIn + e.y, isGUI, 0);
   }

   private static void drawTemplate(GuiGraphics gg, BookPageLayout.Element e, int xIn, int yIn, boolean isGUI) {
      BookTemplates.Template t = BookTemplates.byName(e.value);
      if (t == null) return;
      gg.blit(t.texture, xIn + e.x, yIn + e.y, 0, 0, t.width, t.height);
      for (int i = 0; i < t.slots.length && i < e.slots.size(); i++) {
         ItemStack stack = stackOf(e.slots.get(i));
         if (stack.isEmpty()) continue;
         DRAW.drawItemStack(gg, stack, xIn + e.x + t.slots[i][0], yIn + e.y + t.slots[i][1], isGUI);
      }
   }

   private static PageElementEntity mobOf(BookPageLayout.Element e) {
      if (e.value == null || e.value.isEmpty()) return null;
      PageElementEntity cached = MOBS.get(e.value);
      if (cached == null) {
         ResourceLocation rl = ResourceLocation.tryParse(e.value);
         EntityType<?> type = rl == null ? null : ForgeRegistries.ENTITY_TYPES.getValue(rl);
         if (type == null) return null;
         cached = new PageElementEntity(type, 0, 0, Math.max(4, e.w), 0);
         MOBS.put(e.value, cached);
      }
      cached.setScale(Math.max(4, e.w));
      return cached;
   }

   public static String basePart(String value) {
      if (value == null) return "";
      int star = value.indexOf('*');
      return star < 0 ? value : value.substring(0, star);
   }

   public static String idPart(String value) {
      String base = basePart(value);
      int at = base.indexOf('@');
      return at < 0 ? base : base.substring(0, at);
   }

   public static int damagePart(String value) {
      String base = basePart(value);
      int at = base.indexOf('@');
      if (at < 0) return 0;
      try {
         return Math.max(0, Integer.parseInt(base.substring(at + 1)));
      } catch (NumberFormatException e) {
         return 0;
      }
   }

   public static int countPart(String value) {
      if (value == null) return 1;
      int star = value.indexOf('*');
      if (star < 0) return 1;
      try {
         return Math.max(1, Math.min(64, Integer.parseInt(value.substring(star + 1))));
      } catch (NumberFormatException e) {
         return 1;
      }
   }

   public static String withCount(String value, int count) {
      return count <= 1 ? basePart(value) : basePart(value) + "*" + Math.min(64, count);
   }

   public static ItemStack stackOf(String value) {
      if (value == null || value.isEmpty()) return ItemStack.EMPTY;
      ItemStack cached = STACKS.get(value);
      if (cached != null) return cached;
      ResourceLocation rl = ResourceLocation.tryParse(idPart(value));
      Item item = rl == null ? null : ForgeRegistries.ITEMS.getValue(rl);
      ItemStack stack = ItemStack.EMPTY;
      if (item != null) {
         stack = new ItemStack(item, countPart(value));
         int damage = damagePart(value);
         if (damage > 0) stack.setDamageValue(damage);
      }
      STACKS.put(value, stack);
      return stack;
   }
}
