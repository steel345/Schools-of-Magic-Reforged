package com.paleimitations.schoolsofmagic.client.events;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.ClientProxy;
import com.paleimitations.schoolsofmagic.client.guis.GuiStandardBook;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.IGarmentData;
import com.paleimitations.schoolsofmagic.common.items.ItemBookBase;
import com.paleimitations.schoolsofmagic.common.items.ItemHerbPouch;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketOpenHerbPouch;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

// Activating a worn garment. The belt behaves exactly like the charm slot for a
// pouch or bag, and the grimoire slot opens the book it holds.
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class GarmentActivationHandler {

   @SubscribeEvent
   public static void onKey(InputEvent.Key event) {
      if (event.getAction() != GLFW.GLFW_PRESS) return;
      Minecraft mc = Minecraft.getInstance();
      if (mc.player == null || mc.screen != null) return;

      if (ClientProxy.BELT_ACTIVATE.matches(event.getKey(), event.getScanCode())) {
         activateBelt(mc);
      } else if (ClientProxy.GRIMOIRE_ACTIVATE.matches(event.getKey(), event.getScanCode())) {
         activateGrimoire(mc);
      }
      // The crown and cape hold nothing yet, so their keys have nothing to run.
   }

   private static void activateBelt(Minecraft mc) {
      ItemStack worn = GarmentSlots.getWorn(mc.player, IGarmentData.BELT);
      if (worn.getItem() instanceof ItemHerbPouch) {
         PacketHandler.INSTANCE.sendToServer(new PacketOpenHerbPouch());
      }
      // A potion bag opens its ring, which PotionCharmHandler drives while held.
   }

   private static void activateGrimoire(Minecraft mc) {
      ItemStack worn = GarmentSlots.getWorn(mc.player, IGarmentData.GRIMOIRE);
      if (worn.isEmpty() || worn.getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null) == null) return;
      ItemBookBase.ensureInitialized(worn);
      ItemBookBase.ensureCosmetics(worn);
      mc.setScreen(new GuiStandardBook(mc.player, worn));
   }
}
