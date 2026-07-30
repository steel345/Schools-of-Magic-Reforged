package com.paleimitations.schoolsofmagic.client.events;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.ClientProxy;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.CapabilityCharmData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.ICharmData;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketThrowCharmPotion;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandler;

// The potion bag worn in the charm slot: hold the charm key to open the (existing)
// potion ring and steer the mouse to pick a potion; releasing arms it (its hotbar
// slot glows white), and the next right-click throws that potion.
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class PotionCharmHandler {

   private static boolean armed = false;
   private static int selectedSlot = -1;
   private static boolean ringWasOpen = false;
   private static double lastX = 0.0;
   private static double lastY = 0.0;
   private static boolean primed = false;

   // Whichever bag the key being held actually reaches: the charm key sees only the
   // charm slot, the belt key only the belt.
   private static ItemStack charmBag(Player player) {
      java.util.function.Predicate<ItemStack> isBag = s -> s.getItem() == ItemRegistry.potion_bag.get();
      if (ClientProxy.CHARM_ACTIVATE.isDown()) {
         ItemStack worn = com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots
            .findCharmPouch(player, isBag);
         if (!worn.isEmpty()) return worn;
      }
      if (ClientProxy.BELT_ACTIVATE.isDown()) {
         ItemStack worn = com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots
            .findBeltPouch(player, isBag);
         if (!worn.isEmpty()) return worn;
      }
      // Neither key held: fall back to whichever slot holds one, so the armed hotbar
      // glow and the throw still resolve after the ring closes.
      return com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots
         .findWornPouch(player, isBag);
   }

   @SubscribeEvent
   public static void onClientTick(TickEvent.ClientTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      Minecraft mc = Minecraft.getInstance();
      Player player = mc.player;
      if (player == null) return;

      ItemStack bag = charmBag(player);
      boolean holding = (ClientProxy.CHARM_ACTIVATE.isDown() || ClientProxy.BELT_ACTIVATE.isDown()) && mc.screen == null && !bag.isEmpty()
         && player.getMainHandItem().getItem() != ItemRegistry.potion_bag.get();

      if (holding) {
         IItemHandler handler = bag.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
         if (handler != null) {
            double x = mc.mouseHandler.xpos();
            double y = mc.mouseHandler.ypos();
            if (!primed) {
               lastX = x;
               lastY = y;
               primed = true;
            } else {
               double dx = x - lastX;
               double dy = y - lastY;
               lastX = x;
               lastY = y;
               double length = Math.sqrt(dx * dx + dy * dy);
               if (length > 8.0) {
                  double angle = 2.0 * Math.atan(dx / (dy + length)) * 180.0 / Math.PI;
                  selectedSlot = sectorToSlot(angle);
                  bag.setDamageValue(selectedSlot); // client-side, so the ring highlights it
               }
            }
         }
         ringWasOpen = true;
         armed = false;
      } else {
         primed = false;
         if (ringWasOpen) {
            ringWasOpen = false;
            IItemHandler handler = bag.isEmpty() ? null : bag.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
            armed = handler != null && selectedSlot >= 0 && selectedSlot < handler.getSlots()
               && !handler.getStackInSlot(selectedSlot).isEmpty();
         }
      }
   }

   private static int sectorToSlot(double angle) {
      if (angle >= -18.0 && angle < 18.0) return 0;
      if (angle >= 18.0 && angle < 54.0) return 1;
      if (angle >= 54.0 && angle < 90.0) return 2;
      if (angle >= 90.0 && angle < 126.0) return 3;
      if (angle >= 126.0 && angle < 162.0) return 4;
      if ((angle >= 162.0 && angle < 180.0) || (angle <= -162.0 && angle > -180.0)) return 5;
      if (angle <= -126.0 && angle > -162.0) return 6;
      if (angle <= -90.0 && angle > -126.0) return 7;
      if (angle <= -54.0 && angle > -90.0) return 8;
      if (angle <= -18.0 && angle > -54.0) return 9;
      return 0;
   }

   @SubscribeEvent
   public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
      if (!event.getOverlay().id().equals(net.minecraftforge.client.gui.overlay.VanillaGuiOverlay.HOTBAR.id())) return;
      if (!armed) return;
      Minecraft mc = Minecraft.getInstance();
      Player player = mc.player;
      if (player == null) return;
      if (charmBag(player).isEmpty()) { armed = false; return; }
      if (mc.screen != null) return;
      GuiGraphics gg = event.getGuiGraphics();
      int selected = player.getInventory().selected;
      int x0 = gg.guiWidth() / 2 - 91 + selected * 20 + 2;
      int y0 = gg.guiHeight() - 22 + 2;
      int x1 = x0 + 18;
      int y1 = y0 + 18;
      gg.fill(x0 - 2, y0 - 2, x1 + 2, y1 + 2, 0x66FFFFFF);   // soft white outer glow
      int frame = 0xFFFFFFFF;                                 // bright white frame
      gg.fill(x0 - 1, y0 - 1, x1 + 1, y0, frame);
      gg.fill(x0 - 1, y1, x1 + 1, y1 + 1, frame);
      gg.fill(x0 - 1, y0 - 1, x0, y1 + 1, frame);
      gg.fill(x1, y0 - 1, x1 + 1, y1 + 1, frame);
   }

   @SubscribeEvent(priority = EventPriority.HIGHEST)
   public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
      tryThrow(event);
   }

   @SubscribeEvent(priority = EventPriority.HIGHEST)
   public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
      tryThrow(event);
   }

   @SubscribeEvent(priority = EventPriority.HIGHEST)
   public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
      if (!armed) return;
      Minecraft mc = Minecraft.getInstance();
      if (mc.player == null || event.getEntity() != mc.player) return;
      fireThrow();
   }

   private static void tryThrow(PlayerInteractEvent event) {
      if (!armed) return;
      if (!event.getLevel().isClientSide) return;
      Minecraft mc = Minecraft.getInstance();
      if (mc.player == null || event.getEntity() != mc.player) return;
      event.setCanceled(true);
      event.setCancellationResult(InteractionResult.SUCCESS);
      fireThrow();
   }

   private static void fireThrow() {
      armed = false;
      PacketHandler.INSTANCE.sendToServer(new PacketThrowCharmPotion(selectedSlot));
   }
}
