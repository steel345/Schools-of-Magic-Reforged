package com.paleimitations.schoolsofmagic.client.guis;

import com.paleimitations.schoolsofmagic.common.entity.Quest;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.player_quests.CapabilityPlayerQuests;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.player_quests.IPlayerQuests;
import com.paleimitations.schoolsofmagic.common.items.ItemDryadQuest;
import com.paleimitations.schoolsofmagic.common.network.PacketDryadQuest;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
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

public class GuiDryadQuest extends Screen {
   private static final ResourceLocation QUEST = new ResourceLocation("som", "textures/gui/quest_paper.png");

   private final Player player;
   private final ItemStack stack;
   private final Quest quest;
   private AbstractButton startButton;
   private AbstractButton claimButton;

   public GuiDryadQuest(Player player, ItemStack stack) {
      super(Component.empty());
      this.player = player;
      this.stack = stack;
      this.quest = ItemDryadQuest.getQuest(stack);
   }

   @Override
   protected void init() {
      super.init();
      int x = (this.width - 156) / 2 + 43;
      int y = (this.height - 166) / 2 + 135;
      this.startButton = new LabeledButton(x, y, "gui.start_quest.name", () -> send(PacketDryadQuest.START));
      this.claimButton = new LabeledButton(x, y, "gui.claim_quest.name", () -> {
         send(PacketDryadQuest.CLAIM);
         this.onClose();
      });
      this.addRenderableWidget(this.startButton);
      this.addRenderableWidget(this.claimButton);
   }

   private void send(int action) {
      boolean mainHand = ItemStack.isSameItem(this.player.getItemInHand(InteractionHand.MAIN_HAND), this.stack);
      PacketHandler.INSTANCE.sendToServer(new PacketDryadQuest(action, mainHand));
   }

   private IPlayerQuests quests() {
      return this.player.getCapability(CapabilityPlayerQuests.CAP).orElse(null);
   }

   private boolean onThisQuest(IPlayerQuests data) {
      return data != null && data.isOnQuest() && data.getQuestID() == ItemDryadQuest.getQuestId(this.stack);
   }

   @Override
   public void tick() {
      IPlayerQuests data = quests();
      boolean active = onThisQuest(data);
      this.claimButton.visible = active && data.hasSucceeded();
      this.startButton.visible = !active && (data == null || !data.isOnQuest());
   }

   @Override
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (keyCode == GLFW.GLFW_KEY_ESCAPE || Minecraft.getInstance().options.keyInventory.matches(keyCode, scanCode)) {
         this.onClose();
         return true;
      }
      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   @Override
   public boolean isPauseScreen() {
      return false;
   }

   @Override
   public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTick) {
      if (this.quest == null) {
         super.render(gg, mouseX, mouseY, partialTick);
         return;
      }
      int ox = (this.width - 156) / 2;
      int oy = (this.height - 166) / 2;
      gg.blit(QUEST, ox, oy, 0, 0, 156, 166);

      IPlayerQuests data = quests();
      boolean active = onThisQuest(data);
      if (active && data.hasSucceeded()) {
         gg.blit(QUEST, ox + 13, oy + 9, 158, 0, 22, 22);
      }

      Component title = Component.literal(this.quest.getName());
      int tw = this.font.width(title);
      float scale = Math.min(126.0F / tw, 17.0F / this.font.lineHeight);
      gg.pose().pushPose();
      gg.pose().scale(scale, scale, scale);
      gg.drawString(this.font,
         title,
         Math.round(((78 + ox) - tw * scale / 2.0F) / scale),
         Math.round(((45 + oy) - this.font.lineHeight * scale / 2.0F) / scale),
         0, false);
      gg.pose().popPose();

      String explanation = this.quest.getDialog(2);
      if (explanation == null) explanation = this.quest.getDialog(1);
      if (explanation != null) {
         gg.pose().pushPose();
         gg.pose().scale(0.8F, 0.8F, 0.8F);
         gg.drawWordWrap(this.font, Component.literal(explanation),
            Math.round((15 + ox) / 0.8F), Math.round((56 + oy) / 0.8F),
            Math.round(126 / 0.8F), 0);
         gg.pose().popPose();
      }

      ItemStack icon = com.paleimitations.schoolsofmagic.common.entity.DryadQuests.getIcon(
         ItemDryadQuest.getQuestId(this.stack));
      if (!icon.isEmpty()) {
         gg.renderItem(icon, 72 + ox, 12 + oy);
      }

      ItemStack reward = new ItemStack(ItemRegistry.wand_core.get());
      reward.setDamageValue(ItemDryadQuest.getWood(this.stack));
      gg.renderItem(reward, 16 + ox, 136 + oy);

      if (active && !data.hasSucceeded() && data.getTimer() > 0) {
         int seconds = data.getTimer() / 20 % 60;
         String line = data.getTimer() / 1200 + ":" + (seconds < 10 ? "0" + seconds : String.valueOf(seconds));
         gg.drawString(this.font, line, ox + 78 - this.font.width(line) / 2, oy + 140, 0, false);
      }
      super.render(gg, mouseX, mouseY, partialTick);
   }

   private class LabeledButton extends AbstractButton {
      private final String labelKey;
      private final Runnable onPress;

      LabeledButton(int x, int y, String labelKey, Runnable onPress) {
         super(x, y, 83, 18, Component.empty());
         this.labelKey = labelKey;
         this.onPress = onPress;
      }

      @Override
      public void onPress() {
         this.onPress.run();
      }

      @Override
      protected void updateWidgetNarration(NarrationElementOutput out) {
         defaultButtonNarrationText(out);
      }

      @Override
      public void renderWidget(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
         if (!this.visible) return;
         boolean hovered = mouseX >= this.getX() && mouseY >= this.getY()
            && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
         gg.blit(QUEST, this.getX(), this.getY(), hovered ? 83 : 0, 184, 83, 18);
         Component line = Component.translatable(this.labelKey);
         int tw = GuiDryadQuest.this.font.width(line);
         float scale = Math.min(77.0F / tw, 12.0F / GuiDryadQuest.this.font.lineHeight);
         gg.pose().pushPose();
         gg.pose().scale(scale, scale, scale);
         gg.drawString(GuiDryadQuest.this.font, line,
            Math.round(((this.getX() + 42) - tw * scale / 2.0F) / scale),
            Math.round(((this.getY() + 10) - GuiDryadQuest.this.font.lineHeight * scale / 2.0F) / scale),
            0, false);
         gg.pose().popPose();
      }
   }
}
