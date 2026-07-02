package com.paleimitations.schoolsofmagic.client.guis;

import com.paleimitations.schoolsofmagic.common.blocks.BlockCauldron;
import com.paleimitations.schoolsofmagic.common.blocks.EnumCauldronType;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketLidCauldron;
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
public class GuiCauldronRest extends Screen {
   public static final ResourceLocation TEXTURE_NORMAL = new ResourceLocation("som", "textures/gui/container/cauldron_normal_rest.png");
   public static final ResourceLocation TEXTURE_GOLD = new ResourceLocation("som", "textures/gui/container/cauldron_gold_rest.png");
   public static final ResourceLocation TEXTURE_LION = new ResourceLocation("som", "textures/gui/container/cauldron_lion_rest.png");

   private final TileEntityCauldron tb;
   private final int screenWidthPx = 100;
   private final int screenHeightPx = 117;
   private SOMProgressBar progressBar;

   public GuiCauldronRest(TileEntityCauldron tb) {
      super(Component.empty());
      this.tb = tb;
   }

   private ResourceLocation getTexture() {
      EnumCauldronType type = this.tb.getLevel().getBlockState(this.tb.getBlockPos()).getValue(BlockCauldron.TYPE);
      return switch (type) {
         case GOLD -> TEXTURE_GOLD;
         case LION -> TEXTURE_LION;
         default -> TEXTURE_NORMAL;
      };
   }

   @Override
   protected void init() {
      super.init();
      this.addRenderableWidget(new LidButton(this.tb,
         (this.width - screenWidthPx) / 2 + 31, (this.height - screenHeightPx) / 2 + 10));
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

      if (this.tb.getPhase() != TileEntityCauldron.EnumPotionPhase.RESTING && !this.tb.isLidded()) {
         reopenCauldronScreen();
         return;
      }
      ResourceLocation tex = getTexture();
      this.progressBar = new SOMProgressBar(tex, SOMProgressBar.ProgressBarDirection.LEFT_TO_RIGHT,
         52, 8, (this.width - screenWidthPx) / 2 + 24, (this.height - screenHeightPx) / 2 + 96, 24, 117);
      gg.blit(tex, (this.width - screenWidthPx) / 2, (this.height - screenHeightPx) / 2, 0, 0, screenWidthPx, screenHeightPx);
      this.progressBar.setMin(this.tb.getCounter()).setMax(this.tb.getRestMax());
      this.progressBar.draw(gg);
      super.render(gg, mouseX, mouseY, partialTicks);
   }

   private void reopenCauldronScreen() {
      Minecraft mc = Minecraft.getInstance();
      if (mc.player != null
            && mc.player.containerMenu instanceof com.paleimitations.schoolsofmagic.common.containers.ContainerCauldron menu) {
         mc.setScreen(new GuiCauldron(menu, mc.player.getInventory(),
            net.minecraft.network.chat.Component.translatable("container.gui_cauldron")));
      } else if (mc.player != null) {
         mc.player.closeContainer();
      }
   }

   @Override
   public boolean isPauseScreen() { return false; }

   @OnlyIn(Dist.CLIENT)
   static class LidButton extends AbstractButton {
      private final TileEntityCauldron cauldron;

      public LidButton(TileEntityCauldron cauldron, int posX, int posY) {
         super(posX, posY, 51, 39, Component.empty());
         this.cauldron = cauldron;
      }

      @Override
      public void onPress() {
         PacketHandler.INSTANCE.sendToServer(new PacketLidCauldron(!this.cauldron.isLidded(), this.cauldron.getBlockPos()));
         this.cauldron.setLidded(!this.cauldron.isLidded());
      }

      @Override
      public void renderWidget(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
         if (!this.visible) return;
         boolean hovered = mouseX >= this.getX() && mouseY >= this.getY()
               && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
         EnumCauldronType type = this.cauldron.getLevel().getBlockState(this.cauldron.getBlockPos()).getValue(BlockCauldron.TYPE);
         ResourceLocation tex = switch (type) {
            case GOLD -> TEXTURE_GOLD;
            case LION -> TEXTURE_LION;
            default -> TEXTURE_NORMAL;
         };
         gg.blit(tex, this.getX(), this.getY(),
            100 + (hovered ? 51 : (this.cauldron.isLidded() ? 0 : 102)), 0, 51, 39);
      }

      @Override
      protected void updateWidgetNarration(NarrationElementOutput out) {
         this.defaultButtonNarrationText(out);
      }
   }
}
