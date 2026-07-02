package com.paleimitations.schoolsofmagic.client.guis;

import com.paleimitations.schoolsofmagic.common.blocks.BlockCauldron;
import com.paleimitations.schoolsofmagic.common.blocks.EnumCauldronType;
import com.paleimitations.schoolsofmagic.common.containers.ContainerCauldron;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityCauldron;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiCauldron extends AbstractContainerScreen<ContainerCauldron> {
   public static final ResourceLocation TEXTURE_NORMAL = new ResourceLocation("som", "textures/gui/container/cauldron_normal.png");
   public static final ResourceLocation TEXTURE_GOLD = new ResourceLocation("som", "textures/gui/container/cauldron_gold.png");
   public static final ResourceLocation TEXTURE_LION = new ResourceLocation("som", "textures/gui/container/cauldron_lion.png");
   public static final ResourceLocation SMOKE_NORMAL = new ResourceLocation("som", "textures/gui/container/cauldron_smoke_normal.png");
   public static final ResourceLocation SMOKE_GOLD = new ResourceLocation("som", "textures/gui/container/cauldron_smoke_gold.png");
   public static final ResourceLocation SMOKE_LION = new ResourceLocation("som", "textures/gui/container/cauldron_smoke_lion.png");

   private SOMProgressBar progressBar;

   public GuiCauldron(ContainerCauldron menu, Inventory playerInventory, Component title) {
      super(menu, playerInventory, title);
      this.imageWidth = 176;
      this.imageHeight = 256;
   }

   @Override
   protected void init() {
      super.init();
      TileEntityCauldron tb = this.menu.getTile();
      if (tb != null) {
         EnumCauldronType type = tb.getLevel().getBlockState(tb.getBlockPos()).getValue(BlockCauldron.TYPE);
         ResourceLocation smoke = switch (type) {
            case GOLD -> SMOKE_GOLD;
            case LION -> SMOKE_LION;
            default -> SMOKE_NORMAL;
         };
         this.progressBar = new SOMProgressBar(smoke, SOMProgressBar.ProgressBarDirection.DOWN_TO_UP, 157, 87, 0, 8, 0, 8);
      }
   }

   @Override
   public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {

      TileEntityCauldron tb = this.menu.getTile();
      if (tb != null) {
         if (tb.getPhase() == TileEntityCauldron.EnumPotionPhase.RESTING || tb.isLidded()) {
            Minecraft.getInstance().setScreen(new GuiCauldronRest(tb));
            return;
         }
         if (tb.getPhase() == TileEntityCauldron.EnumPotionPhase.STIRRING) {
            Minecraft.getInstance().setScreen(new GuiCauldronStir(tb));
            return;
         }
      }
      this.renderBackground(gg);
      super.render(gg, mouseX, mouseY, partialTicks);
      this.renderTooltip(gg, mouseX, mouseY);
   }

   @Override
   protected void renderBg(GuiGraphics gg, float partialTicks, int mouseX, int mouseY) {
      TileEntityCauldron tb = this.menu.getTile();
      ResourceLocation tex = TEXTURE_NORMAL;
      if (tb != null) {
         EnumCauldronType type = tb.getLevel().getBlockState(tb.getBlockPos()).getValue(BlockCauldron.TYPE);
         tex = switch (type) {
            case GOLD -> TEXTURE_GOLD;
            case LION -> TEXTURE_LION;
            default -> TEXTURE_NORMAL;
         };
      }
      gg.blit(tex, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
   }

   @Override
   protected void renderLabels(GuiGraphics gg, int mouseX, int mouseY) {
      TileEntityCauldron tb = this.menu.getTile();
      if (tb == null) return;

      if (tb.getPhase() == TileEntityCauldron.EnumPotionPhase.WATER || tb.getPhase() == TileEntityCauldron.EnumPotionPhase.COMPLETE) {
         this.progressBar.setMin(0).setMax(10);
      }
      if (tb.getPhase() == TileEntityCauldron.EnumPotionPhase.BREWING) {
         this.progressBar.setMin(tb.getCounter()).setMax(tb.getBrewTickMax());
      }
      this.progressBar.draw(gg);
   }
}
