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

   public static int proficiencyBonus(net.minecraft.world.entity.LivingEntity entity, MagicElement element) {
      if (entity == null || element == null) return 0;
      net.minecraft.world.effect.MobEffect effect = net.minecraftforge.registries.ForgeRegistries.MOB_EFFECTS
         .getValue(new ResourceLocation("som", element.getName()));
      if (effect == null) return 0;
      MobEffectInstance held = entity.getEffect(effect);
      return held == null ? 0 : held.getAmplifier() + 1;
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
            gg.pose().pushPose();
            gg.pose().translate(-2.3F, 6.0F, 0.0F);
            boolean drawn = drawElementIcon(gg, x, y, 22, 22);
            gg.pose().popPose();
            return drawn;
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
