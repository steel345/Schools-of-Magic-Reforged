package com.paleimitations.schoolsofmagic.client.events;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.CapabilityRingData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.IRingData;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketRingSlotClick;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class RingSlotInventoryHandler {

   private static final ResourceLocation GHOST =
      new ResourceLocation("som", "textures/gui/ring/ring_ghost_texture.png");

   private static final int RING_DX = 77;
   private static final int RING_DY = 44;

   private static boolean over(double mx, double my, int x, int y) {
      return mx >= x && mx < x + 16 && my >= y && my < y + 16;
   }

   @SubscribeEvent
   public static void onRender(ScreenEvent.Render.Post event) {
      if (!(event.getScreen() instanceof InventoryScreen screen)) return;
      Player player = Minecraft.getInstance().player;
      if (player == null) return;
      double mx = event.getMouseX();
      double my = event.getMouseY();

      int x = screen.getGuiLeft() + RING_DX;
      int y = screen.getGuiTop() + RING_DY;
      GuiGraphics gg = event.getGuiGraphics();

      drawSlot(gg, x, y);

      IRingData ring = CapabilityRingData.get(player);
      ItemStack stack = ring != null ? ring.getRing() : ItemStack.EMPTY;

      if (stack.isEmpty()) {
         gg.blit(GHOST, x, y, 0, 0, 16, 16, 16, 16);
      } else {
         gg.renderItem(stack, x, y);
         gg.renderItemDecorations(Minecraft.getInstance().font, stack, x, y);
      }

      ItemStack carried = screen.getMenu().getCarried();
      if (over(mx, my, x, y)) {
         gg.fillGradient(x, y, x + 16, y + 16, 0x80FFFFFF, 0x80FFFFFF);
         if (!carried.isEmpty()) {
            gg.renderItem(carried, (int) mx - 8, (int) my - 8);
            gg.renderItemDecorations(Minecraft.getInstance().font, carried, (int) mx - 8, (int) my - 8);
         } else if (!stack.isEmpty()) {
            gg.renderTooltip(Minecraft.getInstance().font, stack, (int) mx, (int) my);
         }
      }
   }

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
      int x = screen.getGuiLeft() + RING_DX;
      int y = screen.getGuiTop() + RING_DY;
      if (over(mx, my, x, y)) {
         PacketHandler.INSTANCE.sendToServer(new PacketRingSlotClick());
         event.setCanceled(true);
      }
   }
}
