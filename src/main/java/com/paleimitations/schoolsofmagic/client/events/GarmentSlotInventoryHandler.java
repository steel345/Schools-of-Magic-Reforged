package com.paleimitations.schoolsofmagic.client.events;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.CharmScreenState;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.CapabilityGarmentData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.IGarmentData;
import com.paleimitations.schoolsofmagic.common.network.PacketGarmentSlotClick;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// The charm button that sits by the off-hand slot, and the four garment slots it
// reveals over the armour column: crown, cape, belt and grimoire.
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class GarmentSlotInventoryHandler {

   private static final ResourceLocation BUTTON = new ResourceLocation("som", "textures/gui/charm_button.png");
   private static final ResourceLocation BUTTON_SELECTED = new ResourceLocation("som", "textures/gui/charm_button_select.png");

   private static final ResourceLocation[] OVERLAYS = {
      new ResourceLocation("som", "textures/gui/crown_overlay.png"),
      new ResourceLocation("som", "textures/gui/cape_overlay.png"),
      new ResourceLocation("som", "textures/gui/belt_overlay.png"),
      new ResourceLocation("som", "textures/gui/book_smithing_overlay.png")
   };

   private static final String[] NAMES = {
      "gui.som.slot.crown", "gui.som.slot.cape", "gui.som.slot.belt", "gui.som.slot.grimoire"
   };

   // Tucked just above the off-hand slot while closed; it drops onto the off-hand
   // slot itself once the garment slots are showing.
   private static final int BUTTON_DX = 77;
   private static final int BUTTON_DY_CLOSED = 44;
   private static final int BUTTON_DY_OPEN = 62;

   // The armour column, which the garment slots stand in for.
   private static final int GARMENT_DX = 8;
   private static final int GARMENT_DY = 8;

   public static int buttonY() {
      return CharmScreenState.isOpen() ? BUTTON_DY_OPEN : BUTTON_DY_CLOSED;
   }

   private static boolean over(double mx, double my, int x, int y) {
      return mx >= x && mx < x + 16 && my >= y && my < y + 16;
   }

   // The armour column and off-hand slot are where the garment slots go, so while
   // the charm view is open they are parked off-screen: vanilla then neither draws
   // them nor lets them be clicked, and putting them back restores their contents
   // untouched.
   private static final int[] ARMOUR_SLOTS = {5, 6, 7, 8};
   private static final int OFFHAND_SLOT = 45;
   private static final int HIDDEN_Y = -3000;

   // A slot's position is final, so it is written through reflection; the fields are
   // resolved once and the whole thing quietly does nothing if that ever fails.
   private static java.lang.reflect.Field slotX;
   private static java.lang.reflect.Field slotY;
   private static boolean fieldsResolved;

   private static void resolveFields() {
      if (fieldsResolved) return;
      fieldsResolved = true;
      for (java.lang.reflect.Field f : net.minecraft.world.inventory.Slot.class.getDeclaredFields()) {
         if (f.getType() != int.class) continue;
         if ("x".equals(f.getName()) || "f_40220_".equals(f.getName())) {
            f.setAccessible(true);
            slotX = f;
         } else if ("y".equals(f.getName()) || "f_40221_".equals(f.getName())) {
            f.setAccessible(true);
            slotY = f;
         }
      }
   }

   private static void place(net.minecraft.world.inventory.Slot slot, int x, int y) {
      try {
         if (slotX != null) slotX.setInt(slot, x);
         if (slotY != null) slotY.setInt(slot, y);
      } catch (Throwable ignored) {
      }
   }

   private static void applySlotVisibility(InventoryScreen screen) {
      resolveFields();
      if (slotX == null || slotY == null) return;
      java.util.List<net.minecraft.world.inventory.Slot> slots = screen.getMenu().slots;
      boolean hide = CharmScreenState.isOpen();
      for (int i = 0; i < ARMOUR_SLOTS.length; i++) {
         int index = ARMOUR_SLOTS[i];
         if (index >= slots.size()) continue;
         place(slots.get(index), 8, hide ? HIDDEN_Y : 8 + i * 18);
      }
      if (OFFHAND_SLOT < slots.size()) {
         place(slots.get(OFFHAND_SLOT), 77, hide ? HIDDEN_Y : 62);
      }
   }

   @SubscribeEvent
   public static void onRenderPre(ScreenEvent.Render.Pre event) {
      if (event.getScreen() instanceof InventoryScreen screen) applySlotVisibility(screen);
   }

   // Leaving the screen must never strand the vanilla slots off-screen.
   @SubscribeEvent
   public static void onClose(ScreenEvent.Closing event) {
      if (!(event.getScreen() instanceof InventoryScreen screen)) return;
      CharmScreenState.close();
      applySlotVisibility(screen);
   }

   @SubscribeEvent
   public static void onRender(ScreenEvent.Render.Post event) {
      if (!(event.getScreen() instanceof InventoryScreen screen)) return;
      Player player = Minecraft.getInstance().player;
      if (player == null) return;
      GuiGraphics gg = event.getGuiGraphics();
      double mx = event.getMouseX();
      double my = event.getMouseY();

      int bx = screen.getGuiLeft() + BUTTON_DX;
      int by = screen.getGuiTop() + buttonY();
      boolean overButton = over(mx, my, bx, by);
      // Hovering always shows the other of the two faces. Both are 18x18: the extra
      // ring of pixels is what hides the shield slot, so each is drawn whole and
      // pulled back a pixel, leaving its inner 16x16 over the slot itself.
      gg.blit(CharmScreenState.isOpen() != overButton ? BUTTON_SELECTED : BUTTON,
         bx - 1, by - 1, 0, 0, 18, 18, 18, 18);
      if (overButton) {
         gg.renderTooltip(Minecraft.getInstance().font,
            Component.translatable("gui.som.charm_button"), (int) mx, (int) my);
      }

      if (!CharmScreenState.isOpen()) return;

      IGarmentData data = CapabilityGarmentData.get(player);
      ItemStack carried = screen.getMenu().getCarried();
      for (int i = 0; i < IGarmentData.SLOTS; i++) {
         int x = screen.getGuiLeft() + GARMENT_DX;
         int y = screen.getGuiTop() + GARMENT_DY + i * 18;
         drawSlot(gg, x, y);

         ItemStack worn = data == null ? ItemStack.EMPTY : data.getGarment(i);
         if (worn.isEmpty()) {
            gg.blit(OVERLAYS[i], x, y, 0, 0, 16, 16, 16, 16);
         } else {
            gg.renderItem(worn, x, y);
            gg.renderItemDecorations(Minecraft.getInstance().font, worn, x, y);
         }

         if (over(mx, my, x, y)) {
            gg.fillGradient(x, y, x + 16, y + 16, 0x80FFFFFF, 0x80FFFFFF);
            if (!carried.isEmpty()) {
               gg.renderItem(carried, (int) mx - 8, (int) my - 8);
               gg.renderItemDecorations(Minecraft.getInstance().font, carried, (int) mx - 8, (int) my - 8);
            } else if (!worn.isEmpty()) {
               gg.renderTooltip(Minecraft.getInstance().font, worn, (int) mx, (int) my);
            } else {
               gg.renderTooltip(Minecraft.getInstance().font,
                  Component.translatable(NAMES[i]), (int) mx, (int) my);
            }
         }
      }
   }

   // An empty slot drawn in the vanilla style, so the garment column matches the
   // panel it covers.
   private static void drawSlot(GuiGraphics gg, int x, int y) {
      int sx = x - 1;
      int sy = y - 1;
      gg.fill(sx, sy, sx + 18, sy + 18, 0xFF8B8B8B);
      gg.fill(sx, sy, sx + 18, sy + 1, 0xFF373737);
      gg.fill(sx, sy, sx + 1, sy + 18, 0xFF373737);
      gg.fill(sx + 17, sy, sx + 18, sy + 18, 0xFFFFFFFF);
      gg.fill(sx, sy + 17, sx + 18, sy + 18, 0xFFFFFFFF);
   }

   @SubscribeEvent
   public static void onClick(ScreenEvent.MouseButtonPressed.Pre event) {
      if (!(event.getScreen() instanceof InventoryScreen screen)) return;
      double mx = event.getMouseX();
      double my = event.getMouseY();

      int bx = screen.getGuiLeft() + BUTTON_DX;
      int by = screen.getGuiTop() + buttonY();
      if (over(mx, my, bx, by)) {
         CharmScreenState.toggle();
         Minecraft.getInstance().player.playSound(
            net.minecraft.sounds.SoundEvents.UI_BUTTON_CLICK.value(), 0.4F, 1.0F);
         event.setCanceled(true);
         return;
      }

      if (!CharmScreenState.isOpen()) return;
      for (int i = 0; i < IGarmentData.SLOTS; i++) {
         int x = screen.getGuiLeft() + GARMENT_DX;
         int y = screen.getGuiTop() + GARMENT_DY + i * 18;
         if (over(mx, my, x, y)) {
            PacketHandler.INSTANCE.sendToServer(new PacketGarmentSlotClick(i));
            event.setCanceled(true);
            return;
         }
      }
   }
}
