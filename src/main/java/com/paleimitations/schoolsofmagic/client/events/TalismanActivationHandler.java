package com.paleimitations.schoolsofmagic.client.events;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.ClientProxy;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data.CapabilityTalismanData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data.ITalismanData;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketTalismanActivate;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class TalismanActivationHandler {

   @SubscribeEvent
   public static void onKey(InputEvent.Key event) {
      if (event.getAction() != GLFW.GLFW_PRESS) return;
      Minecraft mc = Minecraft.getInstance();
      if (mc.player == null || mc.screen != null) return;
      if (!ClientProxy.TALISMAN_ACTIVATE.matches(event.getKey(), event.getScanCode())) return;
      ITalismanData data = CapabilityTalismanData.get(mc.player);
      if (data == null || data.getTalisman().isEmpty()) return;
      PacketHandler.INSTANCE.sendToServer(new PacketTalismanActivate());
   }
}
