package com.paleimitations.schoolsofmagic.client.events;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.ClientProxy;
import com.paleimitations.schoolsofmagic.common.items.BookDecorations;
import com.paleimitations.schoolsofmagic.common.items.ItemSpellbook;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketSetCastingMode;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class CastingToggleHandler {

   @SubscribeEvent
   public static void onKey(InputEvent.Key event) {
      if (event.getAction() != GLFW.GLFW_PRESS) {
         return;
      }
      Minecraft mc = Minecraft.getInstance();
      if (mc.player == null || mc.screen != null) {
         return;
      }
      if (!ClientProxy.CASTING_TOGGLE.matches(event.getKey(), event.getScanCode())) {
         return;
      }
      ItemStack held = mc.player.getMainHandItem();
      if (!(held.getItem() instanceof ItemSpellbook) || !BookDecorations.hasJewel(held)) {
         return;
      }
      boolean now = !ItemSpellbook.isCastingMode(held);
      ItemSpellbook.setCastingMode(held, now);
      mc.player.playSound(SoundEvents.AMETHYST_BLOCK_CHIME, 0.8F, now ? 1.5F : 1.0F);
      mc.player.displayClientMessage(Component.literal(
         now ? "Grimoire is on casting mode." : "Grimoire is off casting mode.")
         .withStyle(ChatFormatting.LIGHT_PURPLE), true);
      PacketHandler.INSTANCE.sendToServer(new PacketSetCastingMode(now));
   }
}
