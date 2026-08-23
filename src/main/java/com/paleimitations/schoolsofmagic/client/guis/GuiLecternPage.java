package com.paleimitations.schoolsofmagic.client.guis;

import com.paleimitations.schoolsofmagic.client.guis.podium.PodiumGuiHelper;
import com.paleimitations.schoolsofmagic.common.items.capabilities.page.CapabilityPage;
import com.paleimitations.schoolsofmagic.common.items.capabilities.page.IPage;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketTakeLecternBook;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiLecternPage extends Screen {
   private static final ResourceLocation PAPER = new ResourceLocation("som", "textures/gui/books/paper.png");

   private final ItemStack stack;
   private final BlockPos lecternPos;

   public GuiLecternPage(ItemStack stack, BlockPos lecternPos) {
      super(Component.empty());
      this.stack = stack;
      this.lecternPos = lecternPos;
   }

   @Override
   protected void init() {
      super.init();
      int cx = this.width / 2;
      int by = this.height / 2 + 100;
      if (this.lecternPos != null) {
         this.addRenderableWidget(Button.builder(Component.translatable("gui.som.take_page"), b -> {
            PacketHandler.INSTANCE.sendToServer(new PacketTakeLecternBook(this.lecternPos));
            this.onClose();
         }).bounds(cx - 80, by, 78, 20).build());
      }
      this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> this.onClose())
         .bounds(cx + 2, by, 78, 20).build());
   }

   @Override
   public boolean isPauseScreen() {
      return false;
   }

   @Override
   public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
      this.renderBackground(gg);
      int ol = (this.width - 256) / 2;
      int ot = (this.height - 256) / 2 - 20;

      gg.pose().pushPose();
      gg.pose().translate(ol, ot, 0.0F);
      PodiumGuiHelper.renderGuiSubjectFull(gg, mouseX - ol, mouseY - ot, this.stack);
      gg.pose().popPose();

      super.render(gg, mouseX, mouseY, partialTicks);
   }
}
