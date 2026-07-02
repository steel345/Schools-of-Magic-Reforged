package com.paleimitations.schoolsofmagic.client.guis;

import com.mojang.blaze3d.vertex.PoseStack;

import com.paleimitations.schoolsofmagic.common.containers.ContainerSqueakard;
import com.paleimitations.schoolsofmagic.common.entity.EntitySqueakard;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiEntitySqueakard extends EffectRenderingInventoryScreen<ContainerSqueakard> {
   public static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/gui/container/entity_intelligent.png");

   private float oldMouseX;
   private float oldMouseY;

   public GuiEntitySqueakard(ContainerSqueakard menu, Inventory playerInventory, Component title) {
      super(menu, playerInventory, title);
      this.imageWidth = 230;
      this.imageHeight = 215;
   }

   @Override
   public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
      this.renderBackground(gg);
      super.render(gg, mouseX, mouseY, partialTicks);
      this.renderTooltip(gg, mouseX, mouseY);
      this.oldMouseX = mouseX;
      this.oldMouseY = mouseY;
   }

   @Override
   protected void renderBg(GuiGraphics gg, float partialTicks, int mouseX, int mouseY) {
      gg.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
      EntitySqueakard ent = this.menu.getEntity();
      if (ent == null) return;
      Component name = Component.literal(ent.getFirstName() + " " + ent.getLastName());
      int nameWidth = this.font.width(name);
      int nameHeight = this.font.lineHeight;
      float nameScaler = nameWidth > 59 ? 59.0F / (float) nameWidth : 1.0F;

      Component lord = ent.getLordEntity() != null
         ? ent.getLordEntity().getDisplayName().copy()
         : Component.literal("Unaffiliated");
      int lordWidth = this.font.width(lord);
      float lordScaler = lordWidth > 37 ? 37.0F / (float) lordWidth * 0.9F : 0.8F;

      InventoryScreen.renderEntityInInventoryFollowsMouse(gg,
         this.leftPos + 50, this.topPos + 95, 40,
         (float) (this.leftPos + 50) - this.oldMouseX,
         (float) (this.topPos + 95 - 50) - this.oldMouseY, ent);

      PoseStack pose = gg.pose();
      pose.pushPose();
      pose.scale(nameScaler, nameScaler, nameScaler);
      gg.drawString(this.font, name,
         Math.round((this.leftPos + 41) / nameScaler) - nameWidth / 2,
         Math.round((this.topPos + 13) / nameScaler) - nameHeight / 2,
         0xFFFFFF, false);
      pose.popPose();
      pose.pushPose();
      pose.scale(lordScaler, lordScaler, lordScaler);
      gg.drawString(this.font, lord,
         Math.round((this.leftPos + 41) / lordScaler) - lordWidth / 2,
         Math.round((this.topPos + 23) / lordScaler) - nameHeight / 2,
         0xFFFFFF, false);
      pose.popPose();
      pose.pushPose();
      pose.scale(0.5F, 0.5F, 0.5F);
      gg.drawString(this.font, (int) ent.getHealth() + "/" + (int) ent.getMaxHealth(),
         Math.round((this.leftPos + 21) / 0.5F),
         Math.round((this.topPos + 108) / 0.5F), 0xFFFFFF, false);
      gg.drawString(this.font, ent.getFoodManager().getFoodLevel() + "/20",
         Math.round((this.leftPos + 56) / 0.5F),
         Math.round((this.topPos + 108) / 0.5F), 0xFFFFFF, false);
      pose.popPose();
   }

   @Override
   protected void renderLabels(GuiGraphics gg, int mouseX, int mouseY) {}
}
