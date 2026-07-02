package com.paleimitations.schoolsofmagic.client.guis;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.quests.CapabilityQuestData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.quests.IQuestData;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketQuestNote;
import com.paleimitations.schoolsofmagic.common.quests.Quest;
import com.paleimitations.schoolsofmagic.common.quests.Task;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.lwjgl.glfw.GLFW;

public class GuiQuestPaper extends Screen {
   public static final ResourceLocation QUEST = new ResourceLocation("som", "textures/gui/quest_paper.png");

   private final Player player;
   private final ItemStack stack;
   private Quest questDisplay;
   private AbstractButton startQuestButton, clearQuestButton, claimQuestButton;
   private final AbstractButton[] taskButtons = new AbstractButton[14];

   public GuiQuestPaper(Player playerIn, ItemStack stack, Quest q) {
      super(Component.empty());
      this.player = playerIn;
      this.stack = stack;
      this.questDisplay = q;
   }

   @Override
   protected void init() {
      super.init();
      int xL = (this.width - 156) / 2 + 43;
      int yL = (this.height - 166) / 2 + 135;
      this.startQuestButton = new LabeledButton(xL, yL, "gui.start_quest.name", this::onStart);
      this.clearQuestButton = new LabeledButton(xL, yL, "gui.clear_quest.name", this::onClear);
      this.claimQuestButton = new LabeledButton(xL, yL, "gui.claim_quest.name", this::onClaim);
      this.addRenderableWidget(this.startQuestButton);
      this.addRenderableWidget(this.clearQuestButton);
      this.addRenderableWidget(this.claimQuestButton);
      for (int i = 0; i < 14; i++) {
         int idx = i;
         this.taskButtons[i] = new InvisibleButton(
            (this.width - 156) / 2 + (i / 7 == 0 ? -25 : 152),
            (this.height - 166) / 2 + 5 + 22 * (i % 7),
            29, 22, () -> onTask(idx));
         this.addRenderableWidget(this.taskButtons[i]);
      }
   }

   private InteractionHand findHand() {
      ItemStack s = this.player.getItemInHand(InteractionHand.MAIN_HAND);
      return ItemStack.isSameItem(s, this.stack) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
   }

   private void onClear() {
      PacketHandler.INSTANCE.sendToServer(new PacketQuestNote(this.player, findHand(), 0));
      this.questDisplay.dead = true;
      this.player.closeContainer();
   }
   private void onClaim() {
      this.questDisplay.claim(this.player);
      PacketHandler.INSTANCE.sendToServer(new PacketQuestNote(this.player, findHand(), 1));
   }
   private void onStart() {
      PacketHandler.INSTANCE.sendToServer(new PacketQuestNote(this.player, findHand(), 2));
      CompoundTag nbt = this.stack.getTag();
      if (nbt != null && nbt.hasUUID("quest_giver")) {
         IQuestData data = this.player.getCapability(CapabilityQuestData.CAP).orElse(null);
         if (data != null) {
            this.questDisplay.setQuestGiver(nbt.getUUID("quest_giver"));
            data.addQuest(this.questDisplay);
         }
      }
   }
   private void onTask(int i) {
      PacketHandler.INSTANCE.sendToServer(new PacketQuestNote(this.player, findHand(), 3 + i));
      this.questDisplay.tasks.get(i).setStarted(true);
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

   @Override public boolean isPauseScreen() { return false; }

   @Override
   public void tick() {
      IQuestData data = this.player.getCapability(CapabilityQuestData.CAP).orElse(null);
      if (data == null) return;
      if (this.questDisplay != null && this.questDisplay.getQuestGiver() != null) {
         Quest live = data.getQuestbyQuestGiver(this.questDisplay.getQuestGiver());
         if (live != null) this.questDisplay = live;
      }
      boolean has = this.questDisplay.getQuestGiver() != null && data.hasQuest(this.questDisplay.getQuestGiver());
      this.clearQuestButton.visible = this.questDisplay != null && has && this.questDisplay.failed;
      this.claimQuestButton.visible = this.questDisplay != null && has && this.questDisplay.canClaim();
      this.startQuestButton.visible = this.questDisplay != null && !has;
      for (int i = 0; i < 14; i++) {
         this.taskButtons[i].visible = this.questDisplay != null && this.questDisplay.tasks != null
            && has && i < this.questDisplay.tasks.size()
            && this.questDisplay.tasks.get(i) != null
            && this.questDisplay.tasks.get(i).canStart(this.questDisplay);
      }
   }

   @Override
   public void render(GuiGraphics gg, int parWidth, int parHeight, float partialTick) {
      if (this.questDisplay == null || this.stack == null) { super.render(gg, parWidth, parHeight, partialTick); return; }
      int offsetWidth = (this.width - 156) / 2;
      int offsetHeight = (this.height - 166) / 2;
      gg.blit(QUEST, offsetWidth, offsetHeight, 0, 0, 156, 166);
      if (this.questDisplay.failed) gg.blit(QUEST, offsetWidth + 13, offsetHeight + 9, 180, 0, 22, 22);
      else if (this.questDisplay.completed) gg.blit(QUEST, offsetWidth + 13, offsetHeight + 9, 158, 0, 22, 22);

      Component title = Component.translatable("quest." + this.questDisplay.getResourceLocation().toString() + ".name");
      int textW = Minecraft.getInstance().font.width(title);
      float scaler = Math.min(126.0F / textW, 17.0F / Minecraft.getInstance().font.lineHeight);
      int drawX = Math.round((78 + offsetWidth) - textW * scaler / 2.0F);
      int drawY = Math.round((45 + offsetHeight) - Minecraft.getInstance().font.lineHeight * scaler / 2.0F);
      gg.pose().pushPose();
      gg.pose().scale(scaler, scaler, scaler);
      gg.drawString(Minecraft.getInstance().font, title, Math.round(drawX / scaler), Math.round(drawY / scaler), 0, false);
      gg.pose().popPose();
      gg.drawWordWrap(Minecraft.getInstance().font,
         Component.translatable("quest." + this.questDisplay.getResourceLocation().toString() + ".desc"),
         15 + offsetWidth, 56 + offsetHeight, 126, 0);

      if (!this.questDisplay.rewards.isEmpty()) {
         ItemStack reward = this.questDisplay.rewards.get(this.player.tickCount / 40 % this.questDisplay.rewards.size());
         gg.renderItem(reward, 16 + offsetWidth, 136 + offsetHeight);
      }
      if (this.questDisplay.icon != null) {
         gg.renderItem(this.questDisplay.icon, 72 + offsetWidth, 12 + offsetHeight);
      }

      if (this.questDisplay.tasks.size() > 1 && !this.startQuestButton.visible) {
         for (int i = 0; i < this.questDisplay.tasks.size(); i++) {
            this.drawTabs(gg, i, this.questDisplay, this.questDisplay.tasks.get(i));
         }
      }
      super.render(gg, parWidth, parHeight, partialTick);
   }

   public void drawTabs(GuiGraphics gg, int taskNumber, Quest quest, Task task) {
      int offsetWidth = (this.width - 156) / 2;
      int offsetHeight = (this.height - 166) / 2;
      if (task.isOngoing()) {
         gg.blit(QUEST,
            offsetWidth + (taskNumber / 7 == 0 ? -70 : 152),
            offsetHeight + 5 + taskNumber % 7 * 22, 171, taskNumber / 7 == 0 ? 78 : 29, 74, 22);
         if (task.icon != null) {
            gg.renderItem(task.icon, offsetWidth + (taskNumber / 7 == 0 ? -63 : 203),
               offsetHeight + 9 + taskNumber % 7 * 22);
         }
         if (task.getName() != null) {
            Component line = Component.translatable("task." + task.getName() + ".name");
            float scaler = 0.65F;
            int drawX = (taskNumber / 7 == 0 ? -45 : 155) + offsetWidth;
            int drawY = 8 + taskNumber % 7 * 22 + offsetHeight;
            gg.pose().pushPose();
            gg.pose().scale(scaler, scaler, scaler);
            gg.drawWordWrap(Minecraft.getInstance().font, line, Math.round(drawX / scaler),
               Math.round(drawY / scaler), Math.round(44.0F / scaler), 0);
            gg.pose().popPose();
         }
         if (task.isTimed() || task.progress != null) {
            gg.blit(QUEST,
               offsetWidth + (taskNumber / 7 == 0 ? -107 : 226),
               offsetHeight + 10 + taskNumber % 7 * 22, 171, (taskNumber / 7 == 0 ? 142 : 130) + 24, 37, 12);
            int sec = task.getCountdown() / 20 % 60;
            String line = task.isTimed()
               ? task.getCountdown() / 1200 + ":" + (sec < 10 ? "0" + sec : sec)
               : task.progress.getA() + "/" + task.progress.getB();
            float scaler = Math.min(27.0F / Minecraft.getInstance().font.width(line), 10.0F / Minecraft.getInstance().font.lineHeight);
            int drawX = (taskNumber / 7 == 0 ? -99 : 229) + offsetWidth;
            int drawY = 12 + taskNumber % 7 * 22 + offsetHeight;
            gg.pose().pushPose();
            gg.pose().scale(scaler, scaler, scaler);
            gg.drawString(Minecraft.getInstance().font, line, Math.round(drawX / scaler), Math.round(drawY / scaler), 0, false);
            gg.pose().popPose();
         }
      } else {
         int a = 48;
         float[] color = {1.0F, 1.0F, 1.0F};
         if (task.failed) { color = new float[]{0.6328125F, 0.1484375F, 0.1484375F}; a = 48; }
         else if (task.completed) { color = new float[]{0.39453125F, 0.6640625F, 0.390625F}; a = 0; }
         else if (task.canStart(quest)) { a = 24; }
         else { a = 72; }
         RenderSystem.setShaderColor(color[0], color[1], color[2], 1.0F);
         gg.blit(QUEST,
            offsetWidth + (taskNumber / 7 == 0 ? -25 : 152),
            offsetHeight + 5 + taskNumber % 7 * 22,
            (task.completed || task.failed) ? 200 : 171,
            taskNumber / 7 == 0 ? 100 : 51, 29, 22);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         if (task.icon != null) {
            gg.renderItem(task.icon, offsetWidth + (taskNumber / 7 == 0 ? -18 : 158),
               offsetHeight + 9 + taskNumber % 7 * 22);
         }
         if (task.isTimed() || task.progress != null) {
            gg.blit(QUEST,
               offsetWidth + (taskNumber / 7 == 0 ? -62 : 181),
               offsetHeight + 10 + taskNumber % 7 * 22, 171, (taskNumber / 7 == 0 ? 142 : 130) + a, 37, 12);
            String line = task.isTimed()
               ? task.getCountdown() / 1200 + ":" + task.getCountdown() / 20 % 60
               : task.progress.getA() + "/" + task.progress.getB();
            float scaler = Math.min(27.0F / Minecraft.getInstance().font.width(line), 10.0F / Minecraft.getInstance().font.lineHeight);
            int drawX = (taskNumber / 7 == 0 ? -54 : 184) + offsetWidth;
            int drawY = 12 + taskNumber % 7 * 22 + offsetHeight;
            gg.pose().pushPose();
            gg.pose().scale(scaler, scaler, scaler);
            gg.drawString(Minecraft.getInstance().font, line, Math.round(drawX / scaler), Math.round(drawY / scaler), 0, false);
            gg.pose().popPose();
         }
      }
   }

   private class LabeledButton extends AbstractButton {
      private final String labelKey;
      private final Runnable onPress;
      LabeledButton(int x, int y, String labelKey, Runnable onPress) {
         super(x, y, 83, 18, Component.empty());
         this.labelKey = labelKey; this.onPress = onPress;
      }
      @Override public void onPress() { onPress.run(); }
      @Override protected void updateWidgetNarration(NarrationElementOutput out) { defaultButtonNarrationText(out); }
      @Override public void renderWidget(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
         if (!this.visible) return;
         boolean hovered = mouseX >= this.getX() && mouseY >= this.getY()
               && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
         gg.blit(QUEST, this.getX(), this.getY(), hovered ? 83 : 0, 184, 83, 18);
         Component line = Component.translatable(labelKey);
         int tw = Minecraft.getInstance().font.width(line);
         float scaler = Math.min(77.0F / tw, 12.0F / Minecraft.getInstance().font.lineHeight);
         int drawX = Math.round((this.getX() + 42) - tw * scaler / 2.0F);
         int drawY = Math.round((this.getY() + 10) - Minecraft.getInstance().font.lineHeight * scaler / 2.0F);
         gg.pose().pushPose();
         gg.pose().scale(scaler, scaler, scaler);
         gg.drawString(Minecraft.getInstance().font, line, Math.round(drawX / scaler), Math.round(drawY / scaler), 0, false);
         gg.pose().popPose();
      }
   }

   private static class InvisibleButton extends AbstractButton {
      private final Runnable onPress;
      InvisibleButton(int x, int y, int w, int h, Runnable onPress) {
         super(x, y, w, h, Component.empty());
         this.onPress = onPress;
      }
      @Override public void onPress() { onPress.run(); }
      @Override protected void updateWidgetNarration(NarrationElementOutput out) { defaultButtonNarrationText(out); }
      @Override public void renderWidget(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {}
   }
}
