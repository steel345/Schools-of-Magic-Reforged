package com.paleimitations.schoolsofmagic.client.guis;

import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketLetter;
import com.paleimitations.schoolsofmagic.common.registries.BookPageRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.lwjgl.glfw.GLFW;

public class GuiLetter extends Screen {
   public static final ResourceLocation LETTER = new ResourceLocation("som", "textures/gui/letter_ccw.png");
   public static final ResourceLocation CLOSED_ENVELOPE = new ResourceLocation("som", "textures/gui/envelope_closed.png");
   public static final ResourceLocation OPEN_ENVELOPE = new ResourceLocation("som", "textures/gui/envelope_open.png");

   private final Player player;
   private final BookPage letter;
   private int yOffset;
   private int phase;
   private int tick;
   private ItemStack stack;
   private AbstractButton sealButton;
   private AbstractButton questButton;
   private AbstractButton envelopeButton;

   public GuiLetter(Player playerIn) {
      super(Component.empty());
      this.player = playerIn;
      this.letter = BookPageRegistry.getBookPage("ccw_letter_1");
      this.phase = 0;
      this.tick = 0;
   }

   @Override
   protected void init() {
      super.init();
      this.yOffset = this.height;

      this.sealButton = new AbstractButton((this.width - 156) / 2 + 64, (this.height - 166) / 2 + 87, 24, 24, Component.empty()) {
         @Override public void onPress() {
            phase = 2; tick = 0;
         }
         @Override public void renderWidget(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
            if (this.visible && mouseX >= this.getX() && mouseY >= this.getY()
                  && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height) {
               gg.blit(CLOSED_ENVELOPE, this.getX(), this.getY(), 173, 87, 24, 24);
            }
         }
         @Override protected void updateWidgetNarration(NarrationElementOutput out) { defaultButtonNarrationText(out); }
      };
      this.questButton = new AbstractButton((this.width - 156) / 2 + 152, (this.height - 166) / 2 + 32, 30, 62, Component.empty()) {
         @Override public void onPress() {
            InteractionHand hand = InteractionHand.MAIN_HAND;
            if (player.getItemInHand(hand).getItem() != ItemRegistry.magic_letter.get()) hand = InteractionHand.OFF_HAND;
            PacketHandler.INSTANCE.sendToServer(new PacketLetter(player, hand, 2));
         }
         @Override public void renderWidget(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
            if (this.visible && mouseX >= this.getX() && mouseY >= this.getY()
                  && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height) {
               gg.blit(LETTER, this.getX(), this.getY(), 209, 0, 30, 62);
            }
         }
         @Override protected void updateWidgetNarration(NarrationElementOutput out) { defaultButtonNarrationText(out); }
      };
      this.envelopeButton = new AbstractButton((this.width - 156) / 2 - 33, (this.height - 166) / 2 + 28, 37, 122, Component.empty()) {
         @Override public void onPress() {
            InteractionHand hand = InteractionHand.MAIN_HAND;
            if (player.getItemInHand(hand).getItem() != ItemRegistry.magic_letter.get()) hand = InteractionHand.OFF_HAND;
            PacketHandler.INSTANCE.sendToServer(new PacketLetter(player, hand, 1));
            phase = 0;
         }
         @Override public void renderWidget(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
            if (this.visible && mouseX >= this.getX() && mouseY >= this.getY()
                  && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height) {
               gg.blit(LETTER, this.getX(), this.getY(), 216, 63, 37, 122);
            }
         }
         @Override protected void updateWidgetNarration(NarrationElementOutput out) { defaultButtonNarrationText(out); }
      };
      this.addRenderableWidget(this.sealButton);
      this.addRenderableWidget(this.questButton);
      this.addRenderableWidget(this.envelopeButton);
   }

   @Override
   public boolean isPauseScreen() { return false; }

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
   public void tick() {
      this.sealButton.visible = this.stack != null
         && this.stack.getItem() == ItemRegistry.magic_letter.get()
         && this.phase == 1
         && (!this.stack.hasTag() || !this.stack.getTag().getBoolean("opened"));
      this.questButton.visible = this.stack != null
         && this.stack.getItem() == ItemRegistry.magic_letter.get()
         && this.stack.hasTag()
         && this.stack.getTag().getBoolean("opened")
         && this.stack.getTag().getBoolean("quest")
         && this.tick > 40;
      this.envelopeButton.visible = this.stack != null
         && this.stack.getItem() == ItemRegistry.magic_letter.get()
         && this.stack.hasTag()
         && this.stack.getTag().getBoolean("opened")
         && this.tick > 40;
   }

   @Override
   public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTick) {
      int offsetWidth = (this.width - 156) / 2;
      int offsetHeight = (this.height - 166) / 2;
      this.tick++;
      this.stack = this.player.getItemInHand(InteractionHand.MAIN_HAND);
      if (this.stack.getItem() != ItemRegistry.magic_letter.get()) {
         this.stack = this.player.getItemInHand(InteractionHand.OFF_HAND);
      }

      if (this.stack.hasTag() && this.stack.getTag().getBoolean("opened")) {
         if (this.tick > 40) {
            int xOffset = this.tick < 60 ? (int) (-30.0 * (-Math.cos(((double) this.tick - 40.0 + (double) partialTick) * Math.PI / 20.0) - 1.0)) : 0;
            if (this.stack.getTag().getBoolean("quest")) {
               gg.blit(LETTER, offsetWidth - xOffset + 152, offsetHeight + 32, 179, 0, 30, 62);
            }
            int xOffset2 = this.tick < 60 ? (int) (37.0 * (-Math.cos(((double) this.tick - 40.0 + (double) partialTick) * Math.PI / 20.0) - 1.0)) : 0;
            gg.blit(LETTER, offsetWidth - xOffset2 - 33, offsetHeight + 28, 179, 63, 37, 122);
         }
         if (this.tick < 20) {
            this.yOffset = (int) ((double) this.height * (-Math.cos(((double) this.tick + (double) partialTick) * Math.PI / 20.0) - 1.0));
         } else {
            this.yOffset = 0;
         }
         gg.blit(LETTER, offsetWidth, offsetHeight - this.yOffset, 0, 0, 156, 166);
         if (this.letter != null) {
            this.letter.drawPage(gg, mouseX - offsetWidth, mouseY - offsetHeight - this.yOffset,
               offsetWidth, offsetHeight - this.yOffset, true, 0);
         }
      } else {
         if (this.phase == 0) {
            if (this.tick < 40) {
               this.yOffset = (int) ((double) this.height * (-Math.cos(((double) this.tick + (double) partialTick) * Math.PI / 40.0) - 1.0));
            } else {
               this.phase = 1;
               this.yOffset = 0;
            }
         }
         if (this.phase == 2) {
            if (this.tick > 20 && this.tick < 60) {
               this.yOffset = (int) ((double) this.height * (Math.cos(((double) this.tick - 20.0 + (double) partialTick) * Math.PI / 40.0) - 1.0));
            }
            gg.blit(OPEN_ENVELOPE, offsetWidth, offsetHeight - this.yOffset, 0, 0, 156, 166);
            if (this.tick == 60) {
               InteractionHand hand = InteractionHand.MAIN_HAND;
               if (this.player.getItemInHand(hand).getItem() != ItemRegistry.magic_letter.get()) hand = InteractionHand.OFF_HAND;
               PacketHandler.INSTANCE.sendToServer(new PacketLetter(this.player, hand, 0));
               this.tick = 0;
            }
         } else {
            gg.blit(CLOSED_ENVELOPE, offsetWidth, offsetHeight - this.yOffset, 0, 0, 156, 166);
         }
      }
      super.render(gg, mouseX, mouseY, partialTick);
   }
}
