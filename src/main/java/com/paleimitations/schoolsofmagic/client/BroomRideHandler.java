package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.EntityMagicBroom;
import com.paleimitations.schoolsofmagic.common.network.PacketBroomSprint;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class BroomRideHandler {
   private static boolean sprinting;
   private static boolean lastSent;

   @SubscribeEvent
   public static void onClientTick(TickEvent.ClientTickEvent event) {
      if (event.phase != TickEvent.Phase.END) {
         return;
      }
      Minecraft mc = Minecraft.getInstance();
      LocalPlayer player = mc.player;
      if (player == null || !(player.getVehicle() instanceof EntityMagicBroom broom)) {
         sprinting = false;
         lastSent = false;
         return;
      }
      if (!sprinting) {
         if (mc.options.keySprint.isDown() && player.zza > 0.0F) {
            sprinting = true;
         }
      } else if (player.zza <= 0.0F) {
         sprinting = false;
      }
      broom.broomSprint = sprinting;
      if (sprinting != lastSent) {
         lastSent = sprinting;
         PacketHandler.INSTANCE.sendToServer(new PacketBroomSprint(broom.getId(), sprinting));
      }
   }

   @SubscribeEvent
   public static void onFov(net.minecraftforge.client.event.ComputeFovModifierEvent event) {
      if (event.getPlayer().getVehicle() instanceof EntityMagicBroom broom && broom.broomSprint) {
         event.setNewFovModifier(event.getNewFovModifier() * 1.15F);
      }
   }
}
