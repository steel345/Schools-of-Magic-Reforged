package com.paleimitations.schoolsofmagic.client.guis.podium;

import com.paleimitations.schoolsofmagic.common.containers.podium.ContainerPodiumNote;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.CapabilitySpellNotes;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.ISpellNotes;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.SpellNotes;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketSelectNoteOption;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GuiPodiumNote extends AbstractContainerScreen<ContainerPodiumNote> {
   public static final ResourceLocation ICONS = new ResourceLocation("som", "textures/gui/podium/icons.png");
   public static final ResourceLocation ICON_BAR = new ResourceLocation("som", "textures/gui/podium/icon_bar.png");
   public static final ResourceLocation ASH_NOTE = new ResourceLocation("som", "textures/gui/podium/ash_note.png");
   public static final ResourceLocation ELDER_NOTE = new ResourceLocation("som", "textures/gui/podium/elder_note.png");
   public static final ResourceLocation PINE_NOTE = new ResourceLocation("som", "textures/gui/podium/pine_note.png");
   public static final ResourceLocation WILLOW_NOTE = new ResourceLocation("som", "textures/gui/podium/willow_note.png");
   public static final ResourceLocation YEW_NOTE = new ResourceLocation("som", "textures/gui/podium/yew_note.png");
   public static final ResourceLocation VERDE_NOTE = new ResourceLocation("som", "textures/gui/podium/verde_note.png");
   public static final ResourceLocation OAK_NOTE = new ResourceLocation("som", "textures/gui/podium/oak_note.png");
   public static final ResourceLocation SPRUCE_NOTE = new ResourceLocation("som", "textures/gui/podium/spruce_note.png");
   public static final ResourceLocation BIRCH_NOTE = new ResourceLocation("som", "textures/gui/podium/birch_note.png");
   public static final ResourceLocation ACACIA_NOTE = new ResourceLocation("som", "textures/gui/podium/acacia_note.png");
   public static final ResourceLocation DARK_OAK_NOTE = new ResourceLocation("som", "textures/gui/podium/dark_oak_note.png");
   public static final ResourceLocation JUNGLE_NOTE = new ResourceLocation("som", "textures/gui/podium/jungle_note.png");

   public GuiPodiumNote(ContainerPodiumNote menu, Inventory playerInventory, Component title) {
      super(menu, playerInventory, title);
      this.imageWidth = 256;
      this.imageHeight = 256;
   }

   private TileEntityPodium getPodium() { return this.menu.getPodium(); }

   private ResourceLocation getTexture() {
      TileEntityPodium p = getPodium();
      if (p == null) return OAK_NOTE;
      return switch (p.getWood()) {
         case OAK -> OAK_NOTE;
         case SPRUCE -> SPRUCE_NOTE;
         case BIRCH -> BIRCH_NOTE;
         case ACACIA -> ACACIA_NOTE;
         case JUNGLE -> JUNGLE_NOTE;
         case DARK_OAK -> DARK_OAK_NOTE;
         case ASH -> ASH_NOTE;
         case ELDER -> ELDER_NOTE;
         case PINE -> PINE_NOTE;
         case WILLOW -> WILLOW_NOTE;
         case YEW -> YEW_NOTE;
         case VERDE -> VERDE_NOTE;
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
   protected void init() {
      super.init();
      TileEntityPodium podium = getPodium();
      if (podium == null) return;
      this.addRenderableWidget(new PodiumSwitchButton(podium, 4, 0, this.leftPos + 79, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 4, 1, this.leftPos + 104, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 4, 2, this.leftPos + 129, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 4, 3, this.leftPos + 179, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 4, 4, this.leftPos + 54, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 4, 5, this.leftPos + 154, this.topPos + 130));
      for (int i = 0; i < 7; i++) {
         this.addRenderableWidget(new SelectButton(podium, i, this.leftPos + 140, this.topPos + 15 + i * 14));
      }
   }

   @Override
   protected void renderLabels(GuiGraphics gg, int mouseX, int mouseY) {
      TileEntityPodium podium = getPodium();
      if (podium == null) return;
      if (podium.handler.getStackInSlot(6).getItem() == ItemRegistry.spell_note.get()) {
         gg.pose().pushPose();
         gg.pose().translate(6.886178F, 1.642276F, 0.0F);
         PodiumGuiHelper.renderGuiSubject(gg,
            mouseX - this.leftPos, mouseY - this.topPos,
            this, podium.handler.getStackInSlot(6), 0.0F, podium, false);
         gg.pose().popPose();
      }
   }

   @OnlyIn(Dist.CLIENT)
   static class SelectButton extends AbstractButton {
      private final TileEntityPodium podium;
      public final int select;

      public SelectButton(TileEntityPodium podium, int select, int posX, int posY) {
         super(posX, posY, 104, 14, Component.empty());
         this.podium = podium;
         this.select = select;
      }

      @Override
      public void onPress() {
         ItemStack stack = this.podium.handler.getStackInSlot(6);
         if (stack.getItem() != ItemRegistry.spell_note.get()) return;
         ISpellNotes cap = stack.getCapability(CapabilitySpellNotes.SPELL_NOTES_CAPABILITY).orElse(null);
         if (cap == null) return;
         SpellNotes notes = cap.getSpellNotes();
         if (!notes.getOptions().isEmpty() && notes.getOptions().size() > this.select) {
            PacketHandler.INSTANCE.sendToServer(new PacketSelectNoteOption(
               Minecraft.getInstance().player, this.podium.getBlockPos(), this.select));
         }
      }

      @Override protected void updateWidgetNarration(NarrationElementOutput out) { defaultButtonNarrationText(out); }

      @Override
      public void renderWidget(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
         boolean hovered = mouseX >= this.getX() && mouseY >= this.getY()
               && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
         ItemStack stack = this.podium.handler.getStackInSlot(6);
         if (stack.getItem() != ItemRegistry.spell_note.get()) return;
         ISpellNotes cap = stack.getCapability(CapabilitySpellNotes.SPELL_NOTES_CAPABILITY).orElse(null);
         if (cap == null) return;
         SpellNotes notes = cap.getSpellNotes();
         if (notes.getOptions().isEmpty() || notes.getOptions().size() <= this.select) return;
         ItemStack opt = notes.getOptions().get(this.select);
         gg.blit(GuiPodiumRead.ICONS, this.getX(), this.getY(), 87, hovered ? 106 : 92, 104, 14);
         var font = Minecraft.getInstance().font;
         Component line = opt.getHoverName().copy().withStyle(ChatFormatting.BLACK);
         int tw = font.width(line);
         float scaler = Math.min(96.0F / (float) tw, 10.0F / (float) font.lineHeight);
         gg.pose().pushPose();
         gg.pose().scale(scaler, scaler, scaler);

         float textTop = this.getY() + (this.height - font.lineHeight * scaler) / 2.0F;
         gg.drawString(font, line,
            Math.round((this.getX() + 4.0F) / scaler),
            Math.round(textTop / scaler),
            0, false);
         gg.pose().popPose();
      }
   }
}
