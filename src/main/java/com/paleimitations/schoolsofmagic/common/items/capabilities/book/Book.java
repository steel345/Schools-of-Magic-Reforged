package com.paleimitations.schoolsofmagic.common.items.capabilities.book;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.paleimitations.schoolsofmagic.common.books.BookElementSticker;
import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.books.BookPageChapter;
import com.paleimitations.schoolsofmagic.common.books.BookPageSpell;
import com.paleimitations.schoolsofmagic.common.books.BookPageTableContent;
import com.paleimitations.schoolsofmagic.common.books.PageElement;
import com.paleimitations.schoolsofmagic.common.books.PageElementWorldConnector;
import com.paleimitations.schoolsofmagic.common.registries.BookPageRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellHelper;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.INBTSerializable;

public class Book implements INBTSerializable<CompoundTag>, IBook {
   protected int page;
   protected int subpage;
   protected int links;
   protected int tickToConnect;
   protected DyeColor color;
   private List<BookPage> pages = Lists.newArrayList();
   private Map<PageElementWorldConnector, Integer> connectedPages = Maps.newHashMap();
   private List<BookPageChapter> chapters = Lists.newArrayList();
   private List<BookElementSticker> stickers = Lists.newArrayList();

   private boolean edited = false;

   @Override public boolean isEdited() { return this.edited; }
   @Override public void setEdited(boolean edited) { this.edited = edited; }

   public Book(List<BookPage> pages) {
      this.pages = pages;
      this.page = 0;
      this.subpage = 0;
      this.links = 0;
      this.tickToConnect = 0;
      this.color = null;
      this.getConnectedPages();

      for (BookPage page : pages) {
         if (page instanceof BookPageChapter) {
            ((BookPageChapter)page).pageHolder = this;
            ((BookPageChapter)page).buildChapter();
            this.chapters.add((BookPageChapter)page);
         }
      }

      if (this.pages != null && !this.pages.isEmpty() && this.pages.get(0) != null && this.pages.get(0) instanceof BookPageTableContent) {
         ((BookPageTableContent)this.pages.get(0)).pageHolder = this;
         ((BookPageTableContent)this.pages.get(0)).buildTableContent();
      }
   }

   @Override
   public List<BookPageChapter> getChapters() {
      return this.chapters;
   }

   @Override
   public DyeColor getColor() {
      return this.color;
   }

   @Override
   public void setColor(DyeColor color) {
      this.color = color;
   }

   @Override
   public ResourceLocation getCover() {
      if (this.color == null) {
         return new ResourceLocation("som", "textures/gui/books/cover_leather.png");
      } else {
         switch (this.color.getId()) {
            case 0:
               return new ResourceLocation("som", "textures/gui/books/cover_white.png");
            case 1:
               return new ResourceLocation("som", "textures/gui/books/cover_orange.png");
            case 2:
               return new ResourceLocation("som", "textures/gui/books/cover_magenta.png");
            case 3:
               return new ResourceLocation("som", "textures/gui/books/cover_light_blue.png");
            case 4:
               return new ResourceLocation("som", "textures/gui/books/cover_yellow.png");
            case 5:
               return new ResourceLocation("som", "textures/gui/books/cover_lime.png");
            case 6:
               return new ResourceLocation("som", "textures/gui/books/cover_pink.png");
            case 7:
               return new ResourceLocation("som", "textures/gui/books/cover_grey.png");
            case 8:
               return new ResourceLocation("som", "textures/gui/books/cover_light_grey.png");
            case 9:
               return new ResourceLocation("som", "textures/gui/books/cover_cyan.png");
            case 10:
               return new ResourceLocation("som", "textures/gui/books/cover_purple.png");
            case 11:
               return new ResourceLocation("som", "textures/gui/books/cover_blue.png");
            case 12:
               return new ResourceLocation("som", "textures/gui/books/cover_brown.png");
            case 13:
               return new ResourceLocation("som", "textures/gui/books/cover_green.png");
            case 14:
               return new ResourceLocation("som", "textures/gui/books/cover_red.png");
            case 15:
               return new ResourceLocation("som", "textures/gui/books/cover_black.png");
            default:
               return new ResourceLocation("som", "textures/gui/books/cover_leather.png");
         }
      }
   }

   @Override
   public int getLinks() {
      return this.links;
   }

   @Override
   public void setLinks(int links) {
      this.links = links;
   }

   @Override
   public ResourceLocation getLinkLocation() {
      switch (this.links) {
         case 0:
            return new ResourceLocation("som", "textures/gui/books/void_links.png");
         case 1:
            return new ResourceLocation("som", "textures/gui/books/steel_links.png");
         case 2:
            return new ResourceLocation("som", "textures/gui/books/iron_links.png");
         case 3:
            return new ResourceLocation("som", "textures/gui/books/silver_links.png");
         case 4:
            return new ResourceLocation("som", "textures/gui/books/gold_links.png");
         case 5:
            return new ResourceLocation("som", "textures/gui/books/brass_links.png");
         case 6:
            return new ResourceLocation("som", "textures/gui/books/bronze_links.png");
         case 7:
            return new ResourceLocation("som", "textures/gui/books/copper_links.png");
         default:
            return new ResourceLocation("som", "textures/gui/books/iron_links.png");
      }
   }

   public static ResourceLocation coverFor(net.minecraft.world.item.ItemStack stack, IBook book) {
      if (stack != null && stack.hasTag() && stack.getTag().contains("BookColor")) {
         int id = stack.getTag().getInt("BookColor");
         return id < 0 ? coverById(-1) : coverById(id);
      }
      net.minecraft.world.item.DyeColor d = defaultColor(stack);
      return coverById(d == null ? -1 : d.getId());
   }

   public static ResourceLocation linkLocationFor(net.minecraft.world.item.ItemStack stack, IBook book) {
      if (stack != null && stack.hasTag() && stack.getTag().contains("BookLinks")) {
         return linkById(stack.getTag().getInt("BookLinks"));
      }
      return linkById(defaultLinks(stack));
   }

   private static net.minecraft.world.item.DyeColor defaultColor(net.minecraft.world.item.ItemStack stack) {
      if (stack == null) return null;
      net.minecraft.world.item.Item it = stack.getItem();
      if (it == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.complete_spellbook.get()) {
         return DyeColor.BROWN;
      }
      if (it == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.exploration_book.get()) {
         return DyeColor.CYAN;
      }
      if (it == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.alchemical_manual.get()) {
         return DyeColor.BLACK;
      }
      if (it == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.tea_makers_guide.get()) {
         return DyeColor.PURPLE;
      }
      if (it == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.spellworkers_handbook.get()) {
         return DyeColor.RED;
      }
      if (it instanceof com.paleimitations.schoolsofmagic.common.items.ItemMagicBook) {
         return magicSchoolColor(stack.getDamageValue());
      }
      return null;
   }

   private static int defaultLinks(net.minecraft.world.item.ItemStack stack) {
      if (stack == null) return 2;
      net.minecraft.world.item.Item it = stack.getItem();
      if (it instanceof com.paleimitations.schoolsofmagic.common.items.ItemMagicBook) return 5;
      if (it == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.exploration_book.get()) return 5;
      if (it == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.alchemical_manual.get()) return 6;
      if (it == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.tea_makers_guide.get()) return 5;
      if (it == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.spellworkers_handbook.get()) return 5;
      if (it == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.complete_spellbook.get()) return 6;
      if (it == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.spellbook.get()) return 4;
      return 2;
   }

   private static DyeColor magicSchoolColor(int damage) {
      switch (com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType.getFromIndex(damage)) {
         case PYROMANCY: return DyeColor.RED;
         case HYDROMANCY: return DyeColor.LIGHT_BLUE;
         case AEROMANCY: return DyeColor.YELLOW;
         case GEOMANCY: return DyeColor.LIME;
         case CRYOMANCY: return DyeColor.BLUE;
         case ELECTROMANCY: return DyeColor.CYAN;
         case ANIMANCY: return DyeColor.GREEN;
         case AURAMANCY: return DyeColor.PINK;
         case SPECTROMANCY: return DyeColor.GRAY;
         case CHAOTICS: return DyeColor.MAGENTA;
         case UMBRAMANCY: return DyeColor.BLACK;
         case INFERNALITY: return DyeColor.LIGHT_GRAY;
         case ASTROMANCY: return DyeColor.WHITE;
         case HELIOMANCY: return DyeColor.ORANGE;
         case NECROMANCY: return DyeColor.BROWN;
         case HIEROMANCY: return DyeColor.PURPLE;
         default: return null;
      }
   }

   public static ResourceLocation coverById(int id) {
      switch (id) {
         case 0: return new ResourceLocation("som", "textures/gui/books/cover_white.png");
         case 1: return new ResourceLocation("som", "textures/gui/books/cover_orange.png");
         case 2: return new ResourceLocation("som", "textures/gui/books/cover_magenta.png");
         case 3: return new ResourceLocation("som", "textures/gui/books/cover_light_blue.png");
         case 4: return new ResourceLocation("som", "textures/gui/books/cover_yellow.png");
         case 5: return new ResourceLocation("som", "textures/gui/books/cover_lime.png");
         case 6: return new ResourceLocation("som", "textures/gui/books/cover_pink.png");
         case 7: return new ResourceLocation("som", "textures/gui/books/cover_grey.png");
         case 8: return new ResourceLocation("som", "textures/gui/books/cover_light_grey.png");
         case 9: return new ResourceLocation("som", "textures/gui/books/cover_cyan.png");
         case 10: return new ResourceLocation("som", "textures/gui/books/cover_purple.png");
         case 11: return new ResourceLocation("som", "textures/gui/books/cover_blue.png");
         case 12: return new ResourceLocation("som", "textures/gui/books/cover_brown.png");
         case 13: return new ResourceLocation("som", "textures/gui/books/cover_green.png");
         case 14: return new ResourceLocation("som", "textures/gui/books/cover_red.png");
         case 15: return new ResourceLocation("som", "textures/gui/books/cover_black.png");
         default: return new ResourceLocation("som", "textures/gui/books/cover_leather.png");
      }
   }

   public static ResourceLocation linkById(int links) {
      switch (links) {
         case 0: return new ResourceLocation("som", "textures/gui/books/void_links.png");
         case 1: return new ResourceLocation("som", "textures/gui/books/steel_links.png");
         case 2: return new ResourceLocation("som", "textures/gui/books/iron_links.png");
         case 3: return new ResourceLocation("som", "textures/gui/books/silver_links.png");
         case 4: return new ResourceLocation("som", "textures/gui/books/gold_links.png");
         case 5: return new ResourceLocation("som", "textures/gui/books/brass_links.png");
         case 6: return new ResourceLocation("som", "textures/gui/books/bronze_links.png");
         case 7: return new ResourceLocation("som", "textures/gui/books/copper_links.png");
         default: return new ResourceLocation("som", "textures/gui/books/iron_links.png");
      }
   }

   public Book() {
      this.page = 0;
   }

   @Override
   public int getPage() {
      return this.page;
   }

   @Override
   public int getSubPage() {
      return this.subpage;
   }

   @Override
   public void setSubPage(int subpage) {
      if (this.getCurrentPage().getSubPages() > subpage) {
         this.subpage = subpage;
      }

      if (this.subpage < 0) {
         this.subpage = 0;
      }
   }

   @Override
   public BookPage getCurrentPage() {
      if (this.pages.isEmpty()) {
         return null;
      } else {
         if (this.page >= this.pages.size()) {
            this.page = this.pages.size() - 1;
         }

         if (this.page < 0) {
            this.page = 0;
         }

         if (this.subpage >= this.pages.get(this.page).getSubPages()) {
            this.subpage = this.pages.get(this.page).getSubPages() - 1;
         }

         if (this.subpage < 0) {
            this.subpage = 0;
         }

         return this.pages.get(this.page);
      }
   }

   @Override
   public void setPage(int page) {
      this.page = page;
   }

   @Override
   public BookPage getBookPage(int page) {
      if (page >= this.pages.size()) {
         return null;
      } else {
         return page < 0 ? null : this.pages.get(page);
      }
   }

   @Override
   public List<BookPage> getBookPages() {
      return this.pages;
   }

   @Override
   public void setBookPages(List<BookPage> pages) {
      this.pages = pages;
      this.getConnectedPages();
      this.chapters.clear();

      for (BookPage page : pages) {
         if (page instanceof BookPageChapter) {
            ((BookPageChapter)page).pageHolder = this;
            ((BookPageChapter)page).buildChapter();
            this.chapters.add((BookPageChapter)page);
         }
      }

      if (this.pages != null && !this.pages.isEmpty() && this.pages.get(0) != null && this.pages.get(0) instanceof BookPageTableContent) {
         ((BookPageTableContent)this.pages.get(0)).pageHolder = this;
         ((BookPageTableContent)this.pages.get(0)).buildTableContent();
      }
   }

   @Override
   public void addBookPages(List<BookPage> pages) {
      this.pages.addAll(pages);
      this.getConnectedPages();
      this.chapters.clear();

      for (BookPage page : pages) {
         if (page instanceof BookPageChapter) {
            ((BookPageChapter)page).pageHolder = this;
            ((BookPageChapter)page).buildChapter();
            this.chapters.add((BookPageChapter)page);
         }
      }

      if (this.pages != null && !this.pages.isEmpty() && this.pages.get(0) != null && this.pages.get(0) instanceof BookPageTableContent) {
         ((BookPageTableContent)this.pages.get(0)).pageHolder = this;
         ((BookPageTableContent)this.pages.get(0)).buildTableContent();
      }
   }

   @Override
   public boolean hasConnection(Level world, BlockPos pos, BlockState state) {
      for (PageElementWorldConnector connector : this.connectedPages.keySet()) {
         if (connector.connects(world, state, pos)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void connect(Level world, BlockPos pos, BlockState state) {
      for (PageElementWorldConnector connector : this.connectedPages.keySet()) {
         if (connector.connects(world, state, pos)) {
            this.page = this.connectedPages.get(connector);
            this.subpage = 0;
         }
      }
   }

   public void getConnectedPages() {
      Map<PageElementWorldConnector, Integer> connected = Maps.newHashMap();

      for (int i = 0; i < this.pages.size(); i++) {
         BookPage page = this.pages.get(i);

         for (PageElement element : page.elements) {
            if (element instanceof PageElementWorldConnector) {
               connected.put((PageElementWorldConnector)element, i);
            }
         }
      }

      this.connectedPages = connected;
   }

   @Override
   public List<BookElementSticker> getStickers() {
      return this.stickers;
   }

   @Override
   public void setStickers(List<BookElementSticker> stickers) {
      this.stickers = stickers;
   }

   @Override
   public int getConnectTick() {
      return this.tickToConnect;
   }

   @Override
   public void setConnectTick(int i) {
      this.tickToConnect = i;
   }

   public static String getLink(int link) {
      switch (link) {
         case 0:
            return "nuggetObsidian";
         case 1:
            return "nuggetSteel";
         case 2:
            return "nuggetIron";
         case 3:
            return "nuggetSilver";
         case 4:
            return "nuggetGold";
         case 5:
            return "nuggetBrass";
         case 6:
            return "nuggetBronze";
         case 7:
            return "nuggetCopper";
         default:
            return "nuggetIron";
      }
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      nbt.putInt("page", this.page);
      nbt.putInt("subpage", this.subpage);
      nbt.putInt("links", this.links);
      nbt.putInt("tickToConnect", this.tickToConnect);
      if (this.color != null) {
         nbt.putInt("color", this.color.getId());
      }

      nbt.putBoolean("edited", this.edited);
      nbt.putInt("pages_size", this.pages.size());

      for (int i = 0; i < this.pages.size(); i++) {
         if (this.pages.get(i) != null) {
            if (this.pages.get(i) instanceof BookPageSpell) {
               nbt.putBoolean("is_spell_page_" + i, true);
               nbt.putString("spell_location_" + i, ((BookPageSpell)this.pages.get(i)).getSpell().getResourceLocation().toString());
               nbt.put("spell_data_" + i, ((BookPageSpell)this.pages.get(i)).getSpell().serializeNBT());
            } else {
               nbt.putString("page_" + i, this.pages.get(i).getName());
            }
         }
      }

      nbt.putInt("stickers_size", this.stickers.size());

      for (int ix = 0; ix < this.stickers.size(); ix++) {
         BookElementSticker sticker = this.stickers.get(ix);
         if (sticker.sticker != null) {
            nbt.put("sticker_" + ix, sticker.serializeNBT());
         }
      }

      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      this.page = nbt.getInt("page");
      this.subpage = nbt.getInt("subpage");
      this.links = nbt.getInt("links");
      this.tickToConnect = nbt.getInt("tickToConnect");
      this.edited = nbt.getBoolean("edited");
      if (nbt.contains("color")) {
         this.color = DyeColor.byId(nbt.getInt("color"));
      }

      List<BookPage> pagesIn = Lists.newArrayList();

      for (int i = 0; i < nbt.getInt("pages_size"); i++) {
         if (nbt.contains("is_spell_page_" + i)) {
            Spell spell = SpellHelper.getSpellInstance(new ResourceLocation(nbt.getString("spell_location_" + i)), nbt.getCompound("spell_data_" + i));
            if (spell != null) {
               pagesIn.add(new BookPageSpell(spell));
            }
         } else {
            BookPage p = BookPageRegistry.getBookPage(nbt.getString("page_" + i));
            if (p != null) {
               pagesIn.add(p);
            }
         }
      }

      this.setBookPages(pagesIn);
      List<BookElementSticker> stickers1 = Lists.newArrayList();

      for (int ix = 0; ix < nbt.getInt("stickers_size"); ix++) {
         if (nbt.getCompound("sticker_" + ix) != null && !nbt.getCompound("sticker_" + ix).isEmpty()) {
            stickers1.add(new BookElementSticker(nbt.getCompound("sticker_" + ix)));
         }
      }

      this.stickers = stickers1;
   }
}
