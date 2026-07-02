package com.paleimitations.schoolsofmagic.client.guis;

import com.mojang.blaze3d.vertex.PoseStack;

import com.paleimitations.schoolsofmagic.common.containers.ContainerIntelligent;
import com.paleimitations.schoolsofmagic.common.entity.EntityHuman;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiEntityIntelligent extends EffectRenderingInventoryScreen<ContainerIntelligent> {
   public static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/gui/container/entity_intelligent.png");

   private float oldMouseX;
   private float oldMouseY;

   public GuiEntityIntelligent(ContainerIntelligent menu, Inventory playerInventory, Component title) {
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
   protected void renderLabels(GuiGraphics gg, int mouseX, int mouseY) {

   }

   @Override
   protected void renderBg(GuiGraphics gg, float partialTicks, int mouseX, int mouseY) {
      gg.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
      LivingEntity ent = this.menu.getEntity();
      if (!(ent instanceof EntityHuman h)) return;
      Component name = Component.literal(h.getFirstName() + " " + h.getLastName());
      int nameWidth = this.font.width(name);
      int nameHeight = this.font.lineHeight;
      float nameScaler = nameWidth > 59 ? 59.0F / (float) nameWidth : 1.0F;

      Component kingdom = Component.literal("Unaffiliated");
      int kingdomWidth = this.font.width(kingdom);
      float kingdomScaler = kingdomWidth > 37 ? 37.0F / (float) kingdomWidth * 0.9F : 0.8F;

      int i = this.leftPos;
      int j = this.topPos;
      InventoryScreen.renderEntityInInventoryFollowsMouse(gg, i + 50, j + 95, 30,
         (float) (i + 50) - this.oldMouseX,
         (float) (j + 95 - 50) - this.oldMouseY,
         ent);

      PoseStack pose = gg.pose();
      pose.pushPose();
      pose.scale(nameScaler, nameScaler, nameScaler);
      gg.drawString(this.font, name,
         Math.round((this.leftPos + 41) / nameScaler) - nameWidth / 2,
         Math.round((this.topPos + 13) / nameScaler) - nameHeight / 2,
         0xFFFFFF, false);
      pose.popPose();
      pose.pushPose();
      pose.scale(kingdomScaler, kingdomScaler, kingdomScaler);
      gg.drawString(this.font, kingdom,
         Math.round((this.leftPos + 41) / kingdomScaler) - kingdomWidth / 2,
         Math.round((this.topPos + 23) / kingdomScaler) - nameHeight / 2,
         0xFFFFFF, false);
      pose.popPose();
      pose.pushPose();
      pose.scale(0.5F, 0.5F, 0.5F);
      gg.drawString(this.font, (int) ent.getHealth() + "/" + (int) ent.getMaxHealth(),
         Math.round((this.leftPos + 21) / 0.5F),
         Math.round((this.topPos + 108) / 0.5F), 0xFFFFFF, false);

      gg.drawString(this.font, h.getFoodData().getFoodLevel() + "/20",
         Math.round((this.leftPos + 56) / 0.5F),
         Math.round((this.topPos + 108) / 0.5F), 0xFFFFFF, false);
      pose.popPose();
   }
}
