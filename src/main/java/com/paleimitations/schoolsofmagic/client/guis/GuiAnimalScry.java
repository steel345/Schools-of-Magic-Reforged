package com.paleimitations.schoolsofmagic.client.guis;

import com.paleimitations.schoolsofmagic.common.network.PacketAnimalScry;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class GuiAnimalScry extends Screen {
   private static final int ROWS = 12;
   private static final int ROW_HEIGHT = 12;
   private static final int WIDTH = 220;

   private final List<ResourceLocation> all = new ArrayList<>();
   private final List<ResourceLocation> shown = new ArrayList<>();

   private EditBox search;
   private int scroll;
   private int picked = -1;
   private long lastClick;
   private int lastClicked = -1;

   public GuiAnimalScry() {
      super(Component.translatable("gui.som.animal_scry"));
   }

   @Override
   protected void init() {
      super.init();
      this.all.clear();
      this.all.addAll(beasts());

      int left = (this.width - WIDTH) / 2;
      int top = (this.height - (ROWS * ROW_HEIGHT + 34)) / 2;

      this.search = new EditBox(this.font, left + 6, top + 6, WIDTH - 12, 14, Component.empty());
      this.search.setMaxLength(64);
      this.search.setResponder(text -> {
         this.scroll = 0;
         this.picked = -1;
         this.refresh();
      });
      this.addRenderableWidget(this.search);
      this.setInitialFocus(this.search);
      this.refresh();
   }

   private void refresh() {
      String query = this.search == null ? "" : this.search.getValue().toLowerCase(Locale.ROOT).trim();
      this.shown.clear();
      for (ResourceLocation id : this.all) {
         if (query.isEmpty() || label(id).toLowerCase(Locale.ROOT).contains(query)
               || id.toString().toLowerCase(Locale.ROOT).contains(query)) {
            this.shown.add(id);
         }
      }
      this.scroll = Mth.clamp(this.scroll, 0, Math.max(0, this.shown.size() - ROWS));
   }

   private static final java.util.Map<ResourceLocation, String> NAMES = new java.util.HashMap<>();
   private static java.util.List<ResourceLocation> cached;

   // the category alone lets brooms and allays and anything else that walks into the list. the
   // only honest test is what the thing actually is, so one of each is made, checked and dropped
   private static java.util.List<ResourceLocation> beasts() {
      if (cached != null) return cached;
      java.util.List<ResourceLocation> found = new ArrayList<>();
      net.minecraft.client.multiplayer.ClientLevel level = Minecraft.getInstance().level;
      if (level == null) return found;

      for (var entry : net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getEntries()) {
         net.minecraft.world.entity.EntityType<?> type = entry.getValue();
         if (type.getCategory() != net.minecraft.world.entity.MobCategory.CREATURE) continue;

         try {
            net.minecraft.world.entity.Entity one = type.create(level);
            if (one instanceof net.minecraft.world.entity.animal.Animal) {
               ResourceLocation id = entry.getKey().location();
               found.add(id);
               NAMES.put(id, type.getDescription().getString());
            }
            if (one != null) one.discard();
         } catch (Exception ignored) {
         }
      }
      found.sort(java.util.Comparator.comparing(id -> NAMES.getOrDefault(id, id.getPath())));
      cached = found;
      return found;
   }

   // whatever the game calls it, not the id it was registered under
   private static String label(ResourceLocation id) {
      String named = NAMES.get(id);
      if (named != null && !named.isEmpty()) return named;
      String name = id.getPath().replace('_', ' ');
      return Character.toUpperCase(name.charAt(0)) + name.substring(1);
   }

   @Override
   public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
      this.scroll = Mth.clamp(this.scroll - (int) Math.signum(delta) * 2,
         0, Math.max(0, this.shown.size() - ROWS));
      return true;
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      int left = (this.width - WIDTH) / 2;
      int top = (this.height - (ROWS * ROW_HEIGHT + 34)) / 2;
      int listTop = top + 26;

      if (mouseX >= left + 4 && mouseX <= left + WIDTH - 4
            && mouseY >= listTop && mouseY < listTop + ROWS * ROW_HEIGHT) {
         int row = (int) ((mouseY - listTop) / ROW_HEIGHT) + this.scroll;
         if (row >= 0 && row < this.shown.size()) {
            long now = net.minecraft.Util.getMillis();
            // a second click on the same line inside a breath of the first is the pick
            if (row == this.lastClicked && now - this.lastClick < 400L) {
               this.choose(this.shown.get(row));
               return true;
            }
            this.picked = row;
            this.lastClicked = row;
            this.lastClick = now;
            return true;
         }
      }
      return super.mouseClicked(mouseX, mouseY, button);
   }

   private void choose(ResourceLocation biome) {
      PacketHandler.INSTANCE.sendToServer(new PacketAnimalScry(biome));
      this.onClose();
   }

   @Override
   public boolean keyPressed(int key, int scanCode, int modifiers) {
      if (key == 257 && this.picked >= 0 && this.picked < this.shown.size()) {
         this.choose(this.shown.get(this.picked));
         return true;
      }
      if (this.search != null && this.search.isFocused() && key != 256) {
         return this.search.keyPressed(key, scanCode, modifiers) || this.search.canConsumeInput();
      }
      return super.keyPressed(key, scanCode, modifiers);
   }

   @Override
   public void tick() {
      super.tick();
      if (this.search != null) this.search.tick();
   }

   @Override
   public void render(GuiGraphics gg, int mouseX, int mouseY, float partial) {
      this.renderBackground(gg);

      int left = (this.width - WIDTH) / 2;
      int top = (this.height - (ROWS * ROW_HEIGHT + 34)) / 2;
      int listTop = top + 26;

      gg.fill(left, top, left + WIDTH, top + ROWS * ROW_HEIGHT + 34, 0x88000000);
      gg.fill(left + 4, top + 4, left + WIDTH - 4, top + 20, 0x55000000);

      for (int i = 0; i < ROWS; i++) {
         int row = i + this.scroll;
         if (row >= this.shown.size()) break;

         int y = listTop + i * ROW_HEIGHT;
         boolean over = mouseX >= left + 4 && mouseX <= left + WIDTH - 4
            && mouseY >= y && mouseY < y + ROW_HEIGHT;
         if (row == this.picked) gg.fill(left + 4, y, left + WIDTH - 4, y + ROW_HEIGHT, 0x60E0769C);
         else if (over) gg.fill(left + 4, y, left + WIDTH - 4, y + ROW_HEIGHT, 0x30FFFFFF);

         gg.drawString(this.font, label(this.shown.get(row)), left + 8, y + 2,
            row == this.picked ? 0xFFC8DA : 0xBFBFBF, false);
      }

      super.render(gg, mouseX, mouseY, partial);
   }

   @Override
   public boolean isPauseScreen() {
      return false;
   }
}
