package com.paleimitations.schoolsofmagic.common.potions.potions;

import com.paleimitations.schoolsofmagic.common.MagicElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class PotionElement extends PotionBasic {
   private final Supplier<MagicElement> elementSup;

   public PotionElement(MobEffectCategory category, int color, Supplier<MagicElement> elementSup) {
      super(category, color);
      this.elementSup = elementSup;
   }

   @Override
   public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
      consumer.accept(new IClientMobEffectExtensions() {
         @Override
         public boolean isVisibleInGui(MobEffectInstance effect) {

            return false;
         }

         @Override
         public boolean renderInventoryIcon(MobEffectInstance effect,
                                            EffectRenderingInventoryScreen<?> screen,
                                            GuiGraphics gg, int x, int y, int blitOffset) {

            return drawElementIcon(gg, x - 1, y + 6, 22, 22);
         }

         private boolean drawElementIcon(GuiGraphics gg, int x, int y, int destW, int destH) {
            MagicElement el = elementSup.get();
            if (el == null) return false;
            ResourceLocation sheet = el.getIcon();
            int u = el.getIconU();
            int v = el.getIconV();
            int size = el.getIconSize();

            gg.blit(sheet, x, y, destW, destH, u, v, size, size, 256, 256);
            return true;
         }
      });
   }
}
