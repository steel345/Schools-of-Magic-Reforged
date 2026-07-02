package com.paleimitations.schoolsofmagic.client.guis;

import com.paleimitations.schoolsofmagic.common.blocks.BlockCauldron;
import com.paleimitations.schoolsofmagic.common.blocks.EnumCauldronType;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketStirCauldron;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityCauldron;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class GuiCauldronStir extends Screen {
   public static final ResourceLocation TEXTURE_NORMAL = new ResourceLocation("som", "textures/gui/container/cauldron_normal_stir.png");
   public static final ResourceLocation TEXTURE_GOLD = new ResourceLocation("som", "textures/gui/container/cauldron_gold_stir.png");
   public static final ResourceLocation TEXTURE_LION = new ResourceLocation("som", "textures/gui/container/cauldron_lion_stir.png");
   public static final ResourceLocation SMOKE_NORMAL = new ResourceLocation("som", "textures/gui/container/cauldron_smoke_normal_stir.png");
   public static final ResourceLocation SMOKE_GOLD = new ResourceLocation("som", "textures/gui/container/cauldron_smoke_gold_stir.png");
   public static final ResourceLocation SMOKE_LION = new ResourceLocation("som", "textures/gui/container/cauldron_smoke_lion_stir.png");

   private final TileEntityCauldron tb;
   private final int screenH = 144;
   private final int screenW = 148;
   private SOMProgressBar progressBar;

   public GuiCauldronStir(TileEntityCauldron tb) {
      super(Component.empty());
      this.tb = tb;
   }

   private EnumCauldronType type() {
      return this.tb.getLevel().getBlockState(this.tb.getBlockPos()).getValue(BlockCauldron.TYPE);
   }
   private ResourceLocation getTexture() {
      return switch (type()) { case GOLD -> TEXTURE_GOLD; case LION -> TEXTURE_LION; default -> TEXTURE_NORMAL; };
   }
   private ResourceLocation getSmoke() {
      return switch (type()) { case GOLD -> SMOKE_GOLD; case LION -> SMOKE_LION; default -> SMOKE_NORMAL; };
   }

   @Override
   protected void init() {
      super.init();
      this.addRenderableWidget(new StirButton(this.tb,
         (this.width - screenW) / 2 + 53, (this.height - screenH) / 2 + 72));
   }

   @Override
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (keyCode == GLFW.GLFW_KEY_ESCAPE
            || Minecraft.getInstance().options.keyInventory.matches(keyCode, scanCode)) {
         Minecraft.getInstance().player.closeContainer();
         return true;
      }
      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   @Override
   public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {

      if (this.tb.getPhase() != TileEntityCauldron.EnumPotionPhase.STIRRING) {
         reopenCauldronScreen();
         return;
      }
      this.progressBar = new SOMProgressBar(getSmoke(), SOMProgressBar.ProgressBarDirection.DOWN_TO_UP,
         148, 87, (this.width - screenW) / 2, (this.height - screenH) / 2 + 8, 0, 8);
      gg.blit(getTexture(), (this.width - screenW) / 2, (this.height - screenH) / 2, 0, 0, screenW, screenH);
      this.progressBar.setMin(this.tb.getStirCounter()).setMax(this.tb.getStirMax());
      this.progressBar.draw(gg);
      super.render(gg, mouseX, mouseY, partialTicks);
   }

   private void reopenCauldronScreen() {
      Minecraft mc = Minecraft.getInstance();
      if (mc.player != null
            && mc.player.containerMenu instanceof com.paleimitations.schoolsofmagic.common.containers.ContainerCauldron menu) {
         mc.setScreen(new GuiCauldron(menu, mc.player.getInventory(),
            Component.translatable("container.gui_cauldron")));
      } else if (mc.player != null) {
         mc.player.closeContainer();
      }
   }

   @Override
   public boolean isPauseScreen() { return false; }

   @OnlyIn(Dist.CLIENT)
   static class StirButton extends AbstractButton {
      private final TileEntityCauldron cauldron;

      public StirButton(TileEntityCauldron cauldron, int posX, int posY) {
         super(posX, posY, 42, 24, Component.empty());
         this.cauldron = cauldron;
      }

      @Override
      public void onPress() {
         if (this.cauldron.getStirCounter() < this.cauldron.getStirMax()) {
            PacketHandler.INSTANCE.sendToServer(new PacketStirCauldron(this.cauldron.getStirCounter() + 1, this.cauldron.getBlockPos()));
            this.cauldron.setCounter(0);
            this.cauldron.setStirCounter(this.cauldron.getStirCounter() + 1);
         }
      }

      @Override
      public void renderWidget(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
         boolean hovered = mouseX >= this.getX() && mouseY >= this.getY()
               && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
         gg.blit(TEXTURE_NORMAL, this.getX(), this.getY(),
            148 + (hovered ? 42 : 0), this.cauldron.getStirCounter() % 3 * 24, 42, 24);
      }

      @Override
      protected void updateWidgetNarration(NarrationElementOutput out) {
         this.defaultButtonNarrationText(out);
      }
   }
}
