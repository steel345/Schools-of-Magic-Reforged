package com.paleimitations.schoolsofmagic.client.guis.podium;

import java.awt.Color;
import java.util.Random;

import com.paleimitations.schoolsofmagic.client.effects.GuiEffectHelper;
import com.paleimitations.schoolsofmagic.client.guis.SOMProgressBar;
import com.paleimitations.schoolsofmagic.common.containers.podium.ContainerPodiumCopy;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.network.PacketAddScore;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.tileentity.PodiumGame;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;
import com.paleimitations.schoolsofmagic.common.util.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.lwjgl.glfw.GLFW;

public class GuiPodiumCopy extends AbstractContainerScreen<ContainerPodiumCopy> {
   public static final ResourceLocation ICONS = new ResourceLocation("som", "textures/gui/podium/icons.png");
   public static final ResourceLocation OAK_COPY = new ResourceLocation("som", "textures/gui/podium/oak_copy.png");
   public static final ResourceLocation SPRUCE_COPY = new ResourceLocation("som", "textures/gui/podium/spruce_copy.png");
   public static final ResourceLocation BIRCH_COPY = new ResourceLocation("som", "textures/gui/podium/birch_copy.png");
   public static final ResourceLocation ACACIA_COPY = new ResourceLocation("som", "textures/gui/podium/acacia_copy.png");
   public static final ResourceLocation JUNGLE_COPY = new ResourceLocation("som", "textures/gui/podium/jungle_copy.png");
   public static final ResourceLocation DARK_OAK_COPY = new ResourceLocation("som", "textures/gui/podium/dark_oak_copy.png");
   public static final ResourceLocation ASH_COPY = new ResourceLocation("som", "textures/gui/podium/ash_copy.png");
   public static final ResourceLocation ELDER_COPY = new ResourceLocation("som", "textures/gui/podium/elder_copy.png");
   public static final ResourceLocation PINE_COPY = new ResourceLocation("som", "textures/gui/podium/pine_copy.png");
   public static final ResourceLocation WILLOW_COPY = new ResourceLocation("som", "textures/gui/podium/willow_copy.png");
   public static final ResourceLocation YEW_COPY = new ResourceLocation("som", "textures/gui/podium/yew_copy.png");
   public static final ResourceLocation VERDE_COPY = new ResourceLocation("som", "textures/gui/podium/verde_copy.png");

   private int targetX;
   private int targetY;
   private final Random rand = new Random();

   public GuiPodiumCopy(ContainerPodiumCopy menu, Inventory playerInventory, Component title) {
      super(menu, playerInventory, title);
      this.imageWidth = 256;
      this.imageHeight = 256;
   }

   private TileEntityPodium getPodium() { return this.menu.getPodium(); }

   private ResourceLocation getTexture() {
      TileEntityPodium p = getPodium();
      if (p == null) return OAK_COPY;
      return switch (p.getWood()) {
         case OAK -> OAK_COPY;
         case SPRUCE -> SPRUCE_COPY;
         case BIRCH -> BIRCH_COPY;
         case ACACIA -> ACACIA_COPY;
         case JUNGLE -> JUNGLE_COPY;
         case DARK_OAK -> DARK_OAK_COPY;
         case ASH -> ASH_COPY;
         case ELDER -> ELDER_COPY;
         case PINE -> PINE_COPY;
         case WILLOW -> WILLOW_COPY;
         case YEW -> YEW_COPY;
         case VERDE -> VERDE_COPY;
      };
   }

   @Override
   public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
      this.renderBackground(gg);
      super.render(gg, mouseX, mouseY, partialTicks);
      this.renderTooltip(gg, mouseX, mouseY);
   }

   @Override
   protected void renderBg(GuiGraphics gg, float partialTicks, int mouseX, int mouseY) {
      gg.blit(getTexture(), this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
   }

   @Override
   protected void renderLabels(GuiGraphics gg, int mouseX, int mouseY) {
      TileEntityPodium podium = getPodium();
      if (podium == null) return;
      IBook book = podium.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
      if (book != null) {
         gg.pose().pushPose();
         gg.pose().translate(6.886178F, 1.642276F, 0.0F);
         PodiumGuiHelper.renderGuiSubject(gg, 0.0F, 0.0F, this, podium.handler.getStackInSlot(0), 0.0F, podium, true);
         gg.pose().popPose();
      }
      if (ItemStack.isSameItem(podium.handler.getStackInSlot(1), new ItemStack(Items.PAPER))) {
         gg.pose().pushPose();
         gg.pose().translate(140.88618F, 1.642276F, 0.0F);
         gg.pose().scale(0.42276424F, 0.42276424F, 0.42276424F);
         gg.blit(new ResourceLocation("som", "textures/gui/books/paper.png"), 0, 0, 0, 0, 256, 256);
         gg.pose().popPose();
      }

      PodiumGame game = podium.podiumGame;
      if (game.isPlaying()) {
         float tarX = game.getTargetX();
         float tarY = game.getTargetY();
         this.targetX = (tarX > 94.0F) ? this.leftPos + 54 + Math.round(tarX) : this.leftPos + 14 + Math.round(tarX);
         this.targetY = this.topPos + Math.round(tarY) + 21;
         if (this.rand.nextInt(20) == 0) {
            GuiEffectHelper.createGuiSparkleParticle(
               this.targetX + (this.rand.nextFloat() - this.rand.nextFloat()) * 5.0F,
               this.targetY + (this.rand.nextFloat() - this.rand.nextFloat()) * 5.0F,
               0.0F, 0.0F, new Color(150, 150, 200 + this.rand.nextInt(50)));
         }
      }
      new SOMProgressBar(ICONS, SOMProgressBar.ProgressBarDirection.LEFT_TO_RIGHT, 106, 7, 75, 111, 87, 0)
         .setMin(Math.round(podium.podiumGame.getScore())).setMax(Math.round(podium.podiumGame.getGoalScore())).draw(gg);
      new SOMProgressBar(ICONS, SOMProgressBar.ProgressBarDirection.UP_TO_DOWN, 90, 61, 151, 23, 94, 12)
         .setMin(Math.round(podium.podiumGame.getScore())).setMax(Math.round(podium.podiumGame.getGoalScore())).draw(gg);
      boolean leftPressed = GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
      if (mouseX - this.leftPos >= 9 && mouseX - this.leftPos <= 112 && mouseY - this.topPos >= 8 && mouseY - this.topPos <= 99) {
         gg.blit(ICONS, mouseX - this.leftPos - 4, mouseY - this.topPos - 4, leftPressed ? 0 : 15, 206, 15, 15);
      }
      if (mouseX - this.leftPos >= 143 && mouseX - this.leftPos <= 246 && mouseY - this.topPos >= 8 && mouseY - this.topPos <= 99) {
         gg.blit(ICONS, mouseX - this.leftPos, mouseY - this.topPos - 15, leftPressed ? 0 : 15, 182, 15, 15);
      }
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      TileEntityPodium podium = getPodium();
      if (podium != null) {
         PodiumGame game = podium.podiumGame;
         if (game.isPlaying() && button == 0) {
            double distance = Utils.getDistanceDouble(mouseX, mouseY, 0.0, this.targetX, this.targetY, 0.0);
            if (distance < 8.0) {
               float score = (8.0F - (float) distance) / 4.0F;
               game.addScore(score);
               PacketHandler.INSTANCE.sendToServer(new PacketAddScore(score, podium.getBlockPos()));
            }
         }
      }
      return super.mouseClicked(mouseX, mouseY, button);
   }

   @Override
   protected void init() {
      super.init();
      TileEntityPodium podium = getPodium();
      if (podium == null) return;
      this.addRenderableWidget(new PodiumSwitchButton(podium, 1, 0, this.leftPos + 79, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 1, 1, this.leftPos + 104, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 1, 2, this.leftPos + 129, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 1, 3, this.leftPos + 179, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 1, 4, this.leftPos + 54, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 1, 5, this.leftPos + 154, this.topPos + 130));
   }
}
