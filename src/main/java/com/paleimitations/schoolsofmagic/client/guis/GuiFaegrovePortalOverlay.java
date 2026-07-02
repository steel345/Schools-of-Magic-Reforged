package com.paleimitations.schoolsofmagic.client.guis;

import com.mojang.blaze3d.systems.RenderSystem;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class GuiFaegrovePortalOverlay {

   private static final ResourceLocation TEX = new ResourceLocation("som", "textures/blocks/faegrove_portal.png");
   private static final int FRAMES = 32;

   @SubscribeEvent
   public void render(RenderGuiEvent.Post event) {
      Minecraft mc = Minecraft.getInstance();
      LocalPlayer player = mc.player;
      if (player == null || mc.level == null || mc.options.hideGui) return;
      if (!isInPortal(mc, player)) return;

      GuiGraphics g = event.getGuiGraphics();
      int w = mc.getWindow().getGuiScaledWidth();
      int h = mc.getWindow().getGuiScaledHeight();
      int frame = (int)((System.currentTimeMillis() / 90L) % FRAMES);

      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.85F);
      g.blit(TEX, 0, 0, w, h, 0.0F, frame * 16.0F, 16, 16, 16, 16 * FRAMES);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.disableBlend();
   }

   @SubscribeEvent
   public void onScreenOpening(ScreenEvent.Opening event) {
      if (!(event.getNewScreen() instanceof AbstractContainerScreen)) return;
      Minecraft mc = Minecraft.getInstance();
      if (mc.player == null || mc.level == null) return;
      if (isInPortal(mc, mc.player)) {
         event.setCanceled(true);
      }
   }

   private boolean isInPortal(Minecraft mc, LocalPlayer player) {
      BlockPos feet = BlockPos.containing(player.getX(), player.getY() + 0.1D, player.getZ());
      BlockPos eyes = BlockPos.containing(player.getX(), player.getEyeY(), player.getZ());
      return isPortal(mc, feet) || isPortal(mc, eyes);
   }

   private boolean isPortal(Minecraft mc, BlockPos pos) {
      BlockState s = mc.level.getBlockState(pos);
      return s.is(BlockRegistry.faegrove_portal.get());
   }
}
