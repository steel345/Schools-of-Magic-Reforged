package com.paleimitations.schoolsofmagic.client.guis;

import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketNameSpell;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiNameSpell extends Screen {
   private final InteractionHand hand;
   private final String initName;
   private final String initDesc;
   private EditBox nameBox;
   private EditBox descBox;
   private Button doneButton;

   public GuiNameSpell(InteractionHand hand, String initName, String initDesc) {
      super(Component.literal("Name Spell"));
      this.hand = hand;
      this.initName = initName == null ? "" : initName;
      this.initDesc = initDesc == null ? "" : initDesc;
   }

   public static void open(InteractionHand hand, ItemStack stack) {
      String name = "";
      String desc = "";
      if (stack.hasTag() && stack.getTag().contains("CustomSpell")) {
         net.minecraft.nbt.CompoundTag s = stack.getTag().getCompound("CustomSpell");
         name = s.getString("customName");
         desc = s.getString("customDesc");
      }
      Minecraft.getInstance().setScreen(new GuiNameSpell(hand, name, desc));
   }

   @Override
   protected void init() {
      super.init();
      int cx = this.width / 2;
      int top = this.height / 2 - 46;

      this.nameBox = new EditBox(this.font, cx - 100, top, 200, 20, Component.literal("Spell Name"));
      this.nameBox.setMaxLength(32);
      this.nameBox.setHint(Component.literal("Unnamed Spell"));
      this.nameBox.setValue(this.initName);

      this.descBox = new EditBox(this.font, cx - 100, top + 48, 200, 20, Component.literal("Description"));
      this.descBox.setMaxLength(140);
      this.descBox.setHint(Component.literal("Description (optional)"));
      this.descBox.setValue(this.initDesc);

      this.addRenderableWidget(this.nameBox);
      this.addRenderableWidget(this.descBox);

      this.doneButton = Button.builder(Component.literal("Done"), b -> this.commit())
         .bounds(cx - 50, top + 84, 100, 20).build();
      this.addRenderableWidget(this.doneButton);

      this.setInitialFocus(this.nameBox);
      this.doneButton.active = !this.nameBox.getValue().trim().isEmpty();
   }

   private void commit() {
      String name = this.nameBox.getValue().trim();
      if (name.isEmpty()) return;
      PacketHandler.INSTANCE.sendToServer(new PacketNameSpell(this.hand, name, this.descBox.getValue()));
      this.onClose();
   }

   @Override
   public void tick() {
      super.tick();
      if (this.doneButton != null && this.nameBox != null) {
         this.doneButton.active = !this.nameBox.getValue().trim().isEmpty();
      }
   }

   @Override
   public boolean isPauseScreen() {
      return false;
   }

   @Override
   public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
      this.renderBackground(gg);
      int cx = this.width / 2;
      int top = this.height / 2 - 46;
      gg.drawCenteredString(this.font, Component.literal("Name Your Spell"), cx, top - 28, 0xFFFFFF);
      gg.drawString(this.font, Component.literal("Name"), cx - 100, top - 12, 0xA0A0A0, false);
      gg.drawString(this.font, Component.literal("Description"), cx - 100, top + 36, 0xA0A0A0, false);
      super.render(gg, mouseX, mouseY, partialTicks);
   }
}
