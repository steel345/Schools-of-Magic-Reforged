package com.paleimitations.schoolsofmagic.client.events;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.ClientProxy;
import com.paleimitations.schoolsofmagic.client.guis.GuiStandardBook;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.CapabilityCharmData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.ICharmData;
import com.paleimitations.schoolsofmagic.common.items.ItemBookBase;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class CharmActivationHandler {

   @SubscribeEvent
   public static void onKey(InputEvent.Key event) {
      if (event.getAction() != GLFW.GLFW_PRESS) return;
      Minecraft mc = Minecraft.getInstance();
      if (mc.player == null || mc.screen != null) return;
      if (!ClientProxy.CHARM_ACTIVATE.matches(event.getKey(), event.getScanCode())) return;
      ICharmData data = CapabilityCharmData.get(mc.player);
      if (data == null) return;
      ItemStack charm = data.getCharm();
      if (charm.isEmpty() || charm.getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null) == null) return;
      ItemBookBase.ensureInitialized(charm);
      ItemBookBase.ensureCosmetics(charm);
      mc.setScreen(new GuiStandardBook(mc.player, charm));
   }
}
