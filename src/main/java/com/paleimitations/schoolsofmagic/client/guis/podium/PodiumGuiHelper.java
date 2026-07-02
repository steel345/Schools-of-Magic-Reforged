package com.paleimitations.schoolsofmagic.client.guis.podium;

import java.awt.Color;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import com.paleimitations.schoolsofmagic.common.books.BookElementSticker;
import com.paleimitations.schoolsofmagic.common.books.PageElement;
import com.paleimitations.schoolsofmagic.common.books.PageElementPageButton;
import com.paleimitations.schoolsofmagic.common.books.PageElementStandardText;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.page.CapabilityPage;
import com.paleimitations.schoolsofmagic.common.items.capabilities.page.IPage;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_modifier.CapabilitySpellModifier;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_modifier.ISpellModifier;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.CapabilitySpellNotes;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.ISpellNotes;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.SpellNotes;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WrittenBookItem;

public class PodiumGuiHelper {
   public static final ResourceLocation PAGE = new ResourceLocation("som", "textures/gui/books/paper.png");
   public static final ResourceLocation RIBBON = new ResourceLocation("som", "textures/gui/podium/ribbon.png");
   public static final ResourceLocation SCROLL = new ResourceLocation("som", "textures/gui/podium/paper.png");
   public static final ResourceLocation PAGE_DEFAULT = new ResourceLocation("som", "textures/gui/books/paper_default.png");
   public static final ResourceLocation BOOK = new ResourceLocation("som", "textures/gui/container/book.png");

   public PodiumGuiHelper() {}

   public static void renderGuiSubject(GuiGraphics gg, float mouseX, float mouseY,
                                       AbstractContainerScreen<?> gui, IBook book, float zLevel, int page, boolean smaller) {
      PoseStack pose = gg.pose();
      pose.pushPose();
      float scale = smaller ? 0.42276424F : 0.50406504F;
      pose.scale(scale, scale, scale);
      if (!smaller) pose.translate(-20.0F, -23.0F, 0.0F);
      gg.blit(book.getCover(), 0, 0, 0, 0, 256, 256);
      gg.blit(book.getLinkLocation(), 0, 0, 0, 0, 256, 256);
      gg.blit(PAGE_DEFAULT, 0, 0, 0, 0, 256, 256);
      if (book.getBookPage(page) != null) {
         book.getBookPage(page).drawPage(gg,
            mouseX / scale + (smaller ? 0 : 20),
            mouseY / scale + (smaller ? 0 : 23), 0, 0, true, 0);
      }
      pose.popPose();
   }

   public static void clickGuiSubject(float mouseX, float mouseY, ItemStack stack, TileEntityPodium podium, boolean smaller) {
      IBook book = stack.getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
      if (book == null) return;
      float scale = smaller ? 0.42276424F : 0.50406504F;
      if (book.getCurrentPage() != null && book.getCurrentPage().elements != null && !book.getCurrentPage().elements.isEmpty()) {
         for (PageElement element : book.getCurrentPage().elements) {
            if (element instanceof PageElementPageButton b) {
               b.click(mouseX / scale + (smaller ? 0 : 20),
                       mouseY / scale + (smaller ? 0 : 23),
                       book.getSubPage(), book, podium.getBlockPos());
            }
         }
      }
   }

   public static void renderGuiSubject(GuiGraphics gg, float mouseX, float mouseY,
                                       AbstractContainerScreen<?> gui, ItemStack stack, float zLevel,
                                       TileEntityPodium podium, boolean smaller) {
      IBook book = stack.getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
      IPage pageCap = stack.getCapability(CapabilityPage.PAGE_CAPABILITY).orElse(null);
      ISpellModifier mod = stack.getCapability(CapabilitySpellModifier.SPELL_MODIFIER_CAPABILITY).orElse(null);
      ISpellNotes notesCap = stack.getCapability(CapabilitySpellNotes.SPELL_NOTES_CAPABILITY).orElse(null);

      PoseStack pose = gg.pose();
      if (book != null) {
         pose.pushPose();
         float scale = smaller ? 0.42276424F : 0.50406504F;
         pose.scale(scale, scale, scale);
         if (!smaller) pose.translate(-20.0F, -23.0F, 0.0F);
         gg.blit(book.getCover(), 0, 0, 0, 0, 256, 256);
         gg.blit(book.getLinkLocation(), 0, 0, 0, 0, 256, 256);
         gg.blit(PAGE_DEFAULT, 0, 0, 0, 0, 256, 256);
         float mouseX1 = mouseX / scale + (smaller ? 0 : 20);
         float mouseY1 = mouseY / scale + (smaller ? 0 : 23);
         if (!book.getBookPages().isEmpty() && book.getCurrentPage() != null) {
            book.getCurrentPage().drawPage(gg, mouseX1, mouseY1, 0, 0, true, book.getSubPage());
         }
         for (BookElementSticker sticker : book.getStickers()) {
            if (sticker != null) sticker.drawElement(gg, mouseX1, mouseY1, 0, 0, true, book.getSubPage(), book.getPage());
         }
         pose.popPose();
      } else if (pageCap != null) {
         pose.pushPose();
         float scale = smaller ? 0.42276424F : 0.50406504F;
         pose.scale(scale, scale, scale);
         if (!smaller) pose.translate(-20.0F, -23.0F, 0.0F);
         gg.blit(PAGE, 0, 0, 0, 0, 256, 256);
         if (pageCap.getBookPage() != null) {
            pageCap.getBookPage().drawPage(gg,
               mouseX / scale + (smaller ? 0 : 20),
               mouseY / scale + (smaller ? 0 : 23), 0, 0, true, pageCap.getSubPage());
         }
         pose.popPose();
      } else if (stack.hasTag() && stack.getTag().contains("pages")) {
         pose.pushPose();
         if (smaller) {
            pose.scale(0.42276424F, 0.42276424F, 0.42276424F);
         } else {
            pose.scale(0.50406504F, 0.50406504F, 0.50406504F);
            pose.translate(-20.0F, -23.0F, 0.0F);
         }
         gg.blit(BOOK, 0, 0, 0, 0, 256, 256);
         drawText(gg, podium.handler.getStackInSlot(0), podium.getPage(), Minecraft.getInstance().font, 0, 0);
         pose.popPose();
      } else if (mod != null) {
         pose.pushPose();
         pose.scale(0.8F, 0.8F, 0.8F);
         Color color = Color.WHITE;
         if (mod.getSpellModifier() != null) {
            if (mod.getSpellModifier().id == 16) {
               color = new Color(MagicElementRegistry.getElementFromId(mod.getSpellModifier().level - 1).getColor());
            } else {
               color = switch (mod.getSpellModifier().level) {
                  case 1 -> new Color(200, 188, 40);
                  case 2 -> new Color(60, 180, 28);
                  case 3 -> new Color(15, 120, 160);
                  case 4 -> new Color(60, 60, 170);
                  case 5 -> new Color(115, 39, 177);
                  default -> Color.WHITE;
               };
            }
         }
         gg.blit(SCROLL, 0, 0, 0, 0, 256, 256);
         new PageElementStandardText("modifier." + mod.getSpellModifier().getSerializedName() + ".name", 68, 23, 80, 11, 0, true)
            .drawElement(gg, 0.0F, 0.0F, 0, 0, true, 0);
         RenderSystem.setShaderColor(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
         gg.blit(RIBBON, 0, 0, 0, 0, 256, 256);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         pose.pushPose();
         pose.scale(0.8F, 0.8F, 0.8F);
         gg.drawWordWrap(Minecraft.getInstance().font,
            Component.translatable("modifier." + mod.getSpellModifier().getSerializedName() + ".desc"),
            35, 64, 95, 0);
         pose.popPose();
         pose.popPose();
      } else if (notesCap != null) {
         pose.pushPose();
         pose.scale(0.8F, 0.8F, 0.8F);
         SpellNotes notes = notesCap.getSpellNotes();
         gg.blit(SCROLL, 0, 0, 0, 0, 256, 256);
         pose.pushPose();
         pose.scale(0.8F, 0.8F, 0.8F);
         Font font = Minecraft.getInstance().font;

         List<Component> lines = Lists.newArrayList();
         if (notes.magicianUnits > 0.0F) lines.add(point("modifier.magician_points.name", notes.magicianUnits));
         if (notes.spellUnits > 0.0F)    lines.add(point("modifier.spell_points.name", notes.spellUnits));
         if (notes.ritualUnits > 0.0F)   lines.add(point("modifier.ritual_points.name", notes.ritualUnits));
         if (notes.potionUnits > 0.0F)   lines.add(point("modifier.potion_points.name", notes.potionUnits));
         for (int j = 0; j < 6; j++) {
            if (notes.schoolUnits[j] > 0.0F) {
               lines.add(Component.literal(Component.translatable("school." + MagicSchoolRegistry.getSchoolFromId(j).getName() + ".name").getString()
                  + " " + Component.translatable("modifier.points.name").getString() + ": " + trim(notes.schoolUnits[j])));
            }
         }
         for (int j = 0; j < 16; j++) {
            if (notes.elementUnits[j] > 0.0F) {
               lines.add(Component.literal(Component.translatable("element." + MagicElementRegistry.getElementFromId(j).getName() + ".name").getString()
                  + " " + Component.translatable("modifier.points.name").getString() + ": " + trim(notes.elementUnits[j])));
            }
         }
         if (notes.spark >= 0) {
            lines.add(Component.literal(Component.translatable("element." + MagicElementRegistry.getElementFromId(notes.spark).getName() + ".name").getString()
               + " " + Component.translatable("modifier.spark.name").getString()));
         }
         int lx = 32;
         int ly = 18;
         int lh = font.lineHeight + 2;
         for (Component c : lines) {
            gg.drawString(font, c, lx, ly, 0, false);
            ly += lh;
         }
         pose.popPose();
         pose.popPose();
      }
   }

   private static Component point(String key, float value) {
      return Component.literal(Component.translatable(key).getString() + ": " + trim(value));
   }

   private static String trim(float value) {
      return value == Math.floor(value) ? Integer.toString((int) value) : Float.toString(value);
   }

   private static void drawText(GuiGraphics gg, ItemStack book, int page, Font font, int xD, int yD) {
      ListTag bookPages = new ListTag();
      int bookTotalPages = 1;
      if (book.hasTag()) {
         CompoundTag tag = book.getTag();
         bookPages = tag.getList("pages", 8).copy();
         bookTotalPages = bookPages.size();
         if (bookTotalPages < 1) {
            bookPages.add(StringTag.valueOf(""));
            bookTotalPages = 1;
         }
      }
      Component pageIndicator = Component.translatable("book.pageIndicator", page + 1, bookTotalPages);
      String s5 = (page >= 0 && page < bookPages.size()) ? bookPages.getString(page) : "";

      List<Component> cached = null;
      if (WrittenBookItem.makeSureTagIsValid(book.getTag())) {
         try {
            Component textComp = Component.Serializer.fromJson(s5);
            if (textComp != null) {
               cached = Lists.newArrayList(textComp);
            }
         } catch (JsonParseException ex) {
            cached = null;
         }
      } else {
         cached = Lists.newArrayList(Component.literal(ChatFormatting.DARK_RED + "* Invalid book tag *"));
      }
      int indicatorW = font.width(pageIndicator);
      gg.drawString(font, pageIndicator, xD - indicatorW + 192 - 44 + 35, 18 + yD + 36, 0, false);
      if (cached == null) {
         gg.drawWordWrap(font, Component.literal(s5), xD + 36 + 35, yD + 34 + 36, 116, 0);
      } else {
         int max = Math.min(128 / font.lineHeight, cached.size());
         for (int l = 0; l < max; l++) {
            gg.drawString(font, cached.get(l), xD + 36 + 35, yD + 34 + 36 + l * font.lineHeight, 0, false);
         }
      }
   }
}
