package com.paleimitations.schoolsofmagic.client.events;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.screens.inventory.CyclingSlotBackground;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class BookSmithingOverlayHandler {

   private static final ResourceLocation BOOK_ICON =
      new ResourceLocation("som", "item/book_smithing_overlay");

   @SubscribeEvent
   public static void onInit(ScreenEvent.Init.Post event) {
      if (!(event.getScreen() instanceof SmithingScreen screen)) {
         return;
      }
      try {
         Field slotField = CyclingSlotBackground.class.getDeclaredField("slotIndex");
         slotField.setAccessible(true);
         for (Field f : SmithingScreen.class.getDeclaredFields()) {
            if (f.getType() == CyclingSlotBackground.class) {
               f.setAccessible(true);
               CyclingSlotBackground original = (CyclingSlotBackground) f.get(screen);
               if (original instanceof BookTemplateCycling) {
                  return;
               }
               int slot = slotField.getInt(original);
               f.set(screen, new BookTemplateCycling(slot));
               return;
            }
         }
      } catch (Exception ignored) {
      }
   }

   public static class BookTemplateCycling extends CyclingSlotBackground {
      public BookTemplateCycling(int slot) {
         super(slot);
      }

      @Override
      public void tick(List<ResourceLocation> icons) {
         List<ResourceLocation> combined = new ArrayList<>(icons);
         if (!combined.contains(BOOK_ICON)) {
            combined.add(BOOK_ICON);
         }
         super.tick(combined);
      }
   }
}
