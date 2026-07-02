package com.paleimitations.schoolsofmagic.client.events;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data.CapabilityTalismanData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data.ITalismanData;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketTalismanSlotClick;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class TalismanSlotInventoryHandler {

   private static final ResourceLocation GHOST =
      new ResourceLocation("som", "textures/gui/ghost_necklace.png");

   private static final int TALI_DX = 77;
   private static final int TALI_DY = 26;

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

      int x = screen.getGuiLeft() + TALI_DX;
      int y = screen.getGuiTop() + TALI_DY;
      GuiGraphics gg = event.getGuiGraphics();

      drawSlot(gg, x, y);

      ITalismanData talisman = CapabilityTalismanData.get(player);
      ItemStack stack = talisman != null ? talisman.getTalisman() : ItemStack.EMPTY;

      if (stack.isEmpty()) {
         gg.blit(GHOST, x, y, 0, 0, 16, 16, 16, 16);
      } else {
         gg.renderItem(stack, x, y);
         gg.renderItemDecorations(Minecraft.getInstance().font, stack, x, y);
         drawCooldown(gg, stack, x, y);
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

   private static void drawCooldown(GuiGraphics gg, ItemStack stack, int x, int y) {
      if (!(stack.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemPotionCharm)
            || Minecraft.getInstance().level == null) {
         return;
      }
      long now = Minecraft.getInstance().level.getGameTime();
      long start = stack.getOrCreateTag().getLong("cooldownStart");
      long end = stack.getOrCreateTag().getLong("cooldownEnd");
      if (end <= now || end <= start) {
         return;
      }
      float f = (float) (end - now) / (float) (end - start);
      int h = net.minecraft.util.Mth.clamp(net.minecraft.util.Mth.ceil(16.0F * f), 0, 16);
      gg.fill(x, y + 16 - h, x + 16, y + 16, 0x7FFFFFFF);
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
      int x = screen.getGuiLeft() + TALI_DX;
      int y = screen.getGuiTop() + TALI_DY;
      if (over(mx, my, x, y)) {
         PacketHandler.INSTANCE.sendToServer(new PacketTalismanSlotClick());
         event.setCanceled(true);
      }
   }
}
